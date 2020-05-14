package io.renren.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
@Data
@TableName("asset_change")
public class AssetChangeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产变更id
	 */
	@TableId
	private Integer id;
	/**
	 * 变更单号
	 */
	private String recordNo;
	/**
	 * 当前操作资产数量
	 */
	private Integer assetNum;
	/**
	 * 变更备注
	 */
	private String recordRemarks;
	/**
	 * 资产名称
	 */
	private String assetName;
	/**
	 * 资产分类id
	 */
	private Integer categoryId;
	/**
	 * 所属组织id
	 */
	private Integer orgId;
	/**
	 * 使用组织id
	 */
	private Integer useOrgId;
	/**
	 * 使用人id, 从使用组织里面去查找
	 */
	private Integer empId;
	/**
	 * 管理员id
	 */
	private Integer adminUserid;
	/**
	 * 区域id
	 */
	private Integer areaId;
	/**
	 * 审批人
	 */
	private Integer approverId;
	/**
	 * 改变内容
	 */
	private String changeContent;
	/**
	 * 申请人id
	 */
	private Integer createdUserid;
	/**
	 * 申请人名
	 */
	private String createdUsername;
	/**
	 * 申请日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createdTime;
	/**
	 * 0 待审批、1 已同意、2 被驳回、 3 --
	 */
	private Integer recordStatus;

	@TableField(exist=false)
	private List<Integer> assets;


	/**
	 * 资产分类名称
	 */
	@TableField(exist=false)
	private String categoryName;

	/**
	 * 所属组织名称
	 */
	@TableField(exist=false)
	private String orgName;

	/**
	 * 使用组织名称
	 */
	@TableField(exist=false)
	private String useOrgName;

	/**
	 * 使用人名称
	 */
	@TableField(exist=false)
	private String empName;

	/**
	 * 区域名称
	 */
	@TableField(exist=false)
	private String areaName;

}
