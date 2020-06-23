package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.asset.entity.AssetChangeEntity;
import io.renren.modules.asset.vo.AssetChangeVo;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
public interface AssetChangeService extends IService<AssetChangeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入 变更 记录
     * @param assetChange
     */
    void insert(AssetChangeEntity assetChange);

    /**
     * 通过单号查找
     * @param recordNo
     * @return
     */
    AssetChangeVo getByRecordNo(String recordNo);

    /**
     * 驳回
     * @param recordNo
     * @return
     */
    R assetRebut(String recordNo);

    /**
     * 同意
     * @param recordNo
     * @param user
     * @return
     */
    R assetAgree(String recordNo, SysUserEntity user);
}

