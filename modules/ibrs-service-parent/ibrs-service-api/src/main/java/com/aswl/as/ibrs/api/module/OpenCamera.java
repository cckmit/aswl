package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 开箱摄像机Entity
* @author dingfei
* @date 2019-12-16 16:05
*/

@ApiModel(value = "OpenCamera",description = "开箱摄像机Entity")
@Data
public class OpenCamera extends BaseEntity<OpenCamera> {
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
}
