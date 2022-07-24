package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentBannerDto;
import com.aswl.as.ibrs.api.module.ContentBanner;
import com.aswl.as.ibrs.service.ContentBannerService;
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
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * banner管理controller
 *
 * @author hfx
 * @date 2020-03-11 13:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentBanner", tags = "banner管理")
@RestController
@RequestMapping("/v1/contentBanner")
public class ContentBannerController extends BaseController {

    private final ContentBannerService contentBannerService;

    /**
     * 根据ID获取banner管理
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据banner管理ID查询banner管理")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "banner管理ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentBanner> findById(@PathVariable("id") String id) {
        ContentBanner contentBanner = new ContentBanner();
        contentBanner.setId(id);
        return new ResponseBean<>(contentBannerService.get(contentBanner));
    }

    /**
     * 查询所有banner管理
     *
     * @param contentBanner
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有banner管理列表")
    @ApiImplicitParam(name = "contentBanner", value = "banner管理对象", paramType = "path", required = true, dataType = "contentBanner")
    @GetMapping(value = "contentBanners")
    public ResponseBean
            <List<ContentBanner>> findAll(ContentBanner contentBanner) {
        contentBanner.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentBannerService.findList(contentBanner));
    }

    /**
     * 分页查询banner管理列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentBanner
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询banner管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentBanner", value = "banner管理信息", dataType = "contentBanner")
    })

    @GetMapping("contentBannerList")
    public ResponseBean<PageInfo<ContentBanner>> contentBannerList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                   ContentBanner contentBanner) {
        contentBanner.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentBannerService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentBanner));
    }

    /**
     * 新增banner管理
     *
     * @param contentBannerDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增banner管理", notes = "新增banner管理")
    @PostMapping
    @Log("新增banner管理")
    public ResponseBean
            <Boolean> insertContentBanner(@RequestBody @Valid ContentBannerDto contentBannerDto) {
        ContentBanner contentBanner = new ContentBanner();
        BeanUtils.copyProperties(contentBannerDto, contentBanner);
        contentBanner.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentBannerService.insert(contentBanner) > 0);
    }

    /**
     * 修改banner管理
     *
     * @param contentBannerDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新banner管理信息", notes = "根据banner管理ID更新banner管理信息")
    @Log("修改banner管理")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentBanner(@RequestBody @Valid ContentBannerDto contentBannerDto) {
        ContentBanner contentBanner = new ContentBanner();
        BeanUtils.copyProperties(contentBannerDto, contentBanner);
        contentBanner.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentBannerService.update(contentBanner) > 0);
    }

    /**
     * 根据banner管理ID删除banner管理信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除banner管理信息", notes = "根据banner管理ID删除banner管理信息")
    @ApiImplicitParam(name = "id", value = "banner管理ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentBannerById(@PathVariable("id") String id) {
        ContentBanner contentBanner = new ContentBanner();
        contentBanner.setId(id);
        contentBanner.setNewRecord(false);
        contentBanner.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentBannerService.delete(contentBanner) > 0);
    }

    /**
     * 批量删除banner管理信息
     *
     * @param contentBanner
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除banner管理", notes = "根据banner管理ID批量删除banner管理")
    @ApiImplicitParam(name = "contentBanner", value = "banner管理信息", dataType = "contentBanner")
    @Log("批量删除banner管理")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentBanner(@RequestBody ContentBanner contentBanner) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentBanner.getIdString()))
                success = contentBannerService.deleteAll(contentBanner.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除banner管理失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
