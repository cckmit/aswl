package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 点位分布controller
 *
 * @author df
 * @date 2021/01/18 13:53
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/city/points", tags = "点位分布")
@RestController
@RequestMapping("/v1/city/points")
public class PointsDistributeController {
    private final CityService cityService;


    /**
     *  获取地图设备列表
     * @param kind      2 传输箱 3:摄像头
     * @param cityCodes  城市编码集合
     * @param projectId 项目ID
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @return ResponseBean
     */
    @GetMapping("devices")
    @ApiOperation(value = "获取设备列表", notes = "获取设备列表")
    public ResponseBean<List<DeviceVo>> findDeviceList(@RequestParam(value = "kind",required = false) String kind,
                                                       @RequestParam(value = "cityCodes",required = false)String cityCodes,
                                                       @RequestParam(value = "projectId",required = false) String projectId,
                                                       @RequestParam("longMin") String longMin, @RequestParam("longMax") String longMax,
                                                       @RequestParam("latMin") String latMin, @RequestParam("latMax") String latMax) {
        return new ResponseBean<>(cityService.findDeviceList(kind,cityCodes,projectId,longMin,longMax,latMin,latMax));
    }



    /**
     *  获取地图设备统计
     * @param kind      2 传输箱 3:摄像头
     * @param cityCode  城市编码
     * @param cityCodes  城市编码集合
     * @param projectId 项目ID
     * @return ResponseBean
     */
    @GetMapping("devicesStatistics")
    @ApiOperation(value = "获取地图设备统计", notes = "获取地图设备统计")
    public ResponseBean<Map> findDevicesStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                   @RequestParam(value = "cityCode",required = false)String cityCode,
                                                   @RequestParam(value = "cityCodes",required = false) String cityCodes,
                                                   @RequestParam(value = "projectId",required = false) String projectId,
                                                   @RequestParam(value = "longMin",required = false) String longMin, @RequestParam(value = "longMax",required = false) String longMax,
                                                   @RequestParam(value = "latMin",required = false) String latMin, @RequestParam(value = "latMax",required = false) String latMax
                                                   ) {
        return new ResponseBean<>(cityService.findDevicesStatistics(kind,cityCode,cityCodes,projectId,longMin,longMax,latMin,latMax));
    }

}
