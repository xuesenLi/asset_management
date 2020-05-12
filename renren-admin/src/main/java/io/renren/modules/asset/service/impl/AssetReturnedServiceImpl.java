package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.dao.RecordDetailDao;
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

        //批量修改  当前操作的资产状态为 闲置
        assetDao.batchUpdateByIds(assetReturned.getAssets(), AssetStatusEnum.IDLE.getCode());

    }

}
