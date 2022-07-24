package com.aswl.as.ibrs.controller;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.OsProjectDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.KindVo;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.api.vo.StatisticsVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.service.AlarmStatisticsService;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.ProjectService;
import com.aswl.as.ibrs.service.RegionService;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 项目表controller
 *
 * @author hfx
 * @date 2019-12-17 19:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/project", tags = "项目表")
@RestController
@RequestMapping("/v1/project")
public class ProjectController extends BaseController {

    private final ProjectService projectService;
    private final AlarmStatisticsService alarmStatisticsService;
    private final RegionService regionService;
    private DeviceService deviceService;

    private UserServiceClient userServiceClient;

    private MQSender mqSender;


    /**
     * 根据ID获取项目表
     *
     * @param projectId
     * @return ResponseBean
     */
    @ApiOperation(value = "根据项目表ID查询项目表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "项目表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{projectId}")
    public ResponseBean<Project> findById(@PathVariable("projectId") String projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        return new ResponseBean<>(projectService.get(project));
    }

    /**
     * 查询所有项目表
     *
     * @param project
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有项目表列表")
    @ApiImplicitParam(name = "project", value = "项目表对象", paramType = "path", required = true, dataType = "project")
    @GetMapping(value = "projects")
    public ResponseBean
            <List<Project>> findAll(Project project) {
        project.setTenantCode(SysUtil.getTenantCode());
        //TODO ，这里可能需要按人拥有的projectId来过滤，但先不做过滤
        return new ResponseBean<>(projectService.findList(project));
    }

    /**
     * 获取当前用户拥有的项目列表
     *
     * @param project
     * @return ResponseBean
     */
    @ApiOperation(value = "获取当前用户拥有的项目列表")
    @ApiImplicitParam(name = "project", value = "项目表对象", paramType = "path", required = false, dataType = "project")
    @GetMapping(value = "getUserProject")
    public ResponseBean<List<OsProjectDto>>getUserProject(Project project) {

        //TODO 这里要根据人来返回，现在人还没有对应的projectId设置，就直接返回当前租户所有项目
        //TODO ，这里可能需要按人拥有的projectId来过滤，但先不做过滤
        String roles = RoleContextHolder.getRole();
        String regionCode = project.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = null;

        String treeRootId = "-1"; //返回树结结构的顶级parentId
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目
            String userName = SysUtil.getUser();
            ResponseBean<UserVo> responseBean = userServiceClient.findUserByIdentifier(userName, SysUtil.getTenantCode());
            if(responseBean != null && responseBean.getCode() == 200){
                UserVo userVo = responseBean.getData();
                projectId = userVo.getProjectId();
                Project myProject = projectService.findByProjectId(projectId);
                if(myProject != null){
                    treeRootId = myProject.getParentId();
                    List<String> projectIdList = new ArrayList<>();
                    projectIdList.add(projectId);
                    projectIdList = projectService.findAllSubProjectIds(projectIdList);
                    projectId = String.join(",", projectIdList);
                }else{
                    projectId = "-9";   //无该项目
                }
            }else{
                projectId = "-9";   //若查不到用户信息，则返回无项目数据
            }
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return new ResponseBean<>(null,"当前用户暂无项目");
                }
                regionCode = userRegionCode;
            }
        }
        project.setTenantCode(tenantCode);
        project.setRegionCode(regionCode);
        project.setProjectId(projectId);
        // project.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        List<Project> projectList = projectService.findProject(project);
        List<OsProjectDto> treeList = new ArrayList<>();
        if(projectList != null && projectList.size() > 0){
            if(projectList.size() == 1){
                treeRootId = projectList.get(0).getParentId();
            }
            List<OsProjectDto> projectDtoList = projectList.stream().map(OsProjectDto::new).collect(Collectors.toList());
            // 排序、构建树形结构
            treeList = TreeUtil.buildTree(CollUtil.sort(projectDtoList, Comparator.comparingInt(OsProjectDto::getSort)), treeRootId);
        }

        return new ResponseBean<>(treeList);
    }

    /**
     * 分页查询项目表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param project
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询项目表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "project", value = "项目表信息", dataType = "project")
    })

    @GetMapping("projectList")
    public ResponseBean<PageInfo<Project>> projectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                       Project project) {
        if(!SysUtil.isAdmin())
        {
            project.setTenantCode(SysUtil.getTenantCode());
        }
        if(CommonConstant.PAGE_SORT_DEFAULT.equals(sort))
        {
            sort="create_at";
        }
        return new ResponseBean<>(projectService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), project));
    }

    /**
     * 新增项目表
     *
     * @param projectDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增项目表", notes = "新增项目表")
    @PostMapping
    @Log("新增项目表")
    public ResponseBean
            <Boolean> insertProject(@RequestBody @Valid ProjectDto projectDto) {
        int result = projectService.insert(projectDto);
        if(result > 0){
            //通知前台刷新
            Map object = new HashMap();
            object.put("alias", "projectInfo");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return new ResponseBean<>( result > 0);
    }

    /**
     * 修改项目表
     *
     * @param projectDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新项目表信息", notes = "根据项目表ID更新项目表信息")
    @Log("修改项目表")
    @PutMapping
    public ResponseBean
            <Boolean> updateProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        project.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), projectDto.getTenantCode(),projectDto.getProjectId());
        int result = projectService.update(project);
        if(result > 0){
            //通知前台刷新
            Map object = new HashMap();
            object.put("alias", "projectInfo");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return new ResponseBean<>(result > 0);
    }

    /**
     * 根据项目表ID删除项目表信息
     *
     * @param projectId
     * @return ResponseBean
     */
    @ApiOperation(value = "删除项目表信息", notes = "根据项目表ID删除项目表信息")
    @ApiImplicitParam(name = "projectId", value = "项目表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{projectId}")
    public ResponseBean
            <Boolean> deleteProjectById(@PathVariable("projectId") String projectId) {

       /* // 校验项目是否还有设备还没删除
        ResponseBean r=canDeleteProject(projectId);
        if(r!=null)
        {
            return r;
        }*/
       
        Project project = new Project();
        project.setProjectId(projectId);
        return new ResponseBean<>(projectService.delete(project) > 0);
    }

    /**
     * 批量删除项目表信息
     *
     * @param project
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除项目表", notes = "根据项目表ID批量删除项目表")
    @ApiImplicitParam(name = "project", value = "项目表信息", dataType = "project")
    @Log("批量删除项目表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllProject(@RequestBody Project project) {
        boolean success = false;
        try {

            // 判断是否有设备还没删除，如果有，就提示不给删除
            if(StringUtils.isNotEmpty(project.getIdString()))
            {
                for(String s:project.getIdString().split(","))
                {
                    ResponseBean r=canDeleteProject(s);
                    if(r!=null)
                    {
                        return r;
                    }
                }
            }

            if (StringUtils.isNotEmpty(project.getIdString()))
                success = projectService.deleteAll(project.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除项目表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    private ResponseBean<Boolean> canDeleteProject(String projectId)
    {
        Device d=new Device();
        // 暂时用租户来查，后面要换成项目，因为其他模块要上线，暂时不上传代码，所以不直接修改代码
        //d.setTenantCode("aswl");
        d.setProjectId(projectId);
        //如果太久或数据太多，就直接连数据库查
        List<Device> list=deviceService.findList(d);
        if(list.size()>0)
        {
            Project p;
            try
            {
                Project tempP=new Project();
                tempP.setProjectId(projectId);
                p=projectService.get(tempP);
                String projectName="";
                if(p!=null)
                {
                    projectName=p.getProjectName();
                }
                //有设备都是不能删项目的
                throw new Exception("项目 "+projectName+" 下还有设备，请删除或转移设备到其他项目后再重试");
            }
            catch (Exception e)
            {
                return new ResponseBean<>(e);
            }
        }
        return null;
    }

    /**
     * 项目维护统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "项目维护统计")
    @GetMapping(value = "projectStatistics")
    public ResponseBean<List<StatisticsVo>> getProjectMaintainStatistics(DeviceAlarmDto deviceAlarmDto){
        Stopwatch watch = Stopwatch.createStarted();
        String year = deviceAlarmDto.getYear();
        if(year != null && !"".equals(year)){
            deviceAlarmDto.setStartTime(year+"-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year+"-12-31 23:59:59");
        }
        List<StatisticsVo> statistics = alarmStatisticsService.getProjectMaintainStatistics(deviceAlarmDto);
        watch.stop();
        log.info("Query projectStatistics Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(statistics);
    }

    @GetMapping(value = "findDeviceModelKind")
    @ApiOperation(value = "获取设备型号,类型,种类")
    public ResponseBean<List<KindVo>> findDeviceModelKind() {
        return new ResponseBean<>(regionService.findDeviceModelKind1());
    }

    @ApiOperation(value = "获取项目设备清单")
    @PostMapping(value = "projectDeviceList")
    public ResponseBean<List<ProjectDeviceVo>> getProjectDeviceList(@RequestBody DeviceAlarmDto deviceAlarmDto){
        Stopwatch watch = Stopwatch.createStarted();
        String tenantCode = SysUtil.getTenantCode();
        List<ProjectDeviceVo> result = projectService.getProjectDeviceList(deviceAlarmDto);
        if (result == null) {
            result = new ArrayList<>();
        }
        watch.stop();
        log.info("Query projectDeviceList Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(result);
    }


    /**
     * 导出项目数据
     *
     * @param project
     */
    @PostMapping("export")
    @ApiOperation(value = "导出项目数据", notes = "导出项目数据")
    @ApiImplicitParam(name = "project", value = "项目信息", required = true, dataType = "Project")
    @Log(value = "导出项目数据", businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportProject(@RequestBody Project project, HttpServletRequest request, HttpServletResponse response) {
        return projectService.export(project, request, response);
    }

    /**
     * 根据租户编码查询项目列表
     *
     * @param tenantCode
     * @return ResponseBean
     */

    @ApiOperation(value = "根据租户编码查询项目列表")
    @GetMapping(value = "findByTenantCode")
    public ResponseBean
            <List<Project>> findProjectByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(projectService.findProjectByTenantCode(tenantCode));
    }
}
