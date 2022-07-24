package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 外接设备类型表Dto
 *
 * @author dingfei
 * @date 2019-11-09 10:10
 */

@ApiModel(value = "DeviceExtTypeDto", description = "外接设备类型表Dto")
@Data
public class DeviceExtTypeDto implements Serializable {

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
     * 输入输出类型
     */
    @ApiModelProperty(value = "输入输出类型", name = "ioType")
    private Integer ioType;
    /**
     * 厂商
     */
    @ApiModelProperty(value = "厂商", name = "manufacturer")
    private String manufacturer;
    /**
     * 响应时长
     */
    @ApiModelProperty(value = "响应时长", name = "responseTime")
    private Integer responseTime;
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
}
