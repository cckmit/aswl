package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 开箱摄像机Dto
* @author dingfei
* @date 2019-12-16 16:05
*/

@ApiModel(value = "OpenCameraDto",description = "开箱摄像机Dto")
@Data
public class OpenCameraDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
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
    * 设备ID
    */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;

    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码",name="deviceCode")
    private String deviceCode;

    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creator")
    private String creator;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;

    /**
     * 关键字查询
     */
    @ApiModelProperty(value = "关键字查询",name="query")
    private String query;

    /**
     * 租户
     */
    @ApiModelProperty(value = "租户",name="tenantCode")
    private String tenantCode;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;

    /**
     * 项目
     */
    @ApiModelProperty(value = "项目",name="projectId")
    private String projectId;
}
