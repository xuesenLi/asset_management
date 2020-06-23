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
import io.renren.modules.asset.vo.AssetUsedVo;
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

import io.renren.modules.asset.dao.AssetLendDao;
import io.renren.modules.asset.entity.AssetLendEntity;
import io.renren.modules.asset.service.AssetLendService;
import org.springframework.transaction.annotation.Transactional;


@Service("assetLendService")
@Slf4j
public class AssetLendServiceImpl extends ServiceImpl<AssetLendDao, AssetLendEntity> implements AssetLendService {

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
        IPage<AssetLendEntity> page = this.page(
                new Query<AssetLendEntity>().getPage(params),
                new QueryWrapper<AssetLendEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetLendEntity assetLend) {
        //1. 生成单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_LEND);
        assetLend.setRecordNo(recordNo);
        assetLend.setAssetNum(assetLend.getAssets().size());
        //设置单号状态   已同意
        assetLend.setRecordStatus(RecordStatusEnum.AGREED.getCode());
        log.info("assetLend = {}", assetLend);
        //2. 入库
        //借用单号 入库
        baseMapper.insert(assetLend);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetLend.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //资产操作记录 批量入库
        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetLend.getAssets()) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.ASSET_LEND.getCode());
            assetOperRecordEntity.setCreatedUserid(assetLend.getCreatedUserid());
            assetOperRecordEntity.setCreatedUsername(assetLend.getCreatedUsername());
            //借用 处理内容 ： == 使用组织由 ‘ ’ 变更成 。。。” ，使用人由‘ ’ 变更成 “。。。i”
            assetOperRecordEntity.setOperContent("使用组织由 ' ' 变更成 '"+assetLend.getUseOrgName() + "', 使用人由 ' ' 变更成 '"+ assetLend.getEmpName()+ "'");

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);


        //批量修改  当前操作的资产状态为 借用
        AssetUsedVo assetUsedVo = new AssetUsedVo();
        assetUsedVo.setEmpId(assetLend.getEmpId());
        assetUsedVo.setEmpName(assetLend.getEmpName());
        assetUsedVo.setUseOrgId(assetLend.getUseOrgId());
        assetUsedVo.setUseOrgName(assetLend.getUseOrgName());
        assetUsedVo.setAssetStatus(AssetStatusEnum.BORROWING.getCode());
        assetDao.batchUpdateByIds1(assetLend.getAssets(), assetUsedVo);
    }

}
