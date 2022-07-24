package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.AssetsCategoryDto;
import com.aswl.as.ibrs.api.module.AssetsCategory;
import com.aswl.as.ibrs.service.AssetsCategoryService;
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
 * 资产分类controller
 *
 * @author df
 * @date 2022/01/14 15:51
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/assetsCategory", tags = "资产分类")
@RestController
@RequestMapping("/v1/assetsCategory")
public class AssetsCategoryController extends BaseController {

    private final AssetsCategoryService assetsCategoryService;

    /**
     * 根据ID获取资产分类
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据资产分类ID查询资产分类")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "资产分类ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AssetsCategory> findById(@PathVariable("id") String id) {
        AssetsCategory assetsCategory = new AssetsCategory();
        assetsCategory.setId(id);
        return new ResponseBean<>(assetsCategoryService.get(assetsCategory));
    }

    /**
     * 查询所有资产分类
     *
     * @param assetsCategory
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有资产分类列表")
    @ApiImplicitParam(name = "assetsCategory", value = "资产分类对象", paramType = "path", required = true, dataType = "assetsCategory")
    @GetMapping(value = "assetsCategorys")
    public ResponseBean
            <List<AssetsCategory>> findAll(AssetsCategory assetsCategory) {
        assetsCategory.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsCategoryService.findList(assetsCategory));
    }

    /**
     * 分页查询资产分类列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param assetsCategory
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询资产分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "assetsCategory", value = "资产分类信息", dataType = "assetsCategory")
    })

    @GetMapping("assetsCategoryList")
    public ResponseBean
            <PageInfo<AssetsCategory>> assetsCategoryList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                          @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                          @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                          AssetsCategory assetsCategory) {
        assetsCategory.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsCategoryService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), assetsCategory));
    }

    /**
     * 新增资产分类
     *
     * @param assetsCategoryDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增资产分类", notes = "新增资产分类")
    @Log(value = "新增资产分类", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertAssetsCategory(@RequestBody @Valid AssetsCategoryDto assetsCategoryDto) {
        AssetsCategory assetsCategory = new AssetsCategory();
        BeanUtils.copyProperties(assetsCategoryDto, assetsCategory);
        assetsCategory.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsCategoryService.insert(assetsCategory) > 0);
    }

    /**
     * 修改资产分类
     *
     * @param assetsCategoryDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新资产分类信息", notes = "根据资产分类ID更新资产分类信息")
    @Log(value = "更新资产分类", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateAssetsCategory(@RequestBody @Valid AssetsCategoryDto assetsCategoryDto) {
        AssetsCategory assetsCategory = new AssetsCategory();
        BeanUtils.copyProperties(assetsCategoryDto, assetsCategory);
        assetsCategory.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsCategoryService.update(assetsCategory) > 0);
    }

    /**
     * 根据资产分类ID删除资产分类信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除资产分类信息", notes = "根据资产分类ID删除资产分类信息")
    @ApiImplicitParam(name = "id", value = "资产分类ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除资产分类", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAssetsCategoryById(@PathVariable("id") String id) {
        AssetsCategory assetsCategory = new AssetsCategory();
        assetsCategory.setId(id);
        assetsCategory.setNewRecord(false);
        assetsCategory.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsCategoryService.delete(assetsCategory) > 0);
    }

    /**
     * 批量删除资产分类信息
     *
     * @param assetsCategory
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除资产分类", notes = "根据资产分类ID批量删除资产分类")
    @ApiImplicitParam(name = "assetsCategory", value = "资产分类信息", dataType = "assetsCategory")
    @Log(value = "删除资产分类", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllAssetsCategory(@RequestBody AssetsCategory assetsCategory) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(assetsCategory.getIdString()))
                success = assetsCategoryService.deleteAll(assetsCategory.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除资产分类失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
