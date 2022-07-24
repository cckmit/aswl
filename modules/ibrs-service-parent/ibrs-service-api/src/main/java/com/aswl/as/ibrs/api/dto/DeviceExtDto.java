package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 外接设备表Dto
 *
 * @author dingfei
 * @date 2019-11-12 15:57
 */

@ApiModel(value = "DeviceExtDto", description = "外接设备表Dto")
@Data
public class DeviceExtDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", name = "code")
    private String code;
    /**
     * 连接设备类型
     */
    @ApiModelProperty(value = "连接设备类型", name = "connectType")
    private String connectType;
    /**
     * 连接设备ID
     */
    @ApiModelProperty(value = "连接设备ID", name = "connectId")
    private String connectId;
    /**
     * 连接端口
     */
    @ApiModelProperty(value = "连接端口", name = "connectPort")
    private Integer connectPort;
    /**
     * 外接设备类型ID
     */
    @ApiModelProperty(value = "外接设备类型ID", name = "externalDeviceTypeId")
    private String externalDeviceTypeId;
    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址", name = "ip")
    private String ip;
    /**
     * 端口
     */
    @ApiModelProperty(value = "端口", name = "port")
    private String port;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 创建人账号
     */
    @ApiModelProperty(value = "创建人账号", name = "create")
    private String create;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", name = "createName")
    private String createName;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "modifyDate")
    private Date modifyDate;
    /**
     * 更新人账号
     */
    @ApiModelProperty(value = "更新人账号", name = "modifier")
    private String modifier;
    /**
     * 更新人名称
     */
    @ApiModelProperty(value = "更新人名称", name = "updateName")
    private String updateName;
    /**
     * 所属组织
     */
    @ApiModelProperty(value = "所属组织", name = "sysOrgCode")
    private String sysOrgCode;
    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域", name = "sysRegionCode")
    private String sysRegionCode;
    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", name = "delFlag")
    private Integer delFlag;
    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间", name = "delDate")
    private Date delDate;
}
