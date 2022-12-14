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
 * ????????? ???????????????
 * </p>
 *
 * @author hfx
 * @since 2019-11-25
 */
@RestController

@RequestMapping("/ibrs/as-device")
@Api("?????????")
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
     * ??????
     */
    @PostMapping("/test")
    @RequiresPermissions("os:device:delete")
    @ApiOperation("??????????????????")
    public ResponseBean<Boolean> test(@RequestBody DeviceDto deviceDto){

        return new ResponseBean<>();
    }
    /**
     * ??????
     */
    @PostMapping("/insertDevice")
    @RequiresPermissions("os:device:save")
    @ApiOperation("??????ibrs?????????????????????")
    public ResponseBean<Boolean> osDevice2(@RequestBody DeviceDto deviceDto){
        return iAsIbrsAsDeviceService.osDevice2(deviceDto);
    }

    /**
     * ??????
     */
    @PostMapping("/updateDevice")
    @RequiresPermissions("os:device:update")
    @ApiOperation("??????ibrs?????????????????????")
    public ResponseBean<Boolean> osDevice4(@RequestBody DeviceDto deviceDto){
        return iAsIbrsAsDeviceService.osDevice4(deviceDto);
    }

    /**
     * ????????????
     * @param device
     * @return
     */
    @PostMapping("/deleteAllDevice")
    @RequiresPermissions("os:device:delete")
    @ApiOperation("??????ibrs???????????????????????????")
    ResponseBean<Boolean> osDevice5(@RequestBody Device device){
        return iAsIbrsAsDeviceService.osDevice5(device);
    }

    @GetMapping("/deviceList")
    @RequiresPermissions("os:device:list")
    @ApiOperation("??????ibrs????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "????????????", defaultValue = CommonConstant.PAGE_NUM_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "????????????", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "????????????", defaultValue = CommonConstant.PAGE_SORT_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "????????????", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "Device", value = "????????????", dataType = "Device")
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
                    //??????????????????
                    temp.setTenantName(getNameByTenantCode(temp.getTenantCode()));
                }
            }
        }

        return result;
    }

    /**
     * ??????ID????????????
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "????????????ID????????????")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "??????ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceVo> osDevice3(@PathVariable("id") String id) {

        //????????????
        ResponseBean<DeviceVo> r=iAsIbrsAsDeviceService.osDevice3(id);
        if(r.getCode()==ResponseBean.SUCCESS)
        {
            DeviceVo temp=r.getData();
            temp.setTenantName(getNameByTenantCode(temp.getTenantCode()));
        }

        return r;
    }

    /**
     * ??????????????????
     *
     * @param file file
     * @return ResponseBean
     */
    @PostMapping("import")
    @ApiOperation(value = "????????????", notes = "????????????")
    public ResponseBean osDevice7(@ApiParam(value = "??????????????????", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            List<DeviceDto> deviceDtos = MapUtil.map2Java(DeviceDto.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), DeviceUtils.getOsDeviceMap()));

            if (CollectionUtils.isEmpty(deviceDtos))
                throw new CommonException("???????????????????????????.");

            DeviceDto firstDeviceDtos=deviceDtos.get(0);
            firstDeviceDtos.setIsAsOs(CommonConstant.IS_ASOS_TRUE);

            firstDeviceDtos.setApplicationCode(SysUtil.getSysCode());


            // ???????????????????????????????????????????????????Excel???????????????
            firstDeviceDtos.setCreator("admin");

            ResponseBean r=iAsIbrsAsDeviceService.osDevice7(deviceDtos);

            return r;
        } catch (Exception e) {
            logger.error("???????????????????????????", e);
            throw new CommonException("???????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param deviceDto deviceDto
     */
    @PostMapping("export")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "deviceDto", value = "????????????", required = true, dataType = "deviceDto")
    public ResponseBean<Boolean> osDevice8(@RequestBody DeviceDto deviceDto, HttpServletRequest request, HttpServletResponse response) {

        try {
            // ??????response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "????????????" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
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
                //???????????????
                throw new CommonException(r.getMsg());
            }

            if (CollectionUtils.isEmpty(devices)) {
                throw new CommonException("?????????????????????.");
            }

            // ?????????code?????????projectId???
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

            //?????????????????????
            return new ResponseBean<>(true,"????????????");
        } catch (Exception e) {
            //log.error("???????????????????????????????????????", e);
            throw new CommonException("???????????????????????????????????????");
        }
    }


    /**
     * ????????????????????????(????????????????????????????????????ignore???????????????)
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "getSuperiorDevice")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????ID????????????????????????")
    @ApiImplicitParam(name = "parentId", value = "???ID", required = true)
    public ResponseBean<DeviceVo> osGetSuperiorDevice(@RequestParam("parentId") String parentId) {
        return iAsIbrsAsDeviceService.osDevice9(parentId);
    }

    /**
     * ????????????????????????(????????????????????????????????????ignore???????????????)
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "getSubordinateDevice")
    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????ID????????????????????????")
    @ApiImplicitParam(name = "id", value = "??????ID", required = true)
    public ResponseBean<List<DeviceVo>> osGetSubordinateDevice(@RequestParam("id") String id) {
        return iAsIbrsAsDeviceService.osDevice10(id);
    }

    /**
     * ????????????id??????????????????ID??????
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "updateParentDeviceId/{id}")
    @ApiOperation(value = "?????????????????????id??????????????????ID??????", notes = "?????????????????????id??????????????????ID??????")
    @ApiImplicitParam(name = "id", value = "??????ID", required = true)
    public ResponseBean<Boolean> osDevice11(@PathVariable("id") String id) {
        return iAsIbrsAsDeviceService.osDevice11(id);
    }

    private String verifyForm(AsIbrsAsDevice e)
    {

        try
        {
            //????????????
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
                //??????????????????????????????????????? COMMON_TENANT_NAME_MAP
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
