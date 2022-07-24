package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警设置Entity
* @author hzj
* @date 2020/12/08 10:43
*/

@ApiModel(value = "AlarmSettings",description = "告警设置Entity")
@Data
public class AlarmSettings extends BaseEntity<AlarmSettings> {

    /**
    * 外接设备重启次数
    */

    @ApiModelProperty(value = "外接设备重启次数",name="times",example = "1")
    private Integer times;
    /**
    * 重启间隔时间,单位秒
    */

    @ApiModelProperty(value = "重启间隔时间,单位秒",name="intervalTime",example = "30")
    private Integer intervalTime;
    /**
    * 需要抓拍的告警类型,以逗号分隔
    */

    @ApiModelProperty(value = "需要抓拍的告警类型,以逗号分隔",name="alarmType")
    private String alarmType;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
