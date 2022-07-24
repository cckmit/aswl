package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 用电单位设备关联表Entity
* @author df
* @date 2021/06/01 20:56
*/

@ApiModel(value = "ElectricUnitDevice",description = "用电单位设备关联表Entity")
@Data
public class ElectricUnitDevice extends BaseEntity<ElectricUnitDevice> {

    /**
    * 主键
    */

    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 用电单位ID
    */

    @ApiModelProperty(value = "用电单位ID",name="unitId")
    private String unitId;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;

    /**
     * 设备id集合
     */

    @ApiModelProperty(value = "设备id集合",name="deviceIds")
    private String deviceIds;
}
