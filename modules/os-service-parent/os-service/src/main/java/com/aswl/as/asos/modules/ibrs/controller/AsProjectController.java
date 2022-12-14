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
 * ????????? ???????????????
 * </p>
 *
 * @author hfx
 * @since 2019-11-15
 */
@RestController
@RequestMapping("/ibrs/as-project")
@Api("?????????")
@Slf4j
public class AsProjectController extends AbstractController {

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    ISysTenantService iSysTenantService;

    /**
     * ????????????
     */
    @GetMapping("/list")
    @RequiresPermissions("os:project:list")
    @ApiOperation("????????????")
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
     * ????????????
     */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:project:info")
    @ApiOperation("????????????")
    public R info(@PathVariable("entityId") String entityId) {

        AsProject e = iAsProjectService.getEntityById(entityId);

        // ?????????????????????tenantName??????
        e.setTenantName(getNameByTenantCode(e.getTenantCode()));

        return R.ok().put("data", e);
    }

    /**
     * ??????
     */
    @SysLog("????????????")
    @PostMapping("/save")
    @RequiresPermissions("os:project:save")
    @ApiOperation("????????????")
    public R save(@RequestBody AsProject entity) {
        //????????????
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
     * ??????
     */
    @SysLog("????????????")
    @PostMapping("/update")
    @RequiresPermissions("os:project:update")
    @ApiOperation("????????????")
    public R update(@RequestBody AsProject entity) {
        //????????????
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
     * ????????????
     */
    @SysLog("????????????")
    @PostMapping("/delete")
    @RequiresPermissions("os:project:delete")
    @ApiOperation("????????????")
    public R delete(@RequestBody String[] ids) {

        //??????????????????????????????????????????????????????????????????????????????

        //????????????????????????????????????????????????
        String result = iAsProjectService.deleteEntityByIds(ids);
        if (result != null) {
            return R.error(result);
        }

        return R.ok();
    }


    /**
     * ??????tenantCode??????????????????
     */
    @GetMapping("/getProjectListByTenantCode")
    @RequiresPermissions("os:project:list")
    @ApiOperation("????????????")
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
            //????????????
            ValidatorUtils.validateEntity(e);

            //?????????????????????as-user???????????????????????????????????????????????????????????????????????????????????????????????????
            SysTenant tenant = iSysTenantService.getSysTenantByTenantCode(e.getTenantCode());
            if (tenant == null) {
                return "?????????????????????";
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
                //??????????????????????????????????????? COMMON_TENANT_NAME_MAP
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

    //---------------------------------------?????????????????????????????????????????????------------------------------------------

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
    @ApiOperation(value = "??????????????????,??????,??????")
    public List<KindVo> findDeviceModelKind() {
        Stopwatch watch = Stopwatch.createStarted();
        List<KindVo> result = iAsProjectService.findDeviceModelKind();
        watch.stop();
        log.info("Query DeviceModelKind Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    @ApiOperation(value = "????????????????????????????????????")
    @GetMapping(value = "/modelStatistics")
    public List<ModelStatisticsVo> getModelStatistics(@RequestParam("projectId") String projectId) {
        List<ModelStatisticsVo> result = iAsProjectService.findModelStatistics(projectId);
        return result;
    }

    @ApiOperation(value = "????????????????????????")
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

    @ApiOperation(value = "????????????????????????????????????")
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

    @ApiOperation(value = "????????????")
    @GetMapping(value = "/download")
    public void download(@RequestParam("filePath") String filePath, HttpServletResponse response) throws Exception {
        Stopwatch watch = Stopwatch.createStarted();
        //???????????????
        String name = filePath.substring(filePath.lastIndexOf("/"));
        String extName = filePath.substring(filePath.indexOf(".")).toLowerCase().trim(); //?????????????????????
        String realName = name.substring(1, name.lastIndexOf("_")) + extName;
        // ???????????????
        OutputStream outputStream = null;
        try {
            response.setContentType("application/force-download");// ??????????????????????????? ?? ?? ?? ?? ?? ??
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(realName.getBytes("UTF-8"), "iso-8859-1"));
            byte[] buf = iAsProjectService.download(filePath).getBody();
            outputStream = response.getOutputStream();
            outputStream.write(buf);
            response.flushBuffer();
        } catch (Exception e) {
            log.info("??????????????????" + e.getMessage());
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
