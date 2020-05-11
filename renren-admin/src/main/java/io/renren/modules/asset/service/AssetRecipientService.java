package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetRecipientEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-24 10:07:32
 */
public interface AssetRecipientService extends IService<AssetRecipientEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void insert(AssetRecipientEntity assetRecipient);

    AssetRecipientEntity selectByRecordNo(String recordNo);
}

