package io.renren.modules.asset.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.asset.dao.*;
import io.renren.modules.asset.entity.*;
import io.renren.modules.asset.enums.AssetOperTypeEnum;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.RecordStatusEnum;
import io.renren.modules.asset.enums.RecordTypeEnum;
import io.renren.modules.asset.utils.GeneratorRecordNo;
import io.renren.modules.asset.vo.AssetUseInfoVo;
import io.renren.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

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
    @Autowired
    private AssetOperRecordDao assetOperRecordDao;


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

        //批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，并保存本次状态。
        assetDao.batchAssetStatusAndPreStatus(assetTransfer.getAssets(), AssetStatusEnum.PENDING_APPROVAL.getCode());

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

        //批量修改  将当前操作的资产状态 恢复
        assetDao.batchAssetStatusByPreStatus(assetIds);

        //修改单号状态为 被驳回
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        return R.ok();
    }

    /**
     *  调拨 单号 同意
     *  如果调拨填了使用组织，    将状态变为 **在用**
     * ​如果调拨没有填使用组织，  将状态变为**闲置**
     *  并且插入资产操作详情表
     * @param recordNo
     * @return
     */
    @Override
    public R assetAgree(String recordNo, SysUserEntity user) {
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
        //TODO 插入知产操作详情表
        //通过assetId 批量查找当前使用组织、使用人 。  AssetUseInfoVo
        List<AssetUseInfoVo> assetUseInfoVos = assetDao.batchFindAssetUseInfo(assetIds);
        Map<Integer, AssetUseInfoVo> useMap = assetUseInfoVos.stream()
                .collect(Collectors.toMap(AssetUseInfoVo::getId, assetUseInfoVo -> assetUseInfoVo));

        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetIds) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.ASSET_TRANSFER.getCode());
            assetOperRecordEntity.setCreatedUserid(user.getUserId().intValue());
            assetOperRecordEntity.setCreatedUsername(user.getUsername());
            // 调拨处理内容设置
            assetOperRecordEntity.setOperContent(
                    "所属组织由 '" + useMap.get(assetId).getOrgName() + " ' 变更成 '" + assetEntity.getOrgName() + " '" +
                    ", 使用组织由 '" + useMap.get(assetId).getUseOrgName() + " ' 变更成 '" + assetEntity.getUseOrgName() + " '" +
                            ", 使用人由 '" + useMap.get(assetId).getEmpName() +" ' 变更成 '" + assetEntity.getEmpName() + " '");

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);


        //根据资产id 批量修改 资产内容
        assetDao.batchUpdateAssetInfoById(assetIds, assetEntity);


        //3. 批量修改  当前操作的资产状态
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
