package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventInputDto;
import com.aswl.as.ibrs.api.module.EventInput;
import com.aswl.as.ibrs.service.EventInputService;
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
 * 设备事件-输入controller
 *
 * @author df
 * @date 2022/07/07 15:06
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventInput", tags = "设备事件-输入")
@RestController
@RequestMapping("/v1/eventInput")
public class EventInputController extends BaseController {

    private final EventInputService eventInputService;

    /**
     * 根据ID获取设备事件-输入
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-输入ID查询设备事件-输入")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-输入ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventInput> findById(@PathVariable("id") String id) {
        EventInput eventInput = new EventInput();
        eventInput.setId(id);
        return new ResponseBean<>(eventInputService.get(eventInput));
    }

    /**
     * 查询所有设备事件-输入
     *
     * @param eventInput
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-输入列表")
    @ApiImplicitParam(name = "eventInput", value = "设备事件-输入对象", paramType = "path", required = true, dataType = "eventInput")
    @GetMapping(value = "eventInputs")
    public ResponseBean
            <List<EventInput>> findAll(EventInput eventInput) {
        eventInput.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventInputService.findList(eventInput));
    }

    /**
     * 分页查询设备事件-输入列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventInput
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-输入列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventInput", value = "设备事件-输入信息", dataType = "eventInput")
    })

    @GetMapping("eventInputList")
    public ResponseBean
            <PageInfo<EventInput>> eventInputList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                  EventInput eventInput) {
        eventInput.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventInputService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventInput));
    }

    /**
     * 新增设备事件-输入
     *
     * @param eventInputDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备事件-输入", notes = "新增设备事件-输入")
    @Log(value = "新增设备事件-输入", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertEventInput(@RequestBody @Valid EventInputDto eventInputDto) {
        EventInput eventInput = new EventInput();
        BeanUtils.copyProperties(eventInputDto, eventInput);
        eventInput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventInputService.insert(eventInput) > 0);
    }

    /**
     * 修改设备事件-输入
     *
     * @param eventInputDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备事件-输入信息", notes = "根据设备事件-输入ID更新设备事件-输入信息")
    @Log(value = "更新设备事件-输入", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateEventInput(@RequestBody @Valid EventInputDto eventInputDto) {
        EventInput eventInput = new EventInput();
        BeanUtils.copyProperties(eventInputDto, eventInput);
        eventInput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventInputService.update(eventInput) > 0);
    }

    /**
     * 根据设备事件-输入ID删除设备事件-输入信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备事件-输入信息", notes = "根据设备事件-输入ID删除设备事件-输入信息")
    @ApiImplicitParam(name = "id", value = "设备事件-输入ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除设备事件-输入", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteEventInputById(@PathVariable("id") String id) {
        EventInput eventInput = new EventInput();
        eventInput.setId(id);
        eventInput.setNewRecord(false);
        eventInput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventInputService.delete(eventInput) > 0);
    }

    /**
     * 批量删除设备事件-输入信息
     *
     * @param eventInput
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备事件-输入", notes = "根据设备事件-输入ID批量删除设备事件-输入")
    @ApiImplicitParam(name = "eventInput", value = "设备事件-输入信息", dataType = "eventInput")
    @Log(value = "删除设备事件-输入", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllEventInput(@RequestBody EventInput eventInput) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventInput.getIdString()))
                success = eventInputService.deleteAll(eventInput.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-输入失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
