package com.aswl.as.asos.modules.sys.controller;


import cn.hutool.core.collection.CollUtil;
import com.aswl.as.asos.dto.OsDeptDto;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.user.api.dto.DeptDto;
import com.aswl.as.user.api.module.Dept;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.aswl.as.asos.modules.sys.service.SysDeptService;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-09
 */
@RestController

@RequestMapping("/sys/dept")
@Api("部门表")
public class SysDeptController extends AbstractController {

    @Autowired
    SysDeptService sysDeptService;

    /**
    * 部门表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:dept:list")
    @ApiOperation("部门表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= sysDeptService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 部门表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:dept:list")
    @ApiOperation("部门表列表")
    public R findList(@RequestBody SysDeptEntity entity){
        List<SysDeptEntity> list= sysDeptService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
     * 查询树形部门集合
     *
     * @return List
     * @author aswl.com
     * @date 2018/10/25 12:57
     */
    @GetMapping(value = "depts")
    @ApiOperation(value = "获取部门列表")
    public ResponseBean<List<OsDeptDto>> depts() {
        SysDeptEntity dept = new SysDeptEntity();
        // 查询部门集合
        Stream<SysDeptEntity> deptStream = sysDeptService.findList(dept).stream();
        if (Optional.ofNullable(deptStream).isPresent()) {
            List<OsDeptDto> deptTreeList = deptStream.map(OsDeptDto::new).collect(Collectors.toList());
            // 排序、构建树形结构
            return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(deptTreeList, Comparator.comparingInt(OsDeptDto::getSort)), "-1"));
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
    * 部门表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:dept:info")
    @ApiOperation("部门表信息")
    public R info(@PathVariable("entityId") Long entityId){

        SysDeptEntity role = sysDeptService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存部门表")
    @PostMapping("/save")
    @RequiresPermissions("os:dept:save")
    @ApiOperation("保存部门表")
    public R save(@RequestBody SysDeptEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        sysDeptService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新部门表")
    @PostMapping("/update")
    @RequiresPermissions("os:dept:update")
    @ApiOperation("更新部门表")
    public R update(@RequestBody SysDeptEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        sysDeptService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除部门表
    */
    @SysLog("删除部门表")
    @PostMapping("/delete")
    @RequiresPermissions("os:dept:delete")
    @ApiOperation("删除部门表")
    public R delete(@RequestBody String[] ids){
        sysDeptService.deleteEntityByIds(ids);
        return R.ok();
    }

    private String verifyForm(SysDeptEntity e)
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
