package io.renren.modules.asset.vo;

import lombok.Data;

/**
 * 资产 使用信息
 * @author Mr.Li
 * @date 2020/6/17 - 16:02
 */
@Data
public class AssetUseInfoVo {

    /**
     * 资产id
     */
    private Integer id;

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


}
