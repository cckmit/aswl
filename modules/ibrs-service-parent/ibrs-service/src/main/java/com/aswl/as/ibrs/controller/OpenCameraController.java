package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.enums.StreamType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.OpenCameraDto;
import com.aswl.as.ibrs.api.module.OpenCamera;
import com.aswl.as.ibrs.api.vo.OpenCameraVo;
import com.aswl.as.ibrs.service.OpenCameraService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 开箱摄像机controller
 *
 * @author dingfei
 * @date 2019-12-16 16:05
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/openCamera", tags = "开箱摄像机")
@RestController
@RequestMapping("/v1/openCamera")
public class OpenCameraController extends BaseController {

    private final OpenCameraService openCameraService;


    /**
     * 根据ID获取开箱摄像机
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据开箱摄像机ID查询开箱摄像机")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "开箱摄像机ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<OpenCamera> findById(@PathVariable("id") String id) {
        OpenCamera openCamera = new OpenCamera();
        openCamera.setId(id);
        return new ResponseBean<>(openCameraService.get(openCamera));
    }

    /**
     * 查询所有开箱摄像机
     *
     * @param openCamera
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有开箱摄像机列表")
    @ApiImplicitParam(name = "openCamera", value = "开箱摄像机对象", paramType = "path", required = true, dataType = "openCamera")
    @GetMapping(value = "openCameras")
    public ResponseBean
            <List<OpenCamera>> findAll(OpenCamera openCamera) {
        openCamera.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(openCameraService.findList(openCamera));
    }

    /**
     * 分页查询开箱摄像机列表
     *
     * @param pageNum 分页页码
     * @param pageSize 分页大小
     * @param openCameraDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询开箱摄像机列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "openCameraDto", value = "开箱摄像机信息", dataType = "openCamera")
    })

    @GetMapping("openCameraList")
    public ResponseBean<PageInfo<OpenCameraVo>> openCameraList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               OpenCameraDto openCameraDto) {
        return new ResponseBean<>(openCameraService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), openCameraDto));
    }

    /**
     * 新增开箱摄像机
     *
     * @param openCameraDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增开箱摄像机", notes = "新增开箱摄像机")
    @PostMapping
    @Log("新增开箱摄像机")
    public ResponseBean
            <Boolean> insertOpenCamera(@RequestBody @Valid OpenCameraDto openCameraDto) {
        return new ResponseBean<>(openCameraService.insert(openCameraDto) > 0);
    }

    /**
     * 修改开箱摄像机
     *
     * @param openCameraDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新开箱摄像机信息", notes = "根据开箱摄像机ID更新开箱摄像机信息")
    @Log("修改开箱摄像机")
    @PutMapping
    public ResponseBean
            <Boolean> updateOpenCamera(@RequestBody @Valid OpenCameraDto openCameraDto) {
             return new ResponseBean<>(openCameraService.update(openCameraDto) > 0);
    }

    /**
     * 根据开箱摄像机ID删除开箱摄像机信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除开箱摄像机信息", notes = "根据开箱摄像机ID删除开箱摄像机信息")
    @ApiImplicitParam(name = "id", value = "开箱摄像机ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteOpenCameraById(@PathVariable("id") String id) {
        OpenCamera openCamera = new OpenCamera();
        openCamera.setId(id);
        openCamera.setNewRecord(false);
        openCamera.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(openCameraService.delete(openCamera) > 0);
    }

    /**
     * 批量删除开箱摄像机信息
     *
     * @param openCamera
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除开箱摄像机", notes = "根据开箱摄像机ID批量删除开箱摄像机")
    @ApiImplicitParam(name = "openCamera", value = "开箱摄像机信息", dataType = "openCamera")
    @Log("批量删除开箱摄像机")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllOpenCamera(@RequestBody OpenCamera openCamera) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(openCamera.getIdString()))
                success = openCameraService.deleteAll(openCamera.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除开箱摄像机失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 通过excel导入开箱摄像机数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ApiOperation(value = "导入开箱摄像机数据", notes = "导入开箱摄像机数据")
    public ResponseBean importOpenCamera(HttpServletRequest request) {
        ResponseBean responseBean=new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try {
                return openCameraService.importOpenCamera(file);
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
     * 导出开箱摄像机数据
     * @param dto
     */
    @PostMapping("export")
    @ApiOperation(value = "导出开箱摄像机数据", notes = "导出开箱摄像机数据")
    @ApiImplicitParam(name = "OpenCameraDto", value = "开箱摄像机信息", required = true, dataType = "dto")
    @Log(value = "导出开箱摄像机信息",businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportOpenCamera(@RequestBody OpenCameraDto dto, HttpServletRequest request, HttpServletResponse response) {
        return openCameraService.exportOpenCamera(dto,request,response);
    }
}
