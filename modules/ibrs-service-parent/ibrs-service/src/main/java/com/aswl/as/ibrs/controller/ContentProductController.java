package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentProductDto;
import com.aswl.as.ibrs.api.module.CategoryTree;
import com.aswl.as.ibrs.api.module.ContentProduct;
import com.aswl.as.ibrs.service.CategoryTreeService;
import com.aswl.as.ibrs.service.ContentProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 产品中心表controller
 *
 * @author hfx
 * @date 2020-03-06 10:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentProduct", tags = "产品中心表")
@RestController
@RequestMapping("/v1/contentProduct")
public class ContentProductController extends BaseController {

    private final ContentProductService contentProductService;

    private final CategoryTreeService categoryTreeService;

    /**
     * 根据ID获取产品中心表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据产品中心表ID查询产品中心表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "产品中心表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentProduct> findById(@PathVariable("id") String id) {
        ContentProduct contentProduct = new ContentProduct();
        contentProduct.setId(id);
        return new ResponseBean<>(contentProductService.get(contentProduct));
    }

    /**
     * 根据ID获取产品中心表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据产品中心表ID查询产品中心表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "产品中心表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/html/{id}",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
    @ResponseBody
    public String findHtmlById(@PathVariable("id") String id) {
        ContentProduct contentProduct = new ContentProduct();
        contentProduct.setId(id);

        contentProduct=contentProductService.get(contentProduct);
        return contentProduct==null?null:contentProduct.getContent();
    }

    /**
     * 获取 产品中心 的分类和分类下面对应的数据
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "获取 产品中心 的分类和分类下面对应的数据")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "产品中心表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/findAllProductForShow") //,produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"}
    @ResponseBody
    public ResponseBean<List<ContentProduct>> findAllProductForShow() {
        ContentProduct contentProduct = new ContentProduct();
//        contentProduct.setId(id);

        contentProduct=contentProductService.get(contentProduct);

        CategoryTree temp=new CategoryTree();
        temp.setCategory(1);
        temp.setDelFlag(0);
        List<CategoryTree> categoryTreeList=categoryTreeService.findList(temp);

        // ---------------------------------
        List<CategoryTree> list=new ArrayList<CategoryTree>();
        //获取所有的数据
        List<ContentProduct> pList=contentProductService.findList(contentProduct);
        for(CategoryTree c:categoryTreeList)
        {
            if(!"-1".equals(c.getParentId()))
            {
                list.add(c);
                c.setTempDataList(new ArrayList<>());
                for(ContentProduct p:pList)
                {
                    if(c.getId().equals(p.getCategoryTreeId()))
                    {
                        c.getTempDataList().add(p);
                    }
                }
            }
        }

        return new ResponseBean(list);
    }


    /**
     * 查询所有产品中心表
     *
     * @param contentProduct
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有产品中心表列表")
    @ApiImplicitParam(name = "contentProduct", value = "产品中心表对象", paramType = "path", required = true, dataType = "contentProduct")
    @GetMapping(value = "contentProducts")
    public ResponseBean
            <List<ContentProduct>> findAll(ContentProduct contentProduct) {
        contentProduct.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentProductService.findList(contentProduct));
    }

    /**
     * 分页查询产品中心表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentProduct
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询产品中心表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentProduct", value = "产品中心表信息", dataType = "contentProduct")
    })

    @GetMapping("contentProductList")
    public ResponseBean<PageInfo<ContentProduct>> contentProductList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                     ContentProduct contentProduct) {
        contentProduct.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentProductService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentProduct));
    }

    /**
     * 新增产品中心表
     *
     * @param contentProductDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增产品中心表", notes = "新增产品中心表")
    @PostMapping
    @Log("新增产品中心表")
    public ResponseBean
            <Boolean> insertContentProduct(@RequestBody @Valid ContentProductDto contentProductDto) {
        ContentProduct contentProduct = new ContentProduct();
        BeanUtils.copyProperties(contentProductDto, contentProduct);
        contentProduct.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentProductService.insert(contentProduct) > 0);
    }

    /**
     * 修改产品中心表
     *
     * @param contentProductDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新产品中心表信息", notes = "根据产品中心表ID更新产品中心表信息")
    @Log("修改产品中心表")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentProduct(@RequestBody @Valid ContentProductDto contentProductDto) {
        ContentProduct contentProduct = new ContentProduct();
        BeanUtils.copyProperties(contentProductDto, contentProduct);
        contentProduct.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentProductService.update(contentProduct) > 0);
    }

    /**
     * 根据产品中心表ID删除产品中心表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除产品中心表信息", notes = "根据产品中心表ID删除产品中心表信息")
    @ApiImplicitParam(name = "id", value = "产品中心表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentProductById(@PathVariable("id") String id) {
        ContentProduct contentProduct = new ContentProduct();
        contentProduct.setId(id);
        contentProduct.setNewRecord(false);
        contentProduct.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentProductService.delete(contentProduct) > 0);
    }

    /**
     * 批量删除产品中心表信息
     *
     * @param contentProduct
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除产品中心表", notes = "根据产品中心表ID批量删除产品中心表")
    @ApiImplicitParam(name = "contentProduct", value = "产品中心表信息", dataType = "contentProduct")
    @Log("批量删除产品中心表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentProduct(@RequestBody ContentProduct contentProduct) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentProduct.getIdString()))
                success = contentProductService.deleteAll(contentProduct.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除产品中心表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
