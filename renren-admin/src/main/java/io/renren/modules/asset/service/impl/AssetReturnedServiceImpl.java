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

import io.renren.modules.asset.dao.AssetReturnedDao;
import io.renren.modules.asset.entity.AssetReturnedEntity;
import io.renren.modules.asset.service.AssetReturnedService;


@Service("assetReturnedService")
@Slf4j
public class AssetReturnedServiceImpl extends ServiceImpl<AssetReturnedDao, AssetReturnedEntity> implements AssetReturnedService {

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
        IPage<AssetReturnedEntity> page = this.page(
                new Query<AssetReturnedEntity>().getPage(params),
                new QueryWrapper<AssetReturnedEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void insert(AssetReturnedEntity assetReturned) {
        //1. 生成单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_RETURNED);
        assetReturned.setRecordNo(recordNo);
        assetReturned.setAssetNum(assetReturned.getAssets().size());
        //设置单号状态   已同意
        assetReturned.setRecordStatus(RecordStatusEnum.AGREED.getCode());
        log.info("assetReturned = {}", assetReturned);
        //2. 入库
        //退还单号 入库
        baseMapper.insert(assetReturned);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetReturned.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //通过assetId 批量查找当前使用组织、使用人 。  AssetUseInfoVo
        List<AssetUseInfoVo> assetUseInfoVos = assetDao.batchFindAssetUseInfo(assetReturned.getAssets());
        Map<Integer, AssetUseInfoVo> useMap = assetUseInfoVos.stream()
                .collect(Collectors.toMap(AssetUseInfoVo::getId, assetUseInfoVo -> assetUseInfoVo));

        //资产操作记录 批量入库
        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetReturned.getAssets()) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.ASSET_RETURNED.getCode());
            assetOperRecordEntity.setCreatedUserid(assetReturned.getCreatedUserid());
            assetOperRecordEntity.setCreatedUsername(assetReturned.getCreatedUsername());
            //退还 处理内容 ： == 使用组织由 “ 。。。” 变更成  ‘ ’  ，使用人由 “。。” 变更成 ‘’ “”
            assetOperRecordEntity.setOperContent("使用组织由 '" + useMap.get(assetId).getUseOrgName()
                    + " ' 变更成 ' ', 使用人由 '" + useMap.get(assetId).getEmpName() +" ' 变更成 ' '");

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);


        //批量修改  当前操作的资产状态为 闲置
        assetDao.batchUpdateByIds(assetReturned.getAssets(), AssetStatusEnum.IDLE.getCode());

    }

}
