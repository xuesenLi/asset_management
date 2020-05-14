package io.renren.modules.asset.vo;

import lombok.Data;

/**
 * @author Mr.Li
 * @date 2020/5/13 - 14:48
 */
@Data
public class AssetChangeVo {

    /**
     * 单号 状态
     * 0 待审批、1 已同意、2 被驳回、 3 --
     */
    private Integer recordStatus;

    /**
     * 资产名称
     */
    private String assetName;
    /**
     * 资产分类名称
     */
    private String categoryName;

    /**
     * 所属组织名称
     */
    private String orgName;

    /**
     * 使用组织名称
     */
    private String useOrgName;

    /**
     * 使用人名称
     */
    private String empName;

    /**
     * 区域名称
     */
    private String areaName;


    /**
     * 变更备注
     */
    private String recordRemarks;

}
