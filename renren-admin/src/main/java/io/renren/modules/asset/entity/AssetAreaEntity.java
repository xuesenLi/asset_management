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
 * @date 2020-04-10 13:59:02
 */
@Data
@TableName("asset_area")
public class AssetAreaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 区域id
	 */
	@TableId
	private Integer areaId;
	/**
	 * 区域名称
	 */
	private String areaName;
	/**
	 * 1~100 自然数， 值越小，越靠前
	 */
	private Integer sortNum;
	/**
	 * 分类编码
	 */
	private String areaCode;
	/**
	 * 更新人
	 */
	private String updateName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
