package com.aswl.as.asos.modules.ibrs.controller;


import cn.hutool.core.collection.CollUtil;
import com.aswl.as.asos.dto.OsCategoryTree;
import com.aswl.as.asos.dto.OsDeptDto;
import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.TreeUtil;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.aswl.as.asos.modules.ibrs.entity.AsCategoryTree;
import com.aswl.as.asos.modules.ibrs.service.IAsCategoryTreeService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 通用分类树表，普通的树可以直接用一个type辨别来获取 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-03
 */
@RestController

@RequestMapping("/ibrs/as-category-tree")
@Api("通用分类树表，普通的树可以直接用一个type辨别来获取")
public class AsCategoryTreeController extends AbstractController {

    @Autowired
    IAsCategoryTreeService iAsCategoryTreeService;

    /**
    * 通用分类树表，普通的树可以直接用一个type辨别来获取分页列表
    */
    @GetMapping("/list")
//    @RequiresPermissions("os:content")
    @ApiOperation("通用分类树表，普通的树可以直接用一个type辨别来获取分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsCategoryTreeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 通用分类树表，普通的树可以直接用一个type辨别来获取列表
    */
    @PostMapping("/findList")
//    @RequiresPermissions("os:content")
    @ApiOperation("通用分类树表，普通的树可以直接用一个type辨别来获取列表")
    public R findList(@RequestBody AsCategoryTree entity){
        List<AsCategoryTree> list= iAsCategoryTreeService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 通用分类树表，普通的树可以直接用一个type辨别来获取信息
    */
    @GetMapping("/info/{entityId}")
//    @RequiresPermissions("os:content")
    @ApiOperation("通用分类树表，普通的树可以直接用一个type辨别来获取信息")
    public R info(@PathVariable("entityId") String entityId){

        AsCategoryTree data = iAsCategoryTreeService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存通用分类树表，普通的树可以直接用一个type辨别来获取")
    @PostMapping("/save")
//    @RequiresPermissions("os:content")
    @ApiOperation("保存通用分类树表，普通的树可以直接用一个type辨别来获取")
    public R save(@RequestBody AsCategoryTree entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsCategoryTreeService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新通用分类树表，普通的树可以直接用一个type辨别来获取")
    @PostMapping("/update")
//    @RequiresPermissions("os:content")
    @ApiOperation("更新通用分类树表，普通的树可以直接用一个type辨别来获取")
    public R update(@RequestBody AsCategoryTree entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsCategoryTreeService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除通用分类树表，普通的树可以直接用一个type辨别来获取
    */
    @SysLog("删除通用分类树表，普通的树可以直接用一个type辨别来获取")
    @PostMapping("/delete")
//    @RequiresPermissions("os:content")
    @ApiOperation("删除通用分类树表，普通的树可以直接用一个type辨别来获取")
    public R delete(@RequestBody String[] ids){
        iAsCategoryTreeService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 查询树形分类集合
     *
     * @return List
     * @author aswl.com
     * @date 2018/10/25 12:57
     */
    @GetMapping(value = "/trees/{category}")
    @ApiOperation(value = "获取树形分类列表")
    public ResponseBean<List<OsCategoryTree>> trees(@PathVariable("category")Integer category) {
        if(category!=null)
        {
            // 查询树形分类集合
            AsCategoryTree temp=new AsCategoryTree();
            temp.setCategory(category);
            Stream<AsCategoryTree> stream=iAsCategoryTreeService.findList(temp).stream();
            if (Optional.ofNullable(stream).isPresent()) {
                List<OsCategoryTree> treeList = stream.map(OsCategoryTree::new).collect(Collectors.toList());
                // 排序、构建树形结构
                return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsCategoryTree::getSort)), "-1"));
            }
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    private String verifyForm(AsCategoryTree e)
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
