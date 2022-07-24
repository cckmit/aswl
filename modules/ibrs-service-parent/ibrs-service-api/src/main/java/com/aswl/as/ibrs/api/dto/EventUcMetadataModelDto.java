package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件元数据-设备型号Dto
 *
 * @author dingfei
 * @date 2019-11-29 09:41
 */

@ApiModel(value = "EventUcMetadataModelDto", description = "事件元数据-设备型号Dto")
@Data
public class EventUcMetadataModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     * 设备型号ID
     */
    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;
    /**
     * 状态组-设备型号ID
     */
    @ApiModelProperty(value = "状态组-设备型号ID", name = "eventStatusGroupModelId")
    private String eventStatusGroupModelId;
    /**
     * 事件元数据ID
     */
    @ApiModelProperty(value = "事件元数据ID", name = "eventMetadataId")
    private String eventMetadataId;
    /**
     * 端口序号
     */
    @ApiModelProperty(value = "端口序号", name = "portSerial")
    private Integer portSerial;
    /**
     * 值上限
     */
    @ApiModelProperty(value = "值上限", name = "maxLimit")
    private Double maxLimit;
    /**
     * 值下限
     */
    @ApiModelProperty(value = "值下限", name = "minLimit")
    private Double minLimit;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
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
