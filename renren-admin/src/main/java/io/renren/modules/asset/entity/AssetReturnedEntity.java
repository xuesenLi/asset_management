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
 * @date 2020-05-12 13:28:50
 */
@Data
@TableName("asset_returned")
public class AssetReturnedEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产归还id
	 */
	@TableId
	private Integer id;
	/**
	 * 归还单号
	 */
	private String recordNo;
	/**
	 * 当前操作资产数量
	 */
	private Integer assetNum;
	/**
	 * 归还日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date actualTime;
	/**
	 * 归还备注
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
