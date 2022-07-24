package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.AlarmOrderHandleDto;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import com.aswl.as.ibrs.api.vo.AlarmOrderHandleVo;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.AlarmOrderHandleService;
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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 派单处理工单设置controller
 *
 * @author hfx
 * @date 2020-03-23 11:43
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmOrderHandle", tags = "派单处理工单设置")
@RestController
@RequestMapping("/v1/alarmOrderHandle")
public class AlarmOrderHandleController extends BaseController {

    private final AlarmOrderHandleService alarmOrderHandleService;

    /**
     * 根据ID获取派单处理工单设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据派单处理工单设置ID查询派单处理工单设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "派单处理工单设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmOrderHandle> findById(@PathVariable("id") String id) {
        AlarmOrderHandle alarmOrderHandle = new AlarmOrderHandle();
        alarmOrderHandle.setId(id);
        return new ResponseBean<>(alarmOrderHandleService.get(alarmOrderHandle));
    }

    /**
     * 查询所有派单处理工单设置
     *
     * @param alarmOrderHandle
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有派单处理工单设置列表")
    @ApiImplicitParam(name = "alarmOrderHandle", value = "派单处理工单设置对象", paramType = "path", required = true, dataType = "alarmOrderHandle")
    @GetMapping(value = "alarmOrderHandles")
    public ResponseBean
            <List<AlarmOrderHandle>> findAll(AlarmOrderHandle alarmOrderHandle) {
        alarmOrderHandle.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmOrderHandleService.findList(alarmOrderHandle));
    }

    /**
     * 分页查询派单处理工单设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmOrderHandle
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询派单处理工单设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmOrderHandle", value = "派单处理工单设置信息", dataType = "alarmOrderHandle")
    })

    @GetMapping("alarmOrderHandleList")
    public ResponseBean<PageInfo<AlarmOrderHandle>> alarmOrderHandleList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                         AlarmOrderHandle alarmOrderHandle) {
        alarmOrderHandle.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmOrderHandleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmOrderHandle));
    }

    /**
     * 新增派单处理工单设置
     *
     * @param alarmOrderHandleDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增派单处理工单设置", notes = "新增派单处理工单设置")
    @PostMapping
    @Log("新增派单处理工单设置")
    public ResponseBean
            <Boolean> insertAlarmOrderHandle(@RequestBody @Valid AlarmOrderHandleDto alarmOrderHandleDto) {
        AlarmOrderHandle alarmOrderHandle = new AlarmOrderHandle();
        BeanUtils.copyProperties(alarmOrderHandleDto, alarmOrderHandle);
        alarmOrderHandle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(), alarmOrderHandleDto.getProjectId());
        return new ResponseBean<>(alarmOrderHandleService.insert(alarmOrderHandle) > 0);
    }

    /**
     * 修改派单处理工单设置
     *
     * @param alarmOrderHandleDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新派单处理工单设置信息", notes = "根据派单处理工单设置ID更新派单处理工单设置信息")
    @Log("修改派单处理工单设置")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmOrderHandle(@RequestBody @Valid AlarmOrderHandleDto alarmOrderHandleDto) {
        AlarmOrderHandle alarmOrderHandle = new AlarmOrderHandle();
        BeanUtils.copyProperties(alarmOrderHandleDto, alarmOrderHandle);
        alarmOrderHandle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), alarmOrderHandleDto.getTenantCode(), alarmOrderHandleDto.getProjectId());
        return new ResponseBean<>(alarmOrderHandleService.update(alarmOrderHandle) > 0);
    }

    /**
     * 根据派单处理工单设置ID删除派单处理工单设置信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除派单处理工单设置信息", notes = "根据派单处理工单设置ID删除派单处理工单设置信息")
    @ApiImplicitParam(name = "id", value = "派单处理工单设置ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmOrderHandleById(@PathVariable("id") String id) {
        AlarmOrderHandle alarmOrderHandle = new AlarmOrderHandle();
        alarmOrderHandle.setId(id);
        alarmOrderHandle.setNewRecord(false);
        alarmOrderHandle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmOrderHandleService.delete(alarmOrderHandle) > 0);
    }

    /**
     * 批量删除派单处理工单设置信息
     *
     * @param alarmOrderHandle
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除派单处理工单设置", notes = "根据派单处理工单设置ID批量删除派单处理工单设置")
    @ApiImplicitParam(name = "alarmOrderHandle", value = "派单处理工单设置信息", dataType = "alarmOrderHandle")
    @Log("批量删除派单处理工单设置")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmOrderHandle(@RequestBody AlarmOrderHandle alarmOrderHandle) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmOrderHandle.getIdString()))
                success = alarmOrderHandleService.deleteAll(alarmOrderHandle.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除派单处理工单设置失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 分页查询项目派单设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmOrderHandleDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询项目派单设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmOrderHandleDto", value = "派单设置信息", dataType = "alarmOrderHandleDto")
    })
    @GetMapping("projectOrderSettingList")
    public ResponseBean<PageInfo<AlarmOrderHandleVo>> projectOrderSettingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                              @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = "projectId") String sort,
                                                                              @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_ASC) String order,
                                                                              AlarmOrderHandleDto alarmOrderHandleDto) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
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

        }
        alarmOrderHandleDto.setTenantCode(tenantCode);
        alarmOrderHandleDto.setProjectId(projectId);
        return new ResponseBean<>(alarmOrderHandleService.findProjectOrderPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmOrderHandleDto));
    }

    /**
     * 根据项目ID集获取派单设置
     *
     * @param projectIds
     * @return ResponseBean
     */
    @ApiOperation(value = "根据项目ID集获取派单设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "projectIds", value = "项目ID集", paramType = "query", required = true, dataType = "String"))
    @GetMapping(value = "findByProjectIds")
    public ResponseBean<List<AlarmOrderHandleVo>> findByProjectIds(@RequestParam("projectIds") String projectIds) {
        AlarmOrderHandleDto alarmOrderHandleDto = new AlarmOrderHandleDto();
        alarmOrderHandleDto.setProjectId(projectIds);
        return new ResponseBean<>(alarmOrderHandleService.findProjectOrderList(alarmOrderHandleDto));
    }

    /**
     * 批量保存派单设置
     *
     * @param alarmOrderHandleDtoList
     * @return ResponseBean
     */
    @ApiOperation(value = "批量保存派单设置", notes = "批量保存派单设置")
    @PostMapping("batchSaveAlarmOrderHandle")
    @Log("批量保存派单设置")
    public ResponseBean
            <Boolean> batchSaveAlarmOrderHandle(@RequestBody @Valid List<AlarmOrderHandleDto> alarmOrderHandleDtoList) {
        List<AlarmOrderHandle> alarmOrderHandleList = new ArrayList<>();
        AlarmOrderHandle alarmOrderHandle = null;
        for(AlarmOrderHandleDto dto : alarmOrderHandleDtoList){
            alarmOrderHandle = new AlarmOrderHandle();
            BeanUtils.copyProperties(dto, alarmOrderHandle);
            alarmOrderHandleList.add(alarmOrderHandle);
        }
        return new ResponseBean<>(alarmOrderHandleService.batchSave(alarmOrderHandleList) > 0);
    }

    /**
     * 根据租户编码删除工单设置
     *
     * @param tenantCode
     * @return ResponseBean
     */
    @ApiOperation(value = "根据租户编码删除工单设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "tenantCode", value = "租户编码", paramType = "query", required = true, dataType = "String"))
    @GetMapping(value = "deleteByTenantCode")
    public ResponseBean<Boolean> deleteAlarmOrderHandleByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(alarmOrderHandleService.deleteByTenantCode(tenantCode) > 0 );
    }
}
