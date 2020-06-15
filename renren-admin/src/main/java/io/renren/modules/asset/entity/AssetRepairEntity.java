package io.renren.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
@Data
@TableName("asset_repair")
public class AssetRepairEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产维修id
	 */
	@TableId
	private Integer id;
	/**
	 * 维修单号
	 */
	private String recordNo;
	/**
	 * 当前操作资产数量
	 */
	private Integer assetNum;
	/**
	 * 报修人id
	 */
	private Integer empId;
	/**
	 * 报修人
	 */
	private String empName;
	/**
	 * 维修内容
	 */
	private String repairContent;
	/**
	 * 维修费用(元)
	 */
	private BigDecimal repairCost;
	/**
	 * 维修日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date actualTime;
	/**
	 * 维修备注
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

	@TableField(exist=false)
	private List<Integer> assets;

}
