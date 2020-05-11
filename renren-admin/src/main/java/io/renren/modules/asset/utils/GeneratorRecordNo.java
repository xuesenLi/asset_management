package io.renren.modules.asset.utils;

import io.renren.modules.asset.dao.SequenceMapper;
import io.renren.modules.asset.entity.Sequence;
import io.renren.modules.asset.enums.RecordTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mr.Li
 * @date 2020/5/9 - 11:32
 */
@Component
public class GeneratorRecordNo {

    @Autowired
    private SequenceMapper sequenceMapper;

    public String generateRecordNo(RecordTypeEnum recordTypeEnum){
        StringBuilder stringBuilder = new StringBuilder();
        //订单号18位
        //前1~2位标识 字母
        stringBuilder.append(recordTypeEnum.getPrefix());
        //前3 ~ 10 位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        stringBuilder.append(now.format(DateTimeFormatter.ISO_DATE).replace("-", ""));

        //中间11 ~ 16 位为自增序列
        //获取当前的Sequence
        int CurrentSequence = 0;
        Sequence sequence = sequenceMapper.getSequenceByName(recordTypeEnum.getPrefix());
        CurrentSequence = sequence.getCurrentValue();
        sequence.setCurrentValue(sequence.getCurrentValue() + sequence.getStep());
        //使当前的sequence 增加 相应step
        sequenceMapper.updateByPrimaryKeySelective(sequence);

        stringBuilder.append(String.format("%06d",CurrentSequence).toString());

        //最后2位为分库分表位, 暂时写死
        stringBuilder.append("77");

        return stringBuilder.toString();
    }
}
