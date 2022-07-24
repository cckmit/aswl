package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备操作记录Entity
 *
 * @author df
 * @date 2021/07/06 15:42
 */

@ApiModel(value = "DeviceOperationLog", description = "设备操作记录Entity")
@Data
public class DeviceOperationLog extends BaseEntity<DeviceOperationLog> {

    private static final long serialVersionUID = 1L;
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

}
