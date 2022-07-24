package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmTerminalDto;
import com.aswl.as.ibrs.api.module.AlarmTerminal;
import com.aswl.as.ibrs.service.AlarmTerminalService;
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 报警终端设备controller
 *
 * @author dingfei
 * @date 2019-11-09 10:13
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmTerminal", tags = "报警终端设备")
@RestController
@RequestMapping("/v1/alarmTerminal")
public class AlarmTerminalController extends BaseController {

    private final AlarmTerminalService alarmTerminalService;

    /**
     * 根据ID获取报警终端设备
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报警终端设备ID查询报警终端设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警终端设备ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmTerminal> findById(@PathVariable("id") String id) {
        AlarmTerminal alarmTerminal = new AlarmTerminal();
        alarmTerminal.setId(id);
        return new ResponseBean<>(alarmTerminalService.get(alarmTerminal));
    }

    /**
     * 查询所有报警终端设备
     *
     * @param alarmTerminal
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报警终端设备列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTerminal", value = "报警终端设备对象", paramType = "path", required = true, dataType = "alarmTerminal"))
    @GetMapping(value = "alarmTerminals")
    public ResponseBean
            <List<AlarmTerminal>> findAll(AlarmTerminal alarmTerminal) {
        alarmTerminal.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmTerminalService.findAllList(alarmTerminal));
    }

    /**
     * 分页查询报警终端设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmTerminal
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报警终端设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmTerminal", value = "报警终端设备信息", dataType = "alarmTerminal")
    })

    @GetMapping("alarmTerminalList")
    public ResponseBean<PageInfo<AlarmTerminal>> alarmTerminalList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     AlarmTerminal alarmTerminal) {
        alarmTerminal.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmTerminalService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmTerminal));
    }

    /**
     * 新增报警终端设备
     *
     * @param alarmTerminalDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增报警终端设备", notes = "新增报警终端设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTerminalDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmTerminalDto"))
    @PostMapping
    @Log("新增报警终端设备")
    public ResponseBean
            <Boolean> insertAlarmTerminal(@RequestBody @Valid AlarmTerminalDto alarmTerminalDto) {
        AlarmTerminal alarmTerminal = new AlarmTerminal();
        BeanUtils.copyProperties(alarmTerminalDto, alarmTerminal);
        alarmTerminal.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmTerminalService.insert(alarmTerminal) > 0);
    }

    /**
     * 修改报警终端设备
     *
     * @param alarmTerminalDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新报警终端设备信息", notes = "根据报警终端设备ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTerminalDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmTerminalDto"))
    @Log("修改报警终端设备")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmTerminal(@RequestBody @Valid AlarmTerminalDto alarmTerminalDto) {
        AlarmTerminal alarmTerminal = new AlarmTerminal();
        BeanUtils.copyProperties(alarmTerminalDto, alarmTerminal);
        alarmTerminal.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmTerminalService.update(alarmTerminal) > 0);
    }

    /**
     * 根据报警终端设备ID删除报警终端设备信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除报警终端设备信息", notes = "根据报警终端设备ID删除报警终端设备信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警终端设备ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmTerminalById(@PathVariable("id") String id) {
        AlarmTerminal alarmTerminal = new AlarmTerminal();
        alarmTerminal.setId(id);
        alarmTerminal.setNewRecord(false);
        alarmTerminal.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmTerminalService.delete(alarmTerminal) > 0);
    }

    /**
     * 批量删除报警终端设备信息
     *
     * @param alarmTerminal
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除报警终端设备", notes = "根据报警终端设备ID批量删除报警终端设备")
    @ApiImplicitParam(name = "alarmTerminal", value = "报警终端设备信息", dataType = "alarmTerminal")
    @Log("批量删除报警终端设备")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmTerminal(@RequestBody AlarmTerminal alarmTerminal) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmTerminal.getIdString()))
                success = alarmTerminalService.deleteAll(alarmTerminal.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报警终端设备失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 发送短信
     *
     * @return ResponseBean
     */

    @ApiOperation(value = "发送短信", notes = "发送短信")
    @Log("发送短信")
    @GetMapping("sendSms")
    public ResponseBean sendSms() {
        String message = "发送短信测试操作成功";
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(200);
        responseBean.setData(message);
        responseBean.setMsg("success");
        responseBean.setStatus(200);
        return responseBean;
    }


}
