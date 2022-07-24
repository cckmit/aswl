package com.aswl.as.asos.modules.asuser.controller;


import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.user.api.dto.UserDto;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysUser;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysUserService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@RestController
@RequestMapping("/asuser/sys-user")
@Api("租户端用户信息表")
public class AsUserSysUserController extends AbstractController {

    @Autowired
    IAsUserSysUserService iAsUserSysUserService;

    /**
    * 用户信息表列表
    */
//    @GetMapping("/list")
//    @RequiresPermissions("os:sysUser:list")
//    @ApiOperation("用户信息列表")
//    public R list(@RequestParam Map<String, Object> params){
//
//        PageUtils page= iAsUserSysUserService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


    /**
    * 用户信息表信息
    */
//    @GetMapping("/info/{entityId}")
//    @RequiresPermissions("os:sysUser:info")
//    @ApiOperation("用户信息表信息")
//    public R info(@PathVariable("entityId") Long entityId){
//
//        AsUserSysUser role = iAsUserSysUserService.getById(entityId);
//        return R.ok().put("role", role);
//    }

    /**
    * 保存
    */
//    @SysLog("保存用户信息表")
//    @PostMapping("/save")
//    @RequiresPermissions("os:sysUser:save")
//    @ApiOperation("保存用户信息表")
//    public R save(@RequestBody AsUserSysUser entity){
//        //数据校验
//        String vStr=verifyForm(entity);
//        if(vStr!=null)
//        {
//            return R.error(vStr);
//        }
//
//        entity.setCreator(getUserName());
//        entity.setCreateDate(new Date());
//        iAsUserSysUserService.saveEntity(entity);
//
//        return R.ok();
//    }

    /**
    * 更新
    */
//    @SysLog("更新用户信息表")
//    @PostMapping("/update")
//    @RequiresPermissions("os:sysUser:save")
//    @ApiOperation("更新用户信息表")
//    public R update(@RequestBody AsUserSysUser entity){
//        //数据校验
//        String vStr=verifyForm(entity);
//        if(vStr!=null)
//        {
//            return R.error(vStr);
//        }
//
//        entity.setModifier(getUserName());
//        entity.setCreateDate(new Date());
//        iAsUserSysUserService.updateEntityById(entity);
//
//        return R.ok();
//    }

    /**
    * 删除用户信息表
    */
//    @SysLog("删除用户信息表")
//    @PostMapping("/delete")
//    @RequiresPermissions("os:sysUser:delete")
//    @ApiOperation("删除用户信息表")
//    public R delete(@RequestBody String[] ids){
//        iAsUserSysUserService.deleteEntityByIds(ids);
//        return R.ok();
//    }


    // 这里根据tenantCode来获取对应的数据
    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum@NotBlank
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param userVo   userVo
     * @return PageInfo
     * @author aswl.com
     * @date 2018/8/26 22:56
     */
    @GetMapping("userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public ResponseBean<PageInfo<UserDto>> osUser1(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                    @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                    @RequestParam(value = "deptId", required = false, defaultValue = "") String deptId,
                                                    @RequestParam(value = "deptCode", required = false, defaultValue = "") String deptCode,
                                                    @RequestParam(value = "regionCode",required = false,defaultValue = "") String regionCode,
                                                    UserVo userVo)
    {
        return iAsUserSysUserService.osUser1(pageNum,pageSize,sort,order,name,deptId,deptCode,regionCode,userVo);
    }

    /**
     * 验证账号是否存在
     * @param identifier
     * @return boolean
     */
    @GetMapping("checkExist/{identifier}")
    public ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier) {
        return iAsUserSysUserService.checkExist(identifier);
    }

    private String verifyForm(AsUserSysUser e)
    {

        try
        {
            //表单校验
            ValidatorUtils.validateEntity(e);
        }
        catch (Exception tempException)
        {
            return tempException.getMessage();
        }

        return null;
    }

    


}
