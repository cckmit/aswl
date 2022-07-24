package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.utils.DeviceUtils;
import com.aswl.as.asos.common.utils.LRUMap;
import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRole;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRoleMenu;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysMenuService;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysRoleMenuService;
import com.aswl.as.asos.modules.asuser.service.ISysTenantService;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.asos.modules.sys.entity.SysMenuEntity;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.user.api.module.RoleMenu;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.ibrs.entity.AsIbrsAsDevice;
import com.aswl.as.asos.modules.ibrs.service.IAsIbrsAsDeviceService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-25
 */
@RestController

@RequestMapping("/ibrs/as-device")
@Api("设备表")
public class AsIbrsAsDeviceController extends AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IAsIbrsAsDeviceService iAsIbrsAsDeviceService;

    @Autowired
    IAsUserSysMenuService iAsUserSysMenuService;

    @Autowired
    IAsUserSysRoleMenuService iAsUserSysRoleMenuService;

    @Autowired
    ISysTenantService iSysTenantService;

    @Autowired
    IAsProjectService iAsProjectService;

    /**
     * 测试
     */
    @PostMapping("/test")
    @RequiresPermissions("os:device:delete")
    @ApiOperation("测试各个接口")
    public ResponseBean<Boolean> test(@RequestBody DeviceDto deviceDto){

        return new ResponseBean<>();
    }
    /**
     * 保存
     */
    @PostMapping("/insertDevice")
    @RequiresPermissions("os:device:save")
    @ApiOperation("通过ibrs模块保存设备表")
    public ResponseBean<Boolean> osDevice2(@RequestBody DeviceDto deviceDto){
        return iAsIbrsAsDeviceService.osDevice2(deviceDto);
    }

    /**
     * 更新
     */
    @PostMapping("/updateDevice")
    @RequiresPermissions("os:device:update")
    @ApiOperation("通过ibrs模块更新设备表")
    public ResponseBean<Boolean> osDevice4(@RequestBody DeviceDto deviceDto){
        return iAsIbrsAsDeviceService.osDevice4(deviceDto);
    }

    /**
     * 批量删除
     * @param device
     * @return
     */
    @PostMapping("/deleteAllDevice")
    @RequiresPermissions("os:device:delete")
    @ApiOperation("通过ibrs模块批量删除设备表")
    ResponseBean<Boolean> osDevice5(@RequestBody Device device){
        return iAsIbrsAsDeviceService.osDevice5(device);
    }

    @GetMapping("/deviceList")
    @RequiresPermissions("os:device:list")
    @ApiOperation("通过ibrs模块获取分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "Device", value = "设备信息", dataType = "Device")
    })
    ResponseBean<PageInfo<DeviceVo>> deviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                @RequestParam(value = "regionId", defaultValue = "",required = false) String regionId,@RequestParam(value = "tenantCode", defaultValue = "",required = false)String tenantCode,
                                                @RequestParam(value = "projectId", defaultValue = "",required = false)String projectId,
                                                DeviceDto deviceDto)
    {
        deviceDto.setTenantCode(tenantCode);
        deviceDto.setProjectId(projectId);
        ResponseBean<PageInfo<DeviceVo>> result=iAsIbrsAsDeviceService.osDevice6(pageNum,pageSize,sort,order,regionId,deviceDto);
        if(result.getCode()==ResponseBean.SUCCESS && result.getData()!=null)
        {
            List<DeviceVo> list=result.getData().getList();
            if(list!=null)
            {
                for(DeviceVo temp:list)
                {
                    //设置租户名称
                    temp.setTenantName(getNameByTenantCode(temp.getTenantCode()));
                }
            }
        }

        return result;
    }

    /**
     * 根据ID获取设备
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备ID查询设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceVo> osDevice3(@PathVariable("id") String id) {

        //设置名称
        ResponseBean<DeviceVo> r=iAsIbrsAsDeviceService.osDevice3(id);
        if(r.getCode()==ResponseBean.SUCCESS)
        {
            DeviceVo temp=r.getData();
            temp.setTenantName(getNameByTenantCode(temp.getTenantCode()));
        }

        return r;
    }

    /**
     * 导入设备数据
     *
     * @param file file
     * @return ResponseBean
     */
    @PostMapping("import")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    public ResponseBean osDevice7(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            List<DeviceDto> deviceDtos = MapUtil.map2Java(DeviceDto.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), DeviceUtils.getOsDeviceMap()));

            if (CollectionUtils.isEmpty(deviceDtos))
                throw new CommonException("无设备信息数据导入.");

            DeviceDto firstDeviceDtos=deviceDtos.get(0);
            firstDeviceDtos.setIsAsOs(CommonConstant.IS_ASOS_TRUE);

            firstDeviceDtos.setApplicationCode(SysUtil.getSysCode());


            // 导入的时候，需要填写时哪个租户的，Excel模板需要写
            firstDeviceDtos.setCreator("admin");

            ResponseBean r=iAsIbrsAsDeviceService.osDevice7(deviceDtos);

            return r;
        } catch (Exception e) {
            logger.error("导入设备数据失败！", e);
            throw new CommonException("导入设备数据失败！");
        }
    }

    /**
     * 导出设备数据
     *
     * @param deviceDto deviceDto
     */
    @PostMapping("export")
    @ApiOperation(value = "导出设备信息", notes = "导出设备信息")
    @ApiImplicitParam(name = "deviceDto", value = "设备信息", required = true, dataType = "deviceDto")
    public ResponseBean<Boolean> osDevice8(@RequestBody DeviceDto deviceDto, HttpServletRequest request, HttpServletResponse response) {

        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "设备信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            Device device=new Device();
            BeanUtils.copyProperties(deviceDto,device);
            ResponseBean<List<Device>> r=iAsIbrsAsDeviceService.osDevice8(deviceDto);

            List<Device> devices;
            if(r.getCode()==ResponseBean.SUCCESS)
            {
                devices=r.getData();
            }
            else
            {
                //直接抛异常
                throw new CommonException(r.getMsg());
            }

            if (CollectionUtils.isEmpty(devices)) {
                throw new CommonException("无设备信息数据.");
            }

            // 将项目code设置到projectId处
            List<AsProject> projectList =iAsProjectService.findList(new AsProject());
            Map<String,String> projectCodeMap=new HashMap<>();
            for(AsProject p:projectList)
            {
                projectCodeMap.put(p.getProjectId(),p.getProjectCode());
            }

            for(Device d:devices)
            {
                d.setProjectId(projectCodeMap.get(d.getProjectId()));
            }

            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(devices), DeviceUtils.getOsDeviceMap());

            //可能需要返回空
            return new ResponseBean<>(true,"导出成功");
        } catch (Exception e) {
            //log.error("导出设备报警信息数据失败！", e);
            throw new CommonException("导出设备报警信息数据失败！");
        }
    }


    /**
     * 获取上级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "getSuperiorDevice")
    @ApiOperation(value = "运营端获取上级设备信息", notes = "运营端根据父级ID获取上级设备信息")
    @ApiImplicitParam(name = "parentId", value = "父ID", required = true)
    public ResponseBean<DeviceVo> osGetSuperiorDevice(@RequestParam("parentId") String parentId) {
        return iAsIbrsAsDeviceService.osDevice9(parentId);
    }

    /**
     * 获取下级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "getSubordinateDevice")
    @ApiOperation(value = "运营端获取下级设备信息", notes = "运营端根据设备ID获取下级设备信息")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<List<DeviceVo>> osGetSubordinateDevice(@RequestParam("id") String id) {
        return iAsIbrsAsDeviceService.osDevice10(id);
    }

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "updateParentDeviceId/{id}")
    @ApiOperation(value = "运营端根据设备id更新上级设备ID为空", notes = "运营端根据设备id更新上级设备ID为空")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<Boolean> osDevice11(@PathVariable("id") String id) {
        return iAsIbrsAsDeviceService.osDevice11(id);
    }

    private String verifyForm(AsIbrsAsDevice e)
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


    private String getNameByTenantCode(String tenantCode)
    {
        LRUMap<String, String> tenantNameMap= OsGlobalData.COMMON_TENANT_NAME_MAP;

        if(!org.apache.commons.lang.StringUtils.isEmpty(tenantCode))
        {
            if(!tenantNameMap.containsKey(tenantCode))
            {
                //到数据库查对应的资料，放入 COMMON_TENANT_NAME_MAP
                SysTenant tenant=iSysTenantService.getSysTenantByTenantCode(tenantCode);
                if(tenant==null)
                {
                    return null;
                }
                tenantNameMap.put(tenant.getTenantCode(),tenant.getTenantName());
            }
            return (String)tenantNameMap.get(tenantCode);
        }
        return null;
    }

}
