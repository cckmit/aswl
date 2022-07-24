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
     * 新增区域
     *
     * @param region
     * @return int
     */
    @Transactional
    public int insert(Region region) {
        int update ;
        // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
        // 获取父级ID
        String parentId = region.getParentId();
        String[] codeArray = generateRegionCode(parentId);
        if ("".equals(region.getParentId())) {
            region.setParentId("-1");
        }
        region.setRegionCode(codeArray[0]);
        // 生成区域全称
        region.setFullName(genareteRegionFullName(region));
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),region.getProjectId());
        update = super.insert(region);
        // 新增设备数据
         insetDevice(region);
        // 设置项目设备箱数量/摄像机数量
       // insertProjectNum(region);
        return update;
    }


    /**
     * 自动模式添加设备信息
     * @param region
     */
    public void insetDevice(Region region){
        int boxNum = region.getBoxNum();
        DeviceDto deviceDto =  null;
        String deviceModelId = null ;
        //根据项目ID查询型号数据
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
     *  修改项目设备箱数量/摄像机数量
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
     * 创建IP地址
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
     * 自动生成区域全称
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
        // 如果是自动模式则生成设备数据
        if (region.getDeviceIpMode() == 1){
            if (region.getBoxNum() != dbRegion.getBoxNum() || region.getIpPartBegin() != dbRegion.getIpPartBegin()){
                // 删除设备信息
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
                // 重新添加设备
                insetDevice(region);
            }
        }*/
        // 设置项目设备箱数量/摄像机数量
       // insertProjectNum(region);
        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),region.getProjectId());
        if (super.update(region) > 0) {
            update ++ ;
        }
        return update;
    }

    /**
     * 修改区域时先删除设备
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
        //根据项目ID查询型号数据
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectId(region.getProjectId());
        List<ProjectModel> projectModels = projectModelMapper.findList(projectModel);
        //自动模式
        if (region.getDeviceIpMode() == 1){
            region.setId(region.getId());
            //先查询数据库数据
            Region dbRegion = regionMapper.get(region);
            // 如果设备数量或者起始IP发生改变则删除
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
                // 新增设备
                insetDevice(region);
            }
            //手动模式
        }else{
            //先查询出该区域下IP为空的设备数据
            List<Device> devices =deviceService.findDeviceByIpNull(region.getId());
            if (devices!= null && devices.size() > 0){
                List<String> idString = devices.stream().map(Device::getId).collect(Collectors.toList());
                if (idString != null){
                    Device  device  = new Device();
                    device.setIdString(idString.stream().collect(Collectors.joining(",")));
                    deviceService.deleteAllDevice(device);
                }
            }
            // 先查询出该区域下IP不为空的智能箱数量
            int num =deviceService.findDeviceByIpNotNull(region.getId(),"1");
            // 重新添加设备数据
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
     * 删除区域
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
     * 根据用户批量查询
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
     * 条件查询区域设备
     *
     * @param parentId
     * @param query
     * @return
     */
    public List<RegionDeviceVo> findByParentId(String parentId, String query, String regionCode, String tenantCode, String projectId,String kind) {
        return regionMapper.findByParentId(parentId, query, regionCode, tenantCode, projectId,kind);
    }

    /**
     * 查询区域设备拓扑图
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
     * 查询区域
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
     * 导入Excel
     *
     * @param file
     * @return
     * @throws Exception
     */
    public ResponseBean importRegion(MultipartFile file) throws Exception {
        List<String> errorStrs = new ArrayList<>();
        ResponseBean responseBean = new ResponseBean();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        //2021-08-26，局域网版也调整为有多项目，因此此处不用区分判断
//        boolean isCloud=CloudCommon.isCloud();

//        if(isCloud)
//        {
            map.put("projectId","项目编号");//暂时用这个字段，来保存项目编号
//        }
        map.put("regionName", "区域名称");
        map.put("parentName", "上级区域名称");
        map.put("boxNum","智能箱数量");
        map.put("cameraNum","摄像机数量");
        map.put("longitude","区域经度");
        map.put("latitude","区域维度");
        map.put("description", "描述");
       /* map.put("ipPartBegin", "ip开始段");
        map.put("ipPartEnd", "ip结束段");
        map.put("ipPartBegin2", "ip开始段2");
        map.put("ipPartEnd2", "ip结束段2");*/
        List<RegionListDto> regionDtos = MapUtil.map2Java(RegionListDto.class,
                ExcelToolUtil.importExcel(file.getInputStream(), map));

        Map<String,Project> projectCodeMap=new HashMap<>();
//        if(isCloud)
//        {
            String tempTenantCode=SysUtil.getTenantCode();
            //如果不在租户下面
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
                // 需要判断导入的项目编码是否在登录的租户下，或者是否存在
                if(StringUtils.isBlank(regionExcelDto.getProjectId()))
                {
                    errorStrs.add("第 " + (i + 1) + " 行的 项目编码为空,忽略导入");
                    continue;
                }
                if(!projectCodeMap.containsKey(regionExcelDto.getProjectId()))
                {
                    errorStrs.add("第 " + (i + 1) + " 行的 项目编码不存在本租户内,忽略导入");
                    continue;
                }
                regionExcelDto.setProjectId(projectCodeMap.get(regionExcelDto.getProjectId()).getProjectId());
                // 验证区域名称是否存在
                regionByName = regionMapper.findRegionByProjectIdAndName(regionExcelDto.getProjectId(),regionExcelDto.getRegionName());
//            }
//            else
//            {
//                // 验证区域名称是否存在
//                regionByName = regionMapper.findRegionByName(regionExcelDto.getRegionName());
//            }
            if (regionByName != null) {
                errorStrs.add("第 " + (i + 1) + " 行的区域名称已存在,忽略导入");
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
            // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
            // 获取父级ID
            String parentId = region.getParentId();
            String[] codeArray = generateRegionCode(parentId);
            if ("".equals(parentId) || parentId == null) {
                region.setParentId("-1");
            }/*else {  //不是顶级区域
                if(((region.getIpPartBegin() != null && !region.getIpPartBegin().equals("")) && (region.getIpPartEnd() != null && !region.getIpPartEnd().equals(""))) ||
                        ((region.getIpPartBegin2() != null && !region.getIpPartBegin2().equals("")) && (region.getIpPartEnd2() != null && !region.getIpPartEnd2().equals("")))){
                    Region dbRegion = regionMapper.findRegionByIpBetween(region);
                    if(dbRegion != null){
                        errorStrs.add("第 " + (i + 1) + " 行的区域ip段有误,忽略导入");
                    }
                }
            }*/
            region.setSort(i + 1);
            region.setRegionCode(codeArray[0]);
            // 生成区域全称
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
        result.put("title", "文件导入成功，但有错误。");
        result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + (totalCount - (errorStrs.size())) + "，错误行数：" + errorStrs.size());
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
     * 生成区域编码
     *
     * @param parentId
     * @return
     */
    private String[] generateRegionCode(String parentId) {
        String[] strArray = new String[2];
        // 创建一个List集合,存储查询返回的所有Region对象
        List<Region> regionList = new ArrayList<>();
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";
        // 如果是最高级,则查询出同级的region_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
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
        } else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            // 查询出同级部门的集合
            List<Region> parentList = regionMapper.findRegionByParentId(parentId);
            // 查询出父级部门
            Region region = new Region();
            region.setId(parentId);
            Region region1 = regionMapper.get(region);
            // 获取父级部门的Code
            String parentCode = region1.getRegionCode();
            // 处理同级部门为null的情况
            if (parentList == null || parentList.size() == 0) {
                // 直接生成当前的部门编码并返回
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = parentList.get(0).getRegionCode();
                // 返回生成的当前部门编码
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = newOrgCode;
        return strArray;
    }


    /**
     * 导出区域excel
     *
     * @param region   查询参数
     * @param request  请求对象
     * @param response 响应对象
     * @return ResponseBean
     */
    public ResponseBean<Boolean> exportRegion(@RequestBody Region region, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "区域信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            region.setTenantCode(SysUtil.isAdmin()? null: SysUtil.getTenantCode());
            List<Region> regions = regionMapper.findList(region);
            if (CollectionUtils.isEmpty(regions)) {
                throw new CommonException("无设备信息数据.");
            }
            if (regions != null) {
                for (Region r : regions) {
                    // 查询区域下的项目
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
            map.put("projectCode","项目编码");
            map.put("projectName","项目名称");
            map.put("regionName", "区域名称");
            map.put("parentName", "上级区域名称");
            map.put("boxNum","智能箱数量");
            map.put("cameraNum","摄像机数量");
            map.put("longitude","区域经度");
            map.put("latitude","区域维度");/*
            map.put("ip_part_begin","ip开始段");
            map.put("ip_part_end","ip结束段");
            map.put("ip_part_begin2","ip开始段2");
            map.put("ip_part_end2","ip结束段2");*/
            map.put("description", "描述");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(regions), map);
            return new ResponseBean<>(true, "导出成功");
        } catch (Exception e) {
            log.error("导出区域信息数据失败！", e);
            throw new CommonException("导出区域信息数据失败！");
        }
    }

    /**
     * 查询所有的区域
     *
     * @return
     */
    public List<String> getAllRegion() {
        return regionMapper.getAllRegion();
    }

    /**
     * 根据父级id查询最新一条记录
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
     * 根据父级id查询所有下级区域
     */
    public List<Region> findRegionListByParentId(String parentId){
        return regionMapper.findRegionListByParentId(parentId);
    }

    /**
     * 根据IP段查询区域
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
     * 分页查询区域(不同角色)
     * @param info
     * @param region
     * @return
     */
    public PageInfo<Region> findByPage(PageInfo<Region> info, Region region) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String regionCode = region.getRegionCode();
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
     *  APP首页各区智能箱/摄像机在线数完好数和数量
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
     * 根据区域编码删除区域
     * @param regionCode
     * @return int
     */
   public  int deleteByRegionCode( String regionCode) {
       return regionMapper.deleteByRegionCode(regionCode);
   }
    /**
     *  根据项目ID查询区域下智能箱数量
     * @param projectIds
     * @return
     */
    public Map findByProjectId(List<String> projectIds){
        return regionMapper.findByProjectId(projectIds);
    }

    /**
     *  递增IP
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