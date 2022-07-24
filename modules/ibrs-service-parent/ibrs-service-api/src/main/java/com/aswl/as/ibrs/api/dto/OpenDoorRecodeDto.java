package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 开箱记录表Dto
* @author com.aswl
* @date 2019-12-18 17:06
*/

@ApiModel(value = "OpenDoorRecodeDto",description = "开箱记录表Dto")
@Data
public class OpenDoorRecodeDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 设备ID
    */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 告警类型
    */
    @ApiModelProperty(value = "告警类型",name="alarmType")
    private String alarmType;
    /**
    * 开箱时间
    */
    @ApiModelProperty(value = "开箱时间",name="openDoorTime")
    private String openDoorTime;
    /**
    * 关箱时间
    */
    @ApiModelProperty(value = "关箱时间",name="closeDoorTime")
    private String closeDoorTime;
    /**
    * 维护人员ID（多个用逗号分隔）
    */
    @ApiModelProperty(value = "维护人员ID（多个用逗号分隔）",name="maintainUserId")
    private String maintainUserId;
    /**
    * 图片路径（多个用逗号分隔）
    */
    @ApiModelProperty(value = "图片路径（多个用逗号分隔）",name="picPaths")
    private String picPaths;
    /**
    * 视频路径
    */
    @ApiModelProperty(value = "视频路径",name="videoPath")
    private String videoPath;

    @ApiModelProperty(value = "设备名称",name="deviceName")
    private String deviceName;

    @ApiModelProperty(value = "设备编码",name="deviceCode")
    private String deviceCode;

    @ApiModelProperty(value = "区域编码",name="deviceCode")
    private String regionCode;

    @ApiModelProperty(value = "开始时间",name="startTime")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name="endTime")
    private String endTime;
    @ApiModelProperty(value = "天数",name="day")
    private String day;

    @ApiModelProperty(value = "租户",name = "tenantCode")
    private String tenantCode;

    @ApiModelProperty(value = "项目",name = "projectId")
    private String projectId;

}
