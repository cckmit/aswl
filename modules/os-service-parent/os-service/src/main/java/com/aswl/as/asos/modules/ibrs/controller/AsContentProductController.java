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
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.aswl.as.asos.modules.ibrs.service.IAsContentProductService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 产品中心表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@RestController

@RequestMapping("/ibrs/as-content-product")
@Api("产品中心表")
public class AsContentProductController extends AbstractController {

    @Autowired
    IAsContentProductService iAsContentProductService;

    /**
    * 产品中心表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:product:list")
    @ApiOperation("产品中心表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsContentProductService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 产品中心表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:product:list")
    @ApiOperation("产品中心表列表")
    public R findList(@RequestBody AsContentProduct entity){
        List<AsContentProduct> list= iAsContentProductService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 产品中心表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:product:info")
    @ApiOperation("产品中心表信息")
    public R info(@PathVariable("entityId") String entityId){

        AsContentProduct data = iAsContentProductService.getEntityById(entityId);
        return R.ok().put("data", data);

    }

    /**
    * 保存
    */
//    @SysLog("保存产品中心表")
    @PostMapping("/save")
    @RequiresPermissions("os:product:save")
    @ApiOperation("保存产品中心表")
    public R save(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsContentProductService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
//    @SysLog("更新产品中心表")
    @PostMapping("/update")
    @RequiresPermissions("os:product:update")
    @ApiOperation("更新产品中心表")
    public R update(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentProductService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除产品中心表
    */
    @SysLog("删除产品中心表")
    @PostMapping("/delete")
    @RequiresPermissions("os:product:delete")
    @ApiOperation("删除产品中心表")
    public R delete(@RequestBody String[] ids){
        iAsContentProductService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 上移
     */
//    @SysLog("产品中心表 上移数据")
    @PostMapping("/moveUp")
    @RequiresPermissions("os:product:update")
    @ApiOperation("产品中心表 上移数据")
    public R moveUp(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentProductService.moveUp(entity);

        return R.ok();
    }

    /**
     * 下移
     */
//    @SysLog("产品中心表 下移数据")
    @PostMapping("/moveDown")
    @RequiresPermissions("os:product:update")
    @ApiOperation("产品中心表 下移数据")
    public R moveDown(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentProductService.moveDown(entity);

        return R.ok();
    }

    /**
     * 置顶
     */
//    @SysLog("产品中心表 置顶数据")
    @PostMapping("/sticky")
    @RequiresPermissions("os:product:update")
    @ApiOperation("产品中心表 置顶数据")
    public R sticky(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentProductService.sticky(entity);

        return R.ok();
    }

    /**
     * 设置橱窗
     */
//    @SysLog("产品中心表 设置橱窗")
    @PostMapping("/setShowcase")
    @RequiresPermissions("os:product:update")
    @ApiOperation("产品中心表 设置橱窗")
    public R setShowcase(@RequestBody AsContentProduct entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentProductService.setShowcase(entity);

        return R.ok();
    }

    /**
     * 设置已发布 产品中心表
     */
    @SysLog("设置已发布")
    @PostMapping("/release")
    @RequiresPermissions("os:product:update")
    @ApiOperation("设置已发布")
    public R release(@RequestBody String[] ids){
        AsContentProduct entity=new AsContentProduct();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(1);
        iAsContentProductService.setIsRelease(entity,ids);
        return R.ok();
    }

    /**
     * 设置未发布 产品中心表
     */
    @SysLog("设置未发布 产品中心表")
    @PostMapping("/notRelease")
    @RequiresPermissions("os:product:update")
    @ApiOperation("设置未发布 产品中心表")
    public R notRelease(@RequestBody String[] ids){

        AsContentProduct entity=new AsContentProduct();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(0);

        iAsContentProductService.setIsRelease(entity,ids);
        return R.ok();
    }

    private String verifyForm(AsContentProduct e)
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
