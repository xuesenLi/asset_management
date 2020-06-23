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
 * @email xuesenli@gmail.com
 * @date 2020-06-23 09:37:46
 */
@Data
@TableName("t_inventory_task")
public class InventoryTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 盘点任务id
	 */
	@TableId
	private Integer id;
	/**
	 * 盘点任务单号
	 */
	private String taskNo;
	/**
	 * 盘点任务名称
	 */
	private String taskName;
	/**
	 * 0未开始1盘点中9已结束
	 */
	private Integer status;
	/**
	 * 盘点备注
	 */
	private String remarks;
	/**
	 * 申请人id
	 */
	private Integer createdUserid;
	/**
	 * 申请人名
	 */
	private String createdUsername;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
