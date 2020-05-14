package io.renren.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
@Data
@TableName("asset_audit")
public class AssetAuditEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产审批id
	 */
	@TableId
	private Integer id;
	/**
	 * 单号
	 */
	private String recordNo;
	/**
	 * 单据状态 0 待审批、1 已同意、2 被驳回
	 */
	private Integer recordStatus;
	/**
	 * 单据类型  0 领用、 1 退还、 2 借用、   3 归还 、 4 变更、  5 调拨 、 6 维修、  7 报废
	 */
	private Integer recordType;
	/**
	 * 申请人名
	 */
	private String createdUsername;
	/**
	 * 申请日期
	 */
	private Date createdTime;

}
