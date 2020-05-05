package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/3/31 - 11:23
 *     记录状态：  0 待审批、1 已同意、2 被驳回、 3 --
 */
@Getter
public enum RecordStatusEnum {

    PENDING_APPROVAL(0, "待审批"),

    AGREED(1, "已同意"),

    BE_DISMISSED(2, "被驳回"),

    NO_STATUS(3, "--"),


    ;


    private Integer code;

    private String des;

    RecordStatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
