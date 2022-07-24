package com.aswl.as.asos.modules.ibrs.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备型号事件元数据-状态操作
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsEventUcMetadataModelOperation对象", description="设备型号事件元数据-状态操作")
public class AsEventUcMetadataModelOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "事件元数据-设备型号ID")
    private String eventMetadataModelId;

    @ApiModelProperty(value = "状态操作ID")
    private String statusOperationId;


}
