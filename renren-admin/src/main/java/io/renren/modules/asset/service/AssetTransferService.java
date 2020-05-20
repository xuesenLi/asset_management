package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.asset.entity.AssetTransferEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-18 11:09:07
 */
public interface AssetTransferService extends IService<AssetTransferEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 调拨记录
     * @param assetTransfer
     */
    void insert(AssetTransferEntity assetTransfer);

    R assetRebut(String recordNo);

    R assetAgree(String recordNo);


    /**
     *  通过单号查找
     * @param recordNo
     * @return
     */
    AssetTransferEntity getByRecordNo(String recordNo);

}

