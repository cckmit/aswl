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
 * ??????controller
 *
 * @author dingfei
 * @date 2019-09-27 14:01
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/region", tags = "??????")
@RestController
@RequestMapping("/v1/region")
public class RegionController extends BaseController {

    private final RegionService regionService;
    private final DeviceService deviceService;

    private final ProjectService projectService;

    private final UserServiceClient userServiceClient;

    /**
     * ????????????????????????
     * @return
     */
    @GetMapping(value = "tree")
    @ApiOperation(value = "??????????????????")
    public ResponseBean<List<RegionDto>> regions(@RequestParam(value = "regionCode",required = false) String regionCode,@RequestParam(value = "queryProjectId",required = false) String queryProjectId) {

        // ?????????admin???????????????tenantCode
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId;
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("?????????????????????????????????");
                    return new ResponseBean<>(null,"?????????????????????????????????");
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
        // ??????????????????
        Stream<Region> rsStream = regionService.findListAll(region).stream();
        if (Optional.ofNullable(rsStream).isPresent()) {
            List<RegionDto> treeList = rsStream.map(RegionDto::new).sorted(Comparator.comparing(RegionDto::getId)).collect(Collectors.toList());
            // ???????????????????????????
            List<RegionDto> list =TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(RegionDto::getSort)), parentId);
            return new ResponseBean<>(list);
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * ???????????????????????? ?????? ??????-??????-?????? ????????????????????????admin??????????????????????????????????????????????????? ??????-??????
     * @return
     */
    @GetMapping(value = "newTree")
    @ApiOperation(value = "??????????????????")
    public ResponseBean newTree(@RequestParam(value = "regionCode",required = false) String regionCode,
                                @RequestParam(value = "projectId",required = false) String queryProjectId,
                                @RequestParam(value = "projectName",required = false) String projectName,
                                @RequestParam(value = "isApp",required = false) boolean isApp
                                ) {
        String parentId="-1";
        Region region = new Region();
        //?????????????????????
        region.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {

        }
        //regionCode = null;
        if(regionCode != null && !"".equals(regionCode)){
            parentId = regionService.findRegionId(regionCode,tenantCode,projectId);
        }
        region.setTenantCode(tenantCode);
        region.setProjectId(projectId);
        region.setRegionCode(regionCode);
        //???????????? queryProjectId ????????? //????????????????????????????????????????????????
        if(!StringUtils.isEmpty(queryProjectId))
        {
            region.setProjectId(queryProjectId);
        }

        // ??????????????????
        Stream<Region> rsStream=regionService.findListAll(region).stream();
        List<OsRegionDto> regionDtoList=new ArrayList<>();
        if (Optional.ofNullable(rsStream).isPresent()) {
            List<OsRegionDto> treeList = rsStream.map(OsRegionDto::new).collect(Collectors.toList());
            // ???????????????????????????
            regionDtoList=TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsRegionDto::getSort)), parentId);
        }

        //2021-08-26??????????????????????????????????????????????????????
//        //???????????????????????????????????????
//        if(!CloudCommon.isCloud())
//        {
//            return new ResponseBean<>(regionDtoList);
//        }

        // ????????????
        //??????????????????????????????

        //??????
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
            //?????????????????????
            for(OsRegionDto r:regionDtoList)
            {
                for(OsProjectDto p:treeList)
                {
                    if(p.getProjectId().equals(r.getProjectId()))
                    {
                        p.add(r);
                        break; //??????????????????
                    }
                }
            }
            
            // ???????????????????????????
            projectDtoList=TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsProjectDto::getSort)), parentId);
        }
        if(CloudCommon.isCloud() && SysUtil.isAdmin() && !isApp)  //??????????????????????????????????????????
        {
            //admin ???????????????????????????

            // ?????? ????????????
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
                                break; //??????????????????
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
     * ??????ID????????????
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "????????????ID????????????")
    public ResponseBean<Region> findById(@PathVariable("id") String id) {
        Region region = new Region();
        region.setId(id);
        return new ResponseBean<>(regionService.get(region));
    }

    /**
     * ??????????????????
     *
     * @param region
     * @return ResponseBean
     */
    @GetMapping(value = "regions")
    @ApiOperation(value = "?????????????????????")
    public ResponseBean
            <List<Region>> findAll(Region region) {
        region.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(regionService.findAllList(region));
    }

    /**
     * ????????????????????????
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param region
     * @return PageInfo
     */
    @GetMapping("regionList")
    @ApiOperation(value = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "????????????", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "????????????", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "????????????", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "????????????", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "region", value = "????????????", dataType = "region")
    })
    public PageInfo<Region> regionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       Region region) {
        return regionService.findByPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), region);
    }

    /**
     * ????????????
     *
     * @param region
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "????????????", notes = "????????????")
    @Log("????????????")
    public ResponseBean
            <Boolean> insertRegion(@RequestBody @Valid Region region) {
        return new ResponseBean<>(regionService.insert(region) > 0);
    }

    /**
     * ????????????
     *
     * @param region
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "??????????????????", notes = "????????????ID??????????????????")
    @Log("????????????")
    public ResponseBean
            <Boolean> updateRegion(@RequestBody @Valid Region region) {
        return new ResponseBean<>(regionService.update(region) > 0);
    }

    /**
     * ????????????ID??????????????????
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "??????????????????", notes = "????????????ID??????????????????")
    public ResponseBean
            <Boolean> deleteRegionById(@PathVariable("id") String id) {
        Region region = new Region();
        region.setId(id);
        region.setNewRecord(false);
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(regionService.delete(region) > 0);
    }

    /**
     * ????????????????????????
     *
     * @param region
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "??????????????????", notes = "????????????ID??????????????????")
    @ApiImplicitParam(name = "region", value = "????????????", dataType = "region")
    @Log("??????????????????")
    public ResponseBean
            <Boolean> deleteAllRegion(@RequestBody Region region) {

        boolean success = false;

        // ????????????
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
            // ???????????????????????????/???????????????
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
     * ??????????????????????????????
     *
     * @param userList
     * @return ResponseBean
     */
    @PostMapping(value = "/getRegionListByUsers")
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
    public List<Region> getRegionListByUsers(@RequestBody List<User> userList) {
        return regionService.getRegionListByUsers(userList);
    }

    /**
     * ??????excel??????????????????
     * @param request
     * @return
     */
    @RequestMapping(value = "/importRegion", method = RequestMethod.POST)
    @ApiOperation(value = "??????excel??????????????????", notes = "??????excel??????????????????")
    public ResponseBean importRegion(HttpServletRequest request) {
        ResponseBean responseBean=new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// ????????????????????????
            try {
                return regionService.importRegion(file);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                responseBean.setData("??????????????????:" + e.getMessage());

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
     * ??????????????????
     *
     * @param region region
     */
    @PostMapping("export")
    @ApiOperation(value = "??????????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "region", value = "????????????", required = true, dataType = "Region")
    @Log(value = "??????????????????",businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportRegion(@RequestBody Region region, HttpServletRequest request, HttpServletResponse response) {
        return regionService.exportRegion(region,request,response);
    }

    /**
     * ??????????????????
     *
     * @param region
     * @return ResponseBean
     */
    @GetMapping(value = "getRegionRootByProjectId")
    @ApiOperation(value = "??????x?????????")
    public ResponseBean<Region> getRegionRootByProjectId(Region region) {
//        region.setTenantCode(SysUtil.getTenantCode());
        if(!StringUtils.isEmpty(region.getProjectId()))
        {
            region.setParentId("-1");//??????????????????
            List<Region> list=regionService.findAllList(region);
            if(list.size()>0)
            {
                return new ResponseBean<>(list.get(0));
            }
        }
        return new ResponseBean<>();
    }
    
    /**
     * ??????ID????????????
     *
     * @param regionCode
     * @return ResponseBean
     */
    @GetMapping(value = "/meta/{regionCode}")
    @ApiOperation(value = "????????????ID????????????")
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

    // ------------------------------------ ?????????????????????????????? ----------------------------------------------
    /**
     * ??????????????????????????????
     *
     * @param userList
     * @return ResponseBean
     */
    @PostMapping(value = "/os/getRegionListByUsers")
    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????")
    public List<Region> osRegion1(@RequestParam(value = "randomStr",required = false)String randomStr,@RequestBody List<User> userList) {

        if(OsVo.isWrongRandomStr(randomStr)) return new ArrayList<>();

        return getRegionListByUsers(userList);
    }

}
