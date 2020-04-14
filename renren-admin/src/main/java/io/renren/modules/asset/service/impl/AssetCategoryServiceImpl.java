package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetCategoryDao;
import io.renren.modules.asset.entity.AssetCategoryEntity;
import io.renren.modules.asset.service.AssetCategoryService;


@Service("assetCategoryService")
public class AssetCategoryServiceImpl extends ServiceImpl<AssetCategoryDao, AssetCategoryEntity> implements AssetCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetCategoryEntity> page = this.page(
                new Query<AssetCategoryEntity>().getPage(params),
                new QueryWrapper<AssetCategoryEntity>()
        );

        return new PageUtils(page);
    }

}
