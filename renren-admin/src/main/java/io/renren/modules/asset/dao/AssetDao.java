package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.asset.vo.AssetUseInfoVo;
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

    /**
     * 批量修改资产状态  并且保存当前状态
     * @param assets
     * @param status
     */
    void batchAssetStatusAndPreStatus(@Param("assets") List<Integer> assets, @Param("status") Integer status);

    /**
     * 批量修改资产状态为上一次保存的
     * @param assets
     */
    void batchAssetStatusByPreStatus(@Param("assets") List<Integer> assets);

    /**
     * 修改资产状态为上一次保存的
     * @param id
     * @return
     */
    int updateAssetStatusByPreStatus(@Param("id") Integer id);

    /**
     * 批量查找资产使用组织、使用人
     * @param assets
     * @return
     */
    List<AssetUseInfoVo> batchFindAssetUseInfo(@Param("assets") List<Integer> assets);


    void batchUpdateAssetInfoById(@Param("assetIds") List<Integer> assetIds, @Param("assetEntity") AssetEntity assetEntity);


    /**
     * 批量修改知产状态:
     *               若为闲置， 状态变为  在用。
     *               若为在用， 状态恢复。
     *               若为借用，状态恢复
     * @param assets
     */
    void batchAssetStatusByUseCase(@Param("assets") List<Integer> assets);

    /**
     * 批量插入 资产
     * @param assets
     * @return
     */
    int batchInsertAsset(@Param("assets") List<AssetEntity> assets);

    /**
     * 修改资产  其他信息
     * @param asset
     */
    int updateByOtherInfo(AssetEntity asset);

}
