package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.dao.AssetOperRecordDao;
import io.renren.modules.asset.dao.RecordDetailDao;
import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.entity.RecordDetailEntity;
import io.renren.modules.asset.enums.AssetOperTypeEnum;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.RecordStatusEnum;
import io.renren.modules.asset.enums.RecordTypeEnum;
import io.renren.modules.asset.utils.GeneratorRecordNo;
import io.renren.modules.asset.vo.AssetUseInfoVo;
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

import io.renren.modules.asset.dao.AssetRefundDao;
import io.renren.modules.asset.entity.AssetRefundEntity;
import io.renren.modules.asset.service.AssetRefundService;
import org.springframework.transaction.annotation.Transactional;


@Service("assetRefundService")
@Slf4j
public class AssetRefundServiceImpl extends ServiceImpl<AssetRefundDao, AssetRefundEntity> implements AssetRefundService {

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private AssetOperRecordDao assetOperRecordDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetRefundEntity> page = this.page(
                new Query<AssetRefundEntity>().getPage(params),
                new QueryWrapper<AssetRefundEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetRefundEntity assetRefund) {
        //1. 生成单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_REFUND);
        assetRefund.setRecordNo(recordNo);
        assetRefund.setAssetNum(assetRefund.getAssets().size());
        //设置单号状态   已同意
        assetRefund.setRecordStatus(RecordStatusEnum.AGREED.getCode());
        log.info("assetRefund = {}", assetRefund);
        //2. 入库
        //退还单号 入库
        baseMapper.insert(assetRefund);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetRefund.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //通过assetId 批量查找当前使用组织、使用人 。  AssetUseInfoVo
        List<AssetUseInfoVo> assetUseInfoVos = assetDao.batchFindAssetUseInfo(assetRefund.getAssets());
        Map<Integer, AssetUseInfoVo> useMap = assetUseInfoVos.stream()
                .collect(Collectors.toMap(AssetUseInfoVo::getId, assetUseInfoVo -> assetUseInfoVo));

        //资产操作记录 批量入库
        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetRefund.getAssets()) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.ASSET_REFUND.getCode());
            assetOperRecordEntity.setCreatedUserid(assetRefund.getCreatedUserid());
            assetOperRecordEntity.setCreatedUsername(assetRefund.getCreatedUsername());
            //退还 处理内容 ： == 使用组织由 “ 。。。” 变更成  ‘ ’  ，使用人由 “。。” 变更成 ‘’ “”
            assetOperRecordEntity.setOperContent("使用组织由 '" + useMap.get(assetId).getUseOrgName()
                    + " ' 变更成 ' ', 使用人由 '" + useMap.get(assetId).getEmpName() +" ' 变更成 ' '");

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);

        //批量修改  当前操作的资产状态为 闲置
        assetDao.batchUpdateByIds(assetRefund.getAssets(), AssetStatusEnum.IDLE.getCode());
    }

}
