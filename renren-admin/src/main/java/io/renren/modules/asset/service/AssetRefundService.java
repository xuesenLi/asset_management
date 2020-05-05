package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetRefundEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-24 10:07:31
 */
public interface AssetRefundService extends IService<AssetRefundEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

