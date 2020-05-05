package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/3/31 - 13:52
 * 记录类型： 0 领用、 1 退还、 2 借用、   3 归还 、 4 变更、  5 调拨 、 6 维修、  7 报废
 */
@Getter
public enum RecordTypeEnum {

    ASSET_RECIPIENT(0, "领用", "LY"),

    ASSET_REFUND(1, "退还", "TH"),

    ASSET_LEND(2, "借用", "JY"),

    ASSET_RETURNED(3, "归还", "GH"),

    ASSET_CHANGE(4, "变更", "BG"),

    ASSET_TRANSFER(5, "调拨", "DB"),

    ASSET_REPAIR(6, "维修", "WX"),

    ASSET_SCRAP(7, "报废", "BF"),


    ;


    private Integer code;

    private String des;

    //生成相应单号的前缀
    private String prefix;

    RecordTypeEnum(Integer code, String des, String prefix) {
        this.code = code;
        this.des = des;
        this.prefix = prefix;
    }
}
