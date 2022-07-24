package com.aswl.as.ibrs.api.feign.fallback;
import cn.hutool.json.JSONObject;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.*;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.user.api.module.User;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志断路器实现
 *
 * @author aswl.com
 * @date 2019/3/23 23:39
 */
@Slf4j
@Component
public class RegionServiceClientFallbackImpl implements RegionServiceClient {

    private Throwable throwable;

    /**
     * 根据用户对象获取区域
     * @param  userList
     * @return List<Region>
     */
    @Override
    public List<Region> getRegionListByUsers(List<User> userList) {
        log.error("feign 根据用户对象获取区域信息失败, {}, {}", userList, throwable);
        return null;
    }

    /**
     *根据ID获取区域
     * @param id 区域ID
     * @return Region
     */
    @Override
    public Region getRegionById(String id) {
        log.error("feign 获取区域信息失败, {}, {}", id, throwable);
        return null;
    }

    /**
     * 根据区域ID获取区域维护人员ID
     * @param regionId 区域ID
     * @return RegionLeader 列表
     */
    @Override
    public List<RegionLeader> getRegionLeaderByRegionId(String regionId) {
        log.error("feign 获取区域维护人员ID失败, {}, {}", regionId, throwable);
        return null;
    }

    /**
     * 根据条件获取设备
     * @param device
     * @return
     */
    @Override
    public ResponseBean<List<Device>> osDevice1(Device device)
    {
        log.error("feign 获取设备列表失败, {}, {}", device, throwable);
        return new ResponseBean<>(new Exception("获取设备列表失败："+throwable.getMessage()));
    }

    /**
     * 新增设备
     * @param deviceDto
     * @return
     */
    public ResponseBean<Boolean> osDevice2(DeviceDto deviceDto)
    {
        log.error("feign 新增设备失败, {}, {}", deviceDto, throwable);
        return new ResponseBean(new Exception("新增设备失败："+throwable.getMessage()));
    }

    /**
     * 测试接口
     * @param deviceDto
     * @return
     */
    public ResponseBean<Boolean> test(DeviceDto deviceDto)
    {
        log.error("feign 测试接口失败, {}, {}", deviceDto, throwable);
        return new ResponseBean(new Exception("测试接口失败："+throwable.getMessage()));
    }

    /**
     * 根据ID获取设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<DeviceVo> osDevice3(String id,String randomStr)
    {
        log.error("feign 获取设备失败, {}, {}", id, throwable);
        return new ResponseBean(new Exception("获取设备失败："+throwable.getMessage()));
    }

    /**
     * 修改设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice4(DeviceDto deviceDto)
    {
        log.error("feign 更新设备失败, {}, {}", deviceDto, throwable);
        return new ResponseBean(new Exception("更新设备失败："+throwable.getMessage()));
    }

    /**
     * 批量删除设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param device
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice5(Device device)
    {
        log.error("feign 批量删除设备失败, {}, {}", device, throwable);
        return new ResponseBean("批量删除设备失败："+throwable.getMessage());
    }

    public ResponseBean<PageInfo<DeviceVo>> osDevice6(String pageNum, String pageSize, String sort, String order, String regionId, DeviceDto deviceDto) {
        log.error("feign 获取设备列表设备失败, {}, {}, {}", deviceDto,regionId, throwable);
        return new ResponseBean(new Exception("获取设备列表设备失败："+throwable.getMessage()));
    }

    /**
     * 导入设备数据(专门提供给运营端使用，不ignore原来的接口)
     * @param deviceDtos
     * @return
     */
    public ResponseBean<JSONObject> osDevice7(List<DeviceDto> deviceDtos)
    {
        log.error("feign 传递设备导入数据失败, {}, {}", deviceDtos, throwable);

        ResponseBean responseBean=new ResponseBean();

        JSONObject result = new JSONObject(5);
        result.put("totalCount", deviceDtos.size());
        result.put("errorCount", deviceDtos.size());
        result.put("successCount", 0);
        result.put("isAllTrue",Boolean.FALSE);
        result.put("title","文件导入导入失败，传输中出现问题。");
        result.put("msg", "总上传行数：" + 0 + "，已导入行数：" + 0 + "，错误行数：" + deviceDtos.size());
        result.put("fileName", "");
        responseBean.setData(result);
        responseBean.setCode(ResponseBean.FAIL);
        responseBean.setMsg("文件导入失败，传输时出现错误");
        return responseBean;

    }

    /**
     * 导出设备数据(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto deviceDto
     */
    public ResponseBean<List<Device>> osDevice8(DeviceDto deviceDto)
    {
        log.error("feign 获取设备导出数据失败, {}, {}", deviceDto, throwable);
        return new ResponseBean(new Exception("获取设备导出数据失败"));
    }


    /**
     * 获取上级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param parentId
     * @return ResponseBean
     */
    public ResponseBean<DeviceVo> osDevice9(String parentId,String randomStr)
    {
        log.error("feign 获取上级设备信息失败, {}, {}", parentId, throwable);
        return new ResponseBean(new Exception("获取上级设备信息失败"));
    }

    /**
     * 获取下级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<List<DeviceVo>> osDevice10(String id,String randomStr)
    {
        log.error("feign 获取下级设备信息失败, {}, {}", id, throwable);
        return new ResponseBean(new Exception("获取下级设备信息失败"));
    }

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice11(String id,String randomStr)
    {
        log.error("feign 根据设备id更新上级设备ID为空失败, {}, {}", id, throwable);
        return new ResponseBean(new Exception("根据设备id更新上级设备ID为空失败"));
    }

    //-------------------------------------------------------------------------------------------------------------------

    // 提供给首页调用

    /**
     * 首页设备故障趋势统计
     * @param queryType
     * @param deviceKind
     * @param deviceId
     * @return
     */
    public ResponseBean<Object> osIndex1(String queryType,Integer deviceKind, String deviceId,String randomStr)
    {
        log.error("feign 获取 首页设备故障趋势统计 数据失败, {}, {}, {}, {}", queryType,deviceKind,deviceId, throwable);
        return new ResponseBean<>(new Exception("获取 首页设备故障趋势统计 数据失败："+throwable.getMessage()));
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
    /* */
    public ResponseBean<List<DeviceVo>> osIndex2(double longMin,  double longMax,
                                                 double latMin, double latMax,
                                                 String devType, String keyWords,String randomStr)
    {
        //longMin,longMax,latMin,latMax,devType,keyWords,
        log.error("feign 获取 根据经纬度查询区域设备信息 数据失败, {}", throwable);
        return new ResponseBean<>(new Exception("获取 根据经纬度查询区域设备信息 数据失败："+throwable.getMessage()));
    }


    /**
     * 首页告警分级统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Map> osIndex3(String randomStr)
    {
        log.error("feign 获取 首页告警分级统计 数据失败, {}", throwable);
        return new ResponseBean<>(new Exception("获取 首页告警分级统计 数据失败："+throwable.getMessage()));
    }

    /**
     * 首页告警分类统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<List<Map>> osIndex4(String randomStr)
    {
        log.error("feign 获取 首页告警分类统计 数据失败, {}", throwable);
        return new ResponseBean<>(new Exception("获取 首页告警分类统计 数据失败："+throwable.getMessage()));
    }

    /**
     * 首页总体健康率统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Integer> osIndex5(String randomStr)
    {
        log.error("feign 获取 首页总体健康率统计 数据失败, {}", throwable);
        return new ResponseBean<>(new Exception("获取 首页总体健康率统计 数据失败："+throwable.getMessage()));
    }

    /**
     * 地图上设备数统计（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex6(String randomStr)
    {
        log.error("feign 获取 地图上设备数统计 数据失败, {}", throwable);
        return new ResponseBean<>(new Exception("获取 地图上设备数统计 数据失败："+throwable.getMessage()));
    }

    /**
     * 首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex7(String type,String randomStr)
    {
        log.error("feign 获取 首页报障箱和摄像头统计(2报障箱 3摄像机) 数据失败, {}, {}", type,throwable);
        return new ResponseBean<>(new Exception("获取 首页报障箱和摄像头统计(2报障箱 3摄像机) 数据失败："+throwable.getMessage()));
    }

    @Override
    public ResponseBean<DeviceStautsOverallVo> getDeviceStautsData(String id) {
        log.error("feign 获取 设备运行参数失败, {}, {}", id,throwable);
        return new ResponseBean<>(new Exception("获取 设备运行参数失败："+throwable.getMessage()));
    }

    @Override
    public ResponseBean<Map> getOnlineStatusByDeviceId(String id) {
        log.error("feign 获取 设备在线状态失败, {}, {}", id,throwable);
        return new ResponseBean<>(new Exception("获取 设备在线状态失败："+throwable.getMessage()));
    }

    @Override
    public ResponseBean<OpenDeviceDetailsVo> findDeviceDetails(String id) {
        return null;
    }

    @Override
    public ResponseBean<PageInfo<LinkedHashMap>> openDeviceList(String pageNum, String pageSize, DeviceDto deviceDto) {
        return null;
    }

    @Override
    public ResponseBean<List<LinkedHashMap>> findRegionList(String regionName) {
        return null;
    }

    @Override
    public ResponseBean<Boolean> updateBath(DeviceSettingsListDto dto) {
        return null;
    }

    /**
     * 根据设备id与用户Id查询关注的告警类型
     * @param deviceId 设备Id
     * @param userId 用户Id
     * @return 返回关注告警类型数据
     */
    @Override
    public UserWatchVo getAlarmTypeDeviceFavoriteBydeviceId(String deviceId, String userId) {
        log.error("feign 获取用户设备关注告警类型错误, {},{},{}", deviceId,userId, throwable);
        return null;
    }


    /**
     * 新增区域负责人
     * @param regionLeaderDto regionLeaderDto
     * @return Boolean
     */
    @Override
    public  ResponseBean<Boolean> insertRegionLeader (RegionLeaderDto regionLeaderDto) {
        log.error("feign 新增区域负责人错误, {},{},{}", regionLeaderDto, throwable);
        return null;
    }

    /**
     * 根据用户id批量查询区域负责人信息
     * @param userList userList
     * @return list
     */
    @Override
    public ResponseBean<List<RegionLeader>> getRegionLeaderByUserIds(@RequestBody List<User> userList){
        log.error("feign 根据用户id批量查询区域负责人信息错误, {},{},{}", userList, throwable);
        return null;
    }

    /**
     * 根据用户id更新区域负责人信息
     * @param regionLeaderDto
     * @return Boolean
     */
    @Override
    public ResponseBean<Boolean> updateRegionLeaderByUserId(@RequestBody RegionLeaderDto regionLeaderDto){
        log.error("feign 根据用户id更新区域负责人信息错误, {},{},{}", regionLeaderDto, throwable);
        return null;
    }


    /**
     * 根据用户ID批量删除区域负责人
     * @param ids ids
     * @return Boolean
     */
    
    public ResponseBean<Boolean> deleteRegionLeaderByUserId(@RequestBody String [] ids){
        log.error("feign 根据用户ID批量删除区域负责人信息错误, {},{},{}", ids, throwable);
        return null;
    }

    /**
     * 根据用户ID获取区域负责人
     * @param userId
     * @return
     */
    @Override
    public ResponseBean<RegionLeader> getRegionLeaderByUserId(String userId) {
        log.error("feign 根据用户ID获取区域负责人, {},{}", userId, throwable);
        return null;
    }

    /**
     * 根据设备id与用户Id查询关注的告警类型
     * @param userId 用户Id
     * @return 返回关注告警类型数据
     */
    @Override
    public List<AlarmTypeDeviceFavorite> getAlarmTypeDeviceFavoriteByUserId(String userId) {
        log.error("feign 获取用户设备关注告警类型列表错误, {},{}",userId, throwable);
        return null;
    }

    @Override
    public AlarmTypeDeviceFavorite getFavorite(String userId, String deviceId) {
        log.error("feign 获取用户设备关注列表错误, {},{}",userId,deviceId, throwable);
        return null;
    }

    /**
     * 根据用户对象获取区域(运营端使用)
     * @param  userList
     * @return List<Region>
     */
    @Override
    public List<Region> osRegion1(String randomStr,List<User> userList) {
        log.error("feign的osRegion1函数根据用户对象获取区域信息失败, {}, {}", userList, throwable);
        return null;
    }

    public ResponseBean<List<EventUcStatusOperationVo>> osEvent1(String eventMetadataId, String randomStr)
    {
        log.error("feign的osEvent1根据元数据ID查询状态操作列表, {}, {}", eventMetadataId, throwable);
        return new ResponseBean<>(new Exception("根据元数据ID查询状态操作列表 失败："+throwable.getMessage()));
    }

    public ResponseBean<List<EventUcStatusGroupModelVo>> osEvent2(String deviceModelId, String randomStr)
    {
        log.error("feign的osEvent2 根据设备型号ID查询时间状态组数据, {}, {}", deviceModelId, throwable);
        return new ResponseBean<>(new Exception("根据设备型号ID查询时间状态组数据 失败："+throwable.getMessage()));
    }

    /**
     * 根据设备型号ID和状态组ID查询事件状态组属性
     * @param deviceModelId
     * @param groupId
     * @param randomStr
     * @return
     */
    public ResponseBean
            <List<EventUcMetadataVo>> osEvent3(String deviceModelId, String groupId
            ,String randomStr)
    {
        log.error("feign的osEvent3 根据设备型号ID和状态组ID查询事件状态组属性, {}, {}, {}", deviceModelId,groupId, throwable);
        return new ResponseBean<>(new Exception("根据设备型号ID和状态组ID查询事件状态组属性 失败："+throwable.getMessage()));
    }

    @Override
    public ResponseBean<List<RegionLeader>> getRegionLeaderByPatrolKeyId(String patrolKeyId) {
        log.error("feign根据巡更Id获取区域责任人错误, {}, {}", patrolKeyId, throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public Region metaGetRegionById(String id) {
        log.error("feign 获取区域名称错误, {},{}",id, throwable);
        return null;
    }

    @Override
    public ResponseBean<PageInfo<Project>> projectList(String pageNum, String pageSize, String sort, String order, Project project) {
        log.error("feign 获取项目列表错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> insertProject(@Valid ProjectDto projectDto) {
        log.error("feign 新增项目错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> updateProject(@Valid ProjectDto projectDto) {
        return null;
    }

    @Override
    public ResponseBean<List<StatisticsVo>> getProjectMaintainStatistics(DeviceAlarmDto deviceAlarmDto) {
        log.error("feign 查看项目维护统计错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseBean<List<KindVo>> findDeviceModelKind() {
        log.error("feign 查看设备型号错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseBean<List<ProjectDeviceVo>> getProjectDeviceList(DeviceAlarmDto deviceAlarmDto) {
        log.error("feign 查看设备清单错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseBean<List<Attachment>> findAttachments(String projectId) {
        log.error("feign 查看附件列表错误, {},{}","", throwable);
        return null;
    }

    @Override
    public ResponseEntity<byte[]> download(String filePath) {
        return null;
    }

    @Override
    public ResponseBean<List<ModelStatisticsVo>> findModelStatistics(String projectId) {
        log.error("feign获取项目设备清单型号信息失败, {},{}",projectId, throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> getIsExistLeader(String regionCode) {
        log.error("feign根据区域编码查询区域负责人是否存在信息失败, {},{}",regionCode, throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> deleteProjectById(String projectId) {
        log.error("feign根据项目表ID删除项目表信息失败, {},{}",projectId, throwable);
        return null;
    }

    @Override
    public ResponseBean<List<Project>> findProjectByTenantCode(String tenantCode) {
        log.error("feign根据租户编码查询项目信息失败, {},{}",tenantCode, throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> updateAlarmTypeUserFavorite(AlarmTypeUserFavorite alarmTypeUserFavorite) {
        log.error("feign用户告警类型订阅设置失败, {},{}",alarmTypeUserFavorite, throwable);
        return null;
    }
    @Override
    public ResponseBean<AlarmTypeUserFavorite> findByUserId(@RequestParam("userId") String userId) {
        log.error("feign查看用户订阅的告警类型失败, {},{}",userId, throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> deleteAlarmLevelByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        log.error("feign根据租户编码删除报警级别失败, {},{}",tenantCode, throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> deleteAlarmOrderHandleByTenantCode(@RequestParam("tenantCode") String tenantCode) {
        log.error("feign根据租户编码删除工单设置失败, {},{}",tenantCode, throwable);
        return null;
    }
}
