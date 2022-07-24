package com.aswl.as.ibrs.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件状态组-设备型号Entity
 *
 * @author dingfei
 * @date 2019-12-02 10:22
 */

@ApiModel(value = "EventUcStatusGroupModel", description = "事件状态组-设备型号Entity")
@Data
public class EventUcStatusGroupModel extends BaseEntity<EventUcStatusGroupModel> {
    /**
     * 扩展状态组ID
     */

    @ApiModelProperty(value = "扩展状态组ID", name = "eventStatusGroupId")
    private String eventStatusGroupId;
    /**
     * 设备型号ID
     */

    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;
    /**
     * 端口数量
     */

    @ApiModelProperty(value = "端口数量", name = "portNum")
    private Integer portNum;
    /**
     * 创建人账号
     */

    @ApiModelProperty(value = "创建人账号", name = "createBy")
    private String createBy;
    /**
     * 创建人名称
     */

    @ApiModelProperty(value = "创建人名称", name = "createName")
    private String createName;
    /**
     * 更新时间
     */

    @ApiModelProperty(value = "更新时间", name = "updateDate")
    private Date updateDate;
    /**
     * 更新人账号
     */

    @ApiModelProperty(value = "更新人账号", name = "updateBy")
    private String updateBy;
    /**
     * 更新人名称
     */

    @ApiModelProperty(value = "更新人名称", name = "updateName")
    private String updateName;
}
