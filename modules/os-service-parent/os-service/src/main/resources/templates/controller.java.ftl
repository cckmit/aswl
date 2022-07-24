package ${package.Controller};


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>

@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Api("${table.comment!}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    I${entity}Service i${entity}Service;

    /**
    * ${table.comment!}分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("${table.comment!}分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=i${entity}Service.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * ${table.comment!}列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("${table.comment!}列表")
    public R findList(@RequestBody ${entity} entity){
        List<${entity}> list= i${entity}Service.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * ${table.comment!}信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("${table.comment!}信息")
    public R info(@PathVariable("entityId") Long entityId){

        ${entity} data = i${entity}Service.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存${table.comment!}")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存${table.comment!}")
    public R save(@RequestBody ${entity} entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateName(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        i${entity}Service.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新${table.comment!}")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新${table.comment!}")
    public R update(@RequestBody ${entity} entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        i${entity}Service.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除${table.comment!}
    */
    @SysLog("删除${table.comment!}")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除${table.comment!}")
    public R delete(@RequestBody String[] ids){
        i${entity}Service.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(${entity} e)
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
</#if>
