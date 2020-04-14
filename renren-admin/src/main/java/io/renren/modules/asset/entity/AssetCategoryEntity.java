package io.renren.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 17:16:23
 */
@Data
@TableName("asset_category")
public class AssetCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产分类id
	 */
	@TableId
	private Integer categoryId;
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
	 * 是否删除  -1：已删除  0：正常
	 */
	private Integer delFlag;


	/**
	 * 上级部门名称
	 */
	@TableField(exist=false)
	private String parentName;

	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;

}
