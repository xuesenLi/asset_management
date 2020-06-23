package io.renren.modules.asset.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.InventoryTaskDao;
import io.renren.modules.asset.entity.InventoryTaskEntity;
import io.renren.modules.asset.service.InventoryTaskService;


@Service("inventoryTaskService")
public class InventoryTaskServiceImpl extends ServiceImpl<InventoryTaskDao, InventoryTaskEntity> implements InventoryTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InventoryTaskEntity> page = this.page(
                new Query<InventoryTaskEntity>().getPage(params),
                new QueryWrapper<InventoryTaskEntity>()
        );

        return new PageUtils(page);
    }

}
