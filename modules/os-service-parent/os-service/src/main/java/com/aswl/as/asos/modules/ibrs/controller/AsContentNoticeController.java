package com.aswl.as.asos.modules.ibrs.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsContentNotice;
import com.aswl.as.asos.modules.ibrs.service.IAsContentNoticeService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 系统消息表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@RestController

@RequestMapping("/ibrs/as-content-notice")
@Api("系统消息表")
public class AsContentNoticeController extends AbstractController {

    @Autowired
    IAsContentNoticeService iAsContentNoticeService;

    /**
    * 系统消息表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:notice:list")
    @ApiOperation("系统消息表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsContentNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 系统消息表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:notice:list")
    @ApiOperation("系统消息表列表")
    public R findList(@RequestBody AsContentNotice entity){
        List<AsContentNotice> list= iAsContentNoticeService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 系统消息表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:notice:info")
    @ApiOperation("系统消息表信息")
    public R info(@PathVariable("entityId") String entityId){

        AsContentNotice data = iAsContentNoticeService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存系统消息表")
    @PostMapping("/save")
    @RequiresPermissions("os:notice:save")
    @ApiOperation("保存系统消息表")
    public R save(@RequestBody AsContentNotice entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsContentNoticeService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新系统消息表")
    @PostMapping("/update")
    @RequiresPermissions("os:notice:update")
    @ApiOperation("更新系统消息表")
    public R update(@RequestBody AsContentNotice entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentNoticeService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除系统消息表
    */
    @SysLog("删除系统消息表")
    @PostMapping("/delete")
    @RequiresPermissions("os:notice:delete")
    @ApiOperation("删除系统消息表")
    public R delete(@RequestBody String[] ids){
        iAsContentNoticeService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsContentNotice e)
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
