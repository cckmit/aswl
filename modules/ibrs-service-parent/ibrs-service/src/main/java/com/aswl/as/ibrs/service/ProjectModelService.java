package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.ProjectModelDto;
import com.aswl.as.ibrs.api.module.ProjectModel;
import com.aswl.as.ibrs.api.vo.ProjectModelVo;
import com.aswl.as.ibrs.mapper.ProjectModelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ProjectModelService extends CrudService<ProjectModelMapper, ProjectModel> {
    private final ProjectModelMapper projectModelMapper;

    /**
     * 分页查询列表
     *
     * @param page
     * @param projectModelDto
     * @return ProjectModelVo
     */
    public PageInfo<ProjectModelVo> findPage(PageInfo<ProjectModelDto> page, ProjectModelDto projectModelDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(projectModelMapper.findPageList(projectModelDto));
    }

    /**
     * 新增项目型号关联表
     *
     * @param projectModel
     * @return int
     */
    @Transactional
    @Override
    public int insert(ProjectModel projectModel) {
        return projectModelMapper.insert(projectModel);
    }

    /**
     * 删除项目型号关联表
     *
     * @param projectModel
     * @return int
     */
    @Transactional
    @Override
    public int delete(ProjectModel projectModel) {
        return projectModelMapper.delete(projectModel);
    }

    /**
     * 根据项目ID删除数据 
     * @param projectId
     * @return int 
     */
    public int deleteByProjectId (String projectId){
        
       return projectModelMapper.deleteByProjectId(projectId); 
    }
    /**
     * 新增修改项目型号关联表信息
     *
     * @param list
     * @return int
     */
    @Transactional
    public int insertBath(List<ProjectModel> list) {
       int update = 0 ;
        for (int i = 0; i <list.size() ; i++) {
            ProjectModel projectModel = new ProjectModel();
            projectModel.setId(IdGen.snowflakeId());
            projectModel.setProjectId(list.get(i).getProjectId());
            projectModel.setDeviceModelId(list.get(i).getDeviceModelId());
            projectModelMapper.insert(projectModel);
            update ++ ; 
        }
        return update ;
    }
}