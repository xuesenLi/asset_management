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
 * @date 2020-05-21 13:41:23
 */
@Data
@TableName("asset_oper_record")
public class AssetOperRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 资产操作记录id
	 */
	@TableId
	private Integer id;
	/**
	 * 资产id
	 */
	private Integer assetId;
	/**
	 * 处理类型 0 领用、 1 退还、 2 借用、   3 归还 、 4 变更、  5 调拨 、 6 维修、  7 报废、  8 维修完成、 9 入库
	 */
	private Integer operType;
	/**
	 * 处理时间
	 */
	private Date operTime;
	/**
	 * 申请人id
	 */
	private Integer createdUserid;
	/**
	 * 申请人名
	 */
	private String createdUsername;
	/**
	 * 处理内容
	 */
	private String operContent;
	/**
	 * 关联单号
	 */
	private String recordNo;

}
