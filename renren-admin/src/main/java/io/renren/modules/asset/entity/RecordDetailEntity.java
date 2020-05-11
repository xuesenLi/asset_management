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
 * @date 2020-05-09 09:55:29
 */
@Data
@TableName("record_detail")
public class RecordDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单号详情id
	 */
	@TableId
	private Integer id;
	/**
	 * 单号
	 */
	private String recordNo;
	/**
	 * 资产id
	 */
	private Integer assetId;
	/**
	 * 创建日期
	 */
	private Date createdTime;

}
