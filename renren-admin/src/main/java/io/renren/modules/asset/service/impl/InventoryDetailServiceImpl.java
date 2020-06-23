package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.InventoryDetailDao;
import io.renren.modules.asset.entity.InventoryDetailEntity;
import io.renren.modules.asset.service.InventoryDetailService;


@Service("inventoryDetailService")
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailDao, InventoryDetailEntity> implements InventoryDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InventoryDetailEntity> page = this.page(
                new Query<InventoryDetailEntity>().getPage(params),
                new QueryWrapper<InventoryDetailEntity>()
        );

        return new PageUtils(page);
    }

}
