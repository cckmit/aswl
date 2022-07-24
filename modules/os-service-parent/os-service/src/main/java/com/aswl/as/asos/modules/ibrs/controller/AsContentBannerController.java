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
import com.aswl.as.asos.modules.ibrs.entity.AsContentBanner;
import com.aswl.as.asos.modules.ibrs.service.IAsContentBannerService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * banner管理 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@RestController

@RequestMapping("/ibrs/as-content-banner")
@Api("banner管理")
public class AsContentBannerController extends AbstractController {

    @Autowired
    IAsContentBannerService iAsContentBannerService;

    /**
    * banner管理分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:banner:list")
    @ApiOperation("banner管理分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsContentBannerService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * banner管理列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:banner:list")
    @ApiOperation("banner管理列表")
    public R findList(@RequestBody AsContentBanner entity){
        List<AsContentBanner> list= iAsContentBannerService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * banner管理信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:banner:info")
    @ApiOperation("banner管理信息")
    public R info(@PathVariable("entityId") String entityId){

        AsContentBanner data = iAsContentBannerService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存banner管理")
    @PostMapping("/save")
    @RequiresPermissions("os:banner:save")
    @ApiOperation("保存banner管理")
    public R save(@RequestBody AsContentBanner entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsContentBannerService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新banner管理")
    @PostMapping("/update")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("更新banner管理")
    public R update(@RequestBody AsContentBanner entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentBannerService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除banner管理
    */
    @SysLog("删除banner管理")
    @PostMapping("/delete")
    @RequiresPermissions("os:banner:delete")
    @ApiOperation("删除banner管理")
    public R delete(@RequestBody String[] ids){
        iAsContentBannerService.deleteEntityByIds(ids);
        return R.ok();
    }



    /**
     * 上移
     */
    @SysLog("上移banner管理")
    @PostMapping("/moveUp")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("上移banner管理")
    public R moveUp(@RequestBody AsContentBanner entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentBannerService.moveUp(entity);

        return R.ok();
    }

    /**
     * 下移
     */
    @SysLog("下移banner管理")
    @PostMapping("/moveDown")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("下移banner管理")
    public R moveDown(@RequestBody AsContentBanner entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentBannerService.moveDown(entity);

        return R.ok();
    }

    /**
     * 置顶
     */
    @SysLog("置顶banner管理")
    @PostMapping("/sticky")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("置顶banner管理")
    public R sticky(@RequestBody AsContentBanner entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsContentBannerService.sticky(entity);

        return R.ok();
    }


    /**
     * 设置已发布 banner管理
     */
    @SysLog("设置已发布 banner管理")
    @PostMapping("/release")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("设置已发布")
    public R release(@RequestBody String[] ids){
        AsContentBanner entity=new AsContentBanner();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(1);
        iAsContentBannerService.setIsRelease(entity,ids);
        return R.ok();
    }

    /**
     * 设置未发布 banner管理
     */
    @SysLog("设置未发布 banner管理")
    @PostMapping("/notRelease")
    @RequiresPermissions("os:banner:update")
    @ApiOperation("设置未发布 产品中心表")
    public R notRelease(@RequestBody String[] ids){

        AsContentBanner entity=new AsContentBanner();
        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        entity.setIsRelease(0);

        iAsContentBannerService.setIsRelease(entity,ids);
        return R.ok();
    }

    private String verifyForm(AsContentBanner e)
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
