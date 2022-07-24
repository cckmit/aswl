package com.aswl.as.asos.modules.asuser.controller;


import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysMenuService;

import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

// 本类不会公开，只是创建时使用
/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-13
 */
//@RestController
//@RequestMapping("/asuser/sys-menu")
//@Api("菜单表")
public class SysMenuController extends AbstractController {

    @Autowired
    IAsUserSysMenuService iAsUserSysMenuService;

    /**
    * 菜单表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:menu:list")
    @ApiOperation("菜单表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysMenuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 菜单表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:menu:list")
    @ApiOperation("菜单表列表")
    public R findList(@RequestBody AsUserSysMenu entity){
        List<AsUserSysMenu> list= iAsUserSysMenuService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 菜单表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:menu:info")
    @ApiOperation("菜单表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysMenu role = iAsUserSysMenuService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存菜单表")
    @PostMapping("/save")
    @RequiresPermissions("os:menu:save")
    @ApiOperation("保存菜单表")
    public R save(@RequestBody AsUserSysMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsUserSysMenuService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新菜单表")
    @PostMapping("/update")
    @RequiresPermissions("os:menu:update")
    @ApiOperation("更新菜单表")
    public R update(@RequestBody AsUserSysMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsUserSysMenuService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除菜单表
    */
    @SysLog("删除菜单表")
    @PostMapping("/delete")
    @RequiresPermissions("os:menu:delete")
    @ApiOperation("删除菜单表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysMenuService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysMenu e)
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
