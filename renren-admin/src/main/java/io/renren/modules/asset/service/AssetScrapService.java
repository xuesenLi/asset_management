package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.asset.entity.AssetScrapEntity;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-20 10:57:50
 */
public interface AssetScrapService extends IService<AssetScrapEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 报废记录
     * @param assetScrap
     */
    void insert(AssetScrapEntity assetScrap);

    /**
     * 通过单号查找
     * @param recordNo
     * @return
     */
    AssetScrapEntity getByRecordNo(String recordNo);


    /**
     * 资产报废 驳回
     * @param recordNo
     * @return
     */
    R assetRebut(String recordNo);

    /**
     * 资产报废 同意
     * @param recordNo
     * @return
     */
    R assetAgree(String recordNo, SysUserEntity user);
}

