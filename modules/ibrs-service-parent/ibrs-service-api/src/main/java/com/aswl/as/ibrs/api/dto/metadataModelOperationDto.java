package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingfei
 * @date 2019-12-02 11:47
 * @Version V1
 */
@Data
public class metadataModelOperationDto {

    @ApiModelProperty(value = "事件元数据-设备型号ID", name = "eventMetadataModelId")
    private String eventMetadataModelId;

    @ApiModelProperty(value = "设备型号事件元数据-状态操作", name = "modelOperationDtoList")
    private List<EventUcMetadataModelOperationDto> modelOperationDtoList=new ArrayList<>();
}
