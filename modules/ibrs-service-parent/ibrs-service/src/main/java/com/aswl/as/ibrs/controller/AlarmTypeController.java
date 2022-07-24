package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmTypeDto;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.service.AlarmTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 报警类型controller
 *
 * @author dingfei
 * @date 2019-10-22 11:48
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmType", tags = "报警类型")
@RestController
@RequestMapping("/v1/alarmType")
public class AlarmTypeController extends BaseController {

    private final AlarmTypeService alarmTypeService;

    /**
     * 根据ID获取报警类型
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报警类型ID查询报警类型")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警类型ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmType> findById(@PathVariable("id") String id) {
        AlarmType alarmType = new AlarmType();
        alarmType.setId(id);
        return new ResponseBean<>(alarmTypeService.get(alarmType));
    }

    /**
     * 查询所有报警类型
     *
     * @param alarmType
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报警类型列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmType", value = "报警类型对象", paramType = "path", required = true, dataType = "alarmType"))
    @GetMapping(value = "alarmTypes")
    public ResponseBean
            <List<AlarmType>> findAll(AlarmType alarmType) {
        return new ResponseBean<>(alarmTypeService.findList(alarmType));
    }

    /**
     * 分页查询报警类型列表
     *
     * @param pageNum
     * @param pageSize
     * @param alarmTypeDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报警类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmType", value = "报警类型信息", dataType = "alarmType")
    })

    @GetMapping("alarmTypeList")
    public ResponseBean<PageInfo<AlarmTypeVo>> alarmTypeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                             AlarmTypeDto alarmTypeDto) {
        return new ResponseBean<>(alarmTypeService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), alarmTypeDto));
    }

    /**
     * 新增报警类型
     *
     * @param alarmTypeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增报警类型", notes = "新增报警类型")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTypeDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmTypeDto"))
    @PostMapping
    @Log("新增报警类型")
    public ResponseBean
            <Boolean> insertAlarmType(@RequestBody @Valid AlarmTypeDto alarmTypeDto) {
        return new ResponseBean<>(alarmTypeService.insert(alarmTypeDto) > 0);
    }

    /**
     * 修改报警类型
     *
     * @param alarmTypeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新报警类型信息", notes = "根据报警类型ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTypeDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmTypeDto"))
    @Log("修改报警类型")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmType(@RequestBody @Valid AlarmTypeDto alarmTypeDto) {
            int update = 0;
            String[] ids = alarmTypeDto.getId().split(",");
            for (int i = 0; i < ids.length; i++) {
                AlarmType alarmType = new AlarmType();
                alarmType.setId(ids[i]);
                BeanUtils.copyProperties(alarmTypeDto, alarmType);
               // alarmType.setAlarmLevel(alarmTypeDto.getAlarmLevel());
                alarmTypeService.update(alarmType);
                update++;
            }
            if (update > 0) {
                return new ResponseBean<>(Boolean.TRUE);
            }else {
                return new ResponseBean<>(Boolean.FALSE);
            }
    }

    /**
     * 根据报警类型ID删除报警类型信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除报警类型信息", notes = "根据报警类型ID删除报警类型信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警类型ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmTypeById(@PathVariable("id") String id) {
        AlarmType alarmType = new AlarmType();
        alarmType.setId(id);
        alarmType.setNewRecord(false);
        return new ResponseBean<>(alarmTypeService.delete(alarmType) > 0);
    }

    /**
     * 批量删除报警类型信息
     *
     * @param alarmType
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除报警类型", notes = "根据报警类型ID批量删除报警类型")
    @ApiImplicitParam(name = "alarmType", value = "报警类型信息", dataType = "alarmType")
    @Log("批量删除报警类型")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmType(@RequestBody AlarmType alarmType) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmType.getIdString()))
                success = alarmTypeService.deleteAll(alarmType.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报警类型失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量修改报警类型
     *
     * @param list
     * @return ResponseBean
     */

    @ApiOperation(value = "批量修改报警类型", notes = "批量修改报警类型")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmTypeDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmTypeDto"))
    @Log("修改报警类型")
    @PostMapping(value = "updateBath")
    public ResponseBean
            <Boolean> updateBath(@RequestBody @Valid List<AlarmType> list) {
        return new ResponseBean<>(alarmTypeService.updateBath(list) > 0);
    }
}
