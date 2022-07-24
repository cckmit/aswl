package com.aswl.as.metadata.api.vo;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * 设备型号区间报警Entity
 * @author liuliepan
 * @date 2019-10-28 21:51
 */

@ApiModel(value = "DeviceEventHisAlarmVO",description = "设备型号区间报警Entity")
@Data
public class DeviceEventHisAlarmVO extends BaseEntity<DeviceEventHisAlarmVO> {
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
     * 报警类型中文描述，分号分隔
     */

    @ApiModelProperty(value = "报警类型中文描述，分号分隔",name="alarmTypesDes")
    private String alarmTypesDes;
    /**
     * 存储时间
     */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;

    /**
     * 年月（分表保存使用）
     */
    private String yearMonth;
}
