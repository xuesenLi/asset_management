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
 * @date 2020-04-09 16:41:56
 */
@Data
@TableName("asset_category")
public class AssetCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产分类id
	 */
	@TableId
	private Integer id;
	/**
	 * 父类id
	 */
	private Integer parentId;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 1~100 自然数， 值越小，越靠前
	 */
	private Integer sortNum;
	/**
	 * 分类编码
	 */
	private String categoryCode;
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
