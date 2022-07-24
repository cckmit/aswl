package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.aswl.as.asos.modules.ibrs.service.IAsContentMalfunctionService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 常见故障表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@RestController

@RequestMapping("/ibrs/as-content-malfunction")
@Api("常见故障表")
public class AsContentMalfunctionController extends AbstractController {

    @Autowired
    IAsContentMalfunctionService iAsContentMalfunctionService;

    /**
    * 常见故障表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:malfunction:list")
    @ApiOperation("常见故障表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsContentMalfunctionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 常见故障表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:malfunction:list")
    @ApiOperation("常见故障表列表")
    public R findList(@RequestBody AsContentMalfunction entity){
        List<AsContentMalfunction> list= iAsContentMalfunctionService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 常见故障表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:malfunction:info")
    @ApiOperation("常见故障表信息")
    public R info(@PathVariable("entityId") String entityId){

        AsContentMalfunction data = iAsContentMalfunctionService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
//    @SysLog("保存常见故障表")
    @PostMapping("/save")
    @RequiresPermissions("os:malfunction:save")
    @ApiOperation("保存常见故障表")
    public R save(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsContentMalfunctionService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
//    @SysLog("更新常见故障表")
    @PostMapping("/update")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("更新常见故障表")
    public R update(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentMalfunctionService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除常见故障表
    */
    @SysLog("删除常见故障表")
    @PostMapping("/delete")
    @RequiresPermissions("os:malfunction:delete")
    @ApiOperation("删除常见故障表")
    public R delete(@RequestBody String[] ids){
        iAsContentMalfunctionService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 上移
     */
//    @SysLog("常见故障表 上移数据")
    @PostMapping("/moveUp")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("常见故障表 上移数据")
    public R moveUp(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentMalfunctionService.moveUp(entity);

        return R.ok();
    }

    /**
     * 下移
     */
//    @SysLog("常见故障表 下移数据")
    @PostMapping("/moveDown")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("常见故障表 下移数据")
    public R moveDown(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentMalfunctionService.moveDown(entity);

        return R.ok();
    }

    /**
     * 置顶
     */
//    @SysLog("常见故障表 置顶数据")
    @PostMapping("/sticky")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("常见故障表 置顶数据")
    public R sticky(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentMalfunctionService.sticky(entity);

        return R.ok();
    }

    /**
     * 设置橱窗
     */
//    @SysLog("常见故障表 设置橱窗")
    @PostMapping("/setShowcase")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("常见故障表 设置橱窗")
    public R setShowcase(@RequestBody AsContentMalfunction entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentMalfunctionService.setShowcase(entity);

        return R.ok();
    }

    /**
     * 设置已发布 常见故障表
     */
    @SysLog("设置已发布 常见故障表")
    @PostMapping("/release")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("设置已发布 常见故障表")
    public R release(@RequestBody String[] ids){
        AsContentMalfunction entity=new AsContentMalfunction();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(1);
        iAsContentMalfunctionService.setIsRelease(entity,ids);
        return R.ok();
    }

    /**
     * 设置未发布 产品中心表
     */
    @SysLog("设置未发布 常见故障表")
    @PostMapping("/notRelease")
    @RequiresPermissions("os:malfunction:update")
    @ApiOperation("设置未发布 常见故障表")
    public R notRelease(@RequestBody String[] ids){

        AsContentMalfunction entity=new AsContentMalfunction();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(0);

        iAsContentMalfunctionService.setIsRelease(entity,ids);
        return R.ok();
    }

    private String verifyForm(AsContentMalfunction e)
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
