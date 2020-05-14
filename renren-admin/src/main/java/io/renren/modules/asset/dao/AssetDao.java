package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetChangeEntity;
import io.renren.modules.asset.entity.AssetEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.asset.vo.AssetUsedVo;
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
     * 批量修改资产状态
     * 若修改的资产状态为闲置 0   需要将领用使用组织和 使用人清空
     * @param assets
     */
    void batchUpdateByIds(@Param("assets") List<Integer> assets, @Param("status") Integer status);

    /**
     *
     * @param assets
     * @param assetUsedVo
     */
    void batchUpdateByIds1(@Param("assets") List<Integer> assets, @Param("assetUsedVo") AssetUsedVo assetUsedVo);

    /**
     * 批量修改资产状态为 待审批  status + 10
     * @param assets
     */
    void batchAssetStatusAdd10(@Param("assets") List<Integer> assets);

    /**
     * 批量恢复资产状态   status - 10
     * @param assets
     */
    void batchAssetStatusReduce10(@Param("assets") List<Integer> assets);

}
