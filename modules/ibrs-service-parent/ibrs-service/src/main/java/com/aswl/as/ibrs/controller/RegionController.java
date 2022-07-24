package com.aswl.as.ibrs.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.user.api.dto.UserInfoDto;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.ibrs.api.dto.OsProjectDto;
import com.aswl.as.ibrs.api.dto.OsRegionDto;
import com.aswl.as.ibrs.api.dto.OsSysTenantDto;
import com.aswl.as.ibrs.api.dto.RegionDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.ProjectService;
import com.aswl.as.ibrs.service.RegionService;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Tenant;
import com.aswl.as.user.api.module.User;
import com.github.pagehelper.PageInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 区域controller
 *
 * @author dingfei
 * @date 2019-09-27 14:01
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/region", tags = "区域")
@RestController
@RequestMapping("/v1/region")
public class RegionController extends BaseController {

    private final RegionService regionService;
    private final DeviceService deviceService;

    private final ProjectService projectService;

    private final UserServiceClient userServiceClient;

    /**
     * 查询树形区域集合
     * @return
     */
    @GetMapping(value = "tree")
    @ApiOperation(value = "获取区域列表")
    public ResponseBean<List<RegionDto>> regions(@RequestParam(value = "regionCode",required = false) String regionCode,@RequestParam(value = "queryProjectId",required = false) String queryProjectId) {

        // 看情况admin要不要设置tenantCode
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId;
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户下暂无区域设备");
                    return new ResponseBean<>(null,"当前用户下暂无区域设备");
                }
                regionCode = userRegionCode;
            }
        }
        if (queryProjectId == null){
            projectId =SysUtil.getProjectId();
        }else{
            projectId =queryProjectId;
        }
        String parentId="-1";
        Region region = new Region();
        region.setApplicationCode(SysUtil.getSysCode());
        region.setTenantCode(tenantCode);
        region.setRegionCode(regionCode);
        region.setProjectId(projectId);
        if (StringUtils.isNotEmpty(regionCode)) {
            parentId = regionService.findRegionId(regionCode,tenantCode,projectId);
        }
        // 查询部门集合
        Stream<Region> rsStream = regionService.findListAll(region).stream();
        if (Optional.ofNullable(rsStream).isPresent()) {
            List<RegionDto> treeList = rsStream.map(RegionDto::new).sorted(Comparator.comparing(RegionDto::getId)).collect(Collectors.toList());
            // 排序、构建树形结构
            List<RegionDto> list =TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(RegionDto::getSort)), parentId);
            return new ResponseBean<>(list);
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 查询树形区域集合 获取 租户-项目-区域 格式的树，如果是admin会有租户，如果是普通用户，只会出现 项目-区域
     * @return
     */
    @GetMapping(value = "newTree")
    @ApiOperation(value = "获取区域列表")
    public ResponseBean newTree(@RequestParam(value = "regionCode",required = false) String regionCode,
                                @RequestParam(value = "projectId",required = false) String queryProjectId,
                                @RequestParam(value = "projectName",required = false) String projectName,
                                @RequestParam(value = "isApp",required = false) boolean isApp
                                ) {
        String parentId="-1";
        Region region = new Region();
        //没有删除的区域
        region.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {

        }
        //regionCode = null;
        if(regionCode != null && !"".equals(regionCode)){
            parentId = regionService.findRegionId(regionCode,tenantCode,projectId);
        }
        region.setTenantCode(tenantCode);
        region.setProjectId(projectId);
        region.setRegionCode(regionCode);
        //强制查找 queryProjectId 的地区 //实际上应该加个校验的，但是不加了
        if(!StringUtils.isEmpty(queryProjectId))
        {
            region.setProjectId(queryProjectId);
        }

        // 查询部门集合
        Stream<Region> rsStream=regionService.findListAll(region).stream();
        List<OsRegionDto> regionDtoList=new ArrayList<>();
        if (Optional.ofNullable(rsStream).isPresent()) {
            List<OsRegionDto> treeList = rsStream.map(OsRegionDto::new).collect(Collectors.toList());
            // 排序、构建树形结构
            regionDtoList=TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsRegionDto::getSort)), parentId);
        }

        //2021-08-26，局域网版也调整为有多项目，因此注释
//        //如果不是云平台，直接就返回
//        if(!CloudCommon.isCloud())
//        {
//            return new ResponseBean<>(regionDtoList);
//        }

        // 设置数据
        //获取所有的租户和项目

        //项目
        if(projectId != null && !"".equals(projectId)){
            List<String> projectIdList = new ArrayList<>();
            projectIdList.addAll(Arrays.asList(projectId.split(",")));
            parentId = projectService.findMinParentId(projectIdList);
        }
        Project tempP=new Project();
        tempP.setTenantCode(tenantCode);
        tempP.setProjectId(projectId);
        tempP.setRegionCode(regionCode);
        tempP.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        tempP.setProjectName(projectName);
        Stream<Project> pStream=projectService.findList(tempP).stream();
        List<OsProjectDto> projectDtoList=new ArrayList<>();
        if (Optional.ofNullable(pStream).isPresent()) {
            List<OsProjectDto> treeList =pStream.map(OsProjectDto::new).collect(Collectors.toList());
            //设置对应的数据
            for(OsRegionDto r:regionDtoList)
            {
                for(OsProjectDto p:treeList)
                {
                    if(p.getProjectId().equals(r.getProjectId()))
                    {
                        p.add(r);
                        break; //退出项目循环
                    }
                }
            }
            
            // 排序、构建树形结构
            projectDtoList=TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsProjectDto::getSort)), parentId);
        }
        if(CloudCommon.isCloud() && SysUtil.isAdmin() && !isApp)  //如果是云平台，且是超级管理员
        {
            //admin 的话，就要显示租户

            // 获取 租户列表
            List<OsSysTenantDto> tenantDtoList=new ArrayList<OsSysTenantDto>();
            ResponseBean<List<Tenant>> r=userServiceClient.getOwnTenantList();//=iSysTenantService.findList(tenant);
            if(ResponseBean.SUCCESS == r.getCode())
            {
                List<Tenant> tenantList=r.getData();
                if(tenantList!=null)
                {
                    Stream<Tenant>tStream =tenantList.stream();

                    if (Optional.ofNullable(tStream).isPresent()) {
                        tenantDtoList=tStream.map(OsSysTenantDto::new).collect(Collectors.toList());
                    }

                    for(OsProjectDto p:projectDtoList)
                    {
                        for(OsSysTenantDto t:tenantDtoList)
                        {
                            if(t.getTenantCode().equals(p.getTenantCode()))
                            {
                                t.add(p);
                                break; //退出租户循环
                            }
                        }
                    }
                }
            }


            return new ResponseBean<>(tenantDtoList);
        } else {
            return new ResponseBean<>(projectDtoList);
        }
    }

    /**
     * 根据ID获取区域
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据区域ID查询区域")
    public ResponseBean<Region> findById(@PathVariable("id") String id) {
        Region region = new Region();
        region.setId(id);
        return new ResponseBean<>(regionService.get(region));
    }

    /**
     * 查询所有区域
     *
     * @param region
     * @return ResponseBean
     */
    @GetMapping(value = "regions")
    @ApiOperation(value = "获取区域树列表")
    public ResponseBean
            <List<Region>> findAll(Region region) {
        region.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(regionService.findAllList(region));
    }

    /**
     * 分页查询区域列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param region
     * @return PageInfo
     */
    @GetMapping("regionList")
    @ApiOperation(value = "分页查询区域列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "region", value = "区域信息", dataType = "region")
    })
    public PageInfo<Region> regionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       Region region) {
        return regionService.findByPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), region);
    }

    /**
     * 新增区域
     *
     * @param region
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增区域", notes = "新增区域")
    @Log("新增区域")
    public ResponseBean
            <Boolean> insertRegion(@RequestBody @Valid Region region) {
        return new ResponseBean<>(regionService.insert(region) > 0);
    }

    /**
     * 修改区域
     *
     * @param region
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新区域信息", notes = "根据区域ID更新职位信息")
    @Log("修改区域")
    public ResponseBean
            <Boolean> updateRegion(@RequestBody @Valid Region region) {
        return new ResponseBean<>(regionService.update(region) > 0);
    }

    /**
     * 根据区域ID删除区域信息
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除区域信息", notes = "根据区域ID删除区域信息")
    public ResponseBean
            <Boolean> deleteRegionById(@PathVariable("id") String id) {
        Region region = new Region();
        region.setId(id);
        region.setNewRecord(false);
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(regionService.delete(region) > 0);
    }

    /**
     * 批量删除区域信息
     *
     * @param region
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除区域", notes = "根据区域ID批量删除区域")
    @ApiImplicitParam(name = "region", value = "区域信息", dataType = "region")
    @Log("批量删除区域")
    public ResponseBean
            <Boolean> deleteAllRegion(@RequestBody Region region) {

        boolean success = false;

        // 查出对象
        String[] arr= region.getIdString().split(",");

        Region r = null;

        for(String s:arr)
        {
            Region temp=new Region();
            temp.setId(s);
            r=regionService.get(temp);

            if(r!=null)
            {
                
                 Device device=new Device();
                device.setRegionCode(r.getRegionCode());
                List<Device> list = deviceService.findList(device);
                if (list!=null && list.size()>0){
                    List<String> idString = list.stream().map(Device::getId).collect(Collectors.toList());
                    if (idString != null){
                        Device  devices  = new Device();
                        devices.setIdString(idString.stream().collect(Collectors.joining(",")));
                        deviceService.deleteAllDevice(devices);
                    }
                }
                if (StringUtils.isNotEmpty(r.getRegionCode()))
                    success = regionService.deleteByRegionCode(r.getRegionCode()) > 0;
            }
            // 设置项目设备箱数量/摄像机数量
            /*Map mapNum = regionService.findByProjectId(r.getProjectId());
            Project project = new Project();
            project.setBoxNum(Integer.parseInt(mapNum.get("boxNum").toString()));
            project.setCameraNum(Integer.parseInt(mapNum.get("cameraNum").toString()));
            project.setProjectId(r.getProjectId());
            projectService.update(project);*/
        }
            return new ResponseBean<>(success);
    }

    /**
     * 根据用户对象获取区域
     *
     * @param userList
     * @return ResponseBean
     */
    @PostMapping(value = "/getRegionListByUsers")
    @ApiOperation(value = "根据用户对象获取区域", notes = "根据用户对象获取区域")
    public List<Region> getRegionListByUsers(@RequestBody List<User> userList) {
        return regionService.getRegionListByUsers(userList);
    }

    /**
     * 通过excel导入区域数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/importRegion", method = RequestMethod.POST)
    @ApiOperation(value = "通过excel导入区域数据", notes = "通过excel导入区域数据")
    public ResponseBean importRegion(HttpServletRequest request) {
        ResponseBean responseBean=new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try {
                return regionService.importRegion(file);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                responseBean.setData("文件导入失败:" + e.getMessage());

            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return responseBean;
    }


    /**
     * 导出区域数据
     *
     * @param region region
     */
    @PostMapping("export")
    @ApiOperation(value = "导出区域信息", notes = "导出区域信息")
    @ApiImplicitParam(name = "region", value = "区域信息", required = true, dataType = "Region")
    @Log(value = "导出区域信息",businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportRegion(@RequestBody Region region, HttpServletRequest request, HttpServletResponse response) {
        return regionService.exportRegion(region,request,response);
    }

    /**
     * 查询所有区域
     *
     * @param region
     * @return ResponseBean
     */
    @GetMapping(value = "getRegionRootByProjectId")
    @ApiOperation(value = "获取x树列表")
    public ResponseBean<Region> getRegionRootByProjectId(Region region) {
//        region.setTenantCode(SysUtil.getTenantCode());
        if(!StringUtils.isEmpty(region.getProjectId()))
        {
            region.setParentId("-1");//获取一个节点
            List<Region> list=regionService.findAllList(region);
            if(list.size()>0)
            {
                return new ResponseBean<>(list.get(0));
            }
        }
        return new ResponseBean<>();
    }
    
    /**
     * 根据ID获取区域
     *
     * @param regionCode
     * @return ResponseBean
     */
    @GetMapping(value = "/meta/{regionCode}")
    @ApiOperation(value = "根据区域ID查询区域")
    public Region metaGetRegionById(@PathVariable("regionCode") String regionCode) {
        Region region = new Region();
        region.setRegionCode(regionCode);
        List<Region> list = regionService.findList(region);
        if(list.size() > 0) {
        	return regionService.findList(region).get(0);
        }else {
        	return null;
        }
    }

    // ------------------------------------ 提供接口供运营端使用 ----------------------------------------------
    /**
     * 根据用户对象获取区域
     *
     * @param userList
     * @return ResponseBean
     */
    @PostMapping(value = "/os/getRegionListByUsers")
    @ApiOperation(value = "根据用户对象获取区域", notes = "根据用户对象获取区域")
    public List<Region> osRegion1(@RequestParam(value = "randomStr",required = false)String randomStr,@RequestBody List<User> userList) {

        if(OsVo.isWrongRandomStr(randomStr)) return new ArrayList<>();

        return getRegionListByUsers(userList);
    }

}
