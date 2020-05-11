package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.dao.RecordDetailDao;
import io.renren.modules.asset.dao.SequenceMapper;
import io.renren.modules.asset.entity.RecordDetailEntity;
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

import io.renren.modules.asset.dao.AssetRecipientDao;
import io.renren.modules.asset.entity.AssetRecipientEntity;
import io.renren.modules.asset.service.AssetRecipientService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("assetRecipientService")
public class AssetRecipientServiceImpl extends ServiceImpl<AssetRecipientDao, AssetRecipientEntity> implements AssetRecipientService {

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private AssetRecipientDao assetRecipientDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetRecipientEntity> page = this.page(
                new Query<AssetRecipientEntity>().getPage(params),
                new QueryWrapper<AssetRecipientEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetRecipientEntity assetRecipient) {
        //生成单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_RECIPIENT);
        assetRecipient.setRecordNo(recordNo);
        assetRecipient.setAssetNum(assetRecipient.getAssets().size());
        //设置单号状态   已同意
        assetRecipient.setRecordStatus(RecordStatusEnum.AGREED.getCode());
        log.info("assetRecipinent = {}", assetRecipient);
        //入库
        baseMapper.insert(assetRecipient);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetRecipient.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //批量修改  当前操作的资产状态为 在用
        assetDao.batchUpdateByIds(assetRecipient.getAssets());
    }

    @Override
    public AssetRecipientEntity selectByRecordNo(String recordNo) {
        QueryWrapper<AssetRecipientEntity> assetRecipientEntityQueryWrapper = new QueryWrapper<>();
        assetRecipientEntityQueryWrapper.eq("record_no", recordNo);

        return assetRecipientDao.selectOne(assetRecipientEntityQueryWrapper);
    }

}
