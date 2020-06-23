package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 *
 * 资产盘点状态： 状态；0未盘；1已盘；2盘盈; 3盘亏;4正常;
 * @author Mr.Li
 * @date 2020/6/23 - 10:40
 */
@Getter
public enum  AssetInventoryStatusEnum {

    NOT_INVENTORY(0, "未盘"),

    ALREADY_INVENTORY(1, "已盘"),

    INVENTORY_SURPLUS(2, "盘盈"),

    INVENTORY_LOSS(3, "盘亏"),

    NORMAL(4, "正常"),

    ;


    private Integer code;

    private String des;

    AssetInventoryStatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
