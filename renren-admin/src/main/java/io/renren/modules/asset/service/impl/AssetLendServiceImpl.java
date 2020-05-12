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
        //退还单号 入库
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

        //批量修改  当前操作的资产状态为 借用
        assetDao.batchUpdateByIds(assetLend.getAssets(), AssetStatusEnum.BORROWING.getCode());
    }

}
