package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetAuditDao;
import io.renren.modules.asset.entity.AssetAuditEntity;
import io.renren.modules.asset.service.AssetAuditService;


@Service("assetAuditService")
public class AssetAuditServiceImpl extends ServiceImpl<AssetAuditDao, AssetAuditEntity> implements AssetAuditService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetAuditEntity> page = this.page(
                new Query<AssetAuditEntity>().getPage(params),
                new QueryWrapper<AssetAuditEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

}
