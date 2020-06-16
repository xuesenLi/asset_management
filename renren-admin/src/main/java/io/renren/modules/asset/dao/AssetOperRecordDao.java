package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetOperRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
@Mapper
public interface AssetOperRecordDao extends BaseMapper<AssetOperRecordEntity> {

    /**
     * 批量插入 资产处理记录
     * @param assetOperRecords
     */
    void batchInsert(@Param("assetOperRecords") List<AssetOperRecordEntity> assetOperRecords);


    /**
     * 通过资产id 查找记录中为wei'xiu
     * @param assetId
     * @return
     */
    String findRecordNoByAssetId(@Param("assetId") Integer assetId);

}
