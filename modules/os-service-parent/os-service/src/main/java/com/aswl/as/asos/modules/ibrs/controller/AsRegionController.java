package com.aswl.as.asos.modules.ibrs.controller;


import cn.hutool.core.collection.CollUtil;
import com.aswl.as.asos.dto.OsProjectDto;
import com.aswl.as.asos.dto.OsRegionDto;
import com.aswl.as.asos.dto.OsSysTenantDto;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.asos.modules.asuser.service.ISysTenantService;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.ibrs.entity.AsRegion;
import com.aswl.as.asos.modules.ibrs.service.IAsRegionService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-11
 */
@RestController

@RequestMapping("/ibrs/as-region")
@Api("区域表")
public class AsRegionController extends AbstractController {

    @Autowired
    ISysTenantService iSysTenantService;

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    IAsRegionService iAsRegionService;

    /**
    * 区域表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:region:list")
    @ApiOperation("区域表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsRegionService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 区域表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:region:list")
    @ApiOperation("区域表列表")
    public R findList(@RequestBody AsRegion entity){
        List<AsRegion> list= iAsRegionService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 区域表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:region:info")
    @ApiOperation("区域表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsRegion data = iAsRegionService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存区域表")
    @PostMapping("/save")
    @RequiresPermissions("os:region:save")
    @ApiOperation("保存区域表")
    public R save(@RequestBody AsRegion entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsRegionService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新区域表")
    @PostMapping("/update")
    @RequiresPermissions("os:region:update")
    @ApiOperation("更新区域表")
    public R update(@RequestBody AsRegion entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsRegionService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除区域表
    */
    @SysLog("删除区域表")
    @PostMapping("/delete")
    @RequiresPermissions("os:region:delete")
    @ApiOperation("删除区域表")
    public R delete(@RequestBody String[] ids){
        iAsRegionService.deleteEntityByIds(ids);
        return R.ok();
    }

    // --------------------  获取所有项目 所有区域列表 -----


    //
    //

    @GetMapping(value = "getRegionTree")
    @RequiresPermissions("os:region:list")
    @ApiOperation(value = "获取区域列表")
    public ResponseBean<List<OsRegionDto>> getRegionTree(
            @RequestParam(value = "tenantCode",required = false)String tenantCode,
            @RequestParam(value = "projectId",required = false)String projectId)
    {
        String parentId="-1";
        AsRegion region = new AsRegion();
        region.setApplicationCode(SysUtil.getSysCode());

        //        region.setTenantCode(SysUtil.getTenantCode());

        //暂时显示所有的
        /*
        if (SysUtil.getUser().equalsIgnoreCase("admin")){
            regionCode="";
        }else {
            region.setRegionCode(regionCode);
            parentId = iAsRegionService.getRegionByRegionCode(regionCode);// regionService.findRegionId(regionCode); //
        }
        */

        if(!StringUtils.isEmpty(projectId))
        {
            //没有删除的区域
            region.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
//            region.setTenantCode(tenantCode);
            region.setProjectId(projectId);

            // 查询部门集合
            Stream<AsRegion> rsStream = iAsRegionService.findList(region).stream(); // regionService.findList(region).stream(); //
            List<OsRegionDto> regionDtoList=new ArrayList<>();
            if (Optional.ofNullable(rsStream).isPresent()) {
                List<OsRegionDto> treeList = rsStream.map(OsRegionDto::new).collect(Collectors.toList());

                // 排序、构建树形结构
                return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsRegionDto::getSort)), parentId));
            }
        }

        return new ResponseBean<>(new ArrayList<>());
    }


    /**
     * 查询树形区域集合
     * @return
     */
    @GetMapping(value = "tree")
   // @RequiresPermissions("os:region:list")
    @ApiOperation(value = "获取区域列表")
    public ResponseBean<List<OsSysTenantDto>> regions(@RequestParam(value = "regionCode",required = false) String regionCode) {

        String parentId="-1";
        AsRegion region = new AsRegion();
        region.setApplicationCode(SysUtil.getSysCode());

        //没有删除的区域
        region.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);

        // 查询部门集合
        Stream<AsRegion> rsStream = iAsRegionService.findList(region).stream(); // regionService.findList(region).stream(); //
        List<OsRegionDto> regionDtoList=new ArrayList<>();
        if (Optional.ofNullable(rsStream).isPresent()) {
            List<OsRegionDto> treeList = rsStream.map(OsRegionDto::new).collect(Collectors.toList());
            // 排序、构建树形结构
            regionDtoList=TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(OsRegionDto::getSort)), parentId);
        }

        // 设置数据
        //获取所有的租户和项目
        SysTenant tenant=new SysTenant();
        tenant.setDelFlag(CommonConstant.DEL_FLAG_NORMAL.toString());
        List<SysTenant> tenantList=iSysTenantService.findList(tenant);
        Stream<SysTenant>tStream =tenantList.stream();
        List<OsSysTenantDto> tenantDtoList=new ArrayList<OsSysTenantDto>();
        if (Optional.ofNullable(tStream).isPresent()) {
            tenantDtoList=tStream.map(OsSysTenantDto::new).collect(Collectors.toList());
        }

        //项目
        Stream<AsProject> pStream=iAsProjectService.findList(new AsProject()).stream();
        List<OsProjectDto> projectDtoList=new ArrayList<OsProjectDto>();
        if (Optional.ofNullable(pStream).isPresent()) {
            projectDtoList=pStream.map(OsProjectDto::new).collect(Collectors.toList());
        }

        //设置对应的数据
        for(OsRegionDto r:regionDtoList)
        {
            for(OsProjectDto p:projectDtoList)
            {
                if(p.getProjectId().equals(r.getProjectId()))
                {
                    p.add(r);
                    //r.setParent(p); //这句应该不用
                    break; //退出项目循环
                }
            }
        }

        for(OsProjectDto p:projectDtoList)
        {
            for(OsSysTenantDto t:tenantDtoList)
            {
                if(t.getTenantCode().equals(p.getTenantCode()))
                {
                    t.add(p);
                    break; //退出租户循环
                }
            }
        }

        return new ResponseBean<>(tenantDtoList);
    }



    private String verifyForm(AsRegion e)
    {

        try
        {
            //表单校验
            ValidatorUtils.validateEntity(e);

            if(StringUtils.isEmpty(e.getTenantCode()))
            {
                throw new Exception("需要传递租户编码");
            }

            if(StringUtils.isEmpty(e.getProjectId()))
            {
                throw new Exception("需要传递项目ID");
            }

            if("-1".equals(e.getParentId()))
            {
                AsRegion temp=new AsRegion();
                temp.setProjectId(e.getProjectId());
                temp.setParentId("-1");
                List<AsRegion> list= iAsRegionService.findList(temp);
                if(list!=null&&list.size()>0)
                {
                    if(StringUtils.isBlank(e.getId())
                            || !list.get(0).getId().equals(e.getId())
                    )
                    {
                        throw new Exception("一个项目下，只能有一个顶端区域");
                    }
                }
            }

//            List<> list= iAsRegionService.findList(temp);


        }
        catch (Exception tempException)
        {
            return tempException.getMessage();
        }

        return null;
    }


}
