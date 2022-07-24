package com.aswl.as.asos.modules.ibrs.controller;

import com.aswl.as.asos.modules.ibrs.service.IOsIndexService;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/osIndex")
@Api("首页资料")
public class OsIndexController extends AbstractController {

    // 提供首页接口供前端使用 // ibrs 开放出来的接口，需要有个验证，因为用同一个数据库，所以两边都能读到相同的数据，

    @Autowired
    private IOsIndexService iOsIndexService;


    /**
     * 首页设备故障趋势统计
     * @param queryType
     * @param deviceKind
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/os/getDeviceBreakDown")
    @ApiOperation("首页设备故障趋势统计")
    public ResponseBean<Object> osIndex1(@RequestParam(value = "queryType", defaultValue = "month") String queryType, @RequestParam(value = "deviceKind", defaultValue = "1") int deviceKind, @RequestParam(value="deviceId",required = false)String deviceId)
    {
        return iOsIndexService.osIndex1(queryType,deviceKind,deviceId);
    }

    /**
     * 根据经纬度查询区域设备信息（专门提供给给运营端调用）
     *
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @param devType  设备类型
     * @param keyWords 关键字
     * @return
     */
    @GetMapping(value = "/os/getDeviceLocation")
    @ApiOperation("根据经纬度查询区域设备信息")
    public ResponseBean<List<DeviceVo>> osIndex2(@RequestParam("longMin") double longMin, @RequestParam("longMax") double longMax,
                                                 @RequestParam("latMin") double latMin, @RequestParam("latMax") double latMax,
                                                 @RequestParam("devType") String devType, @RequestParam("keyWords") String keyWords)
    {
        return iOsIndexService.osIndex2(longMin,longMax,latMin,latMax,devType,keyWords);
    }


    /**
     * 首页告警分级统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/os/getAlarmLevel")
    @ApiOperation("首页告警分级统计")
    public ResponseBean<Map> osIndex3()
    {
        return iOsIndexService.osIndex3();
    }

    /**
     * 首页告警分类统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/os/getAlarmTypes")
    @ApiOperation("首页告警分类统计")
    public ResponseBean<List<Map>> osIndex4()
    {
        return iOsIndexService.osIndex4();
    }

    /**
     * 首页总体健康率统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/os/getHealthy")
    @ApiOperation("首页总体健康率统计")
    public ResponseBean<Integer> osIndex5()
    {
        return iOsIndexService.osIndex5();
    }

    /**
     * 地图上设备数统计（专门提供给给运营端调用）
     *
     * @return
     */
    @GetMapping(value = "/os/queryMap")
    @ApiOperation("地图上设备数统计")
    public ResponseBean<Map> osIndex6()
    {
        return iOsIndexService.osIndex6();
    }

    /**
     * 首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）
     *
     * @return
     */
    @GetMapping(value = "/os/queryOnLine")
    @ApiOperation("首页报障箱和摄像头统计(2报障箱 3摄像机)")
    public ResponseBean<Map> osIndex7(@RequestParam(value = "type",defaultValue = "2") String type)
    {
        return iOsIndexService.osIndex7(type);
    }

}
