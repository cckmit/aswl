package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @date 2019-12-16 16:53
 * @Version V1
 */
@Data
public class OpenCameraVo {

    /**
     * ID
     */

    @ApiModelProperty(value = "主键",name="id")
    private String id;


    /**
     * 设备ID
     */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;

    /**
     * 摄像机IP
     */

    @ApiModelProperty(value = "摄像机IP",name="cameraIp")
    private String cameraIp;



    /**
     * 用户名
     */

    @ApiModelProperty(value = "用户名",name="cameraUserName")
    private String cameraUserName;

    /**
     * 密码
     */

    @ApiModelProperty(value = "密码",name="cameraPwd")
    private String cameraPwd;

    /**
     * 品牌类型
     */

    @ApiModelProperty(value = "品牌类型",name="brandType")
    private String brandType;

    /**
     * 码流路径
     */

    @ApiModelProperty(value = "码流路径",name="streamPath")
    private String streamPath;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",name = "deviceCode")
    private String deviceCode;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称",name = "deviceName")
    private String deviceName;

    /**
     * IP
     */

    @ApiModelProperty(value = "IP",name = "ip")
    private String ip;

    /**
     * 区域名称
     */

    @ApiModelProperty(value = "区域名称",name = "regionName")
    private String regionName;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称",name = "projectName")
    private String projectName;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String projectId;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name = "regionCode")
    private String regionCode;

    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码",name = "tentantcode")
    private String tenantCode;

}
