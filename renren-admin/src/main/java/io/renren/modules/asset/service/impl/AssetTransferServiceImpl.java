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

import io.renren.modules.asset.dao.AssetTransferDao;
import io.renren.modules.asset.entity.AssetTransferEntity;
import io.renren.modules.asset.service.AssetTransferService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("assetTransferService")
@Slf4j
public class AssetTransferServiceImpl extends ServiceImpl<AssetTransferDao, AssetTransferEntity> implements AssetTransferService {

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
        IPage<AssetTransferEntity> page = this.page(
                new Query<AssetTransferEntity>().getPage(params),
                new QueryWrapper<AssetTransferEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetTransferEntity assetTransfer) {
        //1. 生成调拨 单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_TRANSFER);
        assetTransfer.setRecordNo(recordNo);
        assetTransfer.setAssetNum(assetTransfer.getAssets().size());

        //2. 设置单号状态   待审批
        assetTransfer.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());

        log.info("assetTransfer = {}", assetTransfer);
        //2. 入库
        //调拨单号 入库
        baseMapper.insert(assetTransfer);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetTransfer.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，所以将状态的值 +10 后面恢复在 -10
        assetDao.batchAssetStatusAdd10(assetTransfer.getAssets());

        //向审批表中 插入 待审批 的 记录
        AssetAuditEntity assetAuditEntity = new AssetAuditEntity();
        assetAuditEntity.setRecordNo(recordNo);
        assetAuditEntity.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());
        assetAuditEntity.setRecordType(RecordTypeEnum.ASSET_TRANSFER.getCode());
        assetAuditEntity.setCreatedUsername(assetTransfer.getCreatedUsername());

        assetAuditDao.insert(assetAuditEntity);

    }

    /**
     * 资产调拨 驳回  将资产状态 恢复为 原来的
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

        //批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，所以将状态的值 +10 后面恢复在 -10
        assetDao.batchAssetStatusReduce10(assetIds);

        //修改单号状态为 被驳回
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        return R.ok();
    }

    /**
     * 调拨 单号 同意
     *  如果调拨填了使用组织，    将状态变为 **在用**
     * ​如果调拨没有填使用组织，  将状态变为**闲置**
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
        //2. 修改资产的相关属性
        // 获取修改类容 assetEntity
        AssetEntity assetEntity = new AssetEntity();
        QueryWrapper<AssetTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        AssetTransferEntity assetTransferEntity = baseMapper.selectOne(queryWrapper);

        if(!StringUtils.isEmpty(assetTransferEntity.getOrgId())){
            assetEntity.setOrgName(assetTransferEntity.getOrgName());
            assetEntity.setOrgId(assetTransferEntity.getOrgId());
        }
        if(!StringUtils.isEmpty(assetTransferEntity.getUseOrgId())){
            assetEntity.setUseOrgId(assetTransferEntity.getUseOrgId());
            assetEntity.setUseOrgName(assetTransferEntity.getUseOrgName());
        }
        if(!StringUtils.isEmpty(assetTransferEntity.getEmpId())){
            assetEntity.setEmpId(assetTransferEntity.getEmpId());
            assetEntity.setEmpName(assetTransferEntity.getEmpName());
        }

        //根据资产id 批量修改 资产内容
        for (Integer id : assetIds) {
            assetEntity.setId(id);
            assetDao.updateById(assetEntity);
        }

        //3. 批量修改  当前操作的资产状态为 待审批
        //恢复为 在用
        if(!StringUtils.isEmpty(assetTransferEntity.getUseOrgId())){
            assetDao.batchUpdateByIds(assetIds, AssetStatusEnum.IN_USE.getCode());
        }
        else{
            //恢复为 闲置
            assetDao.batchUpdateByIds(assetIds, AssetStatusEnum.IDLE.getCode());
        }

        //4. 修改单号状态为 已同意
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());

        //修改审批表   已同意
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());
        return R.ok();
    }

    @Override
    public AssetTransferEntity getByRecordNo(String recordNo) {
        QueryWrapper<AssetTransferEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        return baseMapper.selectOne(queryWrapper);
    }
}
