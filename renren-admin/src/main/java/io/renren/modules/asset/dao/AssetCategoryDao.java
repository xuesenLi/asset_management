package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetCategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 17:16:23
 */
@Mapper
public interface AssetCategoryDao extends BaseMapper<AssetCategoryEntity> {

    List<AssetCategoryEntity> queryList(Map<String, Object> params);

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Integer> queryCategoryIdList(Integer parentId);
}
