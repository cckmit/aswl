package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.service.AlarmStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台报表下载导出controller
 *
 * @author df
 * @date 2021/09/29 15:28
 */

@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/export", tags = "后台报表下载导出controller")
@RestController
@RequestMapping("/v1/export")
public class StatisticsExportController extends BaseController {

    private final AlarmStatisticsService alarmStatisticsService;

    /**
     *  故障设备列表
     *
     * @param deviceAlarmDto deviceAlarmDto
     */
    @PostMapping("faultDeviceExport")
    @ApiOperation(value = "故障设备列表", notes = "故障设备列表")
    public ResponseBean<Boolean> faultDeviceExport(@RequestBody DeviceAlarmDto deviceAlarmDto, HttpServletRequest request, HttpServletResponse response) {
        return alarmStatisticsService.faultDeviceExport(deviceAlarmDto, request, response);
    }


    /**
     *  在线率统计--在线/离线设备列表
     *
     * @param deviceAlarmDto deviceAlarmDto
     */
    @PostMapping("onlineOfflineDeviceExport")
    @ApiOperation(value = "在线/离线设备列表", notes = "在线/离线设备列表")
    public ResponseEntity<byte[]> onlineOfflineDeviceExport(@RequestBody DeviceAlarmDto deviceAlarmDto, HttpServletResponse response) throws Exception {
        return alarmStatisticsService.onlineOfflineDeviceExport(deviceAlarmDto,response);
    }
}
