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
 * ?????????controller
 *
 * @author hfx
 * @date 2019-12-17 19:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/project", tags = "?????????")
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
     * ??????ID???????????????
     *
     * @param projectId
     * @return ResponseBean
     */
    @ApiOperation(value = "???????????????ID???????????????")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "?????????ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{projectId}")
    public ResponseBean<Project> findById(@PathVariable("projectId") String projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        return new ResponseBean<>(projectService.get(project));
    }

    /**
     * ?????????????????????
     *
     * @param project
     * @return ResponseBean
     */

    @ApiOperation(value = "???????????????????????????")
    @ApiImplicitParam(name = "project", value = "???????????????", paramType = "path", required = true, dataType = "project")
    @GetMapping(value = "projects")
    public ResponseBean
            <List<Project>> findAll(Project project) {
        project.setTenantCode(SysUtil.getTenantCode());
        //TODO ????????????????????????????????????projectId??????????????????????????????
        return new ResponseBean<>(projectService.findList(project));
    }

    /**
     * ???????????????????????????????????????
     *
     * @param project
     * @return ResponseBean
     */
    @ApiOperation(value = "???????????????????????????????????????")
    @ApiImplicitParam(name = "project", value = "???????????????", paramType = "path", required = false, dataType = "project")
    @GetMapping(value = "getUserProject")
    public ResponseBean<List<OsProjectDto>>getUserProject(Project project) {

        //TODO ?????????????????????????????????????????????????????????projectId????????????????????????????????????????????????
        //TODO ????????????????????????????????????projectId??????????????????????????????
        String roles = RoleContextHolder.getRole();
        String regionCode = project.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = null;

        String treeRootId = "-1"; //???????????????????????????parentId
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //??????????????????????????????
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
                    projectId = "-9";   //????????????
                }
            }else{
                projectId = "-9";   //???????????????????????????????????????????????????
            }
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return new ResponseBean<>(null,"????????????????????????");
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
            // ???????????????????????????
            treeList = TreeUtil.buildTree(CollUtil.sort(projectDtoList, Comparator.comparingInt(OsProjectDto::getSort)), treeRootId);
        }

        return new ResponseBean<>(treeList);
    }

    /**
     * ???????????????????????????
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param project
     * @return PageInfo
     */
    @ApiOperation(value = "???????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "????????????", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "????????????", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "????????????", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "????????????", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "project", value = "???????????????", dataType = "project")
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
     * ???????????????
     *
     * @param projectDto
     * @return ResponseBean
     */
    @ApiOperation(value = "???????????????", notes = "???????????????")
    @PostMapping
    @Log("???????????????")
    public ResponseBean
            <Boolean> insertProject(@RequestBody @Valid ProjectDto projectDto) {
        int result = projectService.insert(projectDto);
        if(result > 0){
            //??????????????????
            Map object = new HashMap();
            object.put("alias", "projectInfo");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return new ResponseBean<>( result > 0);
    }

    /**
     * ???????????????
     *
     * @param projectDto
     * @return ResponseBean
     */

    @ApiOperation(value = "?????????????????????", notes = "???????????????ID?????????????????????")
    @Log("???????????????")
    @PutMapping
    public ResponseBean
            <Boolean> updateProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        project.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), projectDto.getTenantCode(),projectDto.getProjectId());
        int result = projectService.update(project);
        if(result > 0){
            //??????????????????
            Map object = new HashMap();
            object.put("alias", "projectInfo");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return new ResponseBean<>(result > 0);
    }

    /**
     * ???????????????ID?????????????????????
     *
     * @param projectId
     * @return ResponseBean
     */
    @ApiOperation(value = "?????????????????????", notes = "???????????????ID?????????????????????")
    @ApiImplicitParam(name = "projectId", value = "?????????ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{projectId}")
    public ResponseBean
            <Boolean> deleteProjectById(@PathVariable("projectId") String projectId) {

       /* // ??????????????????????????????????????????
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
     * ???????????????????????????
     *
     * @param project
     * @return ResponseBean
     */

    @ApiOperation(value = "?????????????????????", notes = "???????????????ID?????????????????????")
    @ApiImplicitParam(name = "project", value = "???????????????", dataType = "project")
    @Log("?????????????????????")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllProject(@RequestBody Project project) {
        boolean success = false;
        try {

            // ?????????????????????????????????????????????????????????????????????
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
            log.error("????????????????????????", e);
        }
        return new ResponseBean<>(success);
    }

    private ResponseBean<Boolean> canDeleteProject(String projectId)
    {
        Device d=new Device();
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //d.setTenantCode("aswl");
        d.setProjectId(projectId);
        //??????????????????????????????????????????????????????
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
                //?????????????????????????????????
                throw new Exception("?????? "+projectName+" ?????????????????????????????????????????????????????????????????????");
            }
            catch (Exception e)
            {
                return new ResponseBean<>(e);
            }
        }
        return null;
    }

    /**
     * ??????????????????
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "??????????????????")
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
    @ApiOperation(value = "??????????????????,??????,??????")
    public ResponseBean<List<KindVo>> findDeviceModelKind() {
        return new ResponseBean<>(regionService.findDeviceModelKind1());
    }

    @ApiOperation(value = "????????????????????????")
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
     * ??????????????????
     *
     * @param project
     */
    @PostMapping("export")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "project", value = "????????????", required = true, dataType = "Project")
    @Log(value = "??????????????????", businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportProject(@RequestBody Project project, HttpServletRequest request, HttpServletResponse response) {
        return projectService.export(project, request, response);
    }

    /**
     * ????????????????????????????????????
     *
     * @param tenantCode
     * @return ResponseBean
     */

    @ApiOperation(value = "????????????????????????????????????")
    @GetMapping(value = "findByTenantCode")
    public ResponseBean
            <List<Project>> findProjectByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(projectService.findProjectByTenantCode(tenantCode));
    }
}
