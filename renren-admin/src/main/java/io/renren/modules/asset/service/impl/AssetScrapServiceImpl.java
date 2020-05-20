package io.renren.modules.asset.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.asset.dao.AssetAuditDao;
import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.dao.RecordDetailDao;
import io.renren.modules.asset.entity.AssetAuditEntity;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.entity.RecordDetailEntity;
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

import io.renren.modules.asset.dao.AssetScrapDao;
import io.renren.modules.asset.entity.AssetScrapEntity;
import io.renren.modules.asset.service.AssetScrapService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("assetScrapService")
@Slf4j
public class AssetScrapServiceImpl extends ServiceImpl<AssetScrapDao, AssetScrapEntity> implements AssetScrapService {

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private AssetAuditDao assetAuditDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetScrapEntity> page = this.page(
                new Query<AssetScrapEntity>().getPage(params),
                new QueryWrapper<AssetScrapEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetScrapEntity assetScrap) {
        //1. 生成报废 单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_SCRAP);
        assetScrap.setRecordNo(recordNo);
        assetScrap.setAssetNum(assetScrap.getAssets().size());

        //2. 设置单号状态   待审批
        assetScrap.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());

        log.info("assetScrap = {}", assetScrap);
        //2. 入库
        //调拨单号 入库
        baseMapper.insert(assetScrap);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetScrap.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //批量修改  将当前操作的资产状态改为 待审批
        assetDao.batchUpdateByIds(assetScrap.getAssets(), AssetStatusEnum.PENDING_APPROVAL.getCode());

        //向审批表中 插入 待审批 的 记录
        AssetAuditEntity assetAuditEntity = new AssetAuditEntity();
        assetAuditEntity.setRecordNo(recordNo);
        assetAuditEntity.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());
        assetAuditEntity.setRecordType(RecordTypeEnum.ASSET_SCRAP.getCode());
        assetAuditEntity.setCreatedUsername(assetScrap.getCreatedUsername());

        assetAuditDao.insert(assetAuditEntity);
    }


    @Override
    public AssetScrapEntity getByRecordNo(String recordNo) {
        QueryWrapper<AssetScrapEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 资产报废 驳回
     * 将资产状态 恢复为 原来的 闲置
     * @param recordNo
     * @return
     */
    @Override
    public R assetRebut(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }

        //批量修改  将当前操作的资产状态  恢复为  闲置
        assetDao.batchUpdateByIds(assetIds, AssetStatusEnum.IDLE.getCode());

        //修改单号状态为 被驳回
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        return R.ok();
    }

    /**
     * 资产报废 同意
     * 将资产状态 修改为 报废
     * @param recordNo
     * @return
     */
    @Override
    public R assetAgree(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }

        //3. 批量修改  当前操作的资产状态为 报废
        assetDao.batchUpdateByIds(assetIds, AssetStatusEnum.SCRAP.getCode());


        //4. 修改单号状态为 已同意
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());

        //修改审批表   已同意
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());
        return R.ok();
    }
}
