package com.aswl.as.ibrs.api.vo;

import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author df
 * @date 2022/06/23 14:38
 */
@Data
public class ExtendStatusJsonVo implements Serializable {
    
    @ApiModelProperty(value = "主键",name="id")
    private String id;
   
    @ApiModelProperty(value = "状态名",name="statusName")
    private String statusName;

    @ApiModelProperty(value = "字段名称",name="fldName")
    private String fldName;

    @ApiModelProperty(value = "事件元数据ID",name="eventMetadataId")
    private String  eventMetadataId;

    @ApiModelProperty(value = "端口序号",name="portSerial")
    private int portSerial;

    @ApiModelProperty(value = "报警区间",name="deviceModelAlarmThresholds")
    private List<DeviceModelAlarmThreshold> deviceModelAlarmThresholds;

    @ApiModelProperty(value = "状态操作",name="eventUcStatusOperationVos")
    private List<EventUcStatusOperationVo> eventUcStatusOperationVos;
}
