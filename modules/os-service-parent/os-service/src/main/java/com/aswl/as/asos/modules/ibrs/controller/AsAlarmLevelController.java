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
import com.aswl.as.asos.modules.ibrs.entity.AsAlarmLevel;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmLevelService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 报警级别 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@RestController

@RequestMapping("/ibrs/as-alarm-level")
@Api("报警级别")
public class AsAlarmLevelController extends AbstractController {

    @Autowired
    IAsAlarmLevelService iAsAlarmLevelService;

    /**
    * 报警级别分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("报警级别分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsAlarmLevelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 报警级别列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("报警级别列表")
    public R findList(@RequestBody AsAlarmLevel entity){
        List<AsAlarmLevel> list= iAsAlarmLevelService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 报警级别信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("报警级别信息")
    public R info(@PathVariable("entityId") String entityId){

        AsAlarmLevel data = iAsAlarmLevelService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存报警级别")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存报警级别")
    public R save(@RequestBody AsAlarmLevel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setCreateName(getUserName());
//        entity.setCreateDate(LocalDateTime.now());
        iAsAlarmLevelService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新报警级别")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新报警级别")
    public R update(@RequestBody AsAlarmLevel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setModifier(getUserName());
//        entity.setModifyDate(LocalDateTime.now());
        iAsAlarmLevelService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除报警级别
    */
    @SysLog("删除报警级别")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除报警级别")
    public R delete(@RequestBody String[] ids){
        iAsAlarmLevelService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsAlarmLevel e)
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
