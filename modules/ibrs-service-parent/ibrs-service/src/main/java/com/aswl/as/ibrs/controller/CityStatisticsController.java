package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.vo.CityVo;
import com.aswl.as.ibrs.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 市级项目数据看板controller
 * @author df
 * @date 2021/01/20 09:57
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/cityStatistics", tags = "数据看板")
@RestController
@RequestMapping("/v1/cityStatistics")
public class CityStatisticsController extends BaseController {

    private final CityService cityService;


    /**
     * 总体在线率
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return ResponseBean
     */

    @GetMapping(value = "onlineStatistics")
    @ApiOperation(value = "总体在线率", notes = "总体在线率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期", defaultValue = "1", paramType = "query", dataType = "String")
    })
    public ResponseBean<Map> getOnlineDeviceStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                       @RequestParam(value = "cityCode",required = false) String cityCode,
                                                       @RequestParam(value = "projectId",required = false) String projectId,
                                                       @RequestParam(value = "startTime",required = false) String startTime,
                                                       @RequestParam(value = "endTime",required = false) String endTime)
    {
        return new ResponseBean<>(cityService.getDevicesStatistics(kind, cityCode,projectId,startTime,endTime));


    }

    /**
     * 各区域在线率
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param parentId 父级ID
     * @param projectId 项目ID
     * @param level 级别
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return ResponseBean
     */

    @GetMapping(value = "cityOnlineStatistics")
    @ApiOperation(value = "各区域在线率", notes = "各区域在线率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "父级ID",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "level", value = "级别",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")
    })
    public ResponseBean<List<CityVo>> getCityOnlineStatistics(@RequestParam(value = "kind") String kind,
                                                              @RequestParam(value = "cityCode", required = false) String cityCode,
                                                              @RequestParam(value = "parentId",required = false) String parentId,
                                                              @RequestParam(value = "projectId",required = false) String projectId,
                                                              @RequestParam(value = "level",required = false) String level,
                                                              @RequestParam(value = "startTime") String startTime,
                                                              @RequestParam(value = "endTime") String endTime
                                                              )
    {
        return new ResponseBean<>(cityService.getCityOnlineStatistics(kind, cityCode,parentId,projectId,level,startTime,endTime));


    }


    /**
     * 在线率变化趋势
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param timeUnit     显示单位
     * @return ResponseBean
     */

    @GetMapping(value = "cityOnlineTrend")
    @ApiOperation(value = "在线率变化趋势", notes = "在线率变化趋势")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")

    })
    public ResponseBean<Object> getCityOnlineOnlineTrend(@RequestParam(value = "kind",required = false) String kind,
                                                              @RequestParam(value = "cityCode",required = false) String cityCode,
                                                              @RequestParam(value = "projectId",required = false) String projectId,
                                                              @RequestParam(value = "startTime",required = false) String startTime,
                                                              @RequestParam(value = "endTime",required = false) String endTime,
                                                              @RequestParam(value = "timeUnit",required = false) String timeUnit)
    {
        return new ResponseBean<>(cityService.getDeviceOnlineIntactTrend(kind, cityCode,projectId,startTime,endTime,timeUnit));


    }


    /**
     * 各设备厂商在线率
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @return ResponseBean
     */

    @GetMapping(value = "manufacturerOnline")
    @ApiOperation(value = "各设备厂商在线率", notes = "各设备厂商在线率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")

    })
    public ResponseBean<List<CityVo>> getManufacturerOnlineStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                           @RequestParam(value = "cityCode",required = false) String cityCode,
                                                           @RequestParam(value = "projectId",required = false) String projectId,
                                                           @RequestParam(value = "startTime",required = false) String startTime,
                                                           @RequestParam(value = "endTime",required = false) String endTime)
    {
        return new ResponseBean<>(cityService.getManufacturerOnlineStatistics(kind, cityCode,projectId,startTime,endTime));


    }



    /**
     * 各设备型号在线率
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @return ResponseBean
     */

    @GetMapping(value = "deviceModelOnline")
    @ApiOperation(value = "各设备型号在线率", notes = "各设备型号在线率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")

    })
    public ResponseBean<List<CityVo>> getDeviceModelOnlineStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                                      @RequestParam(value = "cityCode",required = false) String cityCode,
                                                                      @RequestParam(value = "projectId",required = false) String projectId,
                                                                      @RequestParam(value = "startTime",required = false) String startTime,
                                                                      @RequestParam(value = "endTime",required = false) String endTime)
    {
        return new ResponseBean<>(cityService.getDeviceModelOnlineStatistics(kind, cityCode,projectId,startTime,endTime));


    }



    /**
     * 故障类型占比
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @return ResponseBean
     */

    @GetMapping(value = "faultTypeStatistics")
    @ApiOperation(value = "故障类型占比", notes = "故障类型占比")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")

    })
    public ResponseBean<List<Map>> getFaultTypeStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                                      @RequestParam(value = "cityCode",required = false) String cityCode,
                                                                      @RequestParam(value = "projectId",required = false) String projectId,
                                                                      @RequestParam(value = "startTime",required = false) String startTime,
                                                                      @RequestParam(value = "endTime",required = false) String endTime,
                                                                      @RequestParam(value = "manufacturer",required = false) String manufacturer)
    {
        return new ResponseBean<>(cityService.getFaultTypeStatistics(kind, cityCode,projectId,startTime,endTime));


    }


    /**
     * 故障趋势分析
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param timeUnit     显示单位
     * @return ResponseBean
     */

    @GetMapping(value = "faultTrend")
    @ApiOperation(value = "故障趋势分析", notes = "故障趋势分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期",  paramType = "query", dataType = "String")

    })
    public ResponseBean<Object> getFaultTrendStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                          @RequestParam(value = "cityCode",required = false) String cityCode,
                                                          @RequestParam(value = "projectId",required = false) String projectId,
                                                          @RequestParam(value = "startTime",required = false) String startTime,
                                                          @RequestParam(value = "endTime",required = false) String endTime,
                                                          @RequestParam(value = "timeUnit",required = false) String timeUnit)
    {
       return new ResponseBean<>(cityService.getDeviceOnlineIntactTrend(kind, cityCode,projectId,startTime,endTime,timeUnit));

    }

    /**
     * 资产统计
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param parentId 父级ID
     * @param projectId     项目ID
     * @param queryDate     查询日期
     * @param deviceType     设备类型
     * @param deviceModelName    设备型号
     * @return ResponseBean
     */

    @GetMapping(value = "assetsStatistics")
    @ApiOperation(value = "资产统计", notes = "资产统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "父级ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", defaultValue = "1", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "queryDate", value = "查询日期",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceModelName", value = "设备型号",  paramType = "query", dataType = "String")


    })
    public ResponseBean<List<Map>> getAssetsStatistics(@RequestParam(value = "kind",required = false) String kind,
                                                          @RequestParam(value = "cityCode", defaultValue = "A01",required = false) String cityCode,
                                                          @RequestParam(value = "parentId",required = false) String parentId,
                                                          @RequestParam(value = "projectId",required = false) String projectId,
                                                          @RequestParam(value = "queryDate",required = false) String queryDate,
                                                          @RequestParam(value = "deviceType",required = false) String deviceType,
                                                         @RequestParam(value = "deviceModelName",required = false) String deviceModelName)
    {
        return new ResponseBean<>(cityService.getAssetsStatistics(kind, cityCode,parentId,projectId,queryDate,deviceType,deviceModelName));


    }


}
