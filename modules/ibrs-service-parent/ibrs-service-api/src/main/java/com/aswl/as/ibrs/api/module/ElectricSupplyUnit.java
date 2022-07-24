package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 供电单位Entity
* @author df
* @date 2021/06/01 20:25
*/

@ApiModel(value = "ElectricSupplyUnit",description = "供电单位Entity")
@Data
public class ElectricSupplyUnit extends BaseEntity<ElectricSupplyUnit> {
    /**
    * 单位名称
    */

    @ApiModelProperty(value = "单位名称",name="name")
    private String name;
    /**
    * 联系人
    */

    @ApiModelProperty(value = "联系人",name="contact")
    private String contact;
    /**
    * 联系电话
    */

    @ApiModelProperty(value = "联系电话",name="phone")
    private String phone;

    /**
     * 电价（每千瓦时）
     */
    @ApiModelProperty(value = "电价（每千瓦时）",name="phone")
    private Double electricPrice;
    /**
    * 描述
    */

    @ApiModelProperty(value = "描述",name="description")
    private String description;
    /**
    * 项目ID
    */

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
}
