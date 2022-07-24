package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核扣分规则Dto
* @author hfx
* @date 2020-03-19 10:18
*/

@ApiModel(value = "ExamineDeductRuleDto",description = "考核扣分规则Dto")
@Data
public class ExamineDeductRuleDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 考核项ID
    */
    @ApiModelProperty(value = "考核项ID",name="examineItemId")
    private String examineItemId;
    /**
    * 最小值
    */
    @ApiModelProperty(value = "最小值",name="minNum")
    private Integer minNum;
    /**
    * 最大值
    */
    @ApiModelProperty(value = "最大值",name="maxNum")
    private Integer maxNum;
    /**
    * 表达式
    */
    @ApiModelProperty(value = "表达式",name="expression")
    private String expression;
    /**
    * 扣减分数
    */
    @ApiModelProperty(value = "扣减分数",name="deductScore")
    private Double deductScore;
    /**
    * 扣减费用
    */
    @ApiModelProperty(value = "扣减费用",name="deductFee")
    private Double deductFee;
    /**
    * 存储时间
    */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
