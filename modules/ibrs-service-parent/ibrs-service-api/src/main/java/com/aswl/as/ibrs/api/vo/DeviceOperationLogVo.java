package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备操作记录VO
 *
 * @author df
 * @date 2021/07/06 17:03
 */
@Data
public class DeviceOperationLogVo implements Serializable {
    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;
    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", name = "regionName")
    private String regionName;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", name = "deviceName")
    private String deviceName;

    /**
     * 设备种类
     */
    @ApiModelProperty(value = "设备种类", name = "kind")
    private String deviceKindName;

    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号", name = "deviceModelName")
    private String deviceModelName;

    /**
     * 设备IP
     */
    @ApiModelProperty(value = "设备IP", name = "ip")
    private String ip;

    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码", name = "deviceCode")
    private String deviceCode;

    
    /**
     * 操作模式
     */
    @ApiModelProperty(value = "操作模式", name = "operationMode")
    private String operationMode;
    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容", name = "operationContent")
    private String operationContent;
    /**
     * 操作指令
     */
    @ApiModelProperty(value = "操作指令", name = "operationCommand")
    private String operationCommand;
    /**
     * 明细内容
     */
    @ApiModelProperty(value = "明细内容", name = "fullContent")
    private String fullContent;
    /**
     * 操作用户ID
     */
    @ApiModelProperty(value = "操作用户ID", name = "userId")
    private String userId;
    /**
     * 操作用户名
     */
    @ApiModelProperty(value = "操作用户名", name = "userName")
    private String userName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private String createDate;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码", name = "tenantCode")
    private String tenantCode;
}
