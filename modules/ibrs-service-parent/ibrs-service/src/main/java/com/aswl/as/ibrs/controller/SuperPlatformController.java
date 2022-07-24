package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.SuperPlatformDto;
import com.aswl.as.ibrs.api.module.SuperPlatform;
import com.aswl.as.ibrs.service.SuperPlatformService;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.User;
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
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

/**
 * 上级平台信息controller
 *
 * @author dingfei
 * @date 2019-11-11 16:03
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/superPlatform", tags = "上级平台信息")
@RestController
@RequestMapping("/v1/superPlatform")
public class SuperPlatformController extends BaseController {

    private final SuperPlatformService superPlatformService;
    private final UserServiceClient userServiceClient;

    /**
     * 根据ID获取上级平台信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据上级平台信息ID查询上级平台信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "上级平台信息ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<SuperPlatform> findById(@PathVariable("id") String id) {
        SuperPlatform superPlatform = new SuperPlatform();
        superPlatform.setId(id);
        SuperPlatform s = superPlatformService.get(superPlatform);
        if (s.getUserId() != null && !"".equals(s.getUserId())) {
            ResponseBean<User> user = userServiceClient.user(s.getUserId());
            if (user.getCode() == 200 && user.getData().getName() != null) {
                s.setUserName(user.getData().getName());
            }
        }
        return new ResponseBean<>(s);
    }

    /**
     * 查询所有上级平台信息
     *
     * @param superPlatform
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有上级平台信息列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "superPlatform", value = "上级平台信息对象", paramType = "path", required = true, dataType = "superPlatform"))
    @GetMapping(value = "superPlatforms")
    public ResponseBean
            <List<SuperPlatform>> findAll(SuperPlatform superPlatform) {
        superPlatform.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(superPlatformService.findAllList(superPlatform));
    }

    /**
     * 分页查询上级平台信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param superPlatform
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询上级平台信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "superPlatform", value = "上级平台信息信息", dataType = "superPlatform")
    })

    @GetMapping("superPlatformList")
    public ResponseBean<PageInfo<SuperPlatform>> superPlatformList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     SuperPlatform superPlatform) {
        superPlatform.setTenantCode(SysUtil.getTenantCode());
        PageInfo<SuperPlatform> page = superPlatformService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), superPlatform);
        if (page != null && page.getList().size() > 0) {
            List<SuperPlatform> collect = page.getList().stream().map(s -> {
                if (s.getUserId() != null && !"".equals(s.getUserId())) {
                    ResponseBean<User> user = userServiceClient.user(s.getUserId());
                    if (user.getCode() == 200 && user.getData().getName() != null) {
                        s.setUserName(user.getData().getName());
                    }
                }
                return s;
            }).collect(Collectors.toList());
            page.setList(collect);
        }
        return new ResponseBean<>(page);
    }

    /**
     * 新增上级平台信息
     *
     * @param superPlatformDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增上级平台信息", notes = "新增上级平台信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "superPlatformDto", value = "设备dto", required = true, paramType = "body", dataType = "superPlatformDto"))
    @PostMapping
    @Log(value = "新增上级平台信息",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertSuperPlatform(@RequestBody @Valid SuperPlatformDto superPlatformDto) {
        SuperPlatform superPlatform = new SuperPlatform();
        BeanUtils.copyProperties(superPlatformDto, superPlatform);
        superPlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(superPlatformService.insert(superPlatform) > 0);
    }

    /**
     * 修改上级平台信息
     *
     * @param superPlatformDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新上级平台信息信息", notes = "根据上级平台信息ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "superPlatformDto", value = "上级平台dto", required = true, paramType = "body", dataType = "superPlatformDto"))
    @Log(value = "修改上级平台信息",businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseBean
            <Boolean> updateSuperPlatform(@RequestBody @Valid SuperPlatformDto superPlatformDto) {
        SuperPlatform superPlatform = new SuperPlatform();
        BeanUtils.copyProperties(superPlatformDto, superPlatform);
        return new ResponseBean<>(superPlatformService.update(superPlatform) > 0);
    }

    /**
     * 根据上级平台信息ID删除上级平台信息信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除上级平台信息信息", notes = "根据上级平台信息ID删除上级平台信息信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "上级平台信息ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    @Log(value = "删除上级平台信息",businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteSuperPlatformById(@PathVariable("id") String id) {
        SuperPlatform superPlatform = new SuperPlatform();
        superPlatform.setId(id);
        superPlatform.setNewRecord(false);
        superPlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(superPlatformService.delete(superPlatform) > 0);
    }

    /**
     * 批量删除上级平台信息信息
     *
     * @param superPlatform
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除上级平台信息", notes = "根据上级平台信息ID批量删除上级平台信息")
    @ApiImplicitParam(name = "superPlatform", value = "上级平台信息信息", dataType = "superPlatform")
    @Log(value = "批量删除上级平台信息",businessType = BusinessType.DELETE)
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllSuperPlatform(@RequestBody SuperPlatform superPlatform) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(superPlatform.getIdString()))
                success = superPlatformService.deleteAll(superPlatform.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除上级平台信息失败！", e);
        }
        return new ResponseBean<>(success);
    }

    @ApiOperation(value = "重置上级平台密码", notes = "根据clientId重置上级平台密码")
    @ApiImplicitParams(@ApiImplicitParam(name = "superPlatformDto", value = "设备dto", required = true, paramType = "body", dataType = "superPlatformDto"))
    @Log(value = "重置上级平台密码",businessType = BusinessType.UPDATE)
    @PutMapping(value = "resetPassword")
    public ResponseBean
            <Boolean> resetPassword(@RequestBody @Valid SuperPlatformDto superPlatformDto) {
        SuperPlatform superPlatform = new SuperPlatform();
        BeanUtils.copyProperties(superPlatformDto, superPlatform);
        return new ResponseBean<>(superPlatformService.resetPassword(superPlatform));
    }
}
