package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetReturnedEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 13:28:50
 */
public interface AssetReturnedService extends IService<AssetReturnedEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 归还 记录
     * @param assetReturned
     */
    void insert(AssetReturnedEntity assetReturned);
}

