package com.aswl.as.iface.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/30 15:49
 */
@Data
@ApiModel(value = "设备状态",description = "设备状态")
public class DeviceStatus implements java.io.Serializable {

    /**
     * 温度/湿度值
     */
    @ApiModelProperty(value = "温度/湿度值",name = "value")
    private String value;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",name = "code")
    private String code;

    /**
     * code中文名
     */

    @ApiModelProperty(value = "code中文名",name = "codeCN")
    private String codeCN;

    /**
     * 字段值
     */

    @ApiModelProperty(value = "字段值",name = "name")
    private String name;

    /**
     * 单位
     */

    @ApiModelProperty(value = "单位",name = "unit")
    private String unit;

    /**
     * 状态位操作权限list
     */

    @ApiModelProperty(value = "状态位操作权限list",name = "operationList")
    private List<StatusOperation> operationList;
}
