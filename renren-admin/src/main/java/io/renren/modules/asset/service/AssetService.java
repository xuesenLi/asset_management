package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 14:32:22
 */
public interface AssetService extends IService<AssetEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

