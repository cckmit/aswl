package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.utils.LRUMap;
import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.asos.modules.asuser.service.ISysTenantService;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.OsinfoUtil;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.KindVo;
import com.aswl.as.ibrs.api.vo.ModelStatisticsVo;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.api.vo.StatisticsVo;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-15
 */
@RestController
@RequestMapping("/ibrs/as-project")
@Api("项目表")
@Slf4j
public class AsProjectController extends AbstractController {

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    ISysTenantService iSysTenantService;

    /**
     * 项目列表
     */
    @GetMapping("/list")
    @RequiresPermissions("os:project:list")
    @ApiOperation("项目列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = iAsProjectService.queryPage(params);
        List<AsProject> list = (List<AsProject>) page.getList();
        if (list != null && list.size() > 0) {
            for (AsProject p : list) {
                p.setTenantName(getNameByTenantCode(p.getTenantCode()));
            }
        }

        return R.ok().put("page", page);
    }

    /**
     * 项目信息
     */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:project:info")
    @ApiOperation("项目信息")
    public R info(@PathVariable("entityId") String entityId) {

        AsProject e = iAsProjectService.getEntityById(entityId);

        // 在这里把租户的tenantName设置
        e.setTenantName(getNameByTenantCode(e.getTenantCode()));

        return R.ok().put("data", e);
    }

    /**
     * 保存
     */
    @SysLog("保存项目")
    @PostMapping("/save")
    @RequiresPermissions("os:project:save")
    @ApiOperation("保存项目")
    public R save(@RequestBody AsProject entity) {
        //数据校验
        String vStr = verifyForm(entity);
        if (vStr != null) {
            return R.error(vStr);
        }
        entity.setCreatorId(getUserName());
        entity.setCreateAt(new Date());
        iAsProjectService.saveEntity(entity);

        return R.ok();
    }

    /**
     * 保存
     */
    @SysLog("更新项目")
    @PostMapping("/update")
    @RequiresPermissions("os:project:update")
    @ApiOperation("更新项目")
    public R update(@RequestBody AsProject entity) {
        //数据校验
        String vStr = verifyForm(entity);
        if (vStr != null) {
            return R.error(vStr);
        }
        entity.setUpdaterId(getUserName());
        entity.setUpdateAt(new Date());
        iAsProjectService.updateEntityById(entity);

        return R.ok();
    }

    /**
     * 删除项目
     */
    @SysLog("删除项目")
    @PostMapping("/delete")
    @RequiresPermissions("os:project:delete")
    @ApiOperation("删除项目")
    public R delete(@RequestBody String[] ids) {

        //后面要判断权限，看能不能删之类的，或者不提供这个接口

        //删除项目的时候需要判断有没有设备
        String result = iAsProjectService.deleteEntityByIds(ids);
        if (result != null) {
            return R.error(result);
        }

        return R.ok();
    }


    /**
     * 根据tenantCode获取项目列表
     */
    @GetMapping("/getProjectListByTenantCode")
    @RequiresPermissions("os:project:list")
    @ApiOperation("项目列表")
    public R getProjectListByTenantCode(@RequestParam String tenantCode) {
        List<AsProject> list = iAsProjectService.getProjectListByTenantCode(tenantCode);
        if (list != null && list.size() > 0) {
            for (AsProject p : list) {
                p.setTenantName(getNameByTenantCode(p.getTenantCode()));
            }
        }
        return R.ok().put("list", list);
    }

    private String verifyForm(AsProject e) {

        try {
            //表单校验
            ValidatorUtils.validateEntity(e);

            //这里可能需要查as-user表的数据来校验是否有该租户，或者如果耗性能就让前端传值不能为空就行
            SysTenant tenant = iSysTenantService.getSysTenantByTenantCode(e.getTenantCode());
            if (tenant == null) {
                return "没有该租户编号";
            }
        } catch (Exception tempException) {
            return tempException.getMessage();
        }

        return null;
    }

    private String getNameByTenantCode(String tenantCode) {
        LRUMap<String, String> tenantNameMap = OsGlobalData.COMMON_TENANT_NAME_MAP;

        if (!StringUtils.isEmpty(tenantCode)) {
            if (!tenantNameMap.containsKey(tenantCode)) {
                //到数据库查对应的资料，放入 COMMON_TENANT_NAME_MAP
                SysTenant tenant = iSysTenantService.getSysTenantByTenantCode(tenantCode);
                if (tenant == null) {
                    return null;
                }
                tenantNameMap.put(tenant.getTenantCode(), tenant.getTenantName());
            }
            return (String) tenantNameMap.get(tenantCode);
        }
        return null;
    }

    //---------------------------------------以下为远程调用项目管理档案资料------------------------------------------

    @GetMapping(value = "/projectList")
    public PageInfo<Project> projectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                         Project project) {
        Stopwatch watch = Stopwatch.createStarted();
        PageInfo<Project> result = iAsProjectService.projectList(pageNum, pageSize, project);
        watch.stop();
        log.info("Query projectList Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @PostMapping(value = "/insertProject")
    public Boolean insertProject(@RequestBody @Valid ProjectDto projectDto) {
        Stopwatch watch = Stopwatch.createStarted();
        Boolean result = iAsProjectService.insertProject(projectDto);
        watch.stop();
        log.info("Query insertProject Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @PutMapping(value = "/updateProject")
    public Boolean updateProject(@RequestBody @Valid ProjectDto projectDto) {
        Stopwatch watch = Stopwatch.createStarted();
        Boolean result = iAsProjectService.updateProject(projectDto);
        watch.stop();
        log.info("Query updateProject Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @PostMapping(value = "/projectStatistics")
    public List<StatisticsVo> getProjectMaintainStatistics(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        Stopwatch watch = Stopwatch.createStarted();
        List<StatisticsVo> result = iAsProjectService.getProjectMaintainStatistics(deviceAlarmDto);
        watch.stop();
        log.info("Query projectStatistics Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @PostMapping(value = "/findDeviceModelKind")
    @ApiOperation(value = "获取设备型号,类型,种类")
    public List<KindVo> findDeviceModelKind() {
        Stopwatch watch = Stopwatch.createStarted();
        List<KindVo> result = iAsProjectService.findDeviceModelKind();
        watch.stop();
        log.info("Query DeviceModelKind Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @ApiOperation(value = "获取项目设备清单型号信息")
    @GetMapping(value = "/modelStatistics")
    public List<ModelStatisticsVo> getModelStatistics(@RequestParam("projectId") String projectId) {
        List<ModelStatisticsVo> result = iAsProjectService.findModelStatistics(projectId);
        return result;
    }

    @ApiOperation(value = "获取项目设备清单")
    @PostMapping(value = "/projectDeviceList")
    public List<ProjectDeviceVo> getProjectDeviceList(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        Stopwatch watch = Stopwatch.createStarted();
        List<ProjectDeviceVo> result = iAsProjectService.getProjectDeviceList(deviceAlarmDto);
        if (result == null) {
            result = new ArrayList<>();
        }
        watch.stop();
        log.info("Query projectDeviceList Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @ApiOperation(value = "查询指定项目的附件表列表")
    @GetMapping(value = "/attachments")
    public List<Attachment> findAttachments(@RequestParam("projectId") String projectId) {
        Stopwatch watch = Stopwatch.createStarted();
        List<Attachment> result = iAsProjectService.findAttachments(projectId);
        if (result == null) {
            result = new ArrayList<>();
        }
        watch.stop();
        log.info("Query attachmentsList Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @ApiOperation(value = "下载文件")
    @GetMapping(value = "/download")
    public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) throws Exception {
        Stopwatch watch = Stopwatch.createStarted();
        //获取文件名
        String name = filePath.substring(filePath.lastIndexOf("/"));
        String extName = filePath.substring(filePath.indexOf(".")).toLowerCase().trim(); //获取文件后缀名
        String realName = name.substring(1, name.lastIndexOf("_")) + extName;
        // 其余处理略
        OutputStream outputStream = null;
        try {
            response.setContentType("application/force-download");// 设置强制下载不打开            
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(realName.getBytes("UTF-8"), "iso-8859-1"));
            byte[] buf = iAsProjectService.download(filePath).getBody();
            outputStream = response.getOutputStream();
            outputStream.write(buf);
            response.flushBuffer();
        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    watch.stop();
                    log.info("Download {} Cost: {}ms",realName,
                            watch.elapsed(TimeUnit.MILLISECONDS));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
