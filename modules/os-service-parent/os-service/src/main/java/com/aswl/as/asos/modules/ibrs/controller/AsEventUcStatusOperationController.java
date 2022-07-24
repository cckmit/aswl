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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusOperationService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 事件状态操作 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@RestController

@RequestMapping("/ibrs/asEventUcStatusOperation")
@Api("事件状态操作")
public class AsEventUcStatusOperationController extends AbstractController {

    @Autowired
    IAsEventUcStatusOperationService iAsEventUcStatusOperationService;

    /**
    * 事件状态操作分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list") //TODO 到时候需要这个事件状态权限，到时候问清楚
    @ApiOperation("事件状态操作分页列表")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page=iAsEventUcStatusOperationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 事件状态操作列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("事件状态操作列表")
    public R findList(@RequestBody AsEventUcStatusOperation entity){
        List<AsEventUcStatusOperation> list= iAsEventUcStatusOperationService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 事件状态操作信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("事件状态操作信息")
    public R info(@PathVariable("entityId") String entityId){

        AsEventUcStatusOperation data = iAsEventUcStatusOperationService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存事件状态操作")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存事件状态操作")
    public R save(@RequestBody AsEventUcStatusOperation entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateBy(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsEventUcStatusOperationService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新事件状态操作")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新事件状态操作")
    public R update(@RequestBody AsEventUcStatusOperation entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setUpdateBy(getUserName());
        entity.setUpdateDate(LocalDateTime.now());
        iAsEventUcStatusOperationService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除事件状态操作
    */
    @SysLog("删除事件状态操作")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除事件状态操作")
    public R delete(@RequestBody String[] ids){
        iAsEventUcStatusOperationService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsEventUcStatusOperation e)
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
