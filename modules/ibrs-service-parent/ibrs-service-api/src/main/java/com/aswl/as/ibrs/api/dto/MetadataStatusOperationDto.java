package com.aswl.as.ibrs.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingfei
 * @date 2019-11-15 11:31
 * @Version V1
 */
@Data
public class MetadataStatusOperationDto implements java.io.Serializable {
    private String metadataId;
    private List<EventUcMetadataStatusOperationDto> operationDtoList=new ArrayList<>();
}
