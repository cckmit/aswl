package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.CommunicationOperatorDto;
import com.aswl.as.ibrs.api.module.CommunicationOperator;
import com.aswl.as.ibrs.service.CommunicationOperatorService;
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
 * 通讯运营商controller
 *
 * @author df
 * @date 2021/12/03 11:23
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/communicationOperator", tags = "通讯运营商")
@RestController
@RequestMapping("/v1/communicationOperator")
public class CommunicationOperatorController extends BaseController {

    private final CommunicationOperatorService communicationOperatorService;

    /**
     * 根据ID获取通讯运营商
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据通讯运营商ID查询通讯运营商")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "通讯运营商ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CommunicationOperator> findById(@PathVariable("id") String id) {
        CommunicationOperator communicationOperator = new CommunicationOperator();
        communicationOperator.setId(id);
        return new ResponseBean<>(communicationOperatorService.get(communicationOperator));
    }

    /**
     * 查询所有通讯运营商
     *
     * @param communicationOperator
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有通讯运营商列表")
    @ApiImplicitParam(name = "communicationOperator", value = "通讯运营商对象", paramType = "path", required = true, dataType = "communicationOperator")
    @GetMapping(value = "communicationOperators")
    public ResponseBean
            <List<CommunicationOperator>> findAll(CommunicationOperator communicationOperator) {
        communicationOperator.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(communicationOperatorService.findList(communicationOperator));
    }

    /**
     * 分页查询通讯运营商列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param communicationOperator
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询通讯运营商列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "communicationOperator", value = "通讯运营商信息", dataType = "communicationOperator")
    })

    @GetMapping("communicationOperatorList")
    public ResponseBean
            <PageInfo<CommunicationOperator>> communicationOperatorList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                        CommunicationOperator communicationOperator) {
        communicationOperator.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(communicationOperatorService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), communicationOperator));
    }

    /**
     * 新增通讯运营商
     *
     * @param communicationOperatorDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增通讯运营商", notes = "新增通讯运营商")
    @Log(value = "新增通讯运营商", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertCommunicationOperator(@RequestBody @Valid CommunicationOperatorDto communicationOperatorDto) {
        CommunicationOperator communicationOperator = new CommunicationOperator();
        BeanUtils.copyProperties(communicationOperatorDto, communicationOperator);
        communicationOperator.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(communicationOperatorService.insert(communicationOperator) > 0);
    }

    /**
     * 修改通讯运营商
     *
     * @param communicationOperatorDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新通讯运营商信息", notes = "根据通讯运营商ID更新通讯运营商信息")
    @Log(value = "更新通讯运营商", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateCommunicationOperator(@RequestBody @Valid CommunicationOperatorDto communicationOperatorDto) {
        CommunicationOperator communicationOperator = new CommunicationOperator();
        BeanUtils.copyProperties(communicationOperatorDto, communicationOperator);
        communicationOperator.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(communicationOperatorService.update(communicationOperator) > 0);
    }

    /**
     * 根据通讯运营商ID删除通讯运营商信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除通讯运营商信息", notes = "根据通讯运营商ID删除通讯运营商信息")
    @ApiImplicitParam(name = "id", value = "通讯运营商ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除通讯运营商", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteCommunicationOperatorById(@PathVariable("id") String id) {
        CommunicationOperator communicationOperator = new CommunicationOperator();
        communicationOperator.setId(id);
        communicationOperator.setNewRecord(false);
        communicationOperator.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(communicationOperatorService.delete(communicationOperator) > 0);
    }

    /**
     * 批量删除通讯运营商信息
     *
     * @param communicationOperator
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除通讯运营商", notes = "根据通讯运营商ID批量删除通讯运营商")
    @ApiImplicitParam(name = "communicationOperator", value = "通讯运营商信息", dataType = "communicationOperator")
    @Log(value = "删除通讯运营商", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllCommunicationOperator(@RequestBody CommunicationOperator communicationOperator) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(communicationOperator.getIdString()))
                success = communicationOperatorService.deleteAll(communicationOperator.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除通讯运营商失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
