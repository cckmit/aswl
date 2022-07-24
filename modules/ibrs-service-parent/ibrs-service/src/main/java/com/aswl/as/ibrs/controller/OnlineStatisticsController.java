package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.service.OnlineStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;


/**
 * 设备在线统计controller
 *
 * @author dingfei
 * @date 2019-12-16 16:01
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/onlineStatistics", tags = "设备在线统计")
@RestController
@RequestMapping("/v1/onlineStatistics")
public class OnlineStatisticsController extends BaseController {

    private final OnlineStatisticsService onlineStatisticsService;

    /**
     * 在线率统计
     * @param kind 设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return ResponseBean
     */

    @ApiOperation(value = "在线率统计", notes = "在线率统计")
    @Log("在线率统计")
    @GetMapping("queryOnlineRate")
    public ResponseBean<Map> queryOnlineRateStatistics(@RequestParam("kind") String kind) {
        return new ResponseBean<>(onlineStatisticsService.onlineRateStatistics(kind));
    }

    /**
     * 设备在线趋势分析
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "在线趋势图",notes = "在线趋势图")
    @GetMapping("queryOnlineTrend")
    public ResponseBean<List<Map>> queryOnlineTrend(DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(onlineStatisticsService.queryOnlineTrend(deviceAlarmDto));
    }


}
