package com.aswl.as.ibrs.api.feign;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONObject;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.*;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.common.core.vo.OsVo;
import com.github.pagehelper.PageInfo;
import feign.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aswl.as.common.core.constant.ServiceConstant;
import com.aswl.as.common.feign.config.CustomFeignConfig;
import com.aswl.as.ibrs.api.feign.factory.RegionServiceClientFallbackFactory;
import com.aswl.as.user.api.module.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 区域服务
 * @author dingfeigetDeviceBreakDown
 * @date 2019-10-14 16:19
 */

@FeignClient(value = ServiceConstant.IBRS_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = RegionServiceClientFallbackFactory.class)
public interface RegionServiceClient {

    /**
     * 根据用户对象获取区域
     *
     * @param  userList
     * @return  List<Region>
     */
    @RequestMapping(value = "/v1/region/getRegionListByUsers", method = RequestMethod.POST)
    List<Region> getRegionListByUsers(@RequestBody List<User> userList);

    /**
     * 根据ID获取区域
     * @param id 区域ID
     * @return Region
     */
    @RequestMapping(value = "/v1/region/{id}", method = RequestMethod.GET)
    Region getRegionById(@PathVariable("id") String id);


    /**
     * 根据ID获取区域
     * @param regionCode 区域ID
     * @return Region
     */
    @RequestMapping(value = "/v1/region/meta/{regionCode}", method = RequestMethod.GET)
    Region metaGetRegionById(@PathVariable("regionCode") String regionCode);
    /**
     * 根据区域ID获取区域维护人员ID
     * @param regionCode 区域ID
     * @return RegionLeader 列表
     */
    @RequestMapping(value = "/v1/regionLeader/getRegionLeaderByRegionId/{regionCode}", method = RequestMethod.GET)
    List<RegionLeader>  getRegionLeaderByRegionId(@PathVariable("regionCode") String regionCode);

    /**
     * 根据用户ID获取用户关注列表
     * @param userId 用户Id
     * @return 返回用户关注设备列表
     */
    @RequestMapping(value = "/v1/userWatch/getUserAlarmType/list/{userId}", method = RequestMethod.GET)
    List<AlarmTypeDeviceFavorite>  getAlarmTypeDeviceFavoriteByUserId(@PathVariable("userId") String userId);

    /**
     * 根据用户ID获取用户关注列表
     * @param userId 用户Id
     * @param deviceId 设备ID
     * @return 返回用户关注
     */
    @RequestMapping(value = "/v1/userWatch/getFavorite/{userId}/{deviceId}", method = RequestMethod.GET)
    AlarmTypeDeviceFavorite getFavorite(@PathVariable("userId") String userId,@PathVariable("deviceId") String deviceId);

    /**
     * 根据设备id与用户Id查询关注的告警类型
     * @param deviceId 设备Id
     * @param userId 用户Id
     * @return 返回关注告警类型数据
     */
    @RequestMapping(value = "/v1/userWatch/getUserAlarmType/{deviceId}/{userId}", method = RequestMethod.GET)
    UserWatchVo  getAlarmTypeDeviceFavoriteBydeviceId(@PathVariable("deviceId") String deviceId,@PathVariable("userId") String userId);

    /**
     * 新增区域负责人
     * @param regionLeaderDto regionLeaderDto
     * @return Boolean
     */
    @RequestMapping(value = "/v1/regionLeader", method = RequestMethod.POST)
    ResponseBean<Boolean> insertRegionLeader(@RequestBody RegionLeaderDto regionLeaderDto);


    /**
     * 根据用户id批量查询区域负责人信息
     * @param userList userList
     * @return ResponseBean
     */
    @RequestMapping(value = "/v1/regionLeader/getRegionLeaderByUserId", method = RequestMethod.POST)
    ResponseBean<List<RegionLeader>> getRegionLeaderByUserIds(@RequestBody List<User> userList);


    /**
     * 更新区域负责人
     * @param regionLeaderDto 
     * @return Boolean
     */
    @RequestMapping(value = "/v1/regionLeader/updateByUserId", method = RequestMethod.POST)
    ResponseBean<Boolean> updateRegionLeaderByUserId(@RequestBody RegionLeaderDto regionLeaderDto);


    /**
     * 根据用户ID批量删除区域负责人
     * @param ids ids
     * @return Boolean
     */
    @RequestMapping(value = "/v1/regionLeader/deleteByUserId", method = RequestMethod.POST)
    ResponseBean<Boolean> deleteRegionLeaderByUserId(@RequestBody String [] ids);

    /**
     * 根据用户ID获取区域负责人
     * @param userId
     * @return
     */
    @GetMapping(value = "/v1/regionLeader/getRegionLeaderByUserId")
    ResponseBean<RegionLeader> getRegionLeaderByUserId(@RequestParam("userId") String userId);



    //------------ 下面的接口，提供给运维端使用 运维端使用的接口，都有/os/ 如果有特殊的逻辑，才引用租户端的接口 -----------------------------------

    //TODO -- 下面是提供给运营端使用的接口 比如/os/getDeviceList 函数名特意改为osDevice1 以免到时候提交代码后，搜getDeviceList时，
    // 会出来很多getDeviceList，"/os/getDeviceList" 前面有/os/所以不会认为是原ibrs的函数，用来分清哪个是os端用的

    /**
     * 根据条件获取设备
     * @param device
     * @return
     */ //getDeviceList
    @RequestMapping(value = "/v1/device/os/getDeviceList", method = RequestMethod.POST)
    ResponseBean<List<Device>> osDevice1(@RequestBody Device device);

    /**
     * 新增设备(提供给运营端使用)
     * @param deviceDto
     * @return
     */
    @PostMapping("/v1/device/os/insertDevice")
    ResponseBean<Boolean> osDevice2(@RequestBody DeviceDto deviceDto);

    /**
     * 测试接口，测试数据传递(提供给运营端使用)
     * @param deviceDto
     * @return
     */
    @PostMapping("/v1/device/os/test")
    ResponseBean<Boolean> test(@RequestBody DeviceDto deviceDto);

    /**
     * 根据ID获取设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/device/os/findById/{id}")
    ResponseBean<DeviceVo> osDevice3(@PathVariable("id") String id,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 修改设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PostMapping("/v1/device/os/updateDevice")
    ResponseBean<Boolean> osDevice4(@RequestBody @Valid DeviceDto deviceDto);

    /**
     * 批量删除设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param device
     * @return ResponseBean
     */
    @PostMapping("/v1/device/os/deleteAll")
    ResponseBean<Boolean> osDevice5(@RequestBody Device device);

    @PostMapping("/v1/device/os/deviceList")
    ResponseBean<PageInfo<DeviceVo>> osDevice6(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               @RequestParam(value = "regionId", defaultValue = "") String regionId,
                                               @RequestBody DeviceDto deviceDto);

    /**
     * 导入设备数据(专门提供给运营端使用，不ignore原来的接口)
     * @param deviceDtos
     * @return
     */
    @PostMapping("/v1/device/os/import")
    ResponseBean<JSONObject> osDevice7(List<DeviceDto> deviceDtos);

    /**
     * 导出设备数据(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto deviceDto
     */
    @PostMapping("/v1/device/os/export")
    ResponseBean<List<Device>> osDevice8(@RequestBody DeviceDto deviceDto);


    // 上传附件等数据，把用户的上传附件复制过来，因为不能直接调用其它模块的接口，作为一个独立的程序


    /**
     * 获取上级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/device/os/getSuperiorDevice")
    ResponseBean<DeviceVo> osDevice9(@RequestParam("parentId") String parentId,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "")String randomStr);

    /**
     * 获取下级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/device/os/getSubordinateDevice")
    ResponseBean<List<DeviceVo>> osDevice10(@RequestParam("id") String id,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "")String randomStr);

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/device/os/updateParentDeviceId/{id}")
    ResponseBean<Boolean> osDevice11(@PathVariable("id") String id,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "")String randomStr);


    // 显示首页的数据,后面应该从本项目提供接口
    // 此处需要暴露接口给运营端调用
    //----------------------------------------------------------------------------------------

    /**
     * 首页设备故障趋势统计
     * @param queryType
     * @param deviceKind
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/v1/index/os/getDeviceBreakDown")
    public ResponseBean<Object> osIndex1(@RequestParam(value = "queryType", defaultValue = "month") String queryType, @RequestParam(value = "deviceKind", defaultValue = "1") Integer deviceKind,@RequestParam(value="deviceId",required = false)String deviceId
            ,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr) ;

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
    @GetMapping(value = "/v1/index/os/getDeviceLocation")
    ResponseBean<List<DeviceVo>> osIndex2(@RequestParam("longMin") double longMin, @RequestParam("longMax") double longMax,
                                          @RequestParam("latMin") double latMin, @RequestParam("latMax") double latMax,
                                          @RequestParam("devType") String devType, @RequestParam("keyWords") String keyWords,
                                          @RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 首页告警分级统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/index/os/getAlarmLevel")
    ResponseBean<Map> osIndex3(@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 首页告警分类统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/index/os/getAlarmTypes")
    public ResponseBean<List<Map>> osIndex4(@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 首页总体健康率统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/v1/index/os/getHealthy")
    ResponseBean<Integer> osIndex5(@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 地图上设备数统计（专门提供给给运营端调用）
     *
     * @return
     */
    @GetMapping(value = "/v1/index/os/queryMap")
    ResponseBean<Map> osIndex6(@RequestParam(value = OsVo.CHECK_STRING)String randomStr);

    /**
     * 首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）
     *
     * @return
     */
    @GetMapping(value = "/v1/index/os/queryOnLine")
    ResponseBean<Map> osIndex7(@RequestParam(value = "type",defaultValue = "2") String type,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);


    //---------------- 下面是提供给运维端调用的 区域相关 ------------------------------------

    /**
     * 根据用户对象获取区域 （专门提供给运营端使用,或因为运营端需要调用相关的服务，所以单独写出来了）
     *
     * @param  userList
     * @return  List<Region>
     */
    @RequestMapping(value = "/v1/region/os/getRegionListByUsers", method = RequestMethod.POST)
    List<Region> osRegion1(@RequestParam(value = "randomStr",required = false)String randomStr,@RequestBody List<User> userList);

    //-------------------  下面是提供给运维端调用的 事件状态组 ------------------------------------




    //---------------------------------下面为上级平台调用接口---------------------------------------//
    /**
     * 设备详情-->设备状态
     *
     * @param id 设备ID
     * @return ResponseBean
     */
    @GetMapping("v1/deviceMonitor/statusData/{id}")
    ResponseBean<DeviceStautsOverallVo> getDeviceStautsData(@PathVariable("id") String id);

    /**
     * 指定设备的网络状态
     * @param id
     * @return
     */
    @GetMapping("v1/deviceMonitor/getOnlineStatusByDeviceId/{id}")
    ResponseBean<Map> getOnlineStatusByDeviceId(@PathVariable("id") String id);

    @GetMapping(value = "v1/deviceMonitor/statusData/deviceDetails/{id}")
    @ApiOperation(value = "上级平台设备详情")
    ResponseBean<OpenDeviceDetailsVo> findDeviceDetails(@PathVariable("id") String id);

    @PostMapping("v1/deviceMonitor/statusData/openDeviceList")
    ResponseBean<PageInfo<LinkedHashMap>> openDeviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                         @RequestBody DeviceDto deviceDto);

    @GetMapping(value = "v1/deviceMonitor/statusData/regionList")
    @ApiOperation(value = "上级平台区域列表")
    ResponseBean<List<LinkedHashMap>> findRegionList(@RequestParam(value = "regionName",required = false) String regionName);

    @ApiOperation(value = "设置信息", notes = "设置信息")
    @PostMapping(value = "v1/deviceMonitor/statusData/updateBath")
    ResponseBean<Boolean> updateBath(@RequestBody DeviceSettingsListDto dto) ;

    //---------------------------------上面为上级平台调用接口---------------------------------------//


    //TODO 状态组这三个方法 应该转移到 运营端直接写sql，因为里面的sql 一般不会变，不用请求ibrs了
    /**
     * 根据元数据ID查询状态操作列表
     * @param eventMetadataId
     * @param randomStr
     * @return
     */
    @GetMapping("v1/eventUcStatusGroup/os/extendStatusGroupOperationList")
    ResponseBean<List<EventUcStatusOperationVo>> osEvent1(@RequestParam("eventMetadataId") String eventMetadataId,
                                                          @RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 运营端 根据设备型号ID查询时间状态组数据
     * @param deviceModelId
     * @param randomStr
     * @return
     */
    @GetMapping("v1/eventUcStatusGroup/os/extendStatusGroup")
    ResponseBean<List<EventUcStatusGroupModelVo>> osEvent2(@RequestParam("deviceModelId") String deviceModelId,
                                                           @RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);

    /**
     * 根据设备型号ID和状态组ID查询事件状态组属性
     * @param deviceModelId
     * @param groupId
     * @param randomStr
     * @return
     */
    @GetMapping("v1/eventUcStatusGroup/os/extendStatusGroupAttribute")
    ResponseBean
            <List<EventUcMetadataVo>> osEvent3(@RequestParam("deviceModelId") String deviceModelId, @RequestParam("groupId") String groupId
            ,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr);


    /**
     * 根据区域ID获取区域维护人员ID
     * @param patrolKeyId 巡更钥匙Id
     * @return RegionLeader 列表
     */
    @GetMapping("v1/regionLeader/getRegionLeaderByPatrolKeyId/{patrolKeyId}")
    ResponseBean<List<RegionLeader>> getRegionLeaderByPatrolKeyId(@PathVariable("patrolKeyId") String patrolKeyId);

    //----------------------------------------以下为运营端项目管理档案管理接口-------------------------------------------

    @GetMapping("/v1/device/os/projectList")
    ResponseBean<PageInfo<Project>> projectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                @RequestParam("project") Project project);

    @PostMapping("/v1/device/os/insertProject")
    ResponseBean<Boolean> insertProject(@RequestBody @Valid ProjectDto projectDto);

    @PostMapping(value = "/v1/device/os/updateProject")
    ResponseBean<Boolean> updateProject(@RequestBody @Valid ProjectDto projectDto);

    @PostMapping("/v1/device/os/projectStatistics")
    ResponseBean<List<StatisticsVo>> getProjectMaintainStatistics(@RequestBody DeviceAlarmDto deviceAlarmDto);

    @PostMapping("/v1/device/os/findDeviceModelKind")
    ResponseBean<List<KindVo>> findDeviceModelKind();

    @PostMapping("/v1/device/os/projectDeviceList")
    ResponseBean<List<ProjectDeviceVo>> getProjectDeviceList(@RequestBody DeviceAlarmDto deviceAlarmDto);

    @GetMapping(value = "/v1/device/os/attachments")
    ResponseBean<List<Attachment>> findAttachments(@RequestParam("projectId") String projectId);

    @PostMapping(value = "/v1/device/os/download")
    ResponseEntity<byte[]> download(@RequestParam("filePath") String filePath);

    @GetMapping(value = "/v1/device/os/modelStatistics")
    ResponseBean<List<ModelStatisticsVo>> findModelStatistics(@RequestParam("projectId") String projectId);


    @GetMapping(value = "/v1/regionLeader/isExistLeader")
    ResponseBean<Boolean> getIsExistLeader(@RequestParam("regionCode") String regionCode);

    @DeleteMapping(value = "/v1/project/{projectId}")
    ResponseBean<Boolean> deleteProjectById(@PathVariable("projectId") String projectId);

    @GetMapping(value = "/v1/project/findByTenantCode")
    ResponseBean<List<Project>> findProjectByTenantCode(@RequestParam("tenantCode") String tenantCode);

    @PostMapping(value = "/v1/userWatch/updateAlarmTypeUserFavorite")
    ResponseBean<Boolean> updateAlarmTypeUserFavorite(@RequestBody AlarmTypeUserFavorite alarmTypeUserFavorite);
    
    @GetMapping(value = "/v1/userWatch/findByUserId")
     ResponseBean<AlarmTypeUserFavorite> findByUserId(@RequestParam("userId") String userId);
    
    @GetMapping(value = "/v1/alarmLevel/deleteByTenantCode")
    ResponseBean<Boolean> deleteAlarmLevelByTenantCode(@RequestParam("tenantCode") String tenantCode);

    @GetMapping(value = "/v1/alarmOrderHandle/deleteByTenantCode")
    ResponseBean<Boolean> deleteAlarmOrderHandleByTenantCode(@RequestParam("tenantCode") String tenantCode);
}
