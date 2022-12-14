package com.aswl.as.ibrs.service;

import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.RegionListDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.ProjectModelMapper;
import com.aswl.as.ibrs.mapper.RegionMapper;
import com.aswl.as.ibrs.utils.BeanUtils;
import com.aswl.as.ibrs.utils.FileUtil;
import com.aswl.as.user.api.module.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Div;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class RegionService extends CrudService<RegionMapper, Region> {
    private final RegionMapper regionMapper;

    private final DeviceService deviceService;

    private final ProjectService projectService;
    
    private final ProjectModelMapper projectModelMapper;

    /**
     * ????????????
     *
     * @param region
     * @return int
     */
    @Transactional
    public int insert(Region region) {
        int update ;
        // ??????????????????????????????ID,??????????????????????????????,???????????????????????????
        // ????????????ID
        String parentId = region.getParentId();
        String[] codeArray = generateRegionCode(parentId);
        if ("".equals(region.getParentId())) {
            region.setParentId("-1");
        }
        region.setRegionCode(codeArray[0]);
        // ??????????????????
        region.setFullName(genareteRegionFullName(region));
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),region.getProjectId());
        update = super.insert(region);
        // ??????????????????
         insetDevice(region);
        // ???????????????????????????/???????????????
       // insertProjectNum(region);
        return update;
    }


    /**
     * ??????????????????????????????
     * @param region
     */
    public void insetDevice(Region region){
        int boxNum = region.getBoxNum();
        DeviceDto deviceDto =  null;
        String deviceModelId = null ;
        //????????????ID??????????????????
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectId(region.getProjectId());
        List<ProjectModel> projectModels = projectModelMapper.findList(projectModel);
        if (region.getDeviceIpMode() == 1){
            List<String> ipList = createIp(boxNum, region.getIpPartBegin());
            deviceModelId = region.getDeviceModelId();
            for (int i = 0; i < ipList.size(); i++) {
                deviceDto = new DeviceDto();
                deviceDto.setDeviceName(ipList.get(i));
                deviceDto.setDeviceCode(ipList.get(i));
                deviceDto.setIp(ipList.get(i));
                deviceDto.setRegionId(region.getId());
                deviceDto.setRegionCode(region.getRegionCode());
                deviceDto.setProjectId(region.getProjectId());
                deviceDto.setPort(20000);
                deviceDto.setUserName("admin");
                deviceDto.setPassword("admin");
                deviceDto.setDeviceType("AS_StatusTransferBox");
                if (deviceModelId != null) {
                    deviceDto.setDeviceModelId(deviceModelId);
                } else if (projectModels != null && projectModels.size() > 0) {
                    deviceDto.setDeviceModelId(projectModels.get(0).getDeviceModelId());
                } else {
                    deviceDto.setDeviceModelId("662313265078079488");
                }
                deviceService.insert(deviceDto);
            }
        }else{
            for (int i = 0; i <boxNum ; i++) {
                deviceDto = new DeviceDto();
                deviceDto.setRegionId(region.getId());
                deviceDto.setRegionCode(region.getRegionCode());
                deviceDto.setProjectId(region.getProjectId());
                deviceDto.setPort(20000);
                deviceDto.setUserName("admin");
                deviceDto.setPassword("admin");
                deviceDto.setDeviceType("AS_StatusTransferBox");
                if (deviceModelId != null) {
                    deviceDto.setDeviceModelId(deviceModelId);
                } else if (projectModels != null && projectModels.size() > 0) {
                    deviceDto.setDeviceModelId(projectModels.get(0).getDeviceModelId());
                } else {
                    deviceDto.setDeviceModelId("662313265078079488");
                }
                deviceService.insert(deviceDto);
            }
        }
    }

    /**
     *  ???????????????????????????/???????????????
     * @param region
     */
    public void  insertProjectNum(Region region){
        Map mapNum = this.findByProjectId(Arrays.asList(region.getProjectId().split(",")));
        Project project = new Project();
        project.setBoxNum(Integer.parseInt(mapNum.get("boxNum").toString()));
        project.setCameraNum(Integer.parseInt(mapNum.get("cameraNum").toString()));
        project.setProjectId(region.getProjectId());
        projectService.update(project);
    }

    /**
     * ??????IP??????
     * @param number
     * @param ipString
     * @return
     */
    public List<String> createIp(int number ,String ipString){
        String [] split =  ipString.split("\\.");
        int index1= Integer.parseInt(split[0]);
        int index2= Integer.parseInt(split[1]);
        int index3= Integer.parseInt(split[2]);
        int index4= Integer.parseInt(split[3]);
        int[] ip = new int[]{index1, index2, index3, index4};
        List<String> list = new ArrayList<>();
        if (number > 0) {
            list.add(ipString);
            for (int i = 1 ;i < number ;i ++) {
                increaseIp(ip);
                String resultIp = ip[0] +"." + ip[1] + "." +ip[2] + "." +ip[3] ;
                list.add(resultIp);
            }
        }
        return list;
    }

    /**
     * ????????????????????????
     *
     * @return String
     */
    private String genareteRegionFullName(Region region) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(region.getParentId())) {
            if ("-1".equals(region.getParentId())) {
                sb.append(region.getRegionName());
            } else {
                sb.append(region.getParentName()).append(",").append(region.getRegionName());
            }
            return sb.toString();
        }
        return null;
    }

    @Transactional
    public int update(Region region) {
        int update = 0 ;
        region.setId(region.getId());
        Region dbRegion = regionMapper.get(region);
        if (region.getBoxNum().intValue() != dbRegion.getBoxNum().intValue() || region.getDeviceIpMode().intValue() != dbRegion.getDeviceIpMode().intValue()){
            deleteDevice(region);
        }
        /*
        // ??????????????????????????????????????????
        if (region.getDeviceIpMode() == 1){
            if (region.getBoxNum() != dbRegion.getBoxNum() || region.getIpPartBegin() != dbRegion.getIpPartBegin()){
                // ??????????????????
                Device device = new Device();
                device.setRegionId(region.getId());
                List<Device> deviceList = deviceService.findList(device);
                if (deviceList != null && deviceList.size() >0){
                    List<String> idString = deviceList.stream().map(Device::getId).collect(Collectors.toList());
                    if (idString != null){
                        Device  devices  = new Device();
                        devices.setIdString(idString.stream().collect(Collectors.joining(",")));
                        deviceService.deleteAllDevice(devices);
                    }
                }
                // ??????????????????
                insetDevice(region);
            }
        }*/
        // ???????????????????????????/???????????????
       // insertProjectNum(region);
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),region.getProjectId());
        if (super.update(region) > 0) {
            update ++ ;
        }
        return update;
    }

    /**
     * ??????????????????????????????
     * @param region
     */
    public void deleteDevice(Region region){
        int boxNum; 
        if (region.getBoxNum() ==null){
            boxNum = 0 ;
        }else{
            boxNum = region.getBoxNum();
        }
        DeviceDto deviceDto = null;
        String deviceModelId = null ;
        //????????????ID??????????????????
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectId(region.getProjectId());
        List<ProjectModel> projectModels = projectModelMapper.findList(projectModel);
        //????????????
        if (region.getDeviceIpMode() == 1){
            region.setId(region.getId());
            //????????????????????????
            Region dbRegion = regionMapper.get(region);
            // ??????????????????????????????IP?????????????????????
            if (boxNum > 0 ||boxNum != dbRegion.getBoxNum() || region.getIpPartBegin() != dbRegion.getIpPartBegin()){
                Device device = new Device();
                device.setRegionId(region.getId());
                List<Device> deviceList = deviceService.findList(device);
                if (deviceList != null && deviceList.size() >0){
                    List<String> idString = deviceList.stream().map(Device::getId).collect(Collectors.toList());
                    if (idString != null){
                        Device  devices  = new Device();
                        devices.setIdString(idString.stream().collect(Collectors.joining(",")));
                        deviceService.deleteAllDevice(devices);
                    }
                }
                // ????????????
                insetDevice(region);
            }
            //????????????
        }else{
            //????????????????????????IP?????????????????????
            List<Device> devices =deviceService.findDeviceByIpNull(region.getId());
            if (devices!= null && devices.size() > 0){
                List<String> idString = devices.stream().map(Device::getId).collect(Collectors.toList());
                if (idString != null){
                    Device  device  = new Device();
                    device.setIdString(idString.stream().collect(Collectors.joining(",")));
                    deviceService.deleteAllDevice(device);
                }
            }
            // ????????????????????????IP???????????????????????????
            int num =deviceService.findDeviceByIpNotNull(region.getId(),"1");
            // ????????????????????????
            for (int i = 0; i < (boxNum- num); i++) {
                deviceDto = new DeviceDto();
                deviceDto.setRegionId(region.getId());
                deviceDto.setRegionCode(region.getRegionCode());
                deviceDto.setProjectId(region.getProjectId());
                deviceDto.setPort(20000);
                deviceDto.setUserName("admin");
                deviceDto.setPassword("admin");
                deviceDto.setDeviceType("AS_StatusTransferBox");
                if (deviceModelId != null) {
                    deviceDto.setDeviceModelId(deviceModelId);
                } else if (projectModels != null && projectModels.size() > 0) {
                    deviceDto.setDeviceModelId(projectModels.get(0).getDeviceModelId());
                } else {
                    deviceDto.setDeviceModelId("662313265078079488");
                }
                deviceService.insert(deviceDto);
            }
        }
    }


    /**
     * ????????????
     *
     * @param region
     * @return int
     */
    @Transactional
    @Override
    public int delete(Region region) {
        return super.delete(region);
    }


    /**
     * ????????????????????????
     *
     * @param userList
     * @return List
     */
    public List<Region> getRegionListByUsers(List<User> userList) {
        Region region = new Region();
        region.setIds(userList.stream().filter(tempUser -> tempUser.getRegionId() != null).map(User::getRegionId).distinct().toArray(String[]::new));
        return this.findListById(region);
    }


    /**
     * ????????????????????????
     *
     * @param parentId
     * @param query
     * @return
     */
    public List<RegionDeviceVo> findByParentId(String parentId, String query, String regionCode, String tenantCode, String projectId,String kind) {
        return regionMapper.findByParentId(parentId, query, regionCode, tenantCode, projectId,kind);
    }

    /**
     * ???????????????????????????
     *
     * @param parentId
     * @param regionCode
     * @return
     */
    public List<RegionDeviceTree> getRegionTree(String parentId, String regionCode, String tenantCode, String projectId) {
        List<RegionDeviceTree> deviceVos = regionMapper.getRegionTree(parentId, regionCode, tenantCode, projectId);

        List<RegionDeviceTree> list = new ArrayList<>();
        if (deviceVos != null && deviceVos.size() > 0) {
            for (RegionDeviceTree deviceVo : deviceVos) {

                deviceVo.setIsVideo(deviceService.getIsVideoValue(deviceVo.getId(),deviceVo.getType(),deviceVo.getIsOnline()));


                list.add(deviceVo);
            }
        }
        return list;

    }

    /**
     * ????????????
     *
     * @param parentId
     * @param query
     * @param regionCode
     * @return
     */
    public List<Map> getRegions(String parentId, String query, String regionCode) {
        return regionMapper.getRegions(parentId, query, regionCode);
    }

    public List<KindVo> findDeviceModelKind() {
        return regionMapper.findDeviceModelKind();
    }

    public List<RegionVo> findRegionTree(String regionCode, String tenantCode, String projectId) {
        return regionMapper.findRegionTree(regionCode, tenantCode, projectId);
    }

    public String findRegionId(String regionCode, String tenantCode, String projectId) {
        return regionMapper.findRegionId(regionCode, tenantCode, projectId);
    }


    /**
     * ??????Excel
     *
     * @param file
     * @return
     * @throws Exception
     */
    public ResponseBean importRegion(MultipartFile file) throws Exception {
        List<String> errorStrs = new ArrayList<>();
        ResponseBean responseBean = new ResponseBean();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        //2021-08-26????????????????????????????????????????????????????????????????????????
//        boolean isCloud=CloudCommon.isCloud();

//        if(isCloud)
//        {
            map.put("projectId","????????????");//?????????????????????????????????????????????
//        }
        map.put("regionName", "????????????");
        map.put("parentName", "??????????????????");
        map.put("boxNum","???????????????");
        map.put("cameraNum","???????????????");
        map.put("longitude","????????????");
        map.put("latitude","????????????");
        map.put("description", "??????");
       /* map.put("ipPartBegin", "ip?????????");
        map.put("ipPartEnd", "ip?????????");
        map.put("ipPartBegin2", "ip?????????2");
        map.put("ipPartEnd2", "ip?????????2");*/
        List<RegionListDto> regionDtos = MapUtil.map2Java(RegionListDto.class,
                ExcelToolUtil.importExcel(file.getInputStream(), map));

        Map<String,Project> projectCodeMap=new HashMap<>();
//        if(isCloud)
//        {
            String tempTenantCode=SysUtil.getTenantCode();
            //????????????????????????
            Project tempProject=new Project();
            tempProject.setTenantCode(tempTenantCode);
            List<Project> pList= projectService.findList(tempProject);
            for(Project p:pList)
            {
                projectCodeMap.put(p.getProjectCode(),p);
            }
//        }

        int totalCount = regionDtos.size();
        for (int i = 0; i < regionDtos.size(); i++) {
            RegionListDto regionExcelDto = regionDtos.get(i);
            if (regionDtos.size() == 0) {
                continue;
            }


            Region regionByName;
//            if(isCloud)
//            {
                // ?????????????????????????????????????????????????????????????????????????????????
                if(StringUtils.isBlank(regionExcelDto.getProjectId()))
                {
                    errorStrs.add("??? " + (i + 1) + " ?????? ??????????????????,????????????");
                    continue;
                }
                if(!projectCodeMap.containsKey(regionExcelDto.getProjectId()))
                {
                    errorStrs.add("??? " + (i + 1) + " ?????? ?????????????????????????????????,????????????");
                    continue;
                }
                regionExcelDto.setProjectId(projectCodeMap.get(regionExcelDto.getProjectId()).getProjectId());
                // ??????????????????????????????
                regionByName = regionMapper.findRegionByProjectIdAndName(regionExcelDto.getProjectId(),regionExcelDto.getRegionName());
//            }
//            else
//            {
//                // ??????????????????????????????
//                regionByName = regionMapper.findRegionByName(regionExcelDto.getRegionName());
//            }
            if (regionByName != null) {
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                continue;
            }

            Region region = new Region();
            BeanUtils.copyProperties(regionExcelDto, region);
//            region.setId(IdGen.snowflakeId());
            region.setCreator(SysUtil.getUser());
            region.setCreateDate(new Date());
            region.setApplicationCode(SysUtil.getSysCode());
            region.setTenantCode(SysUtil.getTenantCode());
            if (regionExcelDto.getParentName() != null) {
                Region region1 ;
//                if(isCloud)
//                {
                    region1= regionMapper.findRegionByProjectIdAndName(regionExcelDto.getProjectId(),regionExcelDto.getParentName());
//                }
//                else
//                {
//                    region1 = regionMapper.findRegionByName(regionExcelDto.getParentName());
//                }
                region.setParentId(region1.getId());
            }
            // ??????????????????????????????ID,??????????????????????????????,???????????????????????????
            // ????????????ID
            String parentId = region.getParentId();
            String[] codeArray = generateRegionCode(parentId);
            if ("".equals(parentId) || parentId == null) {
                region.setParentId("-1");
            }/*else {  //??????????????????
                if(((region.getIpPartBegin() != null && !region.getIpPartBegin().equals("")) && (region.getIpPartEnd() != null && !region.getIpPartEnd().equals(""))) ||
                        ((region.getIpPartBegin2() != null && !region.getIpPartBegin2().equals("")) && (region.getIpPartEnd2() != null && !region.getIpPartEnd2().equals("")))){
                    Region dbRegion = regionMapper.findRegionByIpBetween(region);
                    if(dbRegion != null){
                        errorStrs.add("??? " + (i + 1) + " ????????????ip?????????,????????????");
                    }
                }
            }*/
            region.setSort(i + 1);
            region.setRegionCode(codeArray[0]);
            // ??????????????????
            region.setFullName(genareteRegionFullName(region));
//            if(isCloud)
//            {
                region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),region.getProjectId());
//            }
//            else
//            {
//                region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
//            }
            regionMapper.insert(region);
        }
        if (errorStrs.size() == 0) {
            responseBean.setCode(200);
            responseBean.setData(Boolean.TRUE);
            return responseBean;
        }
        JSONObject result = new JSONObject(5);
        result.put("totalCount", totalCount);
        result.put("errorCount", errorStrs.size());
        result.put("successCount", (totalCount - (errorStrs.size())));
        result.put("title", "????????????????????????????????????");
        result.put("msg", "??????????????????" + totalCount + "?????????????????????" + (totalCount - (errorStrs.size())) + "??????????????????" + errorStrs.size());
        String fileUrl = FileUtil.saveErrorTxtByList(errorStrs, "regionImportExcelErrorLog");
        int lastIndex = fileUrl.lastIndexOf(File.separator);
        String fileName = fileUrl.substring(lastIndex + 1);
        result.put("fileUrl", "/v1/sys/file/download/" + fileUrl);
        result.put("fileName", fileName);
        responseBean.setData(result);
        responseBean.setCode(200);
        return responseBean;
    }



    /**
     * ??????????????????
     *
     * @param parentId
     * @return
     */
    private String[] generateRegionCode(String parentId) {
        String[] strArray = new String[2];
        // ????????????List??????,???????????????????????????Region??????
        List<Region> regionList = new ArrayList<>();
        // ????????????????????????
        String newOrgCode = "";
        // ????????????????????????
        String oldOrgCode = "";
        // ??????????????????,?????????????????????region_code, ????????????????????????????????????
        if (StringUtil.isNullOrEmpty(parentId)) {
            // ???????????????????????????????????????,??????????????????????????????
            regionList = regionMapper.findRegionByParentId("-1");
            if (regionList == null || regionList.size() == 0) {
                strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                Region region = regionList.get(0);
                oldOrgCode = region.getRegionCode();
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else { // ???????????????????????????????????????,??????????????????????????????,????????????????????????
            // ??????????????????????????????
            List<Region> parentList = regionMapper.findRegionByParentId(parentId);
            // ?????????????????????
            Region region = new Region();
            region.setId(parentId);
            Region region1 = regionMapper.get(region);
            // ?????????????????????Code
            String parentCode = region1.getRegionCode();
            // ?????????????????????null?????????
            if (parentList == null || parentList.size() == 0) {
                // ??????????????????????????????????????????
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //??????????????????????????????
                // ???????????????????????????,???????????????
                String subCode = parentList.get(0).getRegionCode();
                // ?????????????????????????????????
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // ?????????????????????????????????????????????????????????
        strArray[0] = newOrgCode;
        return strArray;
    }


    /**
     * ????????????excel
     *
     * @param region   ????????????
     * @param request  ????????????
     * @param response ????????????
     * @return ResponseBean
     */
    public ResponseBean<Boolean> exportRegion(@RequestBody Region region, HttpServletRequest request, HttpServletResponse response) {
        try {
            // ??????response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "????????????" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            region.setTenantCode(SysUtil.isAdmin()? null: SysUtil.getTenantCode());
            List<Region> regions = regionMapper.findList(region);
            if (CollectionUtils.isEmpty(regions)) {
                throw new CommonException("?????????????????????.");
            }
            if (regions != null) {
                for (Region r : regions) {
                    // ????????????????????????
                    List<Project> list = projectService.findList(new Project());
                    for (Project p: list) {
                        if (p.getProjectId().equals(r.getProjectId())){
                            r.setProjectCode(p.getProjectCode());
                            r.setProjectName(p.getProjectName());
                        }
                    }
                    region.setId(r.getParentId());
                    List<Region> regionByParentId = regionMapper.findList(region);
                    if (regionByParentId == null || regionByParentId.size() == 0) {
                        r.setParentName(null);
                    } else {
                        r.setParentName(regionByParentId.get(0).getRegionName());
                    }
                }
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("projectCode","????????????");
            map.put("projectName","????????????");
            map.put("regionName", "????????????");
            map.put("parentName", "??????????????????");
            map.put("boxNum","???????????????");
            map.put("cameraNum","???????????????");
            map.put("longitude","????????????");
            map.put("latitude","????????????");/*
            map.put("ip_part_begin","ip?????????");
            map.put("ip_part_end","ip?????????");
            map.put("ip_part_begin2","ip?????????2");
            map.put("ip_part_end2","ip?????????2");*/
            map.put("description", "??????");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(regions), map);
            return new ResponseBean<>(true, "????????????");
        } catch (Exception e) {
            log.error("?????????????????????????????????", e);
            throw new CommonException("?????????????????????????????????");
        }
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public List<String> getAllRegion() {
        return regionMapper.getAllRegion();
    }

    /**
     * ????????????id????????????????????????
     *
     * @param parentId
     * @return Region
     */
    public List<Region> findRegionByParentId(String parentId) {
        return regionMapper.findRegionByParentId(parentId);
    }

    public List<LinkedHashMap> findRegionList(String regionName) {
        return regionMapper.findRegionList(regionName);
    }

    /**
     * ????????????id????????????????????????
     */
    public List<Region> findRegionListByParentId(String parentId){
        return regionMapper.findRegionListByParentId(parentId);
    }

    /**
     * ??????IP???????????????
     */
    public Region findRegionByIpBetween(Region region){
        return regionMapper.findRegionByIpBetween(region);
    }

    public List<KindVo> findDeviceModelKind1() {
        return regionMapper.findDeviceModelKind1();
    }

    public List<Region> findListAll(Region region) {
        return regionMapper.findListAll(region);
    }


    /**
     * ??????????????????(????????????)
     * @param info
     * @param region
     * @return
     */
    public PageInfo<Region> findByPage(PageInfo<Region> info, Region region) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String regionCode = region.getRegionCode();
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
                    return new PageInfo<>(new ArrayList<>());
                }
                regionCode = userRegionCode;
            }
        }
        if(region.getProjectId() == null || "".equals(region.getProjectId())){
            region.setProjectId(projectId);
        }
        region.setTenantCode(tenantCode);
        region.setRegionCode(regionCode);
        PageHelper.startPage(info.getPageNum(),info.getPageSize());
        return new PageInfo<>(regionMapper.findByPage(region));
    }

    /**
     *  APP?????????????????????/????????????????????????????????????
     * @param type
     * @return
     */
    public List<RegionDeviceListVo> getAppOnlineAndIntactList(String type){
        String roles = RoleContextHolder.getRole();
        String regionCode = null;
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (CommonConstant.IS_ASOS_TRUE.equals(CommonConstant.IS_ASOS_FALSE)) {
            tenantCode = null;
            projectId = null;
        } else {
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
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        return regionMapper.getAppOnlineAndIntactList(type,regionCode,projectId,tenantCode);
    }

    /**
     * ??????????????????????????????
     * @param regionCode
     * @return int
     */
   public  int deleteByRegionCode( String regionCode) {
       return regionMapper.deleteByRegionCode(regionCode);
   }
    /**
     *  ????????????ID??????????????????????????????
     * @param projectIds
     * @return
     */
    public Map findByProjectId(List<String> projectIds){
        return regionMapper.findByProjectId(projectIds);
    }

    /**
     *  ??????IP
     * @param ip
     * @param part
     * @return  int[]
     */
    public static int[] increaseIp(int[] ip, int... part){
       int index = 4;
       if(part != null && part.length > 0){
           index = part[0];
       }
       if(index == 4){
          ip[3]++;
           if(ip[3] > 255){
               increaseIp(ip, 3);
               ip[3] = 0;
           }
       }else if(index == 3){
           ip[2]++;
           if(ip[2] > 255){
               increaseIp(ip, 2);
               ip[2] = 0;
           }
       }else if(index == 2){
           ip[1]++;
           if(ip[1] > 255){
               increaseIp(ip, 1);
               ip[1] = 0;
           }
       }else if(index == 1){
           ip[0]++;
           if(ip[0] > 255){
               ip[0] = 255;
           }
       }
       return ip;
    }
}