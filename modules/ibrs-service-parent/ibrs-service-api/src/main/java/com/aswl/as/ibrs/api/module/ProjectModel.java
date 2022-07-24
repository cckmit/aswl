package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 项目型号关联表Entity
 *
 * @author df
 * @date 2021/08/11 14:14
 */

@ApiModel(value = "ProjectModel", description = "项目型号关联表Entity")
@Data
public class ProjectModel extends BaseEntity<ProjectModel> {
    /**
     * 项目ID
     */

    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;
    /**
     * 设备型号ID
     */

    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;
}
