package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventNetworkDto;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.ibrs.service.EventNetworkService;
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
 * 设备事件-网络controller
 *
 * @author zgl
 * @date 2019-11-01 14:06
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventNetwork", tags = "设备事件-网络")
@RestController
@RequestMapping("/v1/eventNetwork")
public class EventNetworkController extends BaseController {

    private final EventNetworkService eventNetworkService;

    /**
     * 根据ID获取设备事件-网络
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-网络ID查询设备事件-网络")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-网络ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventNetwork> findById(@PathVariable("id") String id) {
        EventNetwork eventNetwork = new EventNetwork();
        eventNetwork.setId(id);
        return new ResponseBean<>(eventNetworkService.get(eventNetwork));
    }

    /**
     * 查询所有设备事件-网络
     *
     * @param eventNetwork
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-网络列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventNetwork", value = "设备事件-网络对象", paramType = "path", required = true, dataType = "eventNetwork"))
    @GetMapping(value = "eventNetworks")
    public ResponseBean
            <List<EventNetwork>> findAll(EventNetwork eventNetwork) {
        eventNetwork.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventNetworkService.findAllList(eventNetwork));
    }

    /**
     * 分页查询设备事件-网络列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventNetwork
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-网络列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventNetwork", value = "设备事件-网络信息", dataType = "eventNetwork")
    })

    @GetMapping("eventNetworkList")
    public ResponseBean<PageInfo<EventNetwork>> eventNetworkList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   EventNetwork eventNetwork) {
        eventNetwork.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventNetworkService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventNetwork));
    }

    /**
     * 新增设备事件-网络
     *
     * @param eventNetworkDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-网络", notes = "新增设备事件-网络")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventNetworkDto", value = "设备dto", required = true, paramType = "body", dataType = "eventNetworkDto"))
    @PostMapping
    @Log("新增设备事件-网络")
    public ResponseBean
            <Boolean> insertEventNetwork(@RequestBody @Valid EventNetworkDto eventNetworkDto) {
        EventNetwork eventNetwork = new EventNetwork();
        BeanUtils.copyProperties(eventNetworkDto, eventNetwork);
        eventNetwork.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventNetworkService.insert(eventNetwork) > 0);
    }

    /**
     * 修改设备事件-网络
     *
     * @param eventNetworkDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-网络信息", notes = "根据设备事件-网络ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventNetworkDto", value = "设备dto", required = true, paramType = "body", dataType = "eventNetworkDto"))
    @Log("修改设备事件-网络")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventNetwork(@RequestBody @Valid EventNetworkDto eventNetworkDto) {
        EventNetwork eventNetwork = new EventNetwork();
        BeanUtils.copyProperties(eventNetworkDto, eventNetwork);
        eventNetwork.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventNetworkService.update(eventNetwork) > 0);
    }

    /**
     * 根据设备事件-网络ID删除设备事件-网络信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-网络信息", notes = "根据设备事件-网络ID删除设备事件-网络信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-网络ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventNetworkById(@PathVariable("id") String id) {
        EventNetwork eventNetwork = new EventNetwork();
        eventNetwork.setId(id);
        eventNetwork.setNewRecord(false);
        eventNetwork.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventNetworkService.delete(eventNetwork) > 0);
    }

    /**
     * 批量删除设备事件-网络信息
     *
     * @param eventNetwork
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-网络", notes = "根据设备事件-网络ID批量删除设备事件-网络")
    @ApiImplicitParam(name = "eventNetwork", value = "设备事件-网络信息", dataType = "eventNetwork")
    @Log("批量删除设备事件-网络")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventNetwork(@RequestBody EventNetwork eventNetwork) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventNetwork.getIdString()))
                success = eventNetworkService.deleteAll(eventNetwork.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-网络失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
