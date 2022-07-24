package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import com.aswl.as.ibrs.service.AlarmLevelService;
import com.aswl.as.ibrs.service.DeviceAlarmInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 告警记录清理controller
 * @author df
 * @date 2021/07/21 10:38
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/clearAlarm", tags = "告警记录清理")
@RestController
@RequestMapping("/v1/clearAlarm")
public class AlarmInfoClearController extends BaseController {

    private final DeviceAlarmInfoService deviceAlarmInfoService;

    /**
     *  删除告警记录历史
     *
     * @param deviceAlarmDto
     * @return ResponseBean
     */

    @ApiOperation(value = "删除告警记录历史", notes = "删除告警记录历史")
    @ApiImplicitParam(name = "deviceAlarmDto", value = "设备报警Dto", dataType = "DeviceAlarmDto")
    @Log("删除告警记录")
    @PostMapping("delete")
    public ResponseBean<Boolean> deleteAlarmInfo(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        return new ResponseBean<>(deviceAlarmInfoService.deleteAlarmInfo(deviceAlarmDto) > 0 );
    }

    /**
     *  删除告警记录历史状态
     *
     * @param deviceAlarmDto
     * @return ResponseBean
     */

    @ApiOperation(value = "删除告警记录历史状态", notes = "删除告警记录历史状态")
    @ApiImplicitParam(name = "deviceAlarmDto", value = "设备报警Dto", dataType = "DeviceAlarmDto")
    @Log("删除告警记录历史状态")
    @PostMapping("deleteStatus")
    public ResponseBean<Boolean> deleteHisAlarmStatus(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        return new ResponseBean<>(deviceAlarmInfoService.deleteAlarmStatus(deviceAlarmDto) > 0 );
    }
}
