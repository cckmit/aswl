package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备类型Mapper
 *
 * @author dingfei
 * @date 2019-09-26 15:29
 */

@ApiModel(value = "DeviceType",description = "设备类型Entity")
@Data
public class DeviceType extends BaseEntity<DeviceType> {

    /**
     * 设备类型
     */

    @ApiModelProperty(value = "设备类型",name = "deviceType")
    private String deviceType;
    /**
     * 设备种类
     */

    @ApiModelProperty(value = "设备种类",name = "deviceKindId")
    private String deviceKindId;
    /**
     * 设备类型名
     */

    @ApiModelProperty(value = "设备类型名",name = "deviceTypeName")
    private String deviceTypeName;
    /**
     * 是否报告状态
     */

    @ApiModelProperty(value = "是否报告状态",name = "isReportStatus")
    private Integer isReportStatus;

    /**
     * 是否可编辑（0：否，1：是）
     */
    @ApiModelProperty(value = "是否可编辑（0：否，1：是）",name = "isEdit")
    private Integer isEdit;

    /**
     * 创建终端
     */

    @ApiModelProperty(value = "创建终端",name = "createTerminal")
    private String createTerminal;
    /**
     * 修改终端
     */

    @ApiModelProperty(value = "修改终端",name = "modifyTerminal")
    private String modifyTerminal;
    /**
     * 备注
     */

    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;
}
