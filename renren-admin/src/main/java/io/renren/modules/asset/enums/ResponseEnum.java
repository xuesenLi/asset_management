package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/4/10 - 10:59
 */
@Getter
public enum ResponseEnum {

    SUCCESS(0, "成功"),

    ERROR(-1, "服务端错误"),

    PARAM_ERROR(3, "参数错误"),

    DELETE_ASSET_FAIL(4, "只能够删除闲置和报废的资产呢"),



    ;

    private Integer code;
    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
