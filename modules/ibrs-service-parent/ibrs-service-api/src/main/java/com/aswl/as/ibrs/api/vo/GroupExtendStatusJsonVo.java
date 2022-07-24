package com.aswl.as.ibrs.api.vo;

import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author df
 * @date 2022/06/23 14:40
 */
@Data
public class GroupExtendStatusJsonVo implements Serializable{
    
    @ApiModelProperty(value = "事件状态组-设备型号id",name = "id")
    private String id;

    private String groupId;
    
    @ApiModelProperty(value = "状态组显示名称",name = "name")
    private String name;
    
    @ApiModelProperty(value = "端口数量",name = "portNum")
    private int portNum;
    
    private List<ExtendStatusJsonVo> extendStatusJsonVos;

}
