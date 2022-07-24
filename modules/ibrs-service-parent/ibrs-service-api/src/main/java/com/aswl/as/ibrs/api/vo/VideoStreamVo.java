package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/11/20 19:29
 */
@Getter@Setter
public class VideoStreamVo implements Serializable {

    private static final long serialVersionUID = 7265456426423066026L;

    @ApiModelProperty(value = "id",name = "id")
    private String id;
    @ApiModelProperty(value = "userName",name = "用户名")
    private String userName;
    @ApiModelProperty(value = "deviceName",name = "设备名称")
    private String deviceName;
    /**
     * 0-->离线
     * 1-->在线
     */
    @ApiModelProperty(value = "isOnline",name = "网络状态")
    private String isOnline;
    @ApiModelProperty(value = "password",name = "密码")
    private String password;
    @ApiModelProperty(value = "ip",name = "ip")
    private String ip;
    @ApiModelProperty(value = "port",name = "端口")
    private String port;
    private String output;
    private String flvOutput;

    private String tenantCode;

    private String projectCode;

    private String deviceType;

    private Integer currentNum;
}
