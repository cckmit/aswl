package com.aswl.as.user.controller;

import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.user.api.dto.RoleRegionDto;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.api.module.RoleRegion;
import com.aswl.as.user.api.vo.RegionRoleVo;
import com.aswl.as.user.service.RoleRegionService;
import com.aswl.as.user.service.RoleService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 区域角色controller
 *
 * @author dingfei
 * @date 2019-10-15 13:56
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/roleRegion", tags = "区域角色")
@RestController
@RequestMapping("/v1/roleRegion")
public class RoleRegionController extends BaseController {
    private final RoleRegionService roleRegionService;
    private final RoleService roleService;
    private RegionServiceClient regionServiceClient;

    /**
     * 根据ID获取区域角色
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据区域角色ID查询区域角色")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "区域角色ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<RoleRegion> findById(@PathVariable("id") String id) {
        RoleRegion roleRegion = new RoleRegion();
        roleRegion.setId(id);
        return new ResponseBean<>(roleRegionService.get(roleRegion));
    }

    /**
     * 查询所有区域角色
     *
     * @param roleRegion
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有区域角色列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "roleRegion", value = "区域角色对象", paramType = "path", required = true, dataType = "roleRegion"))
    @GetMapping(value = "roleRegions")
    public ResponseBean
            <List<RoleRegion>> findAll(RoleRegion roleRegion) {
        roleRegion.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(roleRegionService.findAllList(roleRegion));
    }

    /**
     * 分页查询区域角色列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param roleRegion
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询区域角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "roleRegion", value = "区域角色信息", dataType = "roleRegion")
    })

    @GetMapping("roleRegionList")
    public PageInfo<RoleRegion> roleRegionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               RoleRegion roleRegion) {
        roleRegion.setTenantCode(SysUtil.getTenantCode());
        return roleRegionService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), roleRegion);
    }

    /**
     * 新增区域角色
     *
     * @param roleRegionDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增区域角色", notes = "新增区域角色")
    @ApiImplicitParams(@ApiImplicitParam(name = "roleRegionDto", value = "设备dto", required = true, paramType = "body", dataType = "roleRegionDto"))
    @PostMapping
    @Log(value = "新增区域角色",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertRoleRegion(@RequestBody @Valid RoleRegionDto roleRegionDto) {
        RoleRegion roleRegion = new RoleRegion();
        BeanUtils.copyProperties(roleRegionDto, roleRegion);
        roleRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(roleRegionService.insert(roleRegion) > 0);
    }

    /**
     * 修改区域角色
     *
     * @param roleRegionDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新区域角色信息", notes = "根据区域角色ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "roleRegionDto", value = "设备dto", required = true, paramType = "body", dataType = "roleRegionDto"))
    @Log(value = "修改区域角色",businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseBean
            <Boolean> updateRoleRegion(@RequestBody @Valid RoleRegionDto roleRegionDto) {
        RoleRegion roleRegion = new RoleRegion();
        BeanUtils.copyProperties(roleRegionDto, roleRegion);
        roleRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(roleRegionService.update(roleRegion) > 0);
    }

    /**
     * 根据区域角色ID删除区域角色信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除区域角色信息", notes = "根据区域角色ID删除区域角色信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "区域角色ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    @Log(value = "删除区域角色",businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteRoleRegionById(@PathVariable("id") String id) {
        RoleRegion roleRegion = new RoleRegion();
        roleRegion.setId(id);
        roleRegion.setNewRecord(false);
        roleRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(roleRegionService.delete(roleRegion) > 0);
    }

    /**
     * 批量删除区域角色信息
     *
     * @param roleRegion
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除区域角色", notes = "根据区域角色ID批量删除区域角色")
    @ApiImplicitParam(name = "roleRegion", value = "区域角色信息", dataType = "roleRegion")
    @PostMapping("deleteAll")
    @Log(value = "批量删除区域角色",businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllRoleRegion(@RequestBody RoleRegion roleRegion) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(roleRegion.getIdString()))
                success = roleRegionService.deleteAll(roleRegion.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除区域角色失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 根据角色查找区域菜单
     *
     * @param roleCode 角色code
     * @return 属性集合
     */
    @GetMapping("regionTree/{roleCode}")
    @ApiOperation(value = "根据角色查找区域菜单", notes = "根据角色code获取区域菜单")
    @ApiImplicitParam(name = "roleCode", value = "角色code", required = true, dataType = "String", paramType = "path")
    public ResponseBean<List<RegionRoleVo>> roleTree(@PathVariable String roleCode) {
        List<RegionRoleVo> regionRoleVos = roleRegionService.findRegionByRole(roleCode, SysUtil.getTenantCode());
        if (null != regionRoleVos && regionRoleVos.size()>0){
            for (RegionRoleVo vo:regionRoleVos) {
                Region region = regionServiceClient.getRegionById(vo.getRegionId());
                vo.setRoleName(region.getRegionName());
            }
        }
        return new ResponseBean<>(regionRoleVos);
    }


    /**
     * 更新角色区域
     *
     * @param role role
     * @return ResponseBean
     * @author dingfei
     * @date 2019/10/15 10:00
     */
    @PutMapping("roleRegionUpdate")
    @ApiOperation(value = "更新角色区域信息", notes = "更新角色区域信息")
    @ApiImplicitParam(name = "role", value = "角色实体role", required = true, dataType = "RoleVo")
    @Log("更新角色区域")
    public ResponseBean<Boolean> updateRoleRegion(@RequestBody Role role) {
        boolean success = false;
        String menuIds = role.getMenuIds();
        if (StringUtils.isNotBlank(role.getId())) {
            role = roleService.get(role);
            // 保存角色区域关系
            if (role != null)
                success = roleRegionService.saveRoleRegions(role.getId(), Stream.of(menuIds.split(",")).collect(Collectors.toList())) > 0;
        }
        return new ResponseBean<>(success);
    }

}
