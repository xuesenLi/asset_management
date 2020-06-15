package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.asset.entity.AssetRepairEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
public interface AssetRepairService extends IService<AssetRepairEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 维修 单号
     * @param assetRepair
     */
    void insert(AssetRepairEntity assetRepair);

    /**
     * 通过单号查找
     * @param recordNo
     * @return
     */
    AssetRepairEntity getByRecordNo(String recordNo);


    /**
     * 资产维修  驳回
     * @param recordNo
     * @return
     */
    R assetRebut(String recordNo);

    /**
     * 资产维修 同意
     * @param recordNo
     * @return
     */
    R assetAgree(String recordNo);
}

