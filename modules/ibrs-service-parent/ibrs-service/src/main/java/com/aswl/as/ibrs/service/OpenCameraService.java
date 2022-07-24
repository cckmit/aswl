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
     * 分页查询开箱摄像机数据
     * @param page 分页
     * @param openCameraDto
     * @return PageInfo
     */
    public PageInfo<OpenCameraVo> findPage(PageInfo<OpenCameraDto> page, OpenCameraDto openCameraDto) {

        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = openCameraDto.getProjectId();
        String regionCode = openCameraDto.getRegionCode();
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
     * 新增开箱摄像机
     *
     * @param openCameraDto
     * @return int
     */
    @Transactional
    public int insert(OpenCameraDto openCameraDto) {
        //查询ip是否存在
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
     * 修改开箱摄像机
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
     * 删除开箱摄像机
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
     * 导入 excel
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
        map.put("cameraIp","设备摄像机IP");
        map.put("deviceCode","设备编码");
        map.put("cameraUserName","设备摄像机账号");
        map.put("cameraPwd","设备摄像机密码");
        map.put("brandType","设备摄像机品牌");
        List<OpenCameraDto> cameraDtos = MapUtil.map2Java(OpenCameraDto.class,
                ExcelToolUtil.importExcel(file.getInputStream(), map));
        int totalCount = cameraDtos.size();
        List<String> errorStrs = new ArrayList<>();
        for (int i = 0; i < cameraDtos.size(); i++) {
            OpenCameraDto openCameraDtoExcel = cameraDtos.get(i);
            // 非空字段验证
            if (StringUtils.isBlank(openCameraDtoExcel.getCameraIp())){
                errorStrs.add("第 " + (i + 1) + " 行的摄像机IP值为空,忽略导入");
                continue;
            }
            if (StringUtils.isNotEmpty(openCameraDtoExcel.getCameraIp()) && openCameraMapper.findByIp(openCameraDtoExcel.getCameraIp())>0){
                errorStrs.add("第 " + (i + 1) + " 行的摄像机IP值已存在,忽略导入");
                continue;
            }

            //设备编码
            if (StringUtils.isBlank(openCameraDtoExcel.getDeviceCode())){
                errorStrs.add("第 " + (i + 1) + " 行的设备编码值为空,忽略导入");
                continue;
                // 验证设备编码是否存在
//            }else {
//                 device = deviceMapper.findUniqueDeviceCode(openCameraDtoExcel.getDeviceCode());
//                if (device==null){
//                    errorStrs.add("第 " + (i + 1) + " 行的设备编码值不存在,忽略导入");
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
            // responseBean.setData("文件导入成功！总导入行数：" + totalCount);
            return responseBean;
        }
        JSONObject result = new JSONObject(5);
        result.put("totalCount", totalCount);
        result.put("errorCount", errorStrs.size());
        result.put("successCount", (totalCount-(errorStrs.size())));
        result.put("title","文件导入成功，但有错误。");
        result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + (totalCount-(errorStrs.size())) + "，错误行数：" + errorStrs.size());
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
     * 导出excel
     * @param dto 查询参数
     * @param request 请求对象
     * @param response 响应对象
     * @return  ResponseBean
     */
    public  ResponseBean<Boolean> exportOpenCamera(@RequestBody OpenCameraDto dto, HttpServletRequest request, HttpServletResponse response){
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "开箱摄像头信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<OpenCameraVo> openCameraVos = openCameraMapper.findOpenCameraInfo(dto);
            if (CollectionUtils.isEmpty(openCameraVos)) {
                throw new CommonException("无设备信息数据.");
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("cameraIp","摄像机IP");
            map.put("deviceCode","设备编码");
            map.put("cameraUserName","设备摄像机账号");
            map.put("cameraPwd","设备摄像机密码");
            map.put("brandType","设备摄像机品牌");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(openCameraVos), map);
            return new ResponseBean<>(true,"导出成功");
        } catch (Exception e) {
            log.error("导出开箱摄像机信息数据失败！", e);
            throw new CommonException("导出开箱摄像机信息数据失败！");
        }
    }

    /**
     *  生成码流路径
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