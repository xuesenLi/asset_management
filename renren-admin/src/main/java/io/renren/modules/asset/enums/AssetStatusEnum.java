package io.renren.modules.asset.enums;

import lombok.Getter;

/**
 * @author Mr.Li
 * @date 2020/3/31 - 10:17
 *
 * 资产状态**： 0闲置、1在用、2借用、3维修中、4报废、5待审批。
 *  添加时， 若使用组织为空，状态为闲置，
 *         若使用组织不为空，状态为在用
 */
@Getter
public enum AssetStatusEnum {

    IDLE(0, "闲置"),

    IN_USE(1, "在用"),

    BORROWING(2, "借用"),

    UNDER_REPAIR(3, "维修中"),

    SCRAP(4, "报废"),

    PENDING_APPROVAL(5, "待审批"),

    ;


    private Integer code;

    private String des;

    AssetStatusEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

}
