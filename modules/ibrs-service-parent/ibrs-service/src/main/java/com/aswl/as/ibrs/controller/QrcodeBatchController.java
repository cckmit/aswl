package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.QrcodeBatchDto;
import com.aswl.as.ibrs.api.module.QrcodeBatch;
import com.aswl.as.ibrs.api.vo.AppSanDeviceVo;
import com.aswl.as.ibrs.api.vo.QrCodeDeviceVo;
import com.aswl.as.ibrs.service.QrcodeBatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 二维码批次表controller
 *
 * @author df
 * @date 2020/12/17 14:13
 */
@Slf4j
@Api(value = "/api/v1/qrcodeBatch", tags = "二维码批次表")
@RestController
@RequestMapping("/v1/qrcodeBatch")
public class QrcodeBatchController extends BaseController {

    @Autowired
    private  QrcodeBatchService qrcodeBatchService;

    @Value(value = "${qrcode.redirectUrl}")
    private String redirectUrl;

    /**
     * 根据ID获取二维码批次表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据二维码批次表ID查询二维码批次表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "二维码批次表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<QrcodeBatch> findById(@PathVariable("id") String id) {
        QrcodeBatch qrcodeBatch = new QrcodeBatch();
        qrcodeBatch.setId(id);
        return new ResponseBean<>(qrcodeBatchService.get(qrcodeBatch));
    }

    /**
     * 查询所有二维码批次表
     *
     * @param qrcodeBatch
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有二维码批次表列表")
    @ApiImplicitParam(name = "qrcodeBatch", value = "二维码批次表对象", paramType = "path", required = true, dataType = "qrcodeBatch")
    @GetMapping(value = "qrcodeBatchs")
    public ResponseBean
            <List<QrcodeBatch>> findAll(QrcodeBatch qrcodeBatch) {
        qrcodeBatch.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(qrcodeBatchService.findList(qrcodeBatch));
    }

    /**
     * 分页查询二维码批次表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param qrcodeBatch
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询二维码批次表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "qrcodeBatch", value = "二维码批次表信息", dataType = "qrcodeBatch")
    })

    @GetMapping("qrcodeBatchList")
    public ResponseBean
            <PageInfo<QrcodeBatch>> qrcodeBatchList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                    QrcodeBatch qrcodeBatch) {
        qrcodeBatch.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(qrcodeBatchService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), qrcodeBatch));
    }

    /**
     * 新增二维码批次表
     *
     * @param qrcodeBatchDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增二维码批次表", notes = "新增二维码批次表")
    @Log(value = "新增二维码批次表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertQrcodeBatch(@RequestBody @Valid QrcodeBatchDto qrcodeBatchDto) {
        QrcodeBatch qrcodeBatch = new QrcodeBatch();
        BeanUtils.copyProperties(qrcodeBatchDto, qrcodeBatch);
        qrcodeBatch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(qrcodeBatchService.insert(qrcodeBatch) > 0);
    }

    /**
     * 修改二维码批次表
     *
     * @param qrcodeBatchDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新二维码批次表信息", notes = "根据二维码批次表ID更新二维码批次表信息")
    @Log(value = "更新二维码批次表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateQrcodeBatch(@RequestBody @Valid QrcodeBatchDto qrcodeBatchDto) {
        QrcodeBatch qrcodeBatch = new QrcodeBatch();
        BeanUtils.copyProperties(qrcodeBatchDto, qrcodeBatch);
        qrcodeBatch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(qrcodeBatchService.update(qrcodeBatch) > 0);
    }

    /**
     * 根据二维码批次表ID删除二维码批次表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除二维码批次表信息", notes = "根据二维码批次表ID删除二维码批次表信息")
    @ApiImplicitParam(name = "id", value = "二维码批次表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除二维码批次表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteQrcodeBatchById(@PathVariable("id") String id) {
        QrcodeBatch qrcodeBatch = new QrcodeBatch();
        qrcodeBatch.setId(id);
        qrcodeBatch.setNewRecord(false);
        qrcodeBatch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(qrcodeBatchService.delete(qrcodeBatch) > 0);
    }

    /**
     * 批量删除二维码批次表信息
     *
     * @param qrcodeBatch
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除二维码批次表", notes = "根据二维码批次表ID批量删除二维码批次表")
    @ApiImplicitParam(name = "qrcodeBatch", value = "二维码批次表信息", dataType = "qrcodeBatch")
    @Log(value = "删除二维码批次表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllQrcodeBatch(@RequestBody QrcodeBatch qrcodeBatch) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(qrcodeBatch.getIdString()))
                success = qrcodeBatchService.deleteAll(qrcodeBatch.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除二维码批次表失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 获取二维码设备信息
     */
    @GetMapping("/getDevice")
    @ApiOperation("微信获取二维码设备信息")
    public ResponseBean<QrCodeDeviceVo> getDevices(@RequestParam("qrcode") String qrcode ,@RequestParam(value = "flag",defaultValue = "") String flag ,HttpServletResponse response) throws IOException {
        if("h5".equals(flag)){
            return new ResponseBean<>(qrcodeBatchService.queryQrCodeBath(qrcode));
        }else{
            response.sendRedirect(""+redirectUrl+"" +"?qrcode="+qrcode);
            return null;
        }
    }


    /**
     * APP扫码获取二维码设备信息
     */
    @GetMapping("/getAppSanDevice")
    @ApiOperation("APP扫码获取二维码设备信息")
    public ResponseBean<AppSanDeviceVo> getSanDevicesInfo(@RequestParam("qrcode") String qrcode) {
        return new ResponseBean<>(qrcodeBatchService.getSanDevicesInfo(qrcode));
    }

}
