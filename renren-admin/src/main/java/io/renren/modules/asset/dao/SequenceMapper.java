package io.renren.modules.asset.dao;

import io.renren.modules.asset.entity.Sequence;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr.Li
 * @date 2020/5/9 - 9:34
 */
@Mapper
public interface SequenceMapper {
    int deleteByPrimaryKey(String name);

    int insert(Sequence record);

    int insertSelective(Sequence record);

    Sequence selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(Sequence record);

    int updateByPrimaryKey(Sequence record);

    /**
     * 拿到序列号并修改
     *
     * @param name
     * @return
     */
    Sequence getSequenceByName(String name);
}
