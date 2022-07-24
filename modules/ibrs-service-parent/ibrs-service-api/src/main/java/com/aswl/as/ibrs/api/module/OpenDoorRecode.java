package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 开箱记录表Entity
* @author com.aswl
* @date 2019-12-18 17:06
*/

@ApiModel(value = "OpenDoorRecode",description = "开箱记录表Entity")
@Data
public class OpenDoorRecode extends BaseEntity<OpenDoorRecode> {


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
    private Date openDoorTime;
    /**
    * 关箱时间
    */

    @ApiModelProperty(value = "关箱时间",name="closeDoorTime")
    private Date closeDoorTime;
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
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;

}
