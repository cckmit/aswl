package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件报警Entity
* @author dingfei
* @date 2019-10-28 17:03
*/

@ApiModel(value = "DeviceEventAlarm",description = "设备事件报警Entity")
@Data
public class DeviceEventAlarm extends BaseEntity<DeviceEventAlarm> {
    /**
    * 统一事件ID
    */

    @ApiModelProperty(value = "统一事件ID",name="uEventId")
    private String uEventId;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 接收时间(从1970-01-01 08-00-00到现在的秒)
    */

    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)",name="recTime")
    private Integer recTime;
    /**
    * 该记录是否报警
    */

    @ApiModelProperty(value = "该记录是否报警",name="isAlarm")
    private Integer isAlarm;
    /**
    * 最高报警等级
    */

    @ApiModelProperty(value = "最高报警等级",name="alarmLevel")
    private Integer alarmLevel;
    /**
    * 报警类型集合，逗号分隔
    */

    @ApiModelProperty(value = "报警类型集合，逗号分隔",name="alarmTypes")
    private String alarmTypes;
    /**
     * 未报警类型集合,逗号分隔
     */

    @ApiModelProperty(value = "未报警类型集合，逗号分隔",name="promptTypes")
    private String promptTypes;
    /**
     * 报警时间集合,逗号分隔
     */

    @ApiModelProperty(value = "报警时间集合，逗号分隔",name="promptTypes")
    private String alarmDates;
    /**
    * 报警类型中文描述，分号分隔
    */
    @ApiModelProperty(value = "报警级别集合，逗号分隔",name="alarmLevels")
    private String alarmLevels;
    /**
     * 报警类型中文描述，分号分隔
     */

    @ApiModelProperty(value = "报警类型中文描述，分号分隔",name="alarmTypesDes")
    private String alarmTypesDes;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
