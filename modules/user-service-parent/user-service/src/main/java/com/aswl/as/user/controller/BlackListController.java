package com.aswl.as.user.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.user.api.dto.BlackListDto;
import com.aswl.as.user.api.module.BlackList;
import com.aswl.as.user.service.BlackListService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 系统IP黑名单controller
 *
 * @author dingfei
 * @date 2019-11-08 15:32
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/blackList", tags = "系统IP黑名单")
@RestController
@RequestMapping("/v1/blackList")
public class BlackListController extends BaseController {

    private final BlackListService blackListService;

    /**
     * 根据ID获取系统IP黑名单
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据系统IP黑名单ID查询系统IP黑名单")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "系统IP黑名单ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<BlackList> findById(@PathVariable("id") String id) {
        BlackList blackList = new BlackList();
        blackList.setId(id);
        return new ResponseBean<>(blackListService.get(blackList));
    }

    /**
     * 查询所有系统IP黑名单
     *
     * @param blackList
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有系统IP黑名单列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "blackList", value = "系统IP黑名单对象", paramType = "path", required = true, dataType = "blackList"))
    @GetMapping(value = "blackLists")
    public ResponseBean
            <List<BlackList>> findAll(BlackList blackList) {
        blackList.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(blackListService.findAllList(blackList));
    }

    /**
     * 分页查询系统IP黑名单列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param blackList
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询系统IP黑名单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "blackList", value = "系统IP黑名单信息", dataType = "blackList")
    })

    @GetMapping("blackListList")
    public PageInfo<BlackList> blackListList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             BlackList blackList) {
        blackList.setTenantCode(SysUtil.getTenantCode());
        return blackListService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), blackList);
    }

    /**
     * 新增系统IP黑名单
     *
     * @param blackListDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增系统IP黑名单", notes = "新增系统IP黑名单")
    @ApiImplicitParams(@ApiImplicitParam(name = "blackListDto", value = "设备dto", required = true, paramType = "body", dataType = "blackListDto"))
    @PostMapping
    @Log(value = "新增系统IP黑名单",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertBlackList(@RequestBody @Valid BlackListDto blackListDto) {
        BlackList blackList = new BlackList();
        BeanUtils.copyProperties(blackListDto, blackList);
        blackList.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(blackListService.insert(blackList) > 0);
    }

    /**
     * 修改系统IP黑名单
     *
     * @param blackListDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新系统IP黑名单信息", notes = "根据系统IP黑名单ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "blackListDto", value = "设备dto", required = true, paramType = "body", dataType = "blackListDto"))
    @Log(value = "修改系统IP黑名单",businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseBean
            <Boolean> updateBlackList(@RequestBody @Valid BlackListDto blackListDto) {
        BlackList blackList = new BlackList();
        BeanUtils.copyProperties(blackListDto, blackList);
        blackList.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(blackListService.update(blackList) > 0);
    }

    /**
     * 根据系统IP黑名单ID删除系统IP黑名单信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除系统IP黑名单信息", notes = "根据系统IP黑名单ID删除系统IP黑名单信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "系统IP黑名单ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    @Log(value = "删除系统IP黑名单",businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteBlackListById(@PathVariable("id") String id) {
        BlackList blackList = new BlackList();
        blackList.setId(id);
        blackList.setNewRecord(false);
        blackList.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(blackListService.delete(blackList) > 0);
    }

    /**
     * 批量删除系统IP黑名单信息
     *
     * @param blackList
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除系统IP黑名单", notes = "根据系统IP黑名单ID批量删除系统IP黑名单")
    @ApiImplicitParam(name = "blackList", value = "系统IP黑名单信息", dataType = "blackList")
    @Log(value = "批量删除系统IP黑名单",businessType = BusinessType.DELETE)
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllBlackList(@RequestBody BlackList blackList) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(blackList.getIdString()))
                success = blackListService.deleteAll(blackList.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除系统IP黑名单失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
