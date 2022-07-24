package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.asuser.entity.SysAppMenu;
import com.aswl.as.asos.modules.asuser.service.ISysAppMenuService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是) 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
@RestController

@RequestMapping("/asuser/sys-app-menu")
@Api("APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
public class SysAppMenuController extends AbstractController {

    @Autowired
    ISysAppMenuService iSysAppMenuService;

    /**
    * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iSysAppMenuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)列表")
    public R findList(@RequestBody SysAppMenu entity){
        List<SysAppMenu> list= iSysAppMenuService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)信息")
    public R info(@PathVariable("entityId") String entityId){

        SysAppMenu data = iSysAppMenuService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    public R save(@RequestBody SysAppMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iSysAppMenuService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    public R update(@RequestBody SysAppMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iSysAppMenuService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)
    */
    @SysLog("删除APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
    public R delete(@RequestBody String[] ids){
        iSysAppMenuService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(SysAppMenu e)
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
