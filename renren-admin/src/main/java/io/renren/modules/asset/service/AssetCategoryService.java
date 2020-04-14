package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetCategoryEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 16:41:56
 */
public interface AssetCategoryService extends IService<AssetCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

