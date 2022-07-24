package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备种类Dto
 *
 * @author dingfei
 * @date 2019-09-26 14:33
 */

@ApiModel(value = "DeviceDto",description = "设备种类Dto")
@Data
public class DeviceKindDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @ApiModelProperty(value = "主键",name = "id")
    private String id;

    /**
     * 设备类型名
     */


    @ApiModelProperty(value = "设备类型名",name = "deviceKindName")
    private String deviceKindName;
    /**
     * 连接类型
     */


    @ApiModelProperty(value = "连接类型",name = "connectType")
    private String connectType;

    /**
     * 修改终端
     */


    @ApiModelProperty(value = "修改终端",name = "modifyTerminal")
    private String modifyTerminal;


    /**
     * 创建终端
     */

    @ApiModelProperty(value = "创建终端",name = "createTerminal")
    private String createTerminal;
    /**
     * 备注
     */


    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;
}
