package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ProjectModelDto;
import com.aswl.as.ibrs.api.module.ProjectModel;
import com.aswl.as.ibrs.api.vo.ProjectModelVo;
import com.aswl.as.ibrs.service.ProjectModelService;
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
 * 项目型号关联表controller
 *
 * @author df
 * @date 2021/08/11 14:14
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/projectModel", tags = "项目型号关联表")
@RestController
@RequestMapping("/v1/projectModel")
public class ProjectModelController extends BaseController {

    private final ProjectModelService projectModelService;

    /**
     * 根据ID获取项目型号关联表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据项目型号关联表ID查询项目型号关联表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "项目型号关联表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ProjectModel> findById(@PathVariable("id") String id) {
        ProjectModel projectModel = new ProjectModel();
        projectModel.setId(id);
        return new ResponseBean<>(projectModelService.get(projectModel));
    }

    /**
     * 查询所有项目型号关联表
     *
     * @param projectModel
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有项目型号关联表列表")
    @ApiImplicitParam(name = "projectModel", value = "项目型号关联表对象", paramType = "path", required = true, dataType = "projectModel")
    @GetMapping(value = "projectModels")
    public ResponseBean
            <List<ProjectModel>> findAll(ProjectModel projectModel) {
        projectModel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(projectModelService.findList(projectModel));
    }

    /**
     * 分页查询项目型号关联表列表
     *
     * @param pageNum
     * @param pageSize
     * @param projectModelDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询项目型号关联表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectModelDto", value = "项目型号关联表信息", dataType = "ProjectModelDto")
    })

    @GetMapping("projectModelList")
    public ResponseBean
            <PageInfo<ProjectModelVo>> projectModelList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                        ProjectModelDto projectModelDto) {
        return new ResponseBean<>(projectModelService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), projectModelDto));
    }

    /**
     * 新增项目型号关联表
     *
     * @param projectModelDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增项目型号关联表", notes = "新增项目型号关联表")
    @Log(value = "新增项目型号关联表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertProjectModel(@RequestBody @Valid ProjectModelDto projectModelDto) {
        ProjectModel projectModel = new ProjectModel();
        BeanUtils.copyProperties(projectModelDto, projectModel);
        projectModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(projectModelService.insert(projectModel) > 0);
    }

    /**
     * 修改项目型号关联表
     *
     * @param projectModelDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新项目型号关联表信息", notes = "根据项目型号关联表ID更新项目型号关联表信息")
    @Log(value = "更新项目型号关联表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateProjectModel(@RequestBody @Valid ProjectModelDto projectModelDto) {
        ProjectModel projectModel = new ProjectModel();
        BeanUtils.copyProperties(projectModelDto, projectModel);
        projectModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(projectModelService.update(projectModel) > 0);
    }

    /**
     * 根据项目型号关联表ID删除项目型号关联表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除项目型号关联表信息", notes = "根据项目型号关联表ID删除项目型号关联表信息")
    @ApiImplicitParam(name = "id", value = "项目型号关联表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除项目型号关联表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteProjectModelById(@PathVariable("id") String id) {
        ProjectModel projectModel = new ProjectModel();
        projectModel.setId(id);
        projectModel.setNewRecord(false);
        projectModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(projectModelService.delete(projectModel) > 0);
    }

    /**
     * 批量删除项目型号关联表信息
     *
     * @param projectModel
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除项目型号关联表", notes = "根据项目型号关联表ID批量删除项目型号关联表")
    @ApiImplicitParam(name = "projectModel", value = "项目型号关联表信息", dataType = "projectModel")
    @Log(value = "删除项目型号关联表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllProjectModel(@RequestBody ProjectModel projectModel) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(projectModel.getIdString()))
                success = projectModelService.deleteAll(projectModel.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除项目型号关联表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量保存项目型号关联表信息
     *
     * @param dto
     * @return ResponseBean
     */

    @ApiOperation(value = "批量保存项目型号关联表信息")
    @ApiImplicitParam(name = "dto", value = "项目型号关联表对象", paramType = "body", required = true, dataType = "ProjectModelDto")
    @PostMapping(value = "insertBath")
    public ResponseBean
            <Boolean> insertBath(@RequestBody  ProjectModelDto dto) {
        // 先删除原数据
        projectModelService.deleteByProjectId(dto.getProjectId());
        if (dto.getList()==null || dto.getList().size()==0){
            return new ResponseBean<>(Boolean.TRUE);
        }else {
            // 新增数据
            return new ResponseBean<>(projectModelService.insertBath(dto.getList()) > 0);
        }
    }
}
