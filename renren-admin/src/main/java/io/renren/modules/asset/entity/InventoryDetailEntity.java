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
@TableName("t_inventory_detail")
public class InventoryDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 盘点详情id
	 */
	@TableId
	private Integer id;
	/**
	 * 资产id
	 */
	private Integer assetId;
	/**
	 * 盘点任务单号
	 */
	private String taskNo;
	/**
	 * 状态；0未盘；1已盘；2盘盈; 3盘亏;4正常;
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
