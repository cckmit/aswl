package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @date 2019-12-02 14:34
 * @Version V1
 */
@Data
public class EventUcStatusGroupModelVo implements  java.io.Serializable {

    @ApiModelProperty(value = "事件状态组-设备型号id",name = "id")
    private String id;
    @ApiModelProperty(value = "组id",name = "groupId")
    private String groupId;
    @ApiModelProperty(value = "状态组显示名称",name = "name")
    private String name;
    @ApiModelProperty(value = "端口数量",name = "portNum")
    private int portNum;
    @ApiModelProperty(value = "状态组英文显示名称",name = "statusGroupName")
    private String statusGroupName;
}
