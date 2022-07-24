package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmLevelDto;
import com.aswl.as.ibrs.api.dto.AlarmTypeDeviceFavoriteDto;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.ibrs.service.AlarmLevelService;
import com.aswl.as.ibrs.service.AlarmOrderHandleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aswl.as.common.core.utils.PageUtil;

/**
 * 报警级别controller
 *
 * @author dingfei
 * @date 2019-11-05 16:53
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmLevel", tags = "报警级别")
@RestController
@RequestMapping("/v1/alarmLevel")
public class AlarmLevelController extends BaseController {

    private final AlarmLevelService alarmLevelService;

    private final AlarmOrderHandleService alarmOrderHandleService;

    /**
     * 根据ID获取报警级别
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报警级别ID查询报警级别")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警级别ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmLevel> findById(@PathVariable("id") String id) {
        AlarmLevel alarmLevel = new AlarmLevel();
        alarmLevel.setId(id);
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.get(alarmLevel));
    }

    /**
     * 查询所有报警级别
     *
     * @param alarmLevel
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报警级别列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmLevel", value = "报警级别对象", paramType = "path", required = true, dataType = "alarmLevel"))
    @GetMapping(value = "alarmLevels")
    public ResponseBean
            <List<AlarmLevel>> findAll(AlarmLevel alarmLevel) {
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.findAllList(alarmLevel));
    }

    /**
     * 分页查询报警级别列表
     *
     * @param pageNum
     * @param pageSize
     * @param alarmLevel
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报警级别列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmLevel", value = "报警级别信息", dataType = "alarmLevel")
    })

    @GetMapping("alarmLevelList")
    public ResponseBean<PageInfo<AlarmLevel>> alarmLevelList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               AlarmLevel alarmLevel) {
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), alarmLevel));
    }

    /**
     * 新增报警级别
     *
     * @param alarmLevelDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增报警级别", notes = "新增报警级别")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmLevelDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmLevelDto"))
    @PostMapping
    @Log("新增报警级别")
    public ResponseBean
            <Boolean> insertAlarmLevel(@RequestBody @Valid AlarmLevelDto alarmLevelDto) {
        AlarmLevel alarmLevel = new AlarmLevel();
        BeanUtils.copyProperties(alarmLevelDto, alarmLevel);
        alarmLevel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.insert(alarmLevel) > 0);
    }

    /**
     * 修改报警级别
     *
     * @param alarmLevelDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新报警级别信息", notes = "根据报警级别ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmLevelDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmLevelDto"))
    @Log("修改报警级别")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmLevel(@RequestBody @Valid AlarmLevelDto alarmLevelDto) {
        AlarmLevel alarmLevel = new AlarmLevel();
        BeanUtils.copyProperties(alarmLevelDto, alarmLevel);
        alarmLevel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.update(alarmLevel) > 0);
    }

    /**
     * 根据报警级别ID删除报警级别信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除报警级别信息", notes = "根据报警级别ID删除报警级别信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警级别ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmLevelById(@PathVariable("id") String id) {
        AlarmLevel alarmLevel = new AlarmLevel();
        alarmLevel.setId(id);
        alarmLevel.setNewRecord(false);
        alarmLevel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        alarmLevel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmLevelService.delete(alarmLevel) > 0);
    }

    /**
     * 批量删除报警级别信息
     *
     * @param alarmLevel
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除报警级别", notes = "根据报警级别ID批量删除报警级别")
    @ApiImplicitParam(name = "alarmLevel", value = "报警级别信息", dataType = "alarmLevel")
    @Log("批量删除报警级别")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmLevel(@RequestBody AlarmLevel alarmLevel) {
        boolean success = false;
        try {
                success = alarmLevelService.deleteAll(alarmLevel.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报警级别失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 本函数用来查询派单配置，跟下面的updateData来 配对 使用
     * @return
     */
    @ApiOperation(value = "查询派单配置")
    @GetMapping(value = "queryDataForUpdate")
    public ResponseBean<Map> queryDataForUpdate() {
        return new ResponseBean<>(alarmLevelService.queryDataForUpdate());
    }


    @PutMapping(value = "updateData")
    @ApiOperation(value = "批量修改派单配置", notes = "批量修改派单配置")
    @Log(value = "批量修改派单配置",businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateData(@RequestBody Map<String,Object> map) {
        return new ResponseBean<>(alarmLevelService.updateData(map));
    }

    /**
     * 这个函数一般不用了
     * @return
     */
    @Deprecated
    @GetMapping(value = "workOrderConfig")
    public ResponseBean
            <List<AlarmLevel>> findWorkOrderConfig() {
        return new ResponseBean<>(alarmLevelService.findWorkOrderConfig());
    }

    /**
     * 这个函数一般不用了
     * @param orderTypes
     * @return
     */
    @Deprecated
    @PutMapping(value = "batchUpdate")
    @ApiOperation(value = "批量修改派单配置", notes = "批量修改派单配置")
    @Log(value = "批量修改派单配置",businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> batchUpdateUserWatchDevice(@RequestBody @Valid String orderTypes) {

        //  到时候修复这些数据

        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AlarmLevelDto.class);
        List<AlarmLevelDto> alarmLevelDtos = null;
        try {
            alarmLevelDtos = mapper.readValue(orderTypes, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AlarmLevel> alarmLevels = new ArrayList<>();
        for (AlarmLevelDto alarmLevelDto : alarmLevelDtos) {
            AlarmLevel alarmLevel  = new AlarmLevel();
            BeanUtils.copyProperties(alarmLevelDto, alarmLevel);
            alarmLevels.add(alarmLevel);
        }
        return new ResponseBean<>(alarmLevelService.batchUpdate(alarmLevels) > 0);
    }

    /**
     * 根据租户编码删除报警级别
     *
     * @param tenantCode
     * @return ResponseBean
     */
    @ApiOperation(value = "根据租户编码删除报警级别")
    @ApiImplicitParams(@ApiImplicitParam(name = "tenantCode", value = "租户编码", paramType = "query", required = true, dataType = "String"))
    @GetMapping(value = "deleteByTenantCode")
    public ResponseBean<Boolean> deleteAlarmLevelByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(alarmLevelService.deleteByTenantCode(tenantCode) > 0);
    }


}
