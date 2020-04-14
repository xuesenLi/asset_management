package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
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

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>()
        );

        return new PageUtils(page);
    }

}
