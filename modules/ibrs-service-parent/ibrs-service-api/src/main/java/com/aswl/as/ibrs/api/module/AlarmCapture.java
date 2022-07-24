package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警抓拍Entity
* @author hzj
* @date 2020/12/08 10:41
*/

@ApiModel(value = "AlarmCapture",description = "告警抓拍Entity")
@Data
public class AlarmCapture extends BaseEntity<AlarmCapture> {


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
     * 告警类型中文描述
     */
    @ApiModelProperty(value = "抓拍的告警类型描述",name="alarmTypeDes")
    private String alarmTypeDes;

    /**
     * 统一事件Id
     */
    @ApiModelProperty(value = "统一事件Id",name = "uEventId")
    private String uEventId;
}
