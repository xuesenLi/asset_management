package io.renren.modules.asset.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Mr.Li
 * @date 2020/4/10 - 10:51
 */
@Data
public class AssetForm {
    /**
     * 资产编码
     */
    @NotBlank(message = "资产编码不能为空")
    private String assetCode;
    /**
     * 资产名称
     */
    @NotBlank(message = "资产名称不能为空")
    private String assetName;
    /**
     * 资产分类id
     */
    private Integer categoryId;
    /**
     * 资产分类名称
     */
    @NotBlank(message = "资产分类不能为空")
    private String categoryName;
    /**
     * 所属组织id
     */
    private Integer orgId;
    /**
     * 所属组织名称
     */
    @NotBlank(message = "资产所属组织不能为空")
    private String orgName;
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
    /**
     * 管理员id
     */
    private Integer adminUserid;
    /**
     * 管理员名称
     */
    private String adminUsername;
    /**
     * 区域id
     */
    private Integer areaId;
    /**
     * 区域名称
     */
    @NotBlank(message = "资产区域不能为空")
    private String areaName;
    /**
     * 存放地点
     */
    private String storagePosition;
    /**
     * 规格型号
     */
    private String standard;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 价值（元）
     */
    private BigDecimal worth;
    /**
     * 使用期限（月）
     */
    private Integer timeLimit;
    /**
     * 购买日期
     */
    private Date buyTime;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 0闲置、1在用、2借用、3维修中、4报废、5待审批。
     */
    private Integer assetStatus;
}
