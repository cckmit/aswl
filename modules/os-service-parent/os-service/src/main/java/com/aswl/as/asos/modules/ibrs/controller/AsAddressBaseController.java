package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.utils.AsAddressBaseUtil;
import com.aswl.as.asos.common.utils.DeviceUtils;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsAddressBase;
import com.aswl.as.asos.modules.ibrs.service.IAsAddressBaseService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 点位地址库 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
@RestController

@RequestMapping("/ibrs/as-address-base")
@Api("点位地址库")
public class AsAddressBaseController extends AbstractController {

    @Autowired
    IAsAddressBaseService iAsAddressBaseService;

    @Autowired
    IAsProjectService iAsProjectService;

    /**
    * 点位地址库分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceBase:list")
    @ApiOperation("点位地址库分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsAddressBaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 点位地址库列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceBase:list")
    @ApiOperation("点位地址库列表")
    public R findList(@RequestBody AsAddressBase entity){
        List<AsAddressBase> list= iAsAddressBaseService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
     * 点位地址库列表，用来提供地图查询
     */
    @PostMapping("/findListForMap")
    @RequiresPermissions("os:deviceBase:list")
    @ApiOperation("点位地址库列表")
    public R findListForMap(@RequestBody Map<String, Object> params){
        List<AsAddressBase> list= iAsAddressBaseService.findListForMap(params);
        return R.ok().put("list",list);
    }

    /**
    * 点位地址库信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceBase:info")
    @ApiOperation("点位地址库信息")
    public R info(@PathVariable("entityId") String entityId){

        AsAddressBase data = iAsAddressBaseService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存点位地址库")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceBase:save")
    @ApiOperation("保存点位地址库")
    public R save(@RequestBody AsAddressBase entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsAddressBaseService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新点位地址库")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceBase:update")
    @ApiOperation("更新点位地址库")
    public R update(@RequestBody AsAddressBase entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsAddressBaseService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除点位地址库
    */
    @SysLog("删除点位地址库")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceBase:delete")
    @ApiOperation("删除点位地址库")
    public R delete(@RequestBody String[] ids){
        iAsAddressBaseService.deleteEntityByIds(ids);
        return R.ok();
    }

    //导入导出
    /**
     * 导入设备数据
     *
     * @param file file
     * @return ResponseBean
     */
    @PostMapping("import")
    @ApiOperation(value = "导入数据", notes = "导入数据")
//    @Log("导入设备")
    public ResponseBean importBase(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
//            log.debug("开始导入设备数据");
            List<AsAddressBase> addressBases = MapUtil.map2Java(AsAddressBase.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), AsAddressBaseUtil.getAsAddressBaseMap()));

            if (CollectionUtils.isEmpty(addressBases))
                throw new CommonException("无地址库信息数据导入.");

            return iAsAddressBaseService.importBase(addressBases);
        } catch (Exception e) {
            logger.error("导入设备数据失败！", e);
            throw new CommonException("导入设备数据失败！");
        }
    }

    /**
     * 导出设备数据
     *
     * @param params
     */
    @PostMapping("export")
    @ApiOperation(value = "导出设备信息", notes = "导出设备信息")
    @ApiImplicitParam(name = "deviceDto", value = "设备信息", required = true, dataType = "deviceDto")
    public ResponseBean<Boolean> exportBase(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {

        try {

            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "设备信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));

            List<AsAddressBase> list= iAsAddressBaseService.findListForMap(params);
            Map<String,AsProject> pMap=new HashMap<>();
            AsProject p;
            for(AsAddressBase temp:list)
            {
                String projectId=temp.getProjectId();
                if(pMap.containsKey(projectId) && pMap.get(projectId)!=null )
                {
                    temp.setProjectCode(pMap.get(projectId).getProjectCode());
                }
                else
                {
                    AsProject tempP=iAsProjectService.getEntityById(temp.getProjectId());
                    if(tempP!=null)
                    {
                        pMap.put(projectId,tempP);
                        temp.setProjectCode(tempP.getProjectCode());
                    }
                    else
                    {
                        pMap.put(projectId,null);
                    }
                }
            }

            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(list), AsAddressBaseUtil.getAsAddressBaseMap());

            //ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(devices), DeviceUtils.getOsDeviceMap());

            //可能需要返回空
            return new ResponseBean<>(true,"导出成功");
        } catch (Exception e) {
            //log.error("导出设备报警信息数据失败！", e);
            throw new CommonException("导出设备报警信息数据失败！");
        }
    }

    /**
     * 点位地址库查询返回资料
     */
    @PostMapping("/findListForQuery")
    @RequiresPermissions("os:deviceBase:list")
    @ApiOperation("点位地址库查询返回资料")
    public R findListForQuery(@RequestParam Map<String, Object> params){
        List<AsAddressBase> list= iAsAddressBaseService.findListForQuery(params);
        return R.ok().put("list",list);
    }

    private String verifyForm(AsAddressBase e)
    {
        try
        {
            //表单校验
            ValidatorUtils.validateEntity(e);
        }
        catch (Exception tempException)
        {
            return tempException.getMessage();
        }

        return null;
    }


}
