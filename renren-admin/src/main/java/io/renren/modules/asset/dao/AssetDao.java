package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 14:32:22
 */
@Mapper
public interface AssetDao extends BaseMapper<AssetEntity> {

    /**
     * 批量修改资产状态为 在用
     * @param assets
     */
    void batchUpdateByIds(@Param("assets") List<Integer> assets, @Param("status") Integer status);

}
