package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetRefundDao;
import io.renren.modules.asset.entity.AssetRefundEntity;
import io.renren.modules.asset.service.AssetRefundService;


@Service("assetRefundService")
public class AssetRefundServiceImpl extends ServiceImpl<AssetRefundDao, AssetRefundEntity> implements AssetRefundService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetRefundEntity> page = this.page(
                new Query<AssetRefundEntity>().getPage(params),
                new QueryWrapper<AssetRefundEntity>()
        );

        return new PageUtils(page);
    }

}
