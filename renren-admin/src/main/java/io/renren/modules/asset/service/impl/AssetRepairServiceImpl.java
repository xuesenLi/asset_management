package io.renren.modules.asset.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.asset.dao.*;
import io.renren.modules.asset.entity.AssetAuditEntity;
import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.entity.RecordDetailEntity;
import io.renren.modules.asset.enums.AssetOperTypeEnum;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.RecordStatusEnum;
import io.renren.modules.asset.enums.RecordTypeEnum;
import io.renren.modules.asset.utils.GeneratorRecordNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.entity.AssetRepairEntity;
import io.renren.modules.asset.service.AssetRepairService;
import org.springframework.transaction.annotation.Transactional;


@Service("assetRepairService")
@Slf4j
public class AssetRepairServiceImpl extends ServiceImpl<AssetRepairDao, AssetRepairEntity> implements AssetRepairService {

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private AssetAuditDao assetAuditDao;

    @Autowired
    private AssetOperRecordDao assetOperRecordDao;




    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetRepairEntity> page = this.page(
                new Query<AssetRepairEntity>().getPage(params),
                new QueryWrapper<AssetRepairEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }


    @Override
    @Transactional
    public void insert(AssetRepairEntity assetRepair) {
        //1. 生成维修 单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_REPAIR);
        assetRepair.setRecordNo(recordNo);
        assetRepair.setAssetNum(assetRepair.getAssets().size());

        //2. 设置单号状态   待审批
        assetRepair.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());

        log.info("assetRepair = {}", assetRepair);
        //2. 入库
        //调拨单号 入库
        baseMapper.insert(assetRepair);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetRepair.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //批量修改  将当前操作的资产状态改为  + 10 待审批
        assetDao.batchAssetStatusAdd10(assetRepair.getAssets());

        //向审批表中 插入 待审批 的 记录
        AssetAuditEntity assetAuditEntity = new AssetAuditEntity();
        assetAuditEntity.setRecordNo(recordNo);
        assetAuditEntity.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());
        assetAuditEntity.setRecordType(RecordTypeEnum.ASSET_SCRAP.getCode());
        assetAuditEntity.setCreatedUsername(assetRepair.getCreatedUsername());

        assetAuditDao.insert(assetAuditEntity);
    }


    @Override
    public AssetRepairEntity getByRecordNo(String recordNo) {
        QueryWrapper<AssetRepairEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 资产维修 驳回
     *  将资产状态 恢复为 原来的 状态
     * @param recordNo
     * @return
     */
    @Override
    @Transactional
    public R assetRebut(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }

        //批量修改  将当前操作的资产状态  恢复为  原来的状态  -10
        assetDao.batchAssetStatusReduce10(assetIds);

        //修改单号状态为 被驳回
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        return R.ok();
    }

    /**
     * 资产维修  同意
     *   将资产状态 修改为 维修
     * @param recordNo
     * @return
     */
    @Override
    @Transactional
    public R assetAgree(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }

        //3. 批量修改  当前操作的资产状态为 维修中
        assetDao.batchUpdateByIds(assetIds, AssetStatusEnum.UNDER_REPAIR.getCode());


        //4. 修改单号状态为 已同意
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());

        //修改审批表   已同意
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());

        //TODO  向资产处理表 中批量插入 维修 记录
        AssetRepairEntity assetRepair = this.getByRecordNo(recordNo);
        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetIds) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.ASSET_REPAIR.getCode());
            assetOperRecordEntity.setCreatedUserid(assetRepair.getCreatedUserid());
            assetOperRecordEntity.setCreatedUsername(assetRepair.getCreatedUsername());
            //处理内容 ： == 维修内容
            assetOperRecordEntity.setOperContent(assetRepair.getRepairContent());

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);

        return R.ok();
    }
}
