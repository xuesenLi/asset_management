package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/4/8 - 10:30
 */
@Getter
public enum StatusEnum {

    VALID(1, "有效"),

    INVALID(2, "无效"),

    ;

    private Integer code;

    private String des;

    StatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
