package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AddressBaseDto;
import com.aswl.as.ibrs.api.module.AddressBase;
import com.aswl.as.ibrs.service.AddressBaseService;
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
 * 地址库表controller
 *
 * @author hfx
 * @date 2020-01-04 15:35
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/addressBase", tags = "地址库表")
@RestController
@RequestMapping("/v1/addressBase")
public class AddressBaseController extends BaseController {

    private final AddressBaseService addressBaseService;

    /**
     * 根据ID获取地址库表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据地址库表ID查询地址库表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "地址库表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AddressBase> findById(@PathVariable("id") String id) {
        AddressBase addressBase = new AddressBase();
        addressBase.setId(id);
        return new ResponseBean<>(addressBaseService.get(addressBase));
    }

    /**
     * 查询所有地址库表
     *
     * @param addressBase
     * @return ResponseBean
     */
    @ApiOperation(value = "查询所有地址库表列表")
    @ApiImplicitParam(name = "addressBase", value = "地址库表对象", paramType = "path", required = true, dataType = "addressBase")
    @GetMapping(value = "addressBases")
    public ResponseBean
            <List<AddressBase>> findAll(AddressBase addressBase) {
        addressBase.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(addressBaseService.findList(addressBase));
    }

    /**
     * 分页查询地址库表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param addressBase
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询地址库表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "addressBase", value = "地址库表信息", dataType = "addressBase")
    })
    @GetMapping("getAddressBaseData")
    public ResponseBean<PageInfo<AddressBase>> getAddressBaseData(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               @RequestParam(value = "queryProjectId",required = false) String queryProjectId,
                                                               AddressBase addressBase) {
        addressBase.setTenantCode(SysUtil.getTenantCode());
        addressBase.setProjectId(SysUtil.getProjectId());
        if (StringUtils.isNotEmpty(queryProjectId)){
            addressBase.setProjectId(queryProjectId);
        }
        Double longitude =addressBase.getLongitude();
        Double latitude =addressBase.getLatitude();

        addressBase.setLongitude(null);
        addressBase.setLatitude(null);

        sort="(ABS(b.longitude-" + longitude+ ")+ABS(b.latitude-" +latitude+ ")) ";
        order="ascending";

        return new ResponseBean<>(addressBaseService.findPageForApp(PageUtil.pageInfo(pageNum, pageSize, sort, order), addressBase));
//        return new ResponseBean<>(addressBaseService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), addressBase));
    }

    //绑定数据的点位
    @ApiOperation(value = "绑定数据的点位")
    @ApiImplicitParam(name = "addressBase", value = "地址库表对象", paramType = "path", required = true, dataType = "addressBase")
    @GetMapping(value = "bindAddressBaseDevice")
    public ResponseBean<Boolean> bindAddressBaseDevice(AddressBase addressBase) {
        //判断该设备是否该租户的
        // 更新数据
        return new ResponseBean<>(addressBaseService.bindAddressBaseDevice(addressBase));
    }


    /**
     * 分页查询地址库表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param addressBase
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询地址库表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "addressBase", value = "地址库表信息", dataType = "addressBase")
    })
    @GetMapping("addressBaseList")
    public ResponseBean<PageInfo<AddressBase>> addressBaseList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               AddressBase addressBase) {
        addressBase.setTenantCode(SysUtil.getTenantCode());



        return new ResponseBean<>(addressBaseService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), addressBase));
    }

    /**
     * 新增地址库表
     *
     * @param addressBaseDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增地址库表", notes = "新增地址库表")
    @PostMapping
    @Log("新增地址库表")
    public ResponseBean
            <Boolean> insertAddressBase(@RequestBody @Valid AddressBaseDto addressBaseDto) {
        AddressBase addressBase = new AddressBase();
        BeanUtils.copyProperties(addressBaseDto, addressBase);
        addressBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(addressBaseService.insert(addressBase) > 0);
    }

    /**
     * 修改地址库表
     *
     * @param addressBaseDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新地址库表信息", notes = "根据地址库表ID更新地址库表信息")
    @Log("修改地址库表")
    @PutMapping
    public ResponseBean
            <Boolean> updateAddressBase(@RequestBody @Valid AddressBaseDto addressBaseDto) {
        AddressBase addressBase = new AddressBase();
        BeanUtils.copyProperties(addressBaseDto, addressBase);
        addressBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),addressBase.getProjectId());
        return new ResponseBean<>(addressBaseService.update(addressBase) > 0);
    }

    /**
     * 根据地址库表ID删除地址库表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除地址库表信息", notes = "根据地址库表ID删除地址库表信息")
    @ApiImplicitParam(name = "id", value = "地址库表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAddressBaseById(@PathVariable("id") String id) {
        AddressBase addressBase = new AddressBase();
        addressBase.setId(id);
        addressBase.setNewRecord(false);
        addressBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(addressBaseService.delete(addressBase) > 0);
    }

    /**
     * 批量删除地址库表信息
     *
     * @param addressBase
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除地址库表", notes = "根据地址库表ID批量删除地址库表")
    @ApiImplicitParam(name = "addressBase", value = "地址库表信息", dataType = "addressBase")
    @Log("批量删除地址库表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAddressBase(@RequestBody AddressBase addressBase) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(addressBase.getIdString()))
                success = addressBaseService.deleteAll(addressBase.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除地址库表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
