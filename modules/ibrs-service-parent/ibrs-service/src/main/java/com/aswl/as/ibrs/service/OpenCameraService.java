package com.aswl.as.ibrs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.enums.StreamType;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.OpenCameraDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.api.vo.OpenCameraVo;
import com.aswl.as.ibrs.enums.MQExchange;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.DeviceMapper;
import com.aswl.as.ibrs.mapper.OpenCameraMapper;
import com.aswl.as.ibrs.task.DeviceThread;
import com.aswl.as.ibrs.utils.DeviceUtil;
import com.aswl.as.ibrs.utils.FileUtil;
import com.aswl.iot.dto.constant.MQConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
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

@AllArgsConstructor
@Slf4j
@Service
public class OpenCameraService extends CrudService<OpenCameraMapper, OpenCamera> {
    private final OpenCameraMapper openCameraMapper;
    private  final DeviceMapper deviceMapper;

    /**
     * ?????????????????????????????????
     * @param page ??????
     * @param openCameraDto
     * @return PageInfo
     */
    public PageInfo<OpenCameraVo> findPage(PageInfo<OpenCameraDto> page, OpenCameraDto openCameraDto) {

        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = openCameraDto.getProjectId();
        String regionCode = openCameraDto.getRegionCode();
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
                if(regionCode == null || "".equals(regionCode)){
                    return new PageInfo<>(new ArrayList<>());
                }
                regionCode = userRegionCode;
            }
        }
        openCameraDto.setTenantCode(tenantCode);
        openCameraDto.setProjectId(projectId);
        openCameraDto.setRegionCode(regionCode);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(openCameraMapper.findOpenCameraInfo(openCameraDto));
    }



    /**
     * ?????????????????????
     *
     * @param openCameraDto
     * @return int
     */
    @Transactional
    public int insert(OpenCameraDto openCameraDto) {
        //??????ip????????????
        int ipCount = openCameraMapper.findByIp(openCameraDto.getCameraIp());
        if (ipCount>0){
            return 0;
        }
        OpenCamera openCamera = new OpenCamera();
        BeanUtils.copyProperties(openCameraDto, openCamera);
        openCamera.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        genStreamPath(openCameraDto,openCamera);
        return super.insert(openCamera);
    }



    /**
     * ?????????????????????
     *
     * @param openCameraDto
     * @return int
     */
    @Transactional
    public int update(OpenCameraDto openCameraDto) {
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("rtsp://"+openCameraDto.getCameraUserName()+":"+openCameraDto.getCameraPwd());
        stringBuilder.append("@"+openCameraDto.getCameraIp()+":554");
        stringBuilder.append("/ch1/main/av_stream");
        OpenCamera openCamera = new OpenCamera();
        BeanUtils.copyProperties(openCameraDto, openCamera);
        openCamera.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        if (openCameraDto.getBrandType().equals(StreamType.DH.getType())){
            openCamera.setStreamPath(stringBuilder.toString());
        }
        else if(openCameraDto.getBrandType().equals(StreamType.HK.getType())){
            openCamera.setStreamPath(stringBuilder.toString());
        }else{
            openCamera.setStreamPath(stringBuilder.toString());
        }
        return openCameraMapper.update(openCamera);
    }

    /**
     * ?????????????????????
     *
     * @param openCamera
     * @return int
     */
    @Transactional
    @Override
    public int delete(OpenCamera openCamera) {
        return super.delete(openCamera);
    }


    /**
     * ?????? excel
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional
    public ResponseBean importOpenCamera(MultipartFile file) throws Exception{
        Device device=null;
        ResponseBean responseBean=new ResponseBean();
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("cameraIp","???????????????IP");
        map.put("deviceCode","????????????");
        map.put("cameraUserName","?????????????????????");
        map.put("cameraPwd","?????????????????????");
        map.put("brandType","?????????????????????");
        List<OpenCameraDto> cameraDtos = MapUtil.map2Java(OpenCameraDto.class,
                ExcelToolUtil.importExcel(file.getInputStream(), map));
        int totalCount = cameraDtos.size();
        List<String> errorStrs = new ArrayList<>();
        for (int i = 0; i < cameraDtos.size(); i++) {
            OpenCameraDto openCameraDtoExcel = cameraDtos.get(i);
            // ??????????????????
            if (StringUtils.isBlank(openCameraDtoExcel.getCameraIp())){
                errorStrs.add("??? " + (i + 1) + " ???????????????IP?????????,????????????");
                continue;
            }
            if (StringUtils.isNotEmpty(openCameraDtoExcel.getCameraIp()) && openCameraMapper.findByIp(openCameraDtoExcel.getCameraIp())>0){
                errorStrs.add("??? " + (i + 1) + " ???????????????IP????????????,????????????");
                continue;
            }

            //????????????
            if (StringUtils.isBlank(openCameraDtoExcel.getDeviceCode())){
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                continue;
                // ??????????????????????????????
//            }else {
//                 device = deviceMapper.findUniqueDeviceCode(openCameraDtoExcel.getDeviceCode());
//                if (device==null){
//                    errorStrs.add("??? " + (i + 1) + " ??????????????????????????????,????????????");
//                    continue;
//                }
            }
                OpenCamera openCamera=new OpenCamera();
                BeanUtils.copyProperties(openCameraDtoExcel,openCamera);
                openCamera.setId(IdGen.snowflakeId());
                openCamera.setDeviceId(device.getId());
                openCamera.setCreator(SysUtil.getUser());
                openCamera.setCreateDate(new Date());
                genStreamPath(openCameraDtoExcel,openCamera);
                openCameraMapper.insert(openCamera);
        }

        if (errorStrs.size() == 0) {
            responseBean.setCode(200);
            responseBean.setData(Boolean.TRUE);
            // responseBean.setData("???????????????????????????????????????" + totalCount);
            return responseBean;
        }
        JSONObject result = new JSONObject(5);
        result.put("totalCount", totalCount);
        result.put("errorCount", errorStrs.size());
        result.put("successCount", (totalCount-(errorStrs.size())));
        result.put("title","????????????????????????????????????");
        result.put("msg", "??????????????????" + totalCount + "?????????????????????" + (totalCount-(errorStrs.size())) + "??????????????????" + errorStrs.size());
        String fileUrl = FileUtil.saveErrorTxtByList(errorStrs, "openCameraImportExcelErrorLog");
        int lastIndex = fileUrl.lastIndexOf(File.separator);
        String fileName = fileUrl.substring(lastIndex + 1);
        result.put("fileUrl", "/v1/sys/file/download/" + fileUrl);
        result.put("fileName", fileName);
        responseBean.setData(result);
        responseBean.setCode(200);
        return responseBean;
    }

    /**
     * ??????excel
     * @param dto ????????????
     * @param request ????????????
     * @param response ????????????
     * @return  ResponseBean
     */
    public  ResponseBean<Boolean> exportOpenCamera(@RequestBody OpenCameraDto dto, HttpServletRequest request, HttpServletResponse response){
        try {
            // ??????response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "?????????????????????" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<OpenCameraVo> openCameraVos = openCameraMapper.findOpenCameraInfo(dto);
            if (CollectionUtils.isEmpty(openCameraVos)) {
                throw new CommonException("?????????????????????.");
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("cameraIp","?????????IP");
            map.put("deviceCode","????????????");
            map.put("cameraUserName","?????????????????????");
            map.put("cameraPwd","?????????????????????");
            map.put("brandType","?????????????????????");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(openCameraVos), map);
            return new ResponseBean<>(true,"????????????");
        } catch (Exception e) {
            log.error("??????????????????????????????????????????", e);
            throw new CommonException("??????????????????????????????????????????");
        }
    }

    /**
     *  ??????????????????
     */
    public void genStreamPath( OpenCameraDto openCameraDto ,OpenCamera openCamera){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("rtsp://"+openCameraDto.getCameraUserName()+":"+openCameraDto.getCameraPwd());
        stringBuilder.append("@"+openCameraDto.getCameraIp()+":554");
        stringBuilder.append("/h264/ch1/main/av_stream");
        if (openCameraDto.getBrandType().equals(StreamType.DH.getType())){
            openCamera.setStreamPath(stringBuilder.toString());
        }
        else if(openCameraDto.getBrandType().equals(StreamType.HK.getType())){
            openCamera.setStreamPath(stringBuilder.toString());
        }else{
            openCamera.setStreamPath(stringBuilder.toString());
        }
    }
}