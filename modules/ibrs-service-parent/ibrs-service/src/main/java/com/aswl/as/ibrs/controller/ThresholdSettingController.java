package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ThresholdSettingDto;
import com.aswl.as.ibrs.api.module.AlarmWaySetting;
import com.aswl.as.ibrs.api.module.ThresholdSetting;
import com.aswl.as.ibrs.service.ThresholdSettingService;
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
 * 阈值设置表controller
 *
 * @author df
 * @date 2021/09/28 09:47
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/thresholdSetting", tags = "阈值设置表")
@RestController
@RequestMapping("/v1/thresholdSetting")
public class ThresholdSettingController extends BaseController {

    private final ThresholdSettingService thresholdSettingService;

    /**
     * 根据ID获取阈值设置表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据阈值设置表ID查询阈值设置表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "阈值设置表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ThresholdSetting> findById(@PathVariable("id") String id) {
        ThresholdSetting thresholdSetting = new ThresholdSetting();
        thresholdSetting.setId(id);
        return new ResponseBean<>(thresholdSettingService.get(thresholdSetting));
    }

    /**
     * 查询所有阈值设置表
     *
     * @param thresholdSetting
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有阈值设置表列表")
    @ApiImplicitParam(name = "thresholdSetting", value = "阈值设置表对象", paramType = "path", required = true, dataType = "thresholdSetting")
    @GetMapping(value = "thresholdSettings")
    public ResponseBean
            <List<ThresholdSetting>> findAll(ThresholdSetting thresholdSetting) {
        thresholdSetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(thresholdSettingService.findList(thresholdSetting));
    }

    /**
     * 分页查询阈值设置表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param thresholdSetting
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询阈值设置表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "thresholdSetting", value = "阈值设置表信息", dataType = "thresholdSetting")
    })

    @GetMapping("thresholdSettingList")
    public ResponseBean
            <PageInfo<ThresholdSetting>> thresholdSettingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                              @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                              @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                              ThresholdSetting thresholdSetting) {
        thresholdSetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(thresholdSettingService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), thresholdSetting));
    }

    /**
     * 新增阈值设置表
     *
     * @param thresholdSettingDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增阈值设置表", notes = "新增阈值设置表")
    @Log(value = "新增阈值设置表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertThresholdSetting(@RequestBody @Valid ThresholdSettingDto thresholdSettingDto) {
        ThresholdSetting thresholdSetting = new ThresholdSetting();
        BeanUtils.copyProperties(thresholdSettingDto, thresholdSetting);
        thresholdSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(thresholdSettingService.insert(thresholdSetting) > 0);
    }

    /**
     * 修改阈值设置表
     *
     * @param thresholdSettingDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新阈值设置表信息", notes = "根据阈值设置表ID更新阈值设置表信息")
    @Log(value = "更新阈值设置表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateThresholdSetting(@RequestBody @Valid ThresholdSettingDto thresholdSettingDto) {
        ThresholdSetting thresholdSetting = new ThresholdSetting();
        BeanUtils.copyProperties(thresholdSettingDto, thresholdSetting);
        thresholdSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(thresholdSettingService.update(thresholdSetting) > 0);
    }

    /**
     * 根据阈值设置表ID删除阈值设置表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除阈值设置表信息", notes = "根据阈值设置表ID删除阈值设置表信息")
    @ApiImplicitParam(name = "id", value = "阈值设置表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除阈值设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteThresholdSettingById(@PathVariable("id") String id) {
        ThresholdSetting thresholdSetting = new ThresholdSetting();
        thresholdSetting.setId(id);
        thresholdSetting.setNewRecord(false);
        thresholdSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(thresholdSettingService.delete(thresholdSetting) > 0);
    }

    /**
     * 批量删除阈值设置表信息
     *
     * @param thresholdSetting
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除阈值设置表", notes = "根据阈值设置表ID批量删除阈值设置表")
    @ApiImplicitParam(name = "thresholdSetting", value = "阈值设置表信息", dataType = "thresholdSetting")
    @Log(value = "删除阈值设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllThresholdSetting(@RequestBody ThresholdSetting thresholdSetting) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(thresholdSetting.getIdString()))
                success = thresholdSettingService.deleteAll(thresholdSetting.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除阈值设置表失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 批量保存更新阈值设置表
     *
     * @param list list
     * @return ResponseBean
     * @author aswl.com
     */
    @PostMapping(value = "insertBath")
    @ApiOperation(value = "批量保存更新阈值设置表信息", notes = "批量保存更新阈值设置表信息")
    @ApiImplicitParam(name = "list", value = "阈值设置表信息", required = true, dataType = "ThresholdSetting")
    @Log(value = "批量保存更新阈值设置表信息", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertBath(@RequestBody List<ThresholdSetting> list) {
        return new ResponseBean<>(thresholdSettingService.insertBath(list) > 0);
    }
}
