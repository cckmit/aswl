package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.aswl.as.asos.modules.ibrs.service.IAsContentIndustryService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 行业资讯表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@RestController

@RequestMapping("/ibrs/as-content-industry")
@Api("行业资讯表")
public class AsContentIndustryController extends AbstractController {

    @Autowired
    IAsContentIndustryService iAsContentIndustryService;

    /**
    * 行业资讯表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:industry:list")
    @ApiOperation("行业资讯表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsContentIndustryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 行业资讯表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:industry:list")
    @ApiOperation("行业资讯表列表")
    public R findList(@RequestBody AsContentIndustry entity){
        List<AsContentIndustry> list= iAsContentIndustryService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 行业资讯表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:industry:info")
    @ApiOperation("行业资讯表信息")
    public R info(@PathVariable("entityId") String entityId){

        AsContentIndustry data = iAsContentIndustryService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
//    @SysLog("保存行业资讯表")
    @PostMapping("/save")
    @RequiresPermissions("os:industry:save")
    @ApiOperation("保存行业资讯表")
    public R save(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsContentIndustryService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
//    @SysLog("更新行业资讯表")
    @PostMapping("/update")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("更新行业资讯表")
    public R update(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentIndustryService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除行业资讯表
    */
    @SysLog("删除行业资讯表")
    @PostMapping("/delete")
    @RequiresPermissions("os:industry:delete")
    @ApiOperation("删除行业资讯表")
    public R delete(@RequestBody String[] ids){
        iAsContentIndustryService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 上移
     */
//    @SysLog("行业资讯表 上移数据")
    @PostMapping("/moveUp")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("行业资讯表 上移数据")
    public R moveUp(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentIndustryService.moveUp(entity);

        return R.ok();
    }

    /**
     * 下移
     */
//    @SysLog("行业资讯表 下移数据")
    @PostMapping("/moveDown")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("行业资讯表 下移数据")
    public R moveDown(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentIndustryService.moveDown(entity);

        return R.ok();
    }

    /**
     * 置顶
     */
//    @SysLog("行业资讯表 置顶数据")
    @PostMapping("/sticky")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("行业资讯表 置顶数据")
    public R sticky(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentIndustryService.sticky(entity);

        return R.ok();
    }

    /**
     * 设置橱窗
     */
//    @SysLog("行业资讯表 设置橱窗")
    @PostMapping("/setShowcase")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("行业资讯表 设置橱窗")
    public R setShowcase(@RequestBody AsContentIndustry entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentIndustryService.setShowcase(entity);

        return R.ok();
    }

    /**
     * 设置已发布 行业资讯表
     */
    @SysLog("设置已发布 行业资讯表")
    @PostMapping("/release")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("设置已发布 行业资讯表")
    public R release(@RequestBody String[] ids){
        AsContentIndustry entity=new AsContentIndustry();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(1);
        iAsContentIndustryService.setIsRelease(entity,ids);
        return R.ok();
    }

    /**
     * 设置未发布 行业资讯表
     */
    @SysLog("设置未发布 行业资讯表")
    @PostMapping("/notRelease")
    @RequiresPermissions("os:industry:update")
    @ApiOperation("设置未发布 行业资讯表")
    public R notRelease(@RequestBody String[] ids){

        AsContentIndustry entity=new AsContentIndustry();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(0);

        iAsContentIndustryService.setIsRelease(entity,ids);
        return R.ok();
    }

    private String verifyForm(AsContentIndustry e)
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
