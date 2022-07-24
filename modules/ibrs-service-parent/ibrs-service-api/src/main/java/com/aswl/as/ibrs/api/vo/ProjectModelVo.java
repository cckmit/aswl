package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 项目型号关联表VO
 *
 * @author df
 * @date 2021/08/11 14:14
 */

@ApiModel(value = "ProjectModelDto", description = "项目型号关联表VO")
@Data
public class ProjectModelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;
    
    /**
     * 设备型号ID
     */
    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;

    /**
     * 设备型号名称
     */
    @ApiModelProperty(value = "设备型号名称", name = "deviceModelName")
    private String deviceModelName;
}
