package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.api.vo.ProjectVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.Project;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
*
* 项目表Mapper
* @author hfx
* @date 2019-12-17 19:28
*/

@Mapper
public interface ProjectMapper extends CrudMapper<Project> {

    Project selectProjectForProjectCode(Project entity);

    List<Project> findProjectByRegionCode(DeviceAlarmDto deviceAlarmDto);

    List<ProjectDeviceVo> getProjectDeviceList(DeviceAlarmDto deviceAlarmDto);

    String findById(@Param("projectId") String projectId);

    List<Project> findListProject(Project project);

    List<Project> findProject(Project project);


    /**
     * 根据行政区域和项目编码查询项目
     * @param projectCode
     * @return
     */
    Project findByCode(String projectCode);

    /**
     * 市级平台新增项目
     * @param project
     */
    void cityPlatformInsert(Project project);

    /**
     * 根据projectId集获取所有子级项目
     * @param projectIds
     * @return
     */
    List<String> findSubProjectIds(@Param("projectIds") List<String> projectIds);

    /**
     * 根据项目Id加载数据
     * @param projectId
     * @return
     */
    Project findByProjectId(@Param("projectId") String projectId);


    /**
     * 根据项目Id获取智能箱/摄像机数量
     * @param projectId
     * @return
     */
    Map findNumByProjectId(@Param("projectId") String projectId);

    /**
     *  根据项目查询最小parentId
     * @param projectIds
     * @return String
     */
    String findMinParentId(@Param("projectIds") List<String> projectIds);

    /**
     * 查询子项目信息
     * @param projectId
     * @return list
     */
    List<Project> findProjectByParentId(@Param("projectId") String projectId);

    /**
     * 根据项目ID删除多个项目信息
     * @param projectIds
     * @return int
     */
    int deleteByProjectIds(@Param("projectIds") List<String> projectIds );

    /**
     * 根据租户编码查询项目
     * @param tenantCode
     * @return list
     */
    List<Project> findProjectByTenantCode(@Param("tenantCode") String tenantCode);


    /**
     * 查询项目级别
     * @return list
     */
    List<ProjectVo> findProjectAlarmLevel(Project project);
}
