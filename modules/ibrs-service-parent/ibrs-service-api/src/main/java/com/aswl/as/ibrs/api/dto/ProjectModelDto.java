package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.aswl.as.ibrs.api.module.ProjectModel;
import lombok.Data;
import io.swagger.annotations.ApiModel;

/**
 * 项目型号关联表Dto
 *
 * @author df
 * @date 2021/08/11 14:14
 */

@ApiModel(value = "ProjectModelDto", description = "项目型号关联表Dto")
@Data
public class ProjectModelDto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String projectId;
    private List<ProjectModel> list = new ArrayList<>();
    
}
