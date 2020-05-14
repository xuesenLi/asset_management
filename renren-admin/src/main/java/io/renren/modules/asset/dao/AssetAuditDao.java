package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.AssetAuditEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
@Mapper
public interface AssetAuditDao extends BaseMapper<AssetAuditEntity> {

    /**
     * 修改单号状态
     * @param recordNo
     * @param status
     */
    void updateByRecordStatus(@Param("recordNo") String recordNo, @Param("status") Integer status);
}
