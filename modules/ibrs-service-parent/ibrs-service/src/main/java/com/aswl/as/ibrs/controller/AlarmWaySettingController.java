package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.AlarmWaySettingDto;
import com.aswl.as.ibrs.api.module.AlarmWaySetting;
import com.aswl.as.ibrs.service.AlarmWaySettingService;
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
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 告警方式设置表controller
 *
 * @author df
 * @date 2021/07/10 17:16
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmWaySetting", tags = "告警方式设置表")
@RestController
@RequestMapping("/v1/alarmWaySetting")
public class AlarmWaySettingController extends BaseController {

    private final AlarmWaySettingService alarmWaySettingService;

    /**
     * 根据ID获取告警方式设置表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据告警方式设置表ID查询告警方式设置表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "告警方式设置表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmWaySetting> findById(@PathVariable("id") String id) {
        AlarmWaySetting alarmWaySetting = new AlarmWaySetting();
        alarmWaySetting.setId(id);
        return new ResponseBean<>(alarmWaySettingService.get(alarmWaySetting));
    }

    /**
     * 查询所有告警方式设置表
     *
     * @param alarmWaySetting
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有告警方式设置表列表")
    @ApiImplicitParam(name = "alarmWaySetting", value = "告警方式设置表对象", paramType = "path", required = true, dataType = "alarmWaySetting")
    @GetMapping(value = "alarmWaySettings")
    public ResponseBean
            <List<AlarmWaySetting>> findAll(AlarmWaySetting alarmWaySetting) {
        alarmWaySetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWaySettingService.findList(alarmWaySetting));
    }

    /**
     * 分页查询告警方式设置表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmWaySetting
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询告警方式设置表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmWaySetting", value = "告警方式设置表信息", dataType = "alarmWaySetting")
    })

    @GetMapping("alarmWaySettingList")
    public ResponseBean
            <PageInfo<AlarmWaySetting>> alarmWaySettingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                            AlarmWaySetting alarmWaySetting) {
        alarmWaySetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWaySettingService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmWaySetting));
    }

    /**
     * 新增告警方式设置表
     *
     * @param alarmWaySettingDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增告警方式设置表", notes = "新增告警方式设置表")
    @Log(value = "新增告警方式设置表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertAlarmWaySetting(@RequestBody @Valid AlarmWaySettingDto alarmWaySettingDto) {
        AlarmWaySetting alarmWaySetting = new AlarmWaySetting();
        BeanUtils.copyProperties(alarmWaySettingDto, alarmWaySetting);
        alarmWaySetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWaySettingService.insert(alarmWaySetting) > 0);
    }

    /**
     * 修改告警方式设置表
     *
     * @param alarmWaySettingDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新告警方式设置表信息", notes = "根据告警方式设置表ID更新告警方式设置表信息")
    @Log(value = "更新告警方式设置表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateAlarmWaySetting(@RequestBody @Valid AlarmWaySettingDto alarmWaySettingDto) {
        AlarmWaySetting alarmWaySetting = new AlarmWaySetting();
        BeanUtils.copyProperties(alarmWaySettingDto, alarmWaySetting);
        alarmWaySetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWaySettingService.update(alarmWaySetting) > 0);
    }

    /**
     * 根据告警方式设置表ID删除告警方式设置表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除告警方式设置表信息", notes = "根据告警方式设置表ID删除告警方式设置表信息")
    @ApiImplicitParam(name = "id", value = "告警方式设置表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除告警方式设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAlarmWaySettingById(@PathVariable("id") String id) {
        AlarmWaySetting alarmWaySetting = new AlarmWaySetting();
        alarmWaySetting.setId(id);
        alarmWaySetting.setNewRecord(false);
        alarmWaySetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWaySettingService.delete(alarmWaySetting) > 0);
    }

    /**
     * 批量删除告警方式设置表信息
     *
     * @param alarmWaySetting
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除告警方式设置表", notes = "根据告警方式设置表ID批量删除告警方式设置表")
    @ApiImplicitParam(name = "alarmWaySetting", value = "告警方式设置表信息", dataType = "alarmWaySetting")
    @Log(value = "删除告警方式设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllAlarmWaySetting(@RequestBody AlarmWaySetting alarmWaySetting) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmWaySetting.getIdString()))
                success = alarmWaySettingService.deleteAll(alarmWaySetting.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除告警方式设置表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量保存更新告警方式设置表
     *
     * @param list list
     * @return ResponseBean
     * @author aswl.com
     */
    @PostMapping(value = "insertBath")
    @ApiOperation(value = "批量保存更新告警方式设置表信息", notes = "批量保存更新告警方式设置表信息")
    @ApiImplicitParam(name = "list", value = "告警方式设置表信息", required = true, dataType = "AlarmWaySetting")
    @Log(value = "批量设备种类数量表信息", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertBath(@RequestBody List<AlarmWaySetting> list) {
        return new ResponseBean<>(alarmWaySettingService.insertBath(list) > 0);
    }
}
