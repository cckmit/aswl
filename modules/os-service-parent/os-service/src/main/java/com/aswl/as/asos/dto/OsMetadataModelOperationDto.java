package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModelOperation;
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
public class OsMetadataModelOperationDto {

    @ApiModelProperty(value = "事件元数据-设备型号ID", name = "eventMetadataModelId")
    private String eventMetadataModelId;

    @ApiModelProperty(value = "设备型号事件元数据-状态操作", name = "modelOperationDtoList")
    private List<AsEventUcMetadataModelOperation> modelOperationDtoList=new ArrayList<>();
}
