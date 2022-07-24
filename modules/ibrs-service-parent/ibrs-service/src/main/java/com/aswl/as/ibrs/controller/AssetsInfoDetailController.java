package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.AssetsInfoDetailDto;
import com.aswl.as.ibrs.api.module.AssetsInfoDetail;
import com.aswl.as.ibrs.service.AssetsInfoDetailService;
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
 * 资产信息明细controller
 *
 * @author df
 * @date 2022/03/11 13:38
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/assetsInfoDetail", tags = "资产信息明细")
@RestController
@RequestMapping("/v1/assetsInfoDetail")
public class AssetsInfoDetailController extends BaseController {

    private final AssetsInfoDetailService assetsInfoDetailService;

    /**
     * 根据ID获取资产信息明细
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据资产信息明细ID查询资产信息明细")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "资产信息明细ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AssetsInfoDetail> findById(@PathVariable("id") String id) {
        AssetsInfoDetail assetsInfoDetail = new AssetsInfoDetail();
        assetsInfoDetail.setId(id);
        return new ResponseBean<>(assetsInfoDetailService.get(assetsInfoDetail));
    }

    /**
     * 查询所有资产信息明细
     *
     * @param assetsInfoDetail
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有资产信息明细列表")
    @ApiImplicitParam(name = "assetsInfoDetail", value = "资产信息明细对象", paramType = "path", required = true, dataType = "assetsInfoDetail")
    @GetMapping(value = "assetsInfoDetails")
    public ResponseBean
            <List<AssetsInfoDetail>> findAll(AssetsInfoDetail assetsInfoDetail) {
        assetsInfoDetail.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoDetailService.findList(assetsInfoDetail));
    }

    /**
     * 分页查询资产信息明细列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param assetsInfoDetail
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询资产信息明细列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "assetsInfoDetail", value = "资产信息明细信息", dataType = "assetsInfoDetail")
    })

    @GetMapping("assetsInfoDetailList")
    public ResponseBean
            <PageInfo<AssetsInfoDetail>> assetsInfoDetailList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                              AssetsInfoDetail assetsInfoDetail) {
        assetsInfoDetail.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoDetailService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), assetsInfoDetail));
    }

    /**
     * 新增资产信息明细
     *
     * @param assetsInfoDetailDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增资产信息明细", notes = "新增资产信息明细")
    @Log(value = "新增资产信息明细", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertAssetsInfoDetail(@RequestBody @Valid AssetsInfoDetailDto assetsInfoDetailDto) {
        AssetsInfoDetail assetsInfoDetail = new AssetsInfoDetail();
        BeanUtils.copyProperties(assetsInfoDetailDto, assetsInfoDetail);
        assetsInfoDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoDetailService.insert(assetsInfoDetail) > 0);
    }

    /**
     * 修改资产信息明细
     *
     * @param assetsInfoDetailDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新资产信息明细信息", notes = "根据资产信息明细ID更新资产信息明细信息")
    @Log(value = "更新资产信息明细", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateAssetsInfoDetail(@RequestBody @Valid AssetsInfoDetailDto assetsInfoDetailDto) {
        AssetsInfoDetail assetsInfoDetail = new AssetsInfoDetail();
        BeanUtils.copyProperties(assetsInfoDetailDto, assetsInfoDetail);
        assetsInfoDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoDetailService.update(assetsInfoDetail) > 0);
    }

    /**
     * 根据资产信息明细ID删除资产信息明细信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除资产信息明细信息", notes = "根据资产信息明细ID删除资产信息明细信息")
    @ApiImplicitParam(name = "id", value = "资产信息明细ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除资产信息明细", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAssetsInfoDetailById(@PathVariable("id") String id) {
        AssetsInfoDetail assetsInfoDetail = new AssetsInfoDetail();
        assetsInfoDetail.setId(id);
        assetsInfoDetail.setNewRecord(false);
        assetsInfoDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoDetailService.delete(assetsInfoDetail) > 0);
    }

    /**
     * 批量删除资产信息明细信息
     *
     * @param assetsInfoDetail
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除资产信息明细", notes = "根据资产信息明细ID批量删除资产信息明细")
    @ApiImplicitParam(name = "assetsInfoDetail", value = "资产信息明细信息", dataType = "assetsInfoDetail")
    @Log(value = "删除资产信息明细", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllAssetsInfoDetail(@RequestBody AssetsInfoDetail assetsInfoDetail) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(assetsInfoDetail.getIdString()))
                success = assetsInfoDetailService.deleteAll(assetsInfoDetail.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除资产信息明细失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
