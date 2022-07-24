package com.aswl.as.user.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.user.api.dto.UserMobileBindingDto;
import com.aswl.as.user.api.module.UserMobileBinding;
import com.aswl.as.user.service.UserMobileBindingService;
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
 * 手机端登录授权controller
 *
 * @author df
 * @date 2022/03/18 14:24
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/userMobileBinding", tags = "手机端登录授权")
@RestController
@RequestMapping("/v1/userMobileBinding")
public class UserMobileBindingController extends BaseController {

    private final UserMobileBindingService userMobileBindingService;

    /**
     * 根据ID获取手机端登录授权
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据手机端登录授权ID查询手机端登录授权")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "手机端登录授权ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<UserMobileBinding> findById(@PathVariable("id") String id) {
        UserMobileBinding userMobileBinding = new UserMobileBinding();
        userMobileBinding.setId(id);
        return new ResponseBean<>(userMobileBindingService.get(userMobileBinding));
    }

    /**
     * 查询所有手机端登录授权
     *
     * @param userMobileBinding
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有手机端登录授权列表")
    @ApiImplicitParam(name = "userMobileBinding", value = "手机端登录授权对象", paramType = "path", required = true, dataType = "userMobileBinding")
    @GetMapping(value = "userMobileBindings")
    public ResponseBean
            <List<UserMobileBinding>> findAll(UserMobileBinding userMobileBinding) {
        userMobileBinding.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(userMobileBindingService.findList(userMobileBinding));
    }

    /**
     * 分页查询手机端登录授权列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param userMobileBinding
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询手机端登录授权列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "userMobileBinding", value = "手机端登录授权信息", dataType = "userMobileBinding")
    })

    @GetMapping("userMobileBindingList")
    public ResponseBean
            <PageInfo<UserMobileBinding>> userMobileBindingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                UserMobileBinding userMobileBinding) {
        userMobileBinding.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(userMobileBindingService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), userMobileBinding));
    }

    /**
     * 新增手机端登录授权
     *
     * @param userMobileBindingDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增手机端登录授权", notes = "新增手机端登录授权")
    @Log(value = "新增手机端登录授权", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertUserMobileBinding(@RequestBody @Valid UserMobileBindingDto userMobileBindingDto) {
        return new ResponseBean<>(userMobileBindingService.insert(userMobileBindingDto) > 0);
    }

    /**
     * 修改手机端登录授权
     *
     * @param userMobileBindingDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新手机端登录授权信息", notes = "根据手机端登录授权ID更新手机端登录授权信息")
    @Log(value = "更新手机端登录授权", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateUserMobileBinding(@RequestBody @Valid UserMobileBindingDto userMobileBindingDto) {
        UserMobileBinding userMobileBinding = new UserMobileBinding();
        BeanUtils.copyProperties(userMobileBindingDto, userMobileBinding);
        userMobileBinding.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(userMobileBindingService.update(userMobileBinding) > 0);
    }

    /**
     * 根据手机端登录授权ID删除手机端登录授权信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除手机端登录授权信息", notes = "根据手机端登录授权ID删除手机端登录授权信息")
    @ApiImplicitParam(name = "id", value = "手机端登录授权ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除手机端登录授权", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteUserMobileBindingById(@PathVariable("id") String id) {
        UserMobileBinding userMobileBinding = new UserMobileBinding();
        userMobileBinding.setId(id);
        userMobileBinding.setNewRecord(false);
        userMobileBinding.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(userMobileBindingService.delete(userMobileBinding) > 0);
    }

    /**
     * 批量修改手机端登录授权
     *
     * @param list
     * @return ResponseBean
     */

    @ApiOperation(value = "批量修改手机端登录授权", notes = "批量修改手机端登录授权")
    @ApiImplicitParams(@ApiImplicitParam(name = "list", value = "手机端登录授权", required = true, paramType = "body", dataType = "UserMobileBinding"))
    @PostMapping(value = "updateBath")
    public ResponseBean
            <Boolean> updateBath(@RequestBody List<UserMobileBinding> list) {
        return new ResponseBean<>(userMobileBindingService.updateBath(list) > 0);
    }
}
