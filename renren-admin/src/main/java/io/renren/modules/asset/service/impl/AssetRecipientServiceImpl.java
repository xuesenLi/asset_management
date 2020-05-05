package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetRecipientDao;
import io.renren.modules.asset.entity.AssetRecipientEntity;
import io.renren.modules.asset.service.AssetRecipientService;


@Service("assetRecipientService")
public class AssetRecipientServiceImpl extends ServiceImpl<AssetRecipientDao, AssetRecipientEntity> implements AssetRecipientService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetRecipientEntity> page = this.page(
                new Query<AssetRecipientEntity>().getPage(params),
                new QueryWrapper<AssetRecipientEntity>()
        );

        return new PageUtils(page);
    }

}
