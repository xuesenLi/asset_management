package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 17:16:23
 */
public interface AssetCategoryService extends IService<AssetCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);


    List<AssetCategoryEntity> queryList(Map<String, Object> map);

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Integer> queryCategoryList(Integer parentId);

    /**
     * 获取子部门ID，用于数据过滤
     */
    List<Integer> getSubCategoryIdList(Integer categoryId);
}

