package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetAuditEntity;

import java.util.Map;

/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
public interface AssetAuditService extends IService<AssetAuditEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

