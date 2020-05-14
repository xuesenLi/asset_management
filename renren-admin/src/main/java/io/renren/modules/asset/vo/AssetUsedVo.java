package io.renren.modules.asset.vo;

import lombok.Data;

/**
 * @author Mr.Li
 * @date 2020/5/14 - 14:23
 *
 * 用来修改资产相关操作后 对 使用组织 使用人 的修改
 */
@Data
public class AssetUsedVo {


    /**
     * 资产状态
     */
    private Integer assetStatus;

    /**
     * 使用组织id
     */
    private Integer useOrgId;
    /**
     * 使用组织名称
     */
    private String useOrgName;
    /**
     * 使用人id, 从使用组织里面去查找
     */
    private Integer empId;
    /**
     * 使用人名称
     */
    private String empName;


}
