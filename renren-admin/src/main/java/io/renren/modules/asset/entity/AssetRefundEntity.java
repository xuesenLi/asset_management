package io.renren.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-24 10:07:31
 */
@Data
@TableName("asset_refund")
public class AssetRefundEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产退还id
	 */
	@TableId
	private Integer id;
	/**
	 * 退还单号
	 */
	private String recordNo;
	/**
	 * 当前操作资产数量
	 */
	private Integer assetNum;
	/**
	 * 退还日期
	 */
	private Date actualTime;
	/**
	 * 退还备注
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
	private Date createdTime;
	/**
	 * 0 待审批、1 已同意、2 被驳回、 3 --
	 */
	private Integer recordStatus;

}
