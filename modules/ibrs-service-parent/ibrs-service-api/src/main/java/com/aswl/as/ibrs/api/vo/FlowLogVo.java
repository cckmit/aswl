package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/29 13:57
 */
@ApiModel(value = "FlowLogVo",description = "工单日志Vo")
@Getter@Setter
public class FlowLogVo {
    @ApiModelProperty(value = "处理时间",name="handleTime")
    private String handleTime;
    @ApiModelProperty(value = "处理人",name="handler")
    private String handler;
    @ApiModelProperty(value = "用户id",name="userId")
    private String userId;
}
