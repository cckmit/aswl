package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmRevokeDto;
import com.aswl.as.ibrs.api.module.AlarmRevoke;
import com.aswl.as.ibrs.service.AlarmRevokeService;
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
 * 报警撤销记录表controller
 *
 * @author dingfei
 * @date 2020-01-08 10:42
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmRevoke", tags = "报警撤销记录表")
@RestController
@RequestMapping("/v1/alarmRevoke")
public class AlarmRevokeController extends BaseController {

    private final AlarmRevokeService alarmRevokeService;

    /**
     * 根据ID获取报警撤销记录表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报警撤销记录表ID查询报警撤销记录表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警撤销记录表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmRevoke> findById(@PathVariable("id") String id) {
        AlarmRevoke alarmRevoke = new AlarmRevoke();
        alarmRevoke.setId(id);
        return new ResponseBean<>(alarmRevokeService.get(alarmRevoke));
    }

    /**
     * 查询所有报警撤销记录表
     *
     * @param alarmRevoke
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报警撤销记录表列表")
    @ApiImplicitParam(name = "alarmRevoke", value = "报警撤销记录表对象", paramType = "path", required = true, dataType = "alarmRevoke")
    @GetMapping(value = "alarmRevokes")
    public ResponseBean
            <List<AlarmRevoke>> findAll(AlarmRevoke alarmRevoke) {
        alarmRevoke.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmRevokeService.findList(alarmRevoke));
    }

    /**
     * 分页查询报警撤销记录表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmRevoke
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报警撤销记录表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmRevoke", value = "报警撤销记录表信息", dataType = "alarmRevoke")
    })

    @GetMapping("alarmRevokeList")
    public ResponseBean<PageInfo<AlarmRevoke>> alarmRevokeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               AlarmRevoke alarmRevoke) {
       // alarmRevoke.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmRevokeService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), alarmRevoke));
    }

    /**
     * 新增报警撤销记录表
     *
     * @param alarmRevokeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增报警撤销记录表", notes = "新增报警撤销记录表")
    @PostMapping
    @Log("新增报警撤销记录表")
    public ResponseBean
            <Boolean> insertAlarmRevoke(@RequestBody @Valid AlarmRevokeDto alarmRevokeDto) {
        AlarmRevoke alarmRevoke = new AlarmRevoke();
        BeanUtils.copyProperties(alarmRevokeDto, alarmRevoke);
        alarmRevoke.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmRevokeService.insert(alarmRevoke) > 0);
    }

    /**
     * 修改报警撤销记录表
     *
     * @param alarmRevokeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新报警撤销记录表信息", notes = "根据报警撤销记录表ID更新报警撤销记录表信息")
    @Log("修改报警撤销记录表")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmRevoke(@RequestBody @Valid AlarmRevokeDto alarmRevokeDto) {
        AlarmRevoke alarmRevoke = new AlarmRevoke();
        BeanUtils.copyProperties(alarmRevokeDto, alarmRevoke);
        alarmRevoke.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmRevokeService.update(alarmRevoke) > 0);
    }

    /**
     * 根据报警撤销记录表ID删除报警撤销记录表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除报警撤销记录表信息", notes = "根据报警撤销记录表ID删除报警撤销记录表信息")
    @ApiImplicitParam(name = "id", value = "报警撤销记录表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmRevokeById(@PathVariable("id") String id) {
        AlarmRevoke alarmRevoke = new AlarmRevoke();
        alarmRevoke.setId(id);
        alarmRevoke.setNewRecord(false);
        alarmRevoke.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmRevokeService.delete(alarmRevoke) > 0);
    }

    /**
     * 批量删除报警撤销记录表信息
     *
     * @param alarmRevoke
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除报警撤销记录表", notes = "根据报警撤销记录表ID批量删除报警撤销记录表")
    @ApiImplicitParam(name = "alarmRevoke", value = "报警撤销记录表信息", dataType = "alarmRevoke")
    @Log("批量删除报警撤销记录表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmRevoke(@RequestBody AlarmRevoke alarmRevoke) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmRevoke.getIdString()))
                success = alarmRevokeService.deleteAll(alarmRevoke.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报警撤销记录表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
