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

        //批量修改  当前操作的资产状态为 闲置
        assetDao.batchUpdateByIds(assetRefund.getAssets(), AssetStatusEnum.IDLE.getCode());
    }

}
