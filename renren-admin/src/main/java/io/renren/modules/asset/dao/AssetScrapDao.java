package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetScrapEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-20 10:57:50
 */
@Mapper
public interface AssetScrapDao extends BaseMapper<AssetScrapEntity> {

    /**
     * 修改单号状态
     * @param recordNo
     * @param status
     */
    void updateByRecordStatus(@Param("recordNo") String recordNo, @Param("status") Integer status);


}
