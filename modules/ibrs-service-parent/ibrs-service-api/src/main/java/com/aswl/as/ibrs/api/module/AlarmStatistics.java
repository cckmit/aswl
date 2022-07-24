package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备故障统计Entity
* @author dingfei
* @date 2019-10-22 19:01
*/

@ApiModel(value = "AlarmStatistics",description = "设备故障统计Entity")
@Data
public class AlarmStatistics extends BaseEntity<AlarmStatistics> {

    /**
    * 类型(1:报障箱,2:摄像头)
    */

    @ApiModelProperty(value = "类型(1:报障箱,2:摄像头)",name="type")
    private Integer type;
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
    * 时间
    */

    @ApiModelProperty(value = "时间",name="createDate")
    private Date createDate;

    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型",name="alarmType")
    private String alarmType;

    /**
     * 预警数
     */
    @ApiModelProperty(value = "预警数",name="alarmNum")
    private Integer alarmNum;

    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号",name="deviceModelId")
    private String deviceModelId;

    /**
     * 故障数量
     */
    @ApiModelProperty(value = "故障数",name="faultNum")
    private Integer faultNum;
}
