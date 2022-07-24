package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.TimetaskDto;
import com.aswl.as.ibrs.api.module.Timetask;
import com.aswl.as.ibrs.service.TimetaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 定时任务controller
 *
 * @author dingfei
 * @date 2019-11-13 10:19
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/timetask", tags = "定时任务")
@RestController
@RequestMapping("/v1/timetask")
public class TimetaskController extends BaseController {

    private final TimetaskService timetaskService;

    /**
     * 根据ID获取定时任务
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据定时任务ID查询定时任务")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "定时任务ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<Timetask> findById(@PathVariable("id") String id) {
        Timetask timetask = new Timetask();
        timetask.setId(id);
        return new ResponseBean<>(timetaskService.get(timetask));
    }

    /**
     * 查询所有定时任务
     *
     * @param timetask
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有定时任务列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "timetask", value = "定时任务对象", paramType = "path", required = true, dataType = "timetask"))
    @GetMapping(value = "timetasks")
    public ResponseBean
            <List<Timetask>> findAll(Timetask timetask) {
        timetask.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(timetaskService.findAllList(timetask));
    }

    /**
     * 分页查询定时任务列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param timetask
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询定时任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "timetask", value = "定时任务信息", dataType = "timetask")
    })

    @GetMapping("timetaskList")
    public PageInfo<Timetask> timetaskList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           Timetask timetask) {
        timetask.setTenantCode(SysUtil.getTenantCode());
        return timetaskService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), timetask);
    }

    /**
     * 新增定时任务
     *
     * @param timetaskDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @ApiImplicitParams(@ApiImplicitParam(name = "timetaskDto", value = "设备dto", required = true, paramType = "body", dataType = "timetaskDto"))
    @PostMapping
    @Log(value = "新增定时任务", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertTimetask(@RequestBody @Valid TimetaskDto timetaskDto) {
        Timetask timetask = new Timetask();
        BeanUtils.copyProperties(timetaskDto, timetask);
        timetask.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(timetaskService.insert(timetask) > 0);
    }

    /**
     * 修改定时任务
     *
     * @param timetaskDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新定时任务信息", notes = "根据定时任务ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "timetaskDto", value = "设备dto", required = true, paramType = "body", dataType = "timetaskDto"))
    @Log(value = "修改定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseBean
            <Boolean> updateTimetask(@RequestBody @Valid TimetaskDto timetaskDto) throws SchedulerException {
        Timetask timetask = new Timetask();
        BeanUtils.copyProperties(timetaskDto, timetask);
            return new ResponseBean<>(timetaskService.updateScheduler(timetask) > 0);

    }

    /**
     * 根据定时任务ID删除定时任务信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除定时任务信息", notes = "根据定时任务ID删除定时任务信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "定时任务ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    @Log(value = "删除定时任务", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteTimetaskById(@PathVariable("id") String id) {
        Timetask timetask = new Timetask();
        timetask.setId(id);
        timetask.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        try {
            return new ResponseBean<>(timetaskService.deleteScheduler(timetask) > 0);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new ResponseBean<>(false);
    }

    /**
     * 批量删除定时任务信息
     *
     * @param timetask
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除定时任务", notes = "根据定时任务ID批量删除定时任务")
    @ApiImplicitParam(name = "timetask", value = "定时任务信息", dataType = "timetask")
    @Log(value = "批量删除定时任务", businessType = BusinessType.DELETE)
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllTimetask(@RequestBody Timetask timetask) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(timetask.getIdString()))
                success = timetaskService.deleteAll(timetask.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除定时任务失败！", e);
        }
        return new ResponseBean<>(success);
    }

    @ApiOperation(value = "任务调度立即执行", notes = "任务调度立即执行")
    @ApiImplicitParam(name = "timetask", value = "定时任务信息", dataType = "timetask")
    @PostMapping(value = "run")
    @Log(value = "任务调度立即执行", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> startScheduler(@RequestBody @Valid TimetaskDto timetaskDto) {
        try {
            Timetask timetask = new Timetask();
            BeanUtils.copyProperties(timetaskDto, timetask);
            timetaskService.run(timetask);
            return new ResponseBean<>(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new ResponseBean<>(false);
    }

    @ApiOperation(value = "停止任务调度", notes = "停止任务调度")
    @ApiImplicitParam(name = "timetask", value = "定时任务信息", dataType = "timetask")
    @PostMapping(value = "stop")
    @Log(value = "更改任务调度立即执行", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> stopScheduler(@RequestBody Timetask timetask) {
        try {
            timetaskService.stop(timetask);
            return new ResponseBean<>(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new ResponseBean<>(false);
    }
}
