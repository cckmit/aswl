package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.mapper.ProjectMapper;
import com.aswl.as.ibrs.mapper.RegionLeaderMapper;
import com.aswl.as.ibrs.mapper.RegionMapper;
import com.aswl.as.user.api.dto.UserDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.api.module.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ProjectService extends CrudService<ProjectMapper, Project> {

    private final ProjectMapper projectMapper;
    private  final RegionMapper regionMapper;
    private final DeviceService deviceService;
    private final UserServiceClient userServiceClient;
    private final RegionLeaderMapper regionLeaderMapper;

    /**
     * 新增项目表
     *
     * @param projectDto
     * @return int
     */
    @Transactional
    public int insert(ProjectDto projectDto) {
        int update = 0 ;
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        // 设置各种值 项目的值
        project.setNewRecord(true);
        project.setCreatorId(SysUtil.getUser());
        project.setApplicationCode(SysUtil.getSysCode());
        if (project.getTenantCode() == null){
            project.setTenantCode(SysUtil.getTenantCode());
        }else {
            project.setTenantCode(project.getTenantCode());
        }
        //字段不一样
        project.setProjectId(IdGen.snowflakeId());

        Date now=new Date();
        project.setCreatorId(SysUtil.getUser());
        project.setUpdaterId(SysUtil.getUser());
        project.setCreateAt(now);
        project.setUpdateAt(now);

        //设置项目编码
        String tempCode= Pinyin4jUtil.getFirstSpell(project.getProjectName());
        generateProjectCodeCode(project,tempCode);
       if (super.insert(project) > 0 ) {
           update ++ ;
           // 增加项目管理员
           projectDto.setProjectId(project.getProjectId());
           this.insertAdminUser(projectDto);
       }
       return update;
    }

    /**
     * 增加项目管理员
     * @param projectDto
     */
    public void insertAdminUser(ProjectDto projectDto){
        // 获取项目管理员角色ID
        String roleId= null ;
        ResponseBean<Role> responseBean = userServiceClient.getRoleId(RoleEnum.ROLE_PROJECT_ADMIN.getCode(), SysUtil.getTenantCode());
        if (responseBean.getStatus() == 200 && responseBean.getData() != null){
            roleId = responseBean.getData().getId();
        }
        UserDto userDto = new UserDto();
        userDto.setName(projectDto.getAdminAccount());
        userDto.setCredential(projectDto.getPassWord());
        userDto.setIdentifier(projectDto.getAdminAccount());
        userDto.setPhone(projectDto.getAdminAccount());
        userDto.setSysRole(RoleEnum.ROLE_PROJECT_ADMIN.getType());
        userDto.setProjectId(projectDto.getProjectId());
        userDto.setProjectName(projectDto.getProjectName());
        userDto.setRole(Arrays.asList(roleId.split(",")));
        userDto.setStatus(0);
        if (projectDto.getTenantCode() == null){
            userDto.setTenantCode(SysUtil.getTenantCode());
        }else {
            userDto.setTenantCode(projectDto.getTenantCode());
        }
        userServiceClient.insertProjectAdminUser(userDto);
    }

    /**
     * 删除项目表
     *
     * @param project
     * @return int
     */
    @Transactional
    @Override
    public int delete(Project project) {
        String projectId = project.getProjectId();
        // 删除子项目
         this.deleteProjectAll(project);
         // 删除区域负责人信息
        List<String> regionIds = new ArrayList<>();
        Region region = new Region();
        region.setProjectId(projectId);
        List<Region> regions = regionMapper.findList(region);
        if (regions!= null && regions.size()>0){
            for (Region r :regions) {
                regionIds.add(r.getId());
            }
            regionLeaderMapper.deleteByRegionIds(regionIds);
        }
        // 先根据项目删除区域
        regionMapper.deleteByProjectId(projectId);
        //删除角色/用户
        this.deleteRoleUser(project);
        //删除设备信息
        Device d=new Device();
        d.setProjectId(projectId);
        List<Device> list=deviceService.findList(d);
        List<String> idString = list.stream().map(Device::getId).collect(Collectors.toList());
        if (idString != null){
            Device  device  = new Device();
            device.setIdString(idString.stream().collect(Collectors.joining(",")));
            deviceService.deleteAllDevice(device);
        }
        return super.delete(project);
    }

    /**
     *  删除项目时同时删除子项目/区域/设备信息
     * @param project
     */
    public  void  deleteProjectAll(Project project){
        // 先查询项目下的子项目
        String projectId =  project.getProjectId();
        List<String> listIds = new ArrayList();
        List<Project> projects  = projectMapper.findProjectByParentId(projectId);
        if (projects != null && projects.size() > 0){
            for (Project project1:projects) {
                listIds.add(project1.getProjectId());
                // 删除子项目信息
                super.deleteAll(listIds.toArray(new String[listIds.size()]));
                // 删除区域
                regionMapper.deleteByProjectId(project1.getProjectId());
                // 删除设备信息
                Device d=new Device();
                d.setProjectId(project1.getProjectId());
                List<Device> list=deviceService.findList(d);
                List<String> idString = list.stream().map(Device::getId).collect(Collectors.toList());
                if (idString != null){
                    Device  device  = new Device();
                    device.setIdString(idString.stream().collect(Collectors.joining(",")));
                    deviceService.deleteAllDevice(device);
                }
            }
        }
        
    }
    
    

    /**
     * 删除角色/用户
     */
    public void deleteRoleUser(Project project){
        List<String> userIds = new ArrayList<>();
        // 根据项目ID查询用户列表
        ResponseBean<List<User>> userVo = userServiceClient.findUserByProjectId(project.getProjectId());
        if (ResponseBean.SUCCESS == userVo.getCode())
        {
            if (userVo.getData() != null && userVo.getData().size() > 0) {
                for (User user : userVo.getData()) {
                    userIds.add(user.getId());
                }
                User user = new User();
                user.setIdString(StringUtils.join(userIds.toArray(), ","));
                userServiceClient.deleteAllUsers(user);
            }
        }
    }

    /**
     * 自动生成项目编码
     * @param asProject 项目
     */
    public void generateProjectCodeCode(Project asProject,String tempCode) {

        Project temp=new Project();
        temp.setProjectCode(tempCode);
        temp=projectMapper.selectProjectForProjectCode(temp);

        if(temp==null|| StringUtils.isEmpty(temp.getProjectCode()))
        {
            asProject.setProjectCode(tempCode+"001");
        }
        else
        {
            asProject.setProjectCode(NumberUtil.addOne(temp.getProjectCode()));
        }
    }

    public List<ProjectDeviceVo> getProjectDeviceList(DeviceAlarmDto deviceAlarmDto) {
        return projectMapper.getProjectDeviceList(deviceAlarmDto);
    }

    public List<Project> findProject(Project project) {
        return projectMapper.findProject(project);
    }

    /**
     * 根据行政区域和项目编码查询项目
     * @param projectCode
     * @return
     */
    public Project findByCode(String projectCode) {
        return projectMapper.findByCode(projectCode);
    }


    /**
     * 市级平台新增项目
     */
    public void cityPlatformInsert(Project project){
        projectMapper.cityPlatformInsert(project);
    }



    /**
     * 导出项目信息
     *
     * @param project 查询参数
     * @param request   请求对象
     * @param response  响应对象
     * @return ResponseBean
     */
    public ResponseBean<Boolean> export(@RequestBody Project project, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "项目信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<Project> projects = projectMapper.findList(project);
            if (CollectionUtils.isEmpty(projects)) {
                throw new CommonException("无项目信息数据.");
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("projectName", "项目名称");
            map.put("maintainDeptName", "维护单位");
            map.put("maintainDeptLeader", "负责人");
            map.put("maintainDeptPhone", "电话");
            map.put("projectOwner", "用户单位");
            map.put("projectAddr","项目地址");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(projects),map);
            return new ResponseBean<>(true, "导出成功");
        } catch (Exception e) {
            log.error("导出项目信息数据失败！", e);
            throw new CommonException("导出项目信息数据数据失败！");
        }
    }

    /**
     * 加载指定项目ID的所有子级项目ID
     * @param projectIds
     * @return  返回的项目ID集包括参数本身的项目ID
     */
    public List<String> findAllSubProjectIds(List<String> projectIds){
        List<String> resultProjectIds = new ArrayList<>();
        if(projectIds != null && projectIds.size() > 0){
            resultProjectIds.addAll(projectIds);
            List<String> subProjectIds = this.findSubProjectIds(projectIds);
            while (subProjectIds != null && subProjectIds.size() > 0){
                resultProjectIds.addAll(subProjectIds);
                subProjectIds = this.findSubProjectIds(subProjectIds);
            }
        }
        return resultProjectIds;
    }

    /**
     * 加载指定项目ID的直属子级项目ID
     * @param projectIds
     * @return
     */
    public List<String> findSubProjectIds(List<String> projectIds){
        List<String> resultProjectIds = projectMapper.findSubProjectIds(projectIds);
        return resultProjectIds;
    }

    /**
     * 根据项目Id加载数据
     * @param projectId
     * @return
     */
    public Project findByProjectId(String projectId){
        if(projectId == null){
            return null;
        }
        return projectMapper.findByProjectId(projectId);
    }
    /**
     *  根据项目查询最小parentId
     * @param projectIds
     * @return String
     */
   public String findMinParentId(List<String> projectIds){
        return projectMapper.findMinParentId(projectIds);
    }

    /**
     * 根据租户编码查询项目
     * @param tenantCode
     * @return list
     */
    public List<Project> findProjectByTenantCode( String tenantCode){
        return projectMapper.findProjectByTenantCode(tenantCode);
    }
}