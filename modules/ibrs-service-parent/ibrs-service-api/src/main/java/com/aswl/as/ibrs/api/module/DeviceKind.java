package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备种类Entity
 *
 * @author dingfei
 * @date 2019-09-26 14:33
 */
@ApiModel(value = "DeviceKind",description = "设备种类Entity")
@Data
public class DeviceKind extends BaseEntity<DeviceKind> {
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
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型",name = "type")
    private String type;

    /**
     * 是否可编辑（0：否，1：是）
     */
    @ApiModelProperty(value = "是否可编辑（0：否，1：是）",name = "isEdit")
    private Integer isEdit;

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
