package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警抓拍记录Dto
* @author hzj
* @date 2020/12/08 10:41
*/

@ApiModel(value = "AlarmCaptureDto",description = "告警抓拍记录Dto")
@Data
public class AlarmCaptureDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 设备id
    */
    @ApiModelProperty(value = "设备id",name="deiceId")
    private String deviceId;
    /**
    * 抓拍的告警类型
    */
    @ApiModelProperty(value = "抓拍的告警类型",name="alarmType")
    private String alarmType;
    /**
    * 抓拍图片的路径
    */
    @ApiModelProperty(value = "抓拍图片的路径",name="picPath")
    private String picPath;
    /**
    * 抓拍图片名
    */
    @ApiModelProperty(value = "抓拍图片名",name="picName")
    private String picName;
    /**
    * 存储时间
    */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
    * 系统编码
    */
    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;


    /**
    * 租户编码
    */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

    /**
     * 告警类型中文描述
     */
    @ApiModelProperty(value = "抓拍的告警类型描述",name="alarmTypeDes")
    private String alarmTypeDes;

    /**
     * 统一事件Id
     */
    @ApiModelProperty(value = "统一事件Id",name = "UEventId")
    private String uEventId;
}
