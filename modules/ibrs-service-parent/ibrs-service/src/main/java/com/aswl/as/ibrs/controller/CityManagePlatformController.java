package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.CityManagePlatformDto;
import com.aswl.as.ibrs.api.module.CityManagePlatform;
import com.aswl.as.ibrs.api.vo.CityManagePlatformVo;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.CityManagePlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 智能箱管理平台数据表controller
 *
 * @author df
 * @date 2021/07/26 14:33
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/cityManagePlatform", tags = "智能箱管理平台数据表")
@RestController
@RequestMapping("/v1/cityManagePlatform")
public class CityManagePlatformController extends BaseController {

    private final CityManagePlatformService cityManagePlatformService;

    /**
     * 根据ID获取智能箱管理平台数据表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据智能箱管理平台数据表ID查询智能箱管理平台数据表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "智能箱管理平台数据表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CityManagePlatform> findById(@PathVariable("id") String id) {
        CityManagePlatform cityManagePlatform = new CityManagePlatform();
        cityManagePlatform.setId(id);
        return new ResponseBean<>(cityManagePlatformService.get(cityManagePlatform));
    }

    /**
     * 查询所有智能箱管理平台数据表
     *
     * @param cityManagePlatform
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有智能箱管理平台数据表列表")
    @ApiImplicitParam(name = "cityManagePlatform", value = "智能箱管理平台数据表对象", paramType = "path", required = true, dataType = "cityManagePlatform")
    @GetMapping(value = "cityManagePlatforms")
    public ResponseBean
            <List<CityManagePlatform>> findAll(CityManagePlatform cityManagePlatform) {
        cityManagePlatform.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityManagePlatformService.findList(cityManagePlatform));
    }

    /**
     * 分页查询智能箱管理平台数据表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param cityManagePlatformDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询智能箱管理平台数据表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityManagePlatform", value = "智能箱管理平台数据表信息", dataType = "cityManagePlatform")
    })

    @GetMapping("cityManagePlatformList")
    public ResponseBean
            <PageInfo<CityManagePlatformVo>> cityManagePlatformList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                    CityManagePlatformDto cityManagePlatformDto) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {

        }
        cityManagePlatformDto.setTenantCode(tenantCode);
        cityManagePlatformDto.setProjectId(projectId);
        return new ResponseBean<>(cityManagePlatformService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), cityManagePlatformDto));
    }

    /**
     * 新增智能箱管理平台数据表
     *
     * @param cityManagePlatformDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增智能箱管理平台数据表", notes = "新增智能箱管理平台数据表")
    @Log(value = "新增智能箱管理平台数据表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertCityManagePlatform(@RequestBody @Valid CityManagePlatformDto cityManagePlatformDto) {
        CityManagePlatform cityManagePlatform = new CityManagePlatform();
        BeanUtils.copyProperties(cityManagePlatformDto, cityManagePlatform);
        cityManagePlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityManagePlatformService.insert(cityManagePlatform) > 0);
    }

    /**
     * 修改智能箱管理平台数据表
     *
     * @param cityManagePlatformDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新智能箱管理平台数据表信息", notes = "根据智能箱管理平台数据表ID更新智能箱管理平台数据表信息")
    @Log(value = "更新智能箱管理平台数据表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateCityManagePlatform(@RequestBody @Valid CityManagePlatformDto cityManagePlatformDto) {
        CityManagePlatform cityManagePlatform = new CityManagePlatform();
        BeanUtils.copyProperties(cityManagePlatformDto, cityManagePlatform);
        cityManagePlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityManagePlatformService.update(cityManagePlatform) > 0);
    }

    /**
     * 根据智能箱管理平台数据表ID删除智能箱管理平台数据表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除智能箱管理平台数据表信息", notes = "根据智能箱管理平台数据表ID删除智能箱管理平台数据表信息")
    @ApiImplicitParam(name = "id", value = "智能箱管理平台数据表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除智能箱管理平台数据表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteCityManagePlatformById(@PathVariable("id") String id) {
        CityManagePlatform cityManagePlatform = new CityManagePlatform();
        cityManagePlatform.setId(id);
        cityManagePlatform.setNewRecord(false);
        cityManagePlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityManagePlatformService.delete(cityManagePlatform) > 0);
    }

    /**
     * 批量删除智能箱管理平台数据表信息
     *
     * @param cityManagePlatform
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除智能箱管理平台数据表", notes = "根据智能箱管理平台数据表ID批量删除智能箱管理平台数据表")
    @ApiImplicitParam(name = "cityManagePlatform", value = "智能箱管理平台数据表信息", dataType = "cityManagePlatform")
    @Log(value = "删除智能箱管理平台数据表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllCityManagePlatform(@RequestBody CityManagePlatform cityManagePlatform) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(cityManagePlatform.getIdString()))
                success = cityManagePlatformService.deleteAll(cityManagePlatform.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除智能箱管理平台数据表失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 批量新增智能箱管理平台数据表
     *
     * @param list list
     * @return ResponseBean
     * @author aswl.com
     */
    @PostMapping(value = "insertBath")
    @ApiOperation(value = "批量新增智能箱管理平台数据表", notes = "批量新增智能箱管理平台数据表")
    @ApiImplicitParam(name = "list", value = "批量新增智能箱管理平台数据表", required = true, dataType = "CityManagePlatform")
    @Log(value = "批量新增智能箱管理平台数据表", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertBath(@RequestBody List<CityManagePlatform> list) {
        return new ResponseBean<>(cityManagePlatformService.insertBath(list) > 0);
    }
}
