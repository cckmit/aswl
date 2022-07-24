package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.CityDto;
import com.aswl.as.ibrs.api.module.City;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.service.CityService;
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
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 城市controller
 *
 * @author hzj
 * @date 2021/01/13 14:51
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/city", tags = "城市")
@RestController
@RequestMapping("/v1/city")
public class CityController extends BaseController {
    private final CityService cityService;

    /**
     * 城市树形菜单
     *
     * @return ResponseBean
     */
    @GetMapping("cityTree")
    @ApiOperation(value = "城市树形菜单", notes = "城市树形菜单")
    public ResponseBean<List<City>> findCityTree() {
        return new ResponseBean<>(cityService.findCityTree());
    }


    /**
     * 查询网络拓扑图
     *
     * @param projectId 项目ID
     * @param cityCodes 城市编码集合
     * @return list
     */
    @GetMapping(value = "topMaps")
    @ApiOperation(value = "查询网络拓扑图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", paramType = "query", dataType = "String")
    })
    public ResponseBean<List<CityProjectDeviceVo>> getCityTree(@RequestParam(value = "projectId",required = false) String projectId,
                                                               @RequestParam(value = "cityCodes" ,required = false) String cityCodes) {
        return  new ResponseBean<>(cityService.getCityTree(cityCodes,projectId));
    }


    /**
     * 区域项目导航城市信息
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param parentId 父级ID
     * @param cityCode 城市编码
     * @return ResponseBean
     */

    @GetMapping(value = "cityRate")
    @ApiOperation(value = "城市在线率完好率信息", notes = "城市在线率完好率信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "父级ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码",  paramType = "query", dataType = "String")
    })
    public ResponseBean
            <List<CityVo>> getCityRate(@RequestParam("kind") String kind, @RequestParam(value = "parentId", required = false) String parentId, @RequestParam(value = "cityCode",required = false) String cityCode) {
        return new ResponseBean<>(cityService.getCityRate(kind, parentId, cityCode));
    }

    /**
     * 区域项目导航项目信息
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @return ResponseBean
     */

    @GetMapping(value = "projectRate")
    @ApiOperation(value = "区域项目导航项目信息", notes = "区域项目导航项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String")
    })
    public ResponseBean
            <List<ProjectVo>> getProjectRate(@RequestParam(value = "kind",required = false) String kind, @RequestParam(value = "cityCode", defaultValue = "A01") String cityCode) {
        return new ResponseBean<>(cityService.getProjectRate(kind,cityCode));
    }

    /**
     * 地图在线率完好率统计
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return ResponseBean
     */

    @GetMapping(value = "mapOnlineIntact")
    @ApiOperation(value = "地图在线率完好率统计", notes = "地图在线率完好率统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID",  paramType = "query", dataType = "String")
    })
    public ResponseBean
            <Map> getMapOnlineIntactStatistics(@RequestParam("kind") String kind, @RequestParam(value = "cityCode") String cityCode,
                                               @RequestParam(value = "projectId") String projectId) {
        return new ResponseBean<>(cityService.getMapOnlineIntactStatistics(kind, cityCode,projectId));
    }



    /**
     * 总体健康率
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return ResponseBean
     */

    @GetMapping(value = "healthyRate")
    @ApiOperation(value = "总体健康率", notes = "总体健康率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectID", value = "项目ID",  paramType = "query", dataType = "String")
    })
    public ResponseBean
            <Map> getHealthyRate(@RequestParam(value = "kind",required = false) String kind, @RequestParam(value = "cityCode", defaultValue = "A01") String cityCode,
                                                                    @RequestParam(value = "projectId", required = false) String projectId) {
        return new ResponseBean<>(cityService.getHealthyRate(kind, cityCode,projectId));
    }


    /**
     * 故障类型排名
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return ResponseBean
     */

    @GetMapping(value = "faultType")
    @ApiOperation(value = "故障类型排名", notes = "故障类型排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "String")
    })
    public ResponseBean
            <List<Map>> getFaultType(@RequestParam("kind") String kind, @RequestParam(value = "cityCode") String cityCode, @RequestParam(value = "projectId") String projectId) {
        return new ResponseBean<>(cityService.getFaultType(kind, cityCode,projectId));
    }

    /**
     * 故障设备型号排名
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return ResponseBean
     */

    @GetMapping(value = "faultModel")
    @ApiOperation(value = "故障设备型号排名", notes = "故障设备型号排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "String")
    })
    public ResponseBean
            <List<Map>> getFaultModel(@RequestParam("kind") String kind, @RequestParam(value = "cityCode") String cityCode,@RequestParam(value = "projectId") String projectId) {
        return new ResponseBean<>(cityService.getFaultModel(kind, cityCode,projectId));
    }

    /**
     * 近30天在线率完好率总体变化趋势
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param timeUnit     显示单位
     * @return ResponseBean
     */
    @ApiOperation(value = "近30天在线率完好率总体变化趋势")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码",  paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "timeUnit", value = "显示单位", paramType = "query", dataType = "String"),

    })
    @GetMapping(value = "onlineIntactTrend")
    public ResponseBean<Object> getDeviceOnlineIntactTrend(
                                                           @RequestParam(value = "kind", defaultValue = "2") String kind,
                                                           @RequestParam(value = "cityCode",required = false) String cityCode,
                                                           @RequestParam(value = "projectId",required = false) String projectId,
                                                           @RequestParam(value = "startTime",required = false) String startTime,
                                                           @RequestParam(value = "endTime",required = false) String endTime,
                                                           @RequestParam(value = "timeUnit",required = false) String timeUnit) {



           return new ResponseBean<>(cityService.getDeviceOnlineIntactTrend(kind,cityCode,projectId,startTime,endTime,timeUnit));

    }


    /**
     * 设备修复率查询
     *
     * @param kind      2 传输箱 3:摄像头
     * @param cityCode  城市编码
     * @param projectId 项目ID
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return ResponseBean
     */

    @GetMapping(value = "repairRate")
    @ApiOperation(value = "设备修复率查询", notes = "设备修复率查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱 ,3,摄像机)", defaultValue = "2", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityCode", value = "城市编码", defaultValue = "A01", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "projectId", value = "项目ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始日期", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束日期", paramType = "query", dataType = "String")
    })
    public ResponseBean
            <List<DeviceRepairRateVo>> findRepairRate(@RequestParam(value = "kind") String kind,
                                                      @RequestParam(value = "cityCode") String cityCode,
                                                      @RequestParam(value = "projectId",required = false) String projectId,
                                                      @RequestParam(value = "startTime") String startTime,
                                                      @RequestParam(value = "endTime") String endTime) {
        return new ResponseBean<>(cityService.findRepairRate(kind, cityCode, projectId, startTime, endTime));
    }

}
