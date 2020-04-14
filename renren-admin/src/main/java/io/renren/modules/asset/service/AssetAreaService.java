package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetAreaEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-10 13:59:02
 */
public interface AssetAreaService extends IService<AssetAreaEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

