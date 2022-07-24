package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.OpenDoorRecodeDto;
import com.aswl.as.ibrs.api.module.OpenDoorRecode;
import com.aswl.as.ibrs.api.vo.DeviceAlarmVo;
import com.aswl.as.ibrs.api.vo.OpenDoorVo;
import com.aswl.as.ibrs.service.OpenDoorRecodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.commons.lang.StringUtils;

/**
 * 开箱记录表controller
 *
 * @author com.aswl
 * @date 2019-12-18 17:06
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/openDoorRecode", tags = "开箱记录表")
@RestController
@RequestMapping("/v1/openDoorRecode")
public class OpenDoorRecodeController extends BaseController {

    private final OpenDoorRecodeService openDoorRecodeService;

    /**
     * 根据ID获取开箱记录表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据开箱记录表ID查询开箱记录表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "开箱记录表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<OpenDoorRecode> findById(@PathVariable("id") String id) {
        OpenDoorRecode openDoorRecode = new OpenDoorRecode();
        openDoorRecode.setId(id);
        return new ResponseBean<>(openDoorRecodeService.get(openDoorRecode));
    }

    /**
     * 查询所有开箱记录表
     *
     * @param openDoorRecode
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有开箱记录表列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "openDoorRecode", value = "开箱记录表对象", paramType = "path", required = true, dataType = "openDoorRecode"))
    @GetMapping(value = "openDoorRecodes")
    public ResponseBean
            <List<OpenDoorRecode>> findAll(OpenDoorRecode openDoorRecode) {
        openDoorRecode.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(openDoorRecodeService.findAllList(openDoorRecode));
    }

    /**
     * 分页查询开箱记录表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param openDoorRecode
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询开箱记录表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "openDoorRecode", value = "开箱记录表信息", dataType = "openDoorRecode")
    })

    @GetMapping("openDoorRecodeList")
    public ResponseBean<PageInfo<OpenDoorRecode>> openDoorRecodeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                       OpenDoorRecode openDoorRecode) {
        openDoorRecode.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(openDoorRecodeService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), openDoorRecode));
    }

    /**
     * 新增开箱记录表
     *
     * @param openDoorRecodeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增开箱记录表", notes = "新增开箱记录表")
    @ApiImplicitParams(@ApiImplicitParam(name = "openDoorRecodeDto", value = "设备dto", required = true, paramType = "body", dataType = "openDoorRecodeDto"))
    @PostMapping
    @Log(value = "新增开箱记录表",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertOpenDoorRecode(@RequestBody @Valid OpenDoorRecodeDto openDoorRecodeDto) {
        OpenDoorRecode openDoorRecode = new OpenDoorRecode();
        BeanUtils.copyProperties(openDoorRecodeDto, openDoorRecode);
        openDoorRecode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(openDoorRecodeService.insert(openDoorRecode) > 0);
    }

    /**
     * 修改开箱记录表
     *
     * @param openDoorRecodeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新开箱记录表信息", notes = "根据开箱记录表ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "openDoorRecodeDto", value = "设备dto", required = true, paramType = "body", dataType = "openDoorRecodeDto"))
    @Log(value = "修改开箱记录表",businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseBean
            <Boolean> updateOpenDoorRecode(@RequestBody @Valid OpenDoorRecodeDto openDoorRecodeDto) {
        OpenDoorRecode openDoorRecode = new OpenDoorRecode();
        BeanUtils.copyProperties(openDoorRecodeDto, openDoorRecode);
        openDoorRecode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(openDoorRecodeService.update(openDoorRecode) > 0);
    }

    /**
     * 根据开箱记录表ID删除开箱记录表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除开箱记录表信息", notes = "根据开箱记录表ID删除开箱记录表信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "开箱记录表ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteOpenDoorRecodeById(@PathVariable("id") String id) {
        OpenDoorRecode openDoorRecode = new OpenDoorRecode();
        openDoorRecode.setId(id);
        openDoorRecode.setNewRecord(false);
        openDoorRecode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(openDoorRecodeService.delete(openDoorRecode) > 0);
    }

    /**
     * 批量删除开箱记录表信息
     *
     * @param openDoorRecode
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除开箱记录表", notes = "根据开箱记录表ID批量删除开箱记录表")
    @ApiImplicitParam(name = "openDoorRecode", value = "开箱记录表信息", dataType = "openDoorRecode")
    @Log(value = "批量删除开箱记录表",businessType = BusinessType.DELETE)
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllOpenDoorRecode(@RequestBody OpenDoorRecode openDoorRecode) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(openDoorRecode.getIdString()))
                success = openDoorRecodeService.deleteAll(openDoorRecode.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除开箱记录表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    @GetMapping(value = "findOpenDoor")
    @ApiOperation(value = "信息查询模块中查询设备开箱记录")
    public ResponseBean<PageInfo<OpenDoorVo>> findOpenDoorInfo(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                  @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                  OpenDoorRecodeDto openDoorRecodeDto) {


        return new ResponseBean<>(openDoorRecodeService.findOpenDoorInfo(PageUtil.pageInfo(pageNum, pageSize, "", ""), openDoorRecodeDto));
    }

    @GetMapping(value = "openDoorDetails")
    @ApiOperation(value = "设备详情中分页条件查询指定设备开箱记录详情")
    public ResponseBean<Map> findOpenDoorList(@RequestParam(value = "pageNum", required = false) String pageNum,
                                              @RequestParam(value = "pageSize", required = false) String pageSize,
                                              OpenDoorRecodeDto openDoorRecodeDto) {
/*
        if (openDoorRecodeDto.getDay() != null && !"".equals(openDoorRecodeDto.getDay())) {
            Date date = new Date();//取时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); //需要将date数据转移到Calender对象中操作
            calendar.add(calendar.DATE, -(openDoorRecodeDto.getDay().equals("0") ? 0 : Integer.parseInt(openDoorRecodeDto.getDay())));//把日期往后增加n天.正数往后推,负数往前移动
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            openDoorRecodeDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")));
        }*/

        return new ResponseBean<>(openDoorRecodeService.findOpenDoorList(PageUtil.pageInfo(pageNum, pageSize, "", ""), openDoorRecodeDto));
    }
}
