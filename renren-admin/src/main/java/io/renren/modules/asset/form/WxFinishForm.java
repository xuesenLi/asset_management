package io.renren.modules.asset.form;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * 维修完成提交表单
 * @author Mr.Li
 * @date 2020/6/16 - 14:31
 */
@Data
public class WxFinishForm {

    /**
     * 资产id
     */
    private Integer id;

    /**
     * 实际 维修费用(元)
     */
/*"^\\d+(\\.\\d{1,2})?$"
    @Pattern(regexp = "^[+]?([0-9]+(.[0-9]{1,2})?)$", message = "{valid.repairCost}")
*/
    @NotBlank(message = "维修费用不能为空")
    private String repairCost;

    /**
     * 维修说明
     */
    @NotBlank(message = "维修说明不能为空")
    private String explain;

}
