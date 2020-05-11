package io.renren.modules.asset.entity;

import lombok.Data;

/**
 *  资产操作的单号 生成。
 */
@Data
public class Sequence {
    private String name;

    private Integer currentValue;

    private Integer step;

}
