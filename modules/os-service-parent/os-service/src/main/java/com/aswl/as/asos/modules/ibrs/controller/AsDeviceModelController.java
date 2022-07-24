package com.aswl.as.asos.modules.ibrs.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceModelService;
import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@RestController

@RequestMapping("/ibrs/as-device-model")
@Api("设备型号")
public class AsDeviceModelController extends AbstractController {

    @Autowired
    IAsDeviceModelService iAsDeviceModelService;

    /**
    * 设备型号分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号列表")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page=iAsDeviceModelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 设备型号列表
     */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号列表")
    public R findList(@RequestBody AsDeviceModel deviceModel){
        List<AsDeviceModel> list= iAsDeviceModelService.findList(deviceModel);
        return R.ok().put("list",list);
    }

    /**
    * 设备型号信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("设备型号信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsDeviceModel data = iAsDeviceModelService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备型号")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存设备型号")
    public R save(@RequestBody AsDeviceModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsDeviceModelService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备型号")
    @PostMapping("/update")
   // @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新设备型号")
    public R update(@RequestBody AsDeviceModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsDeviceModelService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备型号
    */
    @SysLog("删除设备型号")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除设备型号")
    public R delete(@RequestBody String[] ids){
        iAsDeviceModelService.deleteEntityByIds(ids);
        return R.ok();
    }

    @PostMapping("/checkDeviceType")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("根据设备型号查询设备型号是否存在")
    public R checkDeviceType(@RequestBody AsDeviceModel entity){
        return R.ok().put("hasDeviceModel",iAsDeviceModelService.checkDeviceModel(entity));
    }

    private String verifyForm(AsDeviceModel e)
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
