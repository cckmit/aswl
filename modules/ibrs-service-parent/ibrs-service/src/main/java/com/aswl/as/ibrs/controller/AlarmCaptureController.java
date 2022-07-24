package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmCaptureDto;
import com.aswl.as.ibrs.api.module.AlarmCapture;
import com.aswl.as.ibrs.service.AlarmCaptureService;
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
 * 告警抓拍
 *
 * @author hzj
 * @date 2020/12/08 10:41
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmCapture", tags = "告警抓拍")
@RestController
@RequestMapping("/v1/alarmCapture")
public class AlarmCaptureController extends BaseController {

    private final AlarmCaptureService alarmCaptureService;

    /**
     * 根据ID获取告警抓拍
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据ID查询告警抓拍")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmCapture> findById(@PathVariable("id") String id) {
        AlarmCapture alarmCapture = new AlarmCapture();
        alarmCapture.setId(id);
        return new ResponseBean<>(alarmCaptureService.get(alarmCapture));
    }

    /**
     * 查询所有告警抓拍
     *
     * @param alarmCapture
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有告警抓拍列表")
    @ApiImplicitParam(name = "alarmCapture", value = "告警抓拍对象", paramType = "path", required = true, dataType = "alarmCapture")
    @GetMapping(value = "alarmCaptures")
    public ResponseBean
            <List<AlarmCapture>> findAll(AlarmCapture alarmCapture) {
        alarmCapture.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmCaptureService.findList(alarmCapture));
    }

    /**
     * 分页查询告警抓拍列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmCapture
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询告警抓拍列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmCapture", value = "告警抓拍信息", dataType = "alarmCapture")
    })

    @GetMapping("alarmCaptureList")
    public ResponseBean
            <PageInfo<AlarmCapture>> alarmCaptureList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                      AlarmCapture alarmCapture) {
        alarmCapture.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmCaptureService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmCapture));
    }

    /**
     * 新增告警抓拍
     *
     * @param alarmCapture
     * @return ResponseBean
     */
    @ApiOperation(value = "新增告警抓拍", notes = "新增告警抓拍")
    @PostMapping
    @Log("新增告警抓拍")
    public ResponseBean
            <Boolean> insertAlarmCapture(@RequestBody @Valid AlarmCapture alarmCapture) {
        alarmCapture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmCaptureService.insert(alarmCapture) > 0);
    }

    /**
     * 修改告警抓拍
     *
     * @param alarmCapture
     * @return ResponseBean
     */

    @ApiOperation(value = "更新告警抓拍信息", notes = "根据告警抓拍ID更新告警抓拍信息")
    @Log("修改告警抓拍")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmCapture(@RequestBody @Valid AlarmCapture alarmCapture) {
        alarmCapture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmCaptureService.update(alarmCapture) > 0);
    }

    /**
     * 根据告警抓拍ID删除告警抓拍信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除告警抓拍信息", notes = "根据告警抓拍ID删除告警抓拍信息")
    @ApiImplicitParam(name = "id", value = "告警抓拍ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmCaptureById(@PathVariable("id") String id) {
        AlarmCapture alarmCapture = new AlarmCapture();
        alarmCapture.setId(id);
        alarmCapture.setNewRecord(false);
        alarmCapture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmCaptureService.delete(alarmCapture) > 0);
    }

    /**
     * 批量删除告警抓拍信息
     *
     * @param alarmCapture
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除告警抓拍", notes = "根据告警抓拍ID批量删除告警抓拍")
    @ApiImplicitParam(name = "alarmCapture", value = "告警抓拍信息", dataType = "alarmCapture")
    @Log("批量删除告警抓拍")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmCapture(@RequestBody AlarmCapture alarmCapture) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmCapture.getIdString()))
                success = alarmCaptureService.deleteAll(alarmCapture.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除告警抓拍失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
