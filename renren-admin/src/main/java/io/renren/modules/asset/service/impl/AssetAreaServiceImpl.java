package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetAreaDao;
import io.renren.modules.asset.entity.AssetAreaEntity;
import io.renren.modules.asset.service.AssetAreaService;


@Service("assetAreaService")
public class AssetAreaServiceImpl extends ServiceImpl<AssetAreaDao, AssetAreaEntity> implements AssetAreaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetAreaEntity> page = this.page(
                new Query<AssetAreaEntity>().getPage(params),
                new QueryWrapper<AssetAreaEntity>()
        );

        return new PageUtils(page);
    }

}
