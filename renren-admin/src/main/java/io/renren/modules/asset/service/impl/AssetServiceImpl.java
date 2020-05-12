package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.RecordDetailDao;
import io.renren.modules.asset.entity.RecordDetailEntity;
import io.renren.modules.asset.enums.AssetStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.service.AssetService;


@Service("assetService")
public class AssetServiceImpl extends ServiceImpl<AssetDao, AssetEntity> implements AssetService {

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 闲置的
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeXZ(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().eq("asset_status", AssetStatusEnum.IDLE.getCode())
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 在用的
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeZY(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().eq("asset_status", AssetStatusEnum.IN_USE.getCode())
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByIn(Map<String, Object> params, String recordNo) {
        //1. 从单号详情表中查找 单号对应的资产id
        QueryWrapper<RecordDetailEntity> recordDetailQueryWrapper = new QueryWrapper<>();
        recordDetailQueryWrapper.eq("record_no", recordNo);
        List<RecordDetailEntity> recordDetailEntities = recordDetailDao.selectList(recordDetailQueryWrapper);

        List<Integer> assetIds = recordDetailEntities.stream().map(RecordDetailEntity::getAssetId).collect(Collectors.toList());

        //2. 通过资产id 返回资产信息集合
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().in("id", assetIds)
        );

        return new PageUtils(page);
    }

}
