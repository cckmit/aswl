package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.KindVo;
import com.aswl.as.ibrs.api.vo.ModelStatisticsVo;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.api.vo.StatisticsVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import feign.Response;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-15
 */
public interface IAsProjectService extends IService<AsProject> {

    PageUtils queryPage(Map<String, Object> params);

    AsProject getEntityById(String id);

    boolean saveEntity(AsProject entity);

    boolean updateEntityById(AsProject entity);

    boolean deleteEntityById(String id);

    String deleteEntityByIds(String[] ids);

    List<AsProject> getProjectListByProjectCode(String projectCode);

    List<AsProject> getProjectListByTenantCode(String tenantCode);

    List<AsProject> findList(AsProject asProject);

    PageInfo<Project> projectList(String pageNum, String pageSize, Project project);

    Boolean insertProject(ProjectDto projectDto);

    List<StatisticsVo> getProjectMaintainStatistics(DeviceAlarmDto deviceAlarmDto);

    List<KindVo> findDeviceModelKind();

    List<ProjectDeviceVo> getProjectDeviceList(DeviceAlarmDto deviceAlarmDto);

    List<Attachment> findAttachments(String projectId);

    ResponseEntity<byte[]> download(String filePath);

    Boolean updateProject(ProjectDto projectDto);

    List<ModelStatisticsVo> findModelStatistics(String projectId);
}
