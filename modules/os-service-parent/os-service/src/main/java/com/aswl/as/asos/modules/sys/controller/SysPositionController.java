package com.aswl.as.asos.modules.sys.controller;


import cn.hutool.core.collection.CollUtil;
import com.aswl.as.asos.dto.OsPositionDto;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.user.api.dto.PositionDto;
import com.aswl.as.user.api.module.Position;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.aswl.as.asos.modules.sys.entity.SysPositionEntity;
import com.aswl.as.asos.modules.sys.service.SysPositionService;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 职位表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@RestController

@RequestMapping("/sys/position")
@Api("职位表")
public class SysPositionController extends AbstractController {

    @Autowired
    SysPositionService sysPositionService;

    /**
    * 职位表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:position:list")
    @ApiOperation("职位表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= sysPositionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 职位表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:position:list")
    @ApiOperation("职位表列表")
    public R findList(@RequestBody SysPositionEntity entity){
        List<SysPositionEntity> list= sysPositionService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 职位表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:position:info")
    @ApiOperation("职位表信息")
    public R info(@PathVariable("entityId") Long entityId){

        SysPositionEntity role = sysPositionService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存职位表")
    @PostMapping("/save")
    @RequiresPermissions("os:position:save")
    @ApiOperation("保存职位表")
    public R save(@RequestBody SysPositionEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setCreatorId(getUserId().toString());
//        entity.setCreateAt(LocalDateTime.now());
        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        sysPositionService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新职位表")
    @PostMapping("/update")
    @RequiresPermissions("os:position:update")
    @ApiOperation("更新职位表")
    public R update(@RequestBody SysPositionEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        sysPositionService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除职位表
    */
    @SysLog("删除职位表")
    @PostMapping("/delete")
    @RequiresPermissions("os:position:delete")
    @ApiOperation("删除职位表")
    public R delete(@RequestBody String[] ids){
        sysPositionService.deleteEntityByIds(ids);
        return R.ok();
    }

    @GetMapping(value = "positions")
    @ApiOperation(value = "查询所有职位树列表")
    public ResponseBean<List<OsPositionDto>> findAll() {
        SysPositionEntity position = new SysPositionEntity();
//        position.setTenantCode(SysUtil.getTenantCode());
        Stream<SysPositionEntity> postStream = sysPositionService.findList(position).stream();
        if (Optional.ofNullable(postStream).isPresent()) {
            List<OsPositionDto> postTreeList = postStream.map(OsPositionDto::new).collect(Collectors.toList());
            return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(postTreeList, Comparator.comparingInt(OsPositionDto::getSort)),"-1"));
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    private String verifyForm(SysPositionEntity e)
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
