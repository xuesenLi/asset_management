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
 * @email xuesenli@gmail.com
 * @date 2020-05-18 11:09:07
 */
@Data
@TableName("asset_transfer")
public class AssetTransferEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产调拨id
	 */
	@TableId
	private Integer id;
	/**
	 * 调拨单号
	 */
	private String recordNo;
	/**
	 * 当前操作资产数量
	 */
	private Integer assetNum;
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
	 * 调拨日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date actualTime;
	/**
	 * 调拨备注
	 */
	private String recordRemarks;
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

	@TableField(exist=false)
	private List<Integer> assets;

}
