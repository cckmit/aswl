package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.AssetsInfoDto;
import com.aswl.as.ibrs.api.module.AssetsInfo;
import com.aswl.as.ibrs.api.vo.AssetsInfoVo;
import com.aswl.as.ibrs.service.AssetsInfoService;
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
 * 资产信息controller
 *
 * @author df
 * @date 2022/01/14 15:54
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/assetsInfo", tags = "资产信息")
@RestController
@RequestMapping("/v1/assetsInfo")
public class AssetsInfoController extends BaseController {

    private final AssetsInfoService assetsInfoService;

    /**
     * 根据ID获取资产信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据资产信息ID查询资产信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "资产信息ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AssetsInfo> findById(@PathVariable("id") String id) {
        AssetsInfo assetsInfo = new AssetsInfo();
        assetsInfo.setId(id);
        return new ResponseBean<>(assetsInfoService.get(assetsInfo));
    }

    /**
     * 查询所有资产信息
     *
     * @param assetsInfo
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有资产信息列表")
    @ApiImplicitParam(name = "assetsInfo", value = "资产信息对象", paramType = "path", required = true, dataType = "assetsInfo")
    @GetMapping(value = "assetsInfos")
    public ResponseBean
            <List<AssetsInfo>> findAll(AssetsInfo assetsInfo) {
        assetsInfo.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoService.findList(assetsInfo));
    }

    /**
     * 分页查询资产信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param assetsInfoDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询资产信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "assetsInfo", value = "资产信息信息", dataType = "assetsInfo")
    })

    @GetMapping("assetsInfoList")
    public ResponseBean
            <PageInfo<AssetsInfoVo>> assetsInfoList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                    AssetsInfoDto assetsInfoDto) {
        assetsInfoDto.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), assetsInfoDto));
    }

    /**
     * 新增资产信息
     *
     * @param assetsInfoDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增资产信息", notes = "新增资产信息")
    @Log(value = "新增资产信息", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertAssetsInfo(@RequestBody @Valid AssetsInfoDto assetsInfoDto) {
        return new ResponseBean<>(assetsInfoService.insert(assetsInfoDto) > 0);
    }

    /**
     * 修改资产信息
     *
     * @param assetsInfoDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新资产信息信息", notes = "根据资产信息ID更新资产信息信息")
    @Log(value = "更新资产信息", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateAssetsInfo(@RequestBody @Valid AssetsInfoDto assetsInfoDto) {
        AssetsInfo assetsInfo = new AssetsInfo();
        BeanUtils.copyProperties(assetsInfoDto, assetsInfo);
        assetsInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(assetsInfoService.update(assetsInfo) > 0);
    }

    /**
     * 根据资产信息ID删除资产信息信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除资产信息信息", notes = "根据资产信息ID删除资产信息信息")
    @ApiImplicitParam(name = "id", value = "资产信息ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除资产信息", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAssetsInfoById(@PathVariable("id") String id) {
        return new ResponseBean<>(assetsInfoService.delete(id) > 0);
    }

    /**
     * 批量删除资产信息信息
     *
     * @param assetsInfo
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除资产信息", notes = "根据资产信息ID批量删除资产信息")
    @ApiImplicitParam(name = "assetsInfo", value = "资产信息信息", dataType = "assetsInfo")
    @Log(value = "删除资产信息", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllAssetsInfo(@RequestBody AssetsInfo assetsInfo) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(assetsInfo.getIdString()))
                success = assetsInfoService.deleteAll(assetsInfo.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除资产信息失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
