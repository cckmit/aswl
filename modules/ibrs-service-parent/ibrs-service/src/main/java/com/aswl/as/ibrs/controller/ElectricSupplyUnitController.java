package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.ElectricSupplyUnitDto;
import com.aswl.as.ibrs.api.module.ElectricSupplyUnit;
import com.aswl.as.ibrs.api.vo.ElectricSupplyUnitVo;
import com.aswl.as.ibrs.api.vo.UnitDeviceVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.ElectricSupplyUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 供电单位controller
 *
 * @author df
 * @date 2021/06/01 20:25
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/electricSupplyUnit", tags = "供电单位")
@RestController
@RequestMapping("/v1/electricSupplyUnit")
public class ElectricSupplyUnitController extends BaseController {

    private final ElectricSupplyUnitService electricSupplyUnitService;

    /**
     * 根据ID获取供电单位
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据供电单位ID查询供电单位")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "供电单位ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ElectricSupplyUnit> findById(@PathVariable("id") String id) {
        ElectricSupplyUnit electricSupplyUnit = new ElectricSupplyUnit();
        electricSupplyUnit.setId(id);
        return new ResponseBean<>(electricSupplyUnitService.get(electricSupplyUnit));
    }

    /**
     * 查询所有供电单位
     *
     * @param electricSupplyUnit
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有供电单位列表")
    @ApiImplicitParam(name = "electricSupplyUnit", value = "供电单位对象", paramType = "path", required = true, dataType = "electricSupplyUnit")
    @GetMapping(value = "electricSupplyUnits")
    public ResponseBean
            <List<ElectricSupplyUnit>> findAll(ElectricSupplyUnit electricSupplyUnit) {
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
        }
        electricSupplyUnit.setProjectId(projectId);
        electricSupplyUnit.setTenantCode(tenantCode);
        return new ResponseBean<>(electricSupplyUnitService.findList(electricSupplyUnit));
    }

    /**
     * 分页查询供电单位列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param electricSupplyUnit
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询供电单位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "electricSupplyUnit", value = "供电单位信息", dataType = "electricSupplyUnit")
    })

    @GetMapping("electricSupplyUnitList")
    public ResponseBean
            <PageInfo<ElectricSupplyUnit>> electricSupplyUnitList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                  ElectricSupplyUnit electricSupplyUnit) {
        electricSupplyUnit.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(electricSupplyUnitService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), electricSupplyUnit));
    }

    /**
     * 新增供电单位
     *
     * @param electricSupplyUnitDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增供电单位", notes = "新增供电单位")
    @Log(value = "新增供电单位", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertElectricSupplyUnit(@RequestBody @Valid ElectricSupplyUnitDto electricSupplyUnitDto) {
        ElectricSupplyUnit electricSupplyUnit = new ElectricSupplyUnit();
        BeanUtils.copyProperties(electricSupplyUnitDto, electricSupplyUnit);
        electricSupplyUnit.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricSupplyUnitService.insert(electricSupplyUnit) > 0);
    }

    /**
     * 修改供电单位
     *
     * @param electricSupplyUnitDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新供电单位信息", notes = "根据供电单位ID更新供电单位信息")
    @Log(value = "更新供电单位", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateElectricSupplyUnit(@RequestBody @Valid ElectricSupplyUnitDto electricSupplyUnitDto) {
        ElectricSupplyUnit electricSupplyUnit = new ElectricSupplyUnit();
        BeanUtils.copyProperties(electricSupplyUnitDto, electricSupplyUnit);
        electricSupplyUnit.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricSupplyUnitService.update(electricSupplyUnit) > 0);
    }

    /**
     * 根据供电单位ID删除供电单位信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除供电单位信息", notes = "根据供电单位ID删除供电单位信息")
    @ApiImplicitParam(name = "id", value = "供电单位ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除供电单位", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteElectricSupplyUnitById(@PathVariable("id") String id) {
        ElectricSupplyUnit electricSupplyUnit = new ElectricSupplyUnit();
        electricSupplyUnit.setId(id);
        electricSupplyUnit.setNewRecord(false);
        electricSupplyUnit.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricSupplyUnitService.delete(electricSupplyUnit) > 0);
    }

    /**
     * 批量删除供电单位信息
     *
     * @param electricSupplyUnit
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除供电单位", notes = "根据供电单位ID批量删除供电单位")
    @ApiImplicitParam(name = "electricSupplyUnit", value = "供电单位信息", dataType = "electricSupplyUnit")
    @Log(value = "删除供电单位", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllElectricSupplyUnit(@RequestBody ElectricSupplyUnit electricSupplyUnit) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(electricSupplyUnit.getIdString()))
                success = electricSupplyUnitService.deleteAll(electricSupplyUnit.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除供电单位失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 分页查询供电单位列表数据
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param electricSupplyUnitDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询供电单位列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "electricSupplyUnitDto", value = "供电单位信息", dataType = "electricSupplyUnitDto")
    })

    @GetMapping("getElectricSupplyUnitList")
    public ResponseBean
            <PageInfo<ElectricSupplyUnitVo>> getElectricSupplyUnitList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                    ElectricSupplyUnitDto electricSupplyUnitDto) {
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
        }
        electricSupplyUnitDto.setTenantCode(SysUtil.getTenantCode());
        electricSupplyUnitDto.setProjectId(projectId);
        return new ResponseBean<>(electricSupplyUnitService.getElectriSupplyUnitPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), electricSupplyUnitDto));
    }


    /**
     * 根据单位ID查询点位用电量
     *
     * @param unitId
     * @param deviceName
     * @param startTime
     * @param endTime
     * @return ResponseBean
     */
    @GetMapping("findDetails")
    @ApiOperation(value = "根据单位ID查询点位用电量", notes = "根据单位ID查询点位用电量")
    public ResponseBean
            <List<UnitDeviceVo>> findByUnitId(@RequestParam("unitId") String unitId ,@RequestParam(value = "deviceName",required = false) String deviceName, @RequestParam("startTime") String startTime,@RequestParam("endTime") String endTime) {
        return new ResponseBean<>(electricSupplyUnitService.findByUnitId(unitId,deviceName,startTime,endTime));
    }
}
