package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/5/21 - 15:28
 */
@Getter
public enum  AssetOperTypeEnum {
    ASSET_RECIPIENT(0, "领用"),

    ASSET_REFUND(1, "退还"),

    ASSET_LEND(2, "借用"),

    ASSET_RETURNED(3, "归还"),

    ASSET_CHANGE(4, "变更"),

    ASSET_TRANSFER(5, "调拨"),

    ASSET_REPAIR(6, "维修"),

    ASSET_SCRAP(7, "报废"),

    REPAIR_FINISH(8, "维修完成"),

    IN_STORAGE(9, "入库"),

    ;

    private Integer code;

    private String  des;

    AssetOperTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }
}
