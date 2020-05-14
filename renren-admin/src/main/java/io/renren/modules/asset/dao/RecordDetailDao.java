package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.RecordDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-09 09:55:29
 */
@Mapper
public interface RecordDetailDao extends BaseMapper<RecordDetailEntity> {

    /**
     * 批量插入
     * @param recordDetails
     */
    void batchInsert(@Param("recordDetails") List<RecordDetailEntity> recordDetails);

    /**
     * 通过单号查看详情 资产列表
     * @param recordNo
     * @return
     */
    List<Integer> selectByRecordNo(@Param("recordNo") String recordNo);
}
