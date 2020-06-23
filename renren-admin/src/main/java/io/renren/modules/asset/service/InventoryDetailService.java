package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.InventoryDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-06-23 09:37:46
 */
public interface InventoryDetailService extends IService<InventoryDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

