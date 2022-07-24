package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 地址库表Dto
* @author hfx
* @date 2020-01-04 15:35
*/

@ApiModel(value = "AddressBaseDto",description = "地址库表Dto")
@Data
public class AddressBaseDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 地址ID
    */
    @ApiModelProperty(value = "地址ID",name="id")
    private String id;
    /**
    * 点位名称
    */
    @ApiModelProperty(value = "点位名称",name="name")
    private String name;
    /**
    * 详细地址
    */
    @ApiModelProperty(value = "详细地址",name="address")
    private String address;
    /**
    * 经度
    */
    @ApiModelProperty(value = "经度",name="latitude")
    private Double latitude;
    /**
    * 纬度
    */
    @ApiModelProperty(value = "纬度",name="longitude")
    private Double longitude;
    /**
    * 绑定的设备IP
    */
    @ApiModelProperty(value = "绑定的设备IP",name="ip")
    private String ip;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 是否已用
    */
    @ApiModelProperty(value = "是否已用",name="isUsed")
    private Integer isUsed;
    /**
    * 绑定的设备ID
    */
    @ApiModelProperty(value = "绑定的设备ID",name="bindDeviceId")
    private String bindDeviceId;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
