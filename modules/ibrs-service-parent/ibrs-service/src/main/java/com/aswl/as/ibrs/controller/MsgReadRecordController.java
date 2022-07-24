package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.MsgReadRecordDto;
import com.aswl.as.ibrs.api.module.MsgReadRecord;
import com.aswl.as.ibrs.service.MsgReadRecordService;
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
 * 消息阅读表controller
 *
 * @author df
 * @date 2021/12/08 10:08
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/msgReadRecord", tags = "消息阅读表")
@RestController
@RequestMapping("/v1/msgReadRecord")
public class MsgReadRecordController extends BaseController {

    private final MsgReadRecordService msgReadRecordService;

    /**
     * 根据ID获取消息阅读表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据消息阅读表ID查询消息阅读表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "消息阅读表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<MsgReadRecord> findById(@PathVariable("id") String id) {
        MsgReadRecord msgReadRecord = new MsgReadRecord();
        msgReadRecord.setId(id);
        return new ResponseBean<>(msgReadRecordService.get(msgReadRecord));
    }

    /**
     * 查询所有消息阅读表
     *
     * @param msgReadRecord
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有消息阅读表列表")
    @ApiImplicitParam(name = "msgReadRecord", value = "消息阅读表对象", paramType = "path", required = true, dataType = "msgReadRecord")
    @GetMapping(value = "msgReadRecords")
    public ResponseBean
            <List<MsgReadRecord>> findAll(MsgReadRecord msgReadRecord) {
        msgReadRecord.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(msgReadRecordService.findList(msgReadRecord));
    }

    /**
     * 分页查询消息阅读表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param msgReadRecord
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询消息阅读表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "msgReadRecord", value = "消息阅读表信息", dataType = "msgReadRecord")
    })

    @GetMapping("msgReadRecordList")
    public ResponseBean
            <PageInfo<MsgReadRecord>> msgReadRecordList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                        MsgReadRecord msgReadRecord) {
        msgReadRecord.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(msgReadRecordService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), msgReadRecord));
    }

    /**
     * 新增消息阅读表
     *
     * @param msgReadRecordDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增消息阅读表", notes = "新增消息阅读表")
    @Log(value = "新增消息阅读表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertMsgReadRecord(@RequestBody @Valid MsgReadRecordDto msgReadRecordDto) {
        return new ResponseBean<>(msgReadRecordService.insert(msgReadRecordDto) > 0);
    }

    /**
     * 修改消息阅读表
     *
     * @param msgReadRecordDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新消息阅读表信息", notes = "根据消息阅读表ID更新消息阅读表信息")
    @Log(value = "更新消息阅读表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateMsgReadRecord(@RequestBody @Valid MsgReadRecordDto msgReadRecordDto) {
        MsgReadRecord msgReadRecord = new MsgReadRecord();
        BeanUtils.copyProperties(msgReadRecordDto, msgReadRecord);
        msgReadRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(msgReadRecordService.update(msgReadRecord) > 0);
    }

    /**
     * 根据消息阅读表ID删除消息阅读表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除消息阅读表信息", notes = "根据消息阅读表ID删除消息阅读表信息")
    @ApiImplicitParam(name = "id", value = "消息阅读表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除消息阅读表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteMsgReadRecordById(@PathVariable("id") String id) {
        MsgReadRecord msgReadRecord = new MsgReadRecord();
        msgReadRecord.setId(id);
        msgReadRecord.setNewRecord(false);
        msgReadRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(msgReadRecordService.delete(msgReadRecord) > 0);
    }

    /**
     * 批量删除消息阅读表信息
     *
     * @param msgReadRecord
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除消息阅读表", notes = "根据消息阅读表ID批量删除消息阅读表")
    @ApiImplicitParam(name = "msgReadRecord", value = "消息阅读表信息", dataType = "msgReadRecord")
    @Log(value = "删除消息阅读表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllMsgReadRecord(@RequestBody MsgReadRecord msgReadRecord) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(msgReadRecord.getIdString()))
                success = msgReadRecordService.deleteAll(msgReadRecord.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除消息阅读表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
