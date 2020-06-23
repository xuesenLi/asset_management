package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * 盘点单状态  : 0未开始 1盘点中  2已结束
 * @author Mr.Li
 * @date 2020/6/23 - 10:34
 */
@Getter
public enum InventoryStatusEnum {

    NOT_START(0, "未开始"),

    IN_INVENTORY(1, "盘点中"),

    FINISHED(2, "已结束"),

    ;


    private Integer code;

    private String msg;

    InventoryStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
