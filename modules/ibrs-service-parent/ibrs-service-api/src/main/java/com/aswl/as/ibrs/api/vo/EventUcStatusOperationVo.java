package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-28 14:35
 * @Version V1
 */
@Data
public class EventUcStatusOperationVo implements java.io.Serializable {
    /**
     * ID
     */

    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
     * 标题
     */

    @ApiModelProperty(value = "标题",name="title")
    private String title;
    /**
     * 操作名称
     */

    @ApiModelProperty(value = "操作名称",name="operName")
    private String operName;
    /**
     * 操作编码
     */

    @ApiModelProperty(value = "操作编码",name="operCode")
    private String operCode;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序",name="operSort")
    private String operSort;

    /**
     * 元数据ID
     */
    @ApiModelProperty(value = "元数据ID",name="metadataId")
    private String metadataId;

    /**
     * 设备型号ID
     */
    @ApiModelProperty(value = "设备型号ID",name="deviceModelId")
    private String deviceModelId;

    /**
     * 端口序号
     */
    @ApiModelProperty(value = "端口序号",name="portSerial")
    private Integer  portSerial;
   
    /**
     * 状态操作ID
     */

    @ApiModelProperty(value = "状态操作ID",name="statusOperationId")
    private String statusOperationId;

    /**
     * 事件元数据-设备型号ID
     */

    @ApiModelProperty(value = "事件元数据-设备型号ID",name="eventMetadataModelId")
    private String eventMetadataModelId;
}
