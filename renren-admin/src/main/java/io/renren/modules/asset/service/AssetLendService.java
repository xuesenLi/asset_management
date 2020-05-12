package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetLendEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 13:28:50
 */
public interface AssetLendService extends IService<AssetLendEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 借用记录
     * @param assetLend
     */
    void insert(AssetLendEntity assetLend);
}

