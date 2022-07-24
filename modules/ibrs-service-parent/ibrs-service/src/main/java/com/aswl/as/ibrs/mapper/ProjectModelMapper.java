package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.ProjectModelDto;
import com.aswl.as.ibrs.api.vo.ProjectModelVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ProjectModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目型号关联表Mapper
 *
 * @author df
 * @date 2021/08/11 14:14
 */

@Mapper
public interface ProjectModelMapper extends CrudMapper<ProjectModel> {

    /**
     * 分页查询列表
     * @param projectModelDto
     * @return
     */
    List<ProjectModelVo> findPageList(ProjectModelDto projectModelDto);

    /**
     * 根据项目ID删除数据
     * @param projectId
     * @return int 
     */
    int deleteByProjectId(@Param("projectId") String projectId);

}
