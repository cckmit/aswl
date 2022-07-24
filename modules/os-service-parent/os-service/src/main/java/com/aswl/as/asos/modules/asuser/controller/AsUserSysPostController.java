package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysPost;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysPostService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 工作岗位 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
//@RestController
//@RequestMapping("/asuser/sys-post")
//@Api("工作岗位")
public class AsUserSysPostController extends AbstractController {

    @Autowired
    IAsUserSysPostService iAsUserSysPostService;

    /**
    * 工作岗位列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysPost:list")
    @ApiOperation("工作岗位列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysPostService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 工作岗位信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysPost:info")
    @ApiOperation("工作岗位信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysPost role = iAsUserSysPostService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存工作岗位")
    @PostMapping("/save")
    @RequiresPermissions("os:sysPost:save")
    @ApiOperation("保存工作岗位")
    public R save(@RequestBody AsUserSysPost entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsUserSysPostService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新工作岗位")
    @PostMapping("/update")
    @RequiresPermissions("os:sysPost:save")
    @ApiOperation("更新工作岗位")
    public R update(@RequestBody AsUserSysPost entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsUserSysPostService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除工作岗位
    */
    @SysLog("删除工作岗位")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysPost:delete")
    @ApiOperation("删除工作岗位")
    public R delete(@RequestBody String[] ids){
        iAsUserSysPostService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysPost e)
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
