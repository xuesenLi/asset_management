package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.vo.ResponseVo;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 14:32:22
 */
public interface AssetService extends IService<AssetEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取资产状态为 闲置的
     * @param params
     * @return
     */
    PageUtils queryPageByTypeXZ(Map<String, Object> params);

    /**
     * 获取资产状态为 在用的
     * @param params
     * @return
     */
    PageUtils queryPageByTypeZY(Map<String, Object> params);

    /**
     * 获取资产状态为 在用的
     * @param params
     * @returnJ
     */
    PageUtils queryPageByTypeJY(Map<String, Object> params);

    /**
     * 获取资产状态为 闲置、在用、借用
     * @param params
     * @return
     */
    PageUtils queryPageByTypeXZJ(Map<String, Object> params);

    PageUtils queryPageByIn(Map<String, Object> params, String recordNo);

}

