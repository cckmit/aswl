package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/31 16:31
 */
@Getter@Setter
@ApiModel
public class UserWatchVo {
    @ApiModelProperty(value = "id",name = "id")
    private String id;
    @ApiModelProperty(value = "alarmType",name = "报警类型")
    private String alarmType;
    @ApiModelProperty(value = "alarmTypeName",name = "报警类型名称")
    private String alarmTypeName;
    @ApiModelProperty(value = "groupName",name = "组名")
    private String groupName;

}
