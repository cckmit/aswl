package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备操作记录Dto
 *
 * @author df
 * @date 2021/07/06 15:44
 */

@ApiModel(value = "DeviceOperationLogDto", description = "设备操作记录Dto")
@Data
public class DeviceOperationLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", name = "deviceId")
    private String deviceId;
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
    private Date createDate;
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

    /**
     * 多条件查询参数
     */
    @ApiModelProperty(value = "多条件查询参数", name = "query")
    private String query;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;
}
