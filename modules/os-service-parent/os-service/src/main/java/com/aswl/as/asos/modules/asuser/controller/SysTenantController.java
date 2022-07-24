package com.aswl.as.asos.modules.asuser.controller;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.aswl.as.asos.common.utils.*;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.modules.asuser.entity.*;
import com.aswl.as.asos.modules.asuser.service.*;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmLevelService;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmOrderHandleService;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.user.api.constant.RoleConstant;
import com.aswl.as.user.api.enums.IdentityType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SAAS租户信息表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
@RestController

@RequestMapping("/tenant/sys-tenant")
@Api("SAAS租户信息表")
public class SysTenantController extends AbstractController {

    @Autowired
    ISysTenantService iSysTenantService;

    @Autowired
    IAsUserSysRoleService iAsUserSysRoleService;

    @Autowired
    IAsUserSysDeptService iAsUserSysDeptService;

    @Autowired
    IAsUserSysPostService iAsUserSysPostService;

    @Autowired
    IAsUserSysPositionService iAsUserSysPositionService;

    @Autowired
    IAsUserSysUserService iAsUserSysUserService;

    @Autowired
    IAsUserSysUserAuthsService iAsUserSysUserAuthsService;

    @Autowired
    IAsUserSysUserRoleService iAsUserSysUserRoleService;

    @Autowired
    IAsUserSysRoleMenuService iAsUserSysRoleMenuService;

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    IAsAlarmLevelService iAsAlarmLevelService;

    @Autowired
    IAsAlarmOrderHandleService iAsAlarmOrderHandleService;


    /**
    * SAAS租户信息表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:tenant:list")
    @ApiOperation("SAAS租户信息列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iSysTenantService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * SAAS租户信息表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:tenant:info")
    @ApiOperation("SAAS租户信息表信息")
    public R info(@PathVariable("entityId") String entityId){

//        System.out.println(iSysTenantService.getRegionListByUsers());;

        SysTenant role = iSysTenantService.getEntityById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
//    @SysLog("保存SAAS租户信息表")
    @PostMapping("/save")
    @RequiresPermissions("os:tenant:save")
    @ApiOperation("保存SAAS租户信息表")
    public R save(@RequestBody SysTenant entity){
        //数据校验
        entity.setId(null);//这里清空id
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setStatus("1");//设置为能用
        entity.setCreator(getUserName());
        entity.setCreateDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));

        try
        {
            iSysTenantService.insertTenantData(entity);

            //如果上面成功插入了user的东西，就再插入ibrs的东西
            iAsAlarmLevelService.newAlarmLevelByTenant(entity.getTenantCode());

            //添加设置
            iAsAlarmOrderHandleService.saveEntityByTenantCode(entity.getTenantCode(),entity.getCreator());

        }
        catch (Exception e)
        {
            return R.error(e.getMessage());
        }

        //需要上传图片

        return R.ok("保存SAAS租户成功");
    }



    //创建

    /**
    * 更新
    */
    @SysLog("更新SAAS租户信息表")
    @PostMapping("/update")
    @RequiresPermissions("os:tenant:update")
    @ApiOperation("更新SAAS租户信息表")
    public R update(@RequestBody SysTenant entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        iSysTenantService.update(entity);

        return R.ok();
    }

    /**
    * 删除SAAS租户信息表
    */
    @SysLog("删除SAAS租户信息表")
    @PostMapping("/delete")
    @RequiresPermissions("os:tenant:delete")
    @ApiOperation("删除SAAS租户信息表")
    public R delete(@RequestBody String[] ids){

        // 查找该租户下是否有未删除的项目，如果有，就不能删除

      /*  for(String id:ids)
        {
            SysTenant temp=iSysTenantService.getEntityById(id);
            if(temp!=null)
            {
                List<AsProject> list=iAsProjectService.getProjectListByTenantCode(temp.getTenantCode());
                if(list!=null&&list.size()>0)
                {
                    return R.error(temp.getTenantName()+" 租户下还有"+list.size()+"个项目还没删除，删除租户信息失败");
                }
            }
        }*/

        iSysTenantService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 启用
     */
    @SysLog("启用SAAS租户信息表")
    @PostMapping("/enableTenant")
    @RequiresPermissions("os:tenant:update")
    @ApiOperation("启用SAAS租户信息表")
    public R enableTenant(@RequestBody SysTenant entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        return R.ok().put("data",iSysTenantService.enableTenant(entity));
    }

    /**
     * 锁定
     */
    @SysLog("锁定SAAS租户信息表")
    @PostMapping("/disableTenant")
    @RequiresPermissions("os:tenant:update")
    @ApiOperation("锁定SAAS租户信息表")
    public R disableTenant(@RequestBody SysTenant entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        return R.ok().put("data",iSysTenantService.disableTenant(entity));
    }

    /**
     *  续期
     */
    @SysLog("续期SAAS租户信息表")
    @PostMapping("/renewTenant")
    @RequiresPermissions("os:tenant:update")
    @ApiOperation("续期SAAS租户信息表")
    public R renewTenant(@RequestBody SysTenant entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));

        return R.ok().put("data",iSysTenantService.renewTenant(entity));
    }


    // 根据最后续费时间来计算能用多少年多少个月,如果12个月的话，就加到1年里面
//    private String getvail


    //这个没有使用
//    @PostMapping("/updateStatus")
//    @RequiresPermissions("os:tenant:save")
//    @ApiOperation(value = "更新SAAS租户的状态")
//    @ApiImplicitParams({@ApiImplicitParam(name = "tenantCodes",value = "用,隔开的租户编码",paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "status",value = "字符串status状态，0-待审核，1-正常，2-审核不通过",paramType = "query", dataType = "String")})
//    public R updateStatus(@RequestParam(value = "tenantCodes", required = true) String tenantCodes,
//                          @RequestParam(value = "status", required = true) String status){
//
//        if(StringUtils.isEmpty(tenantCodes))
//        {
//            return R.error("租户编号不能为空");
//        }
//        if(StringUtils.isEmpty(status))
//        {
//            return R.error("状态不能为空");
//        }
//
//        String[] tenantCodesArr=tenantCodes.split(",");
//        iSysTenantService.updateStatus(tenantCodesArr,status);
//
//        return R.ok("更改状态成功");
//    }

    private String verifyForm(SysTenant e)
    {
        try
        {

            //表单校验
            ValidatorUtils.validateEntity(e);

            String delFlag=e.getDelFlag();
            if(StringUtils.isEmpty(e.getId()))
            {
                //新增
                if( delFlag!=null && !(delFlag.equals("0")||delFlag.equals("1")))
                {
                    return "删除标记设置错误,请设置为 未删除的值0 或 已删除的值1";
                }

                AsUserSysUserAuths tempAuths=iAsUserSysUserAuthsService.getEntityForCheck(e.getOwnerMobile());
                //System.out.println("tempAuths=="+ JSON.toJSONString(tempAuths));
                if(tempAuths!=null)
                {
                    return "已存在负责人手机相同的账号";
                }
            }
            else
            {
                //更改
                if(!(delFlag.equals("0")||delFlag.equals("1")))
                {
                    return "删除标记设置错误,请设置为 未删除的值0 或 已删除的值1";
                }
            }

            if(!StringUtils.isEmpty(e.getOwnerMobile()) && e.getOwnerMobile().length()!=11)
            {
                return "请输入11位手机号";
            }

            if(!StringUtils.isEmpty(e.getNotifyEmail()) && !CommonUtils.isEmail(e.getNotifyEmail()))
            {
                return "请输入正确的邮件地址";
            }

        }
        catch (Exception tempException)
        {
            return tempException.getMessage();
        }

        return null;
    }


    /**
     * SAAS租户信息表列表
     */
    @PostMapping("/findList")
    @RequiresPermissions("os:tenant:list")
    @ApiOperation("SAAS租户信息列表")
    public R findList(@RequestBody SysTenant entity){
        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        //SysTenant temp=new SysTenant();
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL.toString());
        List<SysTenant> list=iSysTenantService.findList(entity);

        return R.ok().put("list", list);
    }

    //TODO 添加 启用 锁定 续期 功能接口，只能在这些接口操作状态，并且记录

}
