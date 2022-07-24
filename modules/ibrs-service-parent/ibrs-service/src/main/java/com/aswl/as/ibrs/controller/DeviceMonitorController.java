package com.aswl.as.ibrs.controller;

import cc.eguid.commandManager.CommandManagerImpl;
import cc.eguid.commandManager.commandbuidler.CommandBuidlerFactory;
import cc.eguid.commandManager.data.CommandTasker;
import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.enums.StreamType;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.properties.SysProperties;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.ibrs.api.dto.DeviceAlarmConditionDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsListDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.iot.dto.ContentBody;
import com.aswl.iot.dto.constant.MQConstants;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 设备监控controller
 *
 * @author jk
 * @version 1.0.0
 * @create 2019/10/15 16:23
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceMonitor", tags = "设备监控")
@RestController
@RequestMapping("/v1/deviceMonitor")
public class DeviceMonitorController extends BaseController {
    private final RegionService regionService;
    private final DeviceDetailsService deviceDetailsService;
    private final DeviceService deviceService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SysProperties sysProperties;
    private final UserServiceClient userServiceClient;
    private final ProjectService projectService;
    private final DeviceSettingsService deviceSettingsService;
    private final EventUcMetadataService metadataService;
    private final MQSender mqSender;
    private final DeviceEventAlarmService deviceEventAlarmService;
    /**
     * 条件查询区域设备
     *
     * @param parentId
     * @param query
     * @return
     */
    @GetMapping(value = "findByParentId")
    @ApiOperation(value = "条件查询区域设备")
    public ResponseBean<List<RegionDeviceVo>> findByParentId(@RequestParam(value = "parentId", defaultValue = "-1") String parentId,
                                                             @RequestParam(value = "query", defaultValue = "") String query,
                                                             @RequestParam(value = "regionCode", defaultValue = "") String regionCode,
                                                             @RequestParam(value = "kind", defaultValue = "") String kind) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    throw new CommonException("当前用户暂无区域");
                }
                regionCode = userRegionCode;
            }
        }
        if (regionCode != null && !"".equals(regionCode)) {
            parentId = regionService.findRegionId(regionCode, tenantCode, projectId);
        }
        List<RegionDeviceVo> regions = regionService.findByParentId("", query, regionCode, tenantCode, projectId,kind);

        if (query != null && !"".equals(query)) {
            List<RegionDeviceVo> list = new ArrayList<>();
            if (regions != null) {
                for (RegionDeviceVo region : regions) {
                    if (region.getType() != 0) {
                        list.add(region);
                    }
                }
            }
            return new ResponseBean<>(list);
        } else {
            return new ResponseBean<>(TreeUtil.buildTree(regions, parentId));
        }
    }

    /**
     * 条件查询区域设备（带有项目）
     *
     * @param parentId
     * @param query
     * @return
     */
    @GetMapping(value = "newFindByParentId")
    @ApiOperation(value = "条件查询区域设备（含有项目，但是如果项目为空，就返回原来的数据）")
    public ResponseBean<List<RegionDeviceVo>> newFindByParentId(@RequestParam(value = "parentId", defaultValue = "-1") String parentId,
                                                                @RequestParam(value = "query", defaultValue = "") String query,
                                                                @RequestParam(value = "regionCode", defaultValue = "") String regionCode) {

        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();
            projectId = SysUtil.getProjectId();
        }

        //封装一下,在所有元素最顶端，添加对应的项目,type为-1,位置等使用顶端区域的数据
        ResponseBean<List<RegionDeviceVo>> r = findByParentId(parentId, query, regionCode,"");

        if (r.getData() != null && r.getData().size() > 0) {

            //如果是树，添加一个节点
            // 这里判断一下，如果是运营端，就不用添加项目节点了
            boolean IS_CLOUD = CloudCommon.isCloud();
            if (IS_CLOUD && StringUtils.isEmpty(query)) {

                Project tempP = new Project();
                tempP.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
                tempP.setTenantName(tenantCode);
                List<Project> pList = projectService.findList(tempP);
                Map<String, Project> projectMap = new HashMap<>();
                for (Project temp : pList) {
                    projectMap.put(temp.getProjectId(), temp);
                }

                //循环添加
                List<RegionDeviceVo> list = r.getData();
                List<RegionDeviceVo> result = new ArrayList<RegionDeviceVo>();
                //返回的是树
                for (RegionDeviceVo temp : list) {
                    //返回的是树的话，就在每个根节点前添加一个项目节点
                    Project p = projectMap.get(temp.getProjectId());
                    if (p == null) {
                        result.add(temp);
                    } else {
                        RegionDeviceVo pVo = new RegionDeviceVo();
                        String randomId = IdGen.snowflakeId();
                        pVo.setType(-1);
                        pVo.setId(temp.getProjectId() == null ? randomId : temp.getProjectId());
                        pVo.setName(p.getProjectName());
                        pVo.setCode(randomId);
                        pVo.setIsOnline(0);
                        pVo.setIp("0");
                        pVo.setIconSkin("");
                        pVo.setLatitude(temp.getLatitude());
                        pVo.setLatitude(temp.getLatitude());
                        pVo.setAlarmLevel(0);
                        pVo.setParentId("-1");
                        pVo.add(temp);
                        result.add(pVo);
                    }
                }

                //设置数据
                r.setData(result);
            }
        }
        return r;
    }


    /**
     * 查询区域拓扑图
     *
     * @param parentId
     * @return
     */
    @GetMapping(value = "regionTrees")
    @ApiOperation(value = "查询区域拓扑图")
    public ResponseBean<List<RegionDeviceTree>> getRegionTree(@RequestParam(value = "parentId",required = false) String parentId,
                                                              @RequestParam(value = "regionCode") String regionCode, @RequestParam(value = "projectId", required = false) String queryProjectId) {
        Stopwatch watch = Stopwatch.createStarted();
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    throw new CommonException("当前用户暂无区域");
                }
                regionCode = userRegionCode;
            }
        }
        if (regionCode != null && !"".equals(regionCode)) {
            parentId = regionService.findRegionId(regionCode, tenantCode, projectId);
        }
        if(queryProjectId != null && !"".equals(queryProjectId)){
            projectId = queryProjectId;
        }
        //顶级区域
        List<RegionDeviceTree> top = regionService.getRegionTree(parentId, regionCode, tenantCode, projectId);
        //所有区域设备集合
        List<RegionDeviceTree> all = regionService.getRegionTree(null, regionCode, tenantCode, projectId);
        for (RegionDeviceTree regionDeviceTree : top) {
            recursiveTree(regionDeviceTree, all);
        }
        // for (RegionDeviceTree regionDeviceTree : regionTree) {
        //     regionDeviceTree.setChildren(regionService.getRegionTree(regionDeviceTree.getId(), regionCode, tenantCode, projectId));
        //     treeList.add(regionDeviceTree);
        // }
        watch.stop();
        log.info("Query Region-device Tree Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(top);
    }

    /**
     * 查询区域拓扑图
     *
     * @param parentId
     * @return
     */
    @GetMapping(value = "newGetRegionTree")
    @ApiOperation(value = "查询区域拓扑图（含有项目，但是如果项目为空，就返回原来的数据,普通节点点击的时候type为0,type为1的时候是点击那些项目，type为2，就是初始化的时候用来查的）")
    public ResponseBean<List<RegionDeviceTree>> newGetRegionTree(@RequestParam(value = "parentId", defaultValue = "-1") String parentId,
                                                                 @RequestParam(value = "regionCode") String regionCode,
                                                                 @RequestParam(value = "type", required = false, defaultValue = "0") String type) {

        String tenantCode = null;
        String projectId = null;

        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }

        ResponseBean<List<RegionDeviceTree>> r;
        if ("1".equals(type)) {
            //强制用projectId来查
            r = getRegionTree("-1", regionCode, parentId);
        } else {
            r = getRegionTree(parentId, regionCode, null);
        }
        return r;
    }


    /**
     * 获取设备详细基本信息
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = "获取设备基本信息")
    public ResponseBean<DeviceVo> getDeviceBasicDetail(@PathVariable("id") String id) {
        DeviceVo deviceVo = deviceDetailsService.findById(id);
        //需要设置是否有视频

        return new ResponseBean<>(deviceVo);
    }


    /**
     * 设备详细信息
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "{id}")
    @ApiOperation(value = "获取设备详情")
    public ResponseBean<DeviceDetailsVo> getDeviceDetail(@PathVariable("id") String id) {
        DeviceDetailsVo deviceDetails = deviceDetailsService.getDeviceDetail(id);
        return new ResponseBean<>(deviceDetails);
    }


    /**
     * 设备详情-->修改经纬度
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "修改经纬度")
    public ResponseBean<Boolean> updateLatLng(@RequestBody @Valid DeviceDto deviceDto) {
        Device device = new Device();
        BeanUtils.copyProperties(deviceDto, device);
        return new ResponseBean<>(deviceDetailsService.update(device) > 0);
    }


    /**
     * 设备详情-->设备状态
     *
     * @param id 设备ID
     * @return ResponseBean
     */
    @GetMapping("/statusData/{id}")
    @ApiOperation(value = "查询设备状态")
    public ResponseBean<DeviceStautsOverallVo> getDeviceStautsData(@PathVariable("id") String id) {
        return new ResponseBean<>(deviceDetailsService.getDeviceStatusData(id));
    }


    @GetMapping("/startPushStream")
    @ApiOperation(value = "查看视频")
    public ResponseBean<List> getCameraVideo(@RequestParam(value = "id", required = true) String id,
                                             @RequestParam(value = "type", required = true) String type) {
        Object redis_output = redisTemplate.opsForValue().get(CommonConstant.REDIS_VIDEO_STREAM_OUTPUT);
        Object redis_flv_output = redisTemplate.opsForValue().get(CommonConstant.REDIS_VIDEO_STREAM_FLV_OUTPUT);
        if (!CloudCommon.isCloud()) {
            CommandManagerImpl manager = CommandManagerImpl.getInstance();
            if (type != null && type.equals("2")) {
                List<VideoStreamVo> list = new ArrayList<>();
                List<VideoStreamVo> videoVo = deviceService.findVideoVo(id);
                if (videoVo != null) {
                    for (VideoStreamVo vo : videoVo) {
                        if (vo.getIsOnline().equals("1")) {
                            if (redisTemplate.hasKey(vo.getId())) {
                                VideoStreamVo o = JSONUtil.toBean((String) redisTemplate.opsForValue().get(vo.getId()),VideoStreamVo.class);
                                CommandTasker query = manager.query(vo.getId());
                                if (query == null || query.getThread() == null || query.getThread().getThreadGroup() == null) {    //找不到进程（可能之前的进程中断退出）
                                    startPushCameraStreamProcess(manager, vo, o.getOutput());
                                }
                                o.setCurrentNum(o.getCurrentNum()+1);
                                redisTemplate.opsForValue().set(o.getId(), JSONUtil.toJsonStr(o));
                                list.add(o);
                            } else {
                                VideoStreamVo streamVo = new VideoStreamVo();
                                String output = (redis_output != null ? redis_output.toString() : sysProperties.getOutput()) + vo.getId();
                                String flvOutput = (redis_flv_output != null ? redis_flv_output.toString() : sysProperties.getFlvOutput()) + vo.getId();
                                startPushCameraStreamProcess(manager, vo, output);

                                streamVo.setId(vo.getId());
                                streamVo.setDeviceName(vo.getDeviceName());
                                streamVo.setIsOnline(vo.getIsOnline());
                                streamVo.setOutput(output);
                                streamVo.setFlvOutput(flvOutput);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                CommandTasker query = manager.query(vo.getId());
                                if (query != null) {
                                    list.add(streamVo);
                                    streamVo.setCurrentNum(1);
                                    redisTemplate.opsForValue().set(streamVo.getId(), JSONUtil.toJsonStr(streamVo));
                                }
                            }
                        }
                    }
                }
                return new ResponseBean<>(list);
            }
            if (type != null && type.equals("3")) {
                List<VideoStreamVo> list = new ArrayList<>();
                if (redisTemplate.hasKey(id)) {
                    VideoStreamVo o = JSONUtil.toBean((String) redisTemplate.opsForValue().get(id),VideoStreamVo.class);
                    CommandTasker query = manager.query(id);
                    if (query == null || query.getThread() == null || query.getThread().getThreadGroup() == null) {    //找不到进程（可能之前的进程中断退出）
                        VideoStreamVo vo = deviceService.findVideoStreamVo(id);
                        startPushCameraStreamProcess(manager, vo, o.getOutput());
                    }
                    o.setCurrentNum(o.getCurrentNum()+1);
                    redisTemplate.opsForValue().set(o.getId(), JSONUtil.toJsonStr(o));
                    list.add(o);
                } else {
                    VideoStreamVo vo = deviceService.findVideoStreamVo(id);
                    if (vo.getIsOnline().equals("1")) {
                        VideoStreamVo streamVo = new VideoStreamVo();
                        String output = (redis_output != null ? redis_output.toString() : sysProperties.getOutput()) + vo.getId();
                        String flvOutput = (redis_flv_output != null ? redis_flv_output.toString() : sysProperties.getFlvOutput()) + vo.getId();
                        startPushCameraStreamProcess(manager, vo, output);

                        streamVo.setId(vo.getId());
                        streamVo.setDeviceName(vo.getDeviceName());
                        streamVo.setIsOnline(vo.getIsOnline());
                        streamVo.setOutput(output);
                        streamVo.setFlvOutput(flvOutput);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CommandTasker query = manager.query(vo.getId());
                        if (query != null) {
                            list.add(streamVo);
                            streamVo.setCurrentNum(1);
                            redisTemplate.opsForValue().set(vo.getId(), JSONUtil.toJsonStr(streamVo));
                        }
                    }
                }
                return new ResponseBean<>(list);
            }
        }else {
            List<VideoStreamVo> list = new ArrayList<>();
            if("2".equals(type)){
                List<VideoStreamVo> videoVo = deviceService.findVideoVo(id);
                if(videoVo.size() > 0){
                    String outPut = (redis_output != null ? redis_output.toString() : sysProperties.getOutput());
                    String flvOutput = (redis_flv_output != null ? redis_flv_output.toString() : sysProperties.getFlvOutput());
                    for (VideoStreamVo videoStreamVo : videoVo) {
                        if("1".equals(videoStreamVo.getIsOnline())){
                            videoStreamVo.setOutput(outPut + videoStreamVo.getId());
                            videoStreamVo.setFlvOutput(flvOutput + videoStreamVo.getId());
                            list.add(videoStreamVo);
                            ContentBody body = new ContentBody();
                            body.setTenantCode(videoStreamVo.getTenantCode());
                            body.setProjectCode(videoStreamVo.getProjectCode());
                            body.setBody(videoStreamVo);
                            body.setFlag("pushStream");
                            mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + videoStreamVo.getTenantCode() + "_" + videoStreamVo.getProjectCode(), JSONUtil.toJsonStr(body));
                        }
                    }
                }
            }
            if("3".equals(type)){
                String outPut = (redis_output != null ? redis_output.toString() : sysProperties.getOutput());
                String flvOutput = (redis_flv_output != null ? redis_flv_output.toString() : sysProperties.getFlvOutput());
                VideoStreamVo vo = deviceService.findVideoStreamVo(id);
                vo.setOutput(outPut + vo.getId());
                vo.setFlvOutput(flvOutput + vo.getId());
                list.add(vo);
                ContentBody body = new ContentBody();
                body.setTenantCode(vo.getTenantCode());
                body.setProjectCode(vo.getProjectCode());
                body.setBody(vo);
                body.setFlag("pushStream");
                mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + vo.getTenantCode() + "_" + vo.getProjectCode(), JSONUtil.toJsonStr(body));
            }
            return new ResponseBean<>(list);
        }
        return new ResponseBean<>(new ArrayList(), "设备类型不存在");
    }

    /**
     * 启动摄像机推流进程
     * @param manager
     * @param vo
     * @param output
     */
    private void startPushCameraStreamProcess(CommandManagerImpl manager, VideoStreamVo vo, String output) {
        StringBuffer input = new StringBuffer("rtsp://")
                .append(vo.getUserName())
                .append(":")
                .append(vo.getPassword())
                .append("@" + vo.getIp())
                .append( ":")
                .append(vo.getPort());
        if(StringUtils.isEmpty(vo.getDeviceType())) {
            input.append("/h264/ch1/main/av_stream");
        }else if(vo.getDeviceType().toUpperCase().startsWith(StreamType.HK.getType())){ //海康
            input.append("/h264/ch1/main/av_stream");
        }else if(vo.getDeviceType().toUpperCase().startsWith(StreamType.DH.getType())) {    //大华
            input.append("/cam/realmonitor?channel=1&subtype=0");
        }else{
            input.append("/h264/ch1/main/av_stream");
        }
        // String output = "rtmp://120.24.102.159/oflaDemo/" + vo.getId();
        manager.start(vo.getId(), CommandBuidlerFactory.createBuidler()
                        .add("ffmpeg")
                        .add("-rtsp_transport", "tcp")
                        .add("-i", input.toString())
                        .add("-vcodec", "h264")
                        .add("-preset", "superfast")
                        .add("-tune", "zerolatency")
                        .add("-g", "5")
                        .add("-f", "flv")
                        .add("-r", "10")
                        .add("-s", "1920x1080")
//                                .add("-acodec", "aac")
//                                .add("-ar", "44100")
                        .add("-an")
                        .add(output)
        );
    }
       /* String input = "rtsp://admin:12345@192.168.200.20:554/h264/ch1/main/av_stream";
        String output = "rtmp://192.168.200.238/oflaDemo/" + id;
            manager.start(id, CommandBuidlerFactory.createBuidler()
                    .add("ffmpeg")
                    .add("-rtsp_transport","tcp")
                    .add("-i",input)
                    .add("-f","flv")
                    .add("-r","25")
                    .add("-s","640x480")
                    .add("-ar","44100")
                    .add(output)
            );
        List list = new ArrayList();
        list.add(output);
        return new ResponseBean<>(list);*/


    @GetMapping("/stopPushStream")
    @ApiOperation(value = "关闭视频")
    public ResponseBean<Boolean> stopPushStream(@RequestParam(value = "id", required = true) String id,
                                                @RequestParam(value = "type", required = true) String type) {

        CommandManagerImpl manager = CommandManagerImpl.getInstance();
        if (type.equals("2")) {
            List<VideoStreamVo> videoVo = deviceService.findVideoVo(id);
            for (VideoStreamVo vo : videoVo) {
                if (!CloudCommon.isCloud()) {
                    if (redisTemplate.hasKey(vo.getId())) {
                        VideoStreamVo videoStreamVo = JSONUtil.toBean((String) redisTemplate.opsForValue().get(vo.getId()), VideoStreamVo.class);
                        if(videoStreamVo.getCurrentNum()-1 <= 0){
                            redisTemplate.delete(vo.getId());
                            manager.stop(vo.getId());
                            manager.destory();
                        }else {
                            videoStreamVo.setCurrentNum(videoStreamVo.getCurrentNum()-1);
                            redisTemplate.opsForValue().set(vo.getId(),JSONUtil.toJsonStr(videoStreamVo));
                        }
                    }
                }else {
                    ContentBody body = new ContentBody();
                    body.setTenantCode(vo.getTenantCode());
                    body.setProjectCode(vo.getProjectCode());
                    body.setBody(vo.getId());
                    body.setFlag("stopStream");
                    mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + vo.getTenantCode() + "_" + vo.getProjectCode(), JSONUtil.toJsonStr(body));
                }
            }
            return new ResponseBean<>(true);
        }

        if (type.equals("3")) {
            if (!CloudCommon.isCloud()) {
                if (redisTemplate.hasKey(id)) {
                    VideoStreamVo videoStreamVo = JSONUtil.toBean((String) redisTemplate.opsForValue().get(id), VideoStreamVo.class);
                    if(videoStreamVo.getCurrentNum()-1 <= 0){
                        redisTemplate.delete(id);
                        manager.stop(id);
                        manager.destory();
                    }else {
                        videoStreamVo.setCurrentNum(videoStreamVo.getCurrentNum()-1);
                        redisTemplate.opsForValue().set(id,JSONUtil.toJsonStr(videoStreamVo));
                    }
                }
            }else {
                VideoStreamVo vo = deviceService.findVideoStreamVo(id);
                ContentBody body = new ContentBody();
                body.setTenantCode(vo.getTenantCode());
                body.setProjectCode(vo.getProjectCode());
                body.setBody(vo.getId());
                body.setFlag("stopStream");
                mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + vo.getTenantCode() + "_" + vo.getProjectCode(), JSONUtil.toJsonStr(body));
            }
            return new ResponseBean<>(true);
        }
        return new ResponseBean<>(false, "关闭失败");
    }

    @GetMapping("/refreshCameraVideo")
    @ApiOperation(value = "刷新视频")
    public ResponseBean<List> refreshCameraVideo(@RequestParam(value = "id", required = true) String id,
                                             @RequestParam(value = "type", required = true) String type) {
        CommandManagerImpl manager = CommandManagerImpl.getInstance();
        if (type.equals("2")) {
            List<VideoStreamVo> videoVo = deviceService.findVideoVo(id);
            for (VideoStreamVo vo : videoVo) {
                if (!CloudCommon.isCloud()) {
                    redisTemplate.delete(vo.getId());
                    manager.stop(vo.getId());
                    manager.destory();
                }else{
                    ContentBody body = new ContentBody();
                    body.setTenantCode(vo.getTenantCode());
                    body.setProjectCode(vo.getProjectCode());
                    body.setBody(vo.getId());
                    body.setFlag("stopStream");
                    mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + vo.getTenantCode() + "_" + vo.getProjectCode(), JSONUtil.toJsonStr(body));
                }
            }
        } else if(type.equals("3")) {
            if (!CloudCommon.isCloud()) {
                redisTemplate.delete(id);
                manager.stop(id);
                manager.destory();
            } else {
                VideoStreamVo vo = deviceService.findVideoStreamVo(id);
                ContentBody body = new ContentBody();
                body.setTenantCode(vo.getTenantCode());
                body.setProjectCode(vo.getProjectCode());
                body.setBody(vo.getId());
                body.setFlag("stopStream");
                mqSender.send(MQConstants.PROXY_EXCHANGE,"as_" + vo.getTenantCode() + "_" + vo.getProjectCode(), JSONUtil.toJsonStr(body));
            }
        }

        return getCameraVideo(id, type);
    }


    /**
     * 设备详细信息(告警记录列表)
     *
     * @param deviceAlarmDto
     * @return ResponseBean
     */
    @GetMapping(value = "historyAlarm")
    @ApiOperation(value = "设备详细信息(告警记录列表)")
    public ResponseBean<PageInfo<DeviceEventAlarmVo>> getHistoryAlarm(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                      @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                      DeviceAlarmDto deviceAlarmDto) {

        return new ResponseBean<>(deviceDetailsService.getHistoryAlarm(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
    }


    /**
     * 设备详细信息(告警记录列表)
     *
     * @param deviceAlarmDto
     * @return ResponseBean
     */
    @GetMapping(value = "historyAlarmStatistics")
    @ApiOperation(value = "设备详细信息(告警记录列表统计)")
    public ResponseBean<List<Object>> getHistoryAlarmStatistics(DeviceAlarmDto deviceAlarmDto) {

        return new ResponseBean<>(deviceDetailsService.getHistoryAlarmStatistics(deviceAlarmDto));

    }


    /**
     * 维护历史统计
     *
     * @param deviceId  设备ID
     * @param startTime 时间天数
     * @return
     */

    @ApiOperation(value = "维护历史统计", notes = "维护历史统计")
    @Log("维护历史统计")
    @GetMapping("maintainHistory")
    public ResponseBean<Map> queryMaintainHistoryStatistics(@RequestParam("deviceId") String deviceId, @Param("startTime") String startTime,@RequestParam("endTime") String endTime) {
        return new ResponseBean<>(deviceDetailsService.getHistoryMaintainCounts(deviceId, startTime,endTime));
    }

    /**
     * 监控模块分页条件查询设备报警列表
     *
     * @param pageNum
     * @param pageSize
     * @param deviceAlarmConditionDto
     * @return
     */
    @GetMapping(value = "condition")
    @ApiOperation(value = "分页条件查询设备监控报警信息列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findDeviceAlarmInfoByCondition(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                                @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                                DeviceAlarmConditionDto deviceAlarmConditionDto) {

        //需求: 初始化列表以设备状态的设备树展示,过滤列表以单个设备展示
        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }
        String[] alarmLevels = null;
        // String[] queries = null;

        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);

        //报警级别
        if (deviceAlarmConditionDto.getAlarmLevel() != null && !"".equals(deviceAlarmConditionDto.getAlarmLevel())
                && "offline".equals(deviceAlarmConditionDto.getAlarmType())) {
            alarmLevels = deviceAlarmConditionDto.getAlarmLevel().split(",");
            deviceAlarmConditionDto.setAlarmLevels(alarmLevels);
            deviceAlarmConditionDto.setFlag(2);
        } else if (deviceAlarmConditionDto.getAlarmLevel() == null || "".equals(deviceAlarmConditionDto.getAlarmLevel())
                && "offline".equals(deviceAlarmConditionDto.getAlarmType())) {
            deviceAlarmConditionDto.setFlag(1);
        } else {
            alarmLevels = deviceAlarmConditionDto.getAlarmLevel().split(",");
            deviceAlarmConditionDto.setAlarmLevels(alarmLevels);
        }
        PageInfo<DeviceAlarmVo> condition = metadataService.findConditionByPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmConditionDto);
        return new ResponseBean<>(condition);
    }


    /**
     * 监控模块设备状态数量统计
     * @return
     */
    @GetMapping(value = "count")
    @ApiOperation(value = "统计设备数量")
    public ResponseBean<Map> getDeviceInfoCount() {
        String roles = RoleContextHolder.getRole();
        String regionCode = null;
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            String userRegionCode = RegionCodeContextHolder.getRegionCode();
            if(userRegionCode == null || "".equals(userRegionCode)){
                //throw new CommonException("当前用户暂无区域设备");
                return null;
            }
            regionCode = userRegionCode;
        }
        DeviceAlarmConditionDto deviceAlarmConditionDto = new DeviceAlarmConditionDto();
        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);
        deviceAlarmConditionDto.setRegionCode(regionCode);
        return new ResponseBean<>(metadataService.getCount(deviceAlarmConditionDto));
    }


    /**
     * 点位监控地图设备统计
     * @return
     */
    @GetMapping(value = "mapDeviceCount")
    @ApiOperation(value = "点位监控地图设备统计")
    public ResponseBean<Map> getMapDeviceCount(DeviceAlarmConditionDto deviceAlarmConditionDto) {
        return new ResponseBean<>(deviceService.getMapDeviceCount(deviceAlarmConditionDto));
    }

    public static RegionDeviceTree recursiveTree (RegionDeviceTree parent, List < RegionDeviceTree > list){
        for (RegionDeviceTree child : list) {
            if (parent.getId().equals(child.getParentId())) {
                child = recursiveTree(child, list);
                parent.getChildren().add(child);
            }
        }

        return parent;
    }
    ///////////////////////////////////////////////////////////
    //
    //下面方法为上级平台专用接口
    //
    ///////////////////////////////////////////////////////////

    /**
     * 上级平台查询设备在线状态
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getOnlineStatusByDeviceId/{id}")
    @ApiOperation(value = "获取指定设备的在线状态")
    public ResponseBean<Map> getOnlineStatusByDeviceId(@PathVariable("id") String id) {

        return new ResponseBean<>(deviceDetailsService.getOnlineStatusByDeviceId(id));

    }

    @PostMapping("/statusData/openDeviceList")
    @ApiOperation(value = "分页查询设备列表")
    public ResponseBean<PageInfo<LinkedHashMap>> openDeviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                @RequestBody DeviceDto deviceDto) {


        return new ResponseBean<>(deviceService.openDeviceList(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceDto));
    }

    /**
     * 上级平台区域列表
     *
     * @param regionName
     * @return
     */
    @GetMapping(value = "/statusData/regionList")
    @ApiOperation(value = "上级平台区域列表")
    public ResponseBean<List<LinkedHashMap>> findRegionList(@RequestParam(value = "regionName", required = false) String regionName) {

        return new ResponseBean<>(regionService.findRegionList(regionName));
    }

    @GetMapping(value = "/statusData/deviceDetails/{id}")
    @ApiOperation(value = "上级平台设备详情")
    public ResponseBean<OpenDeviceDetailsVo> findDeviceDetails(@PathVariable("id") String id) {

        DeviceDetailsVo deviceDetails = deviceDetailsService.getDeviceDetail(id);
        OpenDeviceDetailsVo openDeviceDetailsVo = new OpenDeviceDetailsVo();
        //当前设备
        DeviceVo device = deviceDetails.getDevice();
        if (device != null) {
            OpenDeviceVo deviceVo = new OpenDeviceVo();
            BeanUtils.copyProperties(device, deviceVo);
            openDeviceDetailsVo.setDevice(deviceVo);
        }

        //当前设备的上级设备
        DeviceVo parentDevice = deviceDetails.getParentDevice();
        if (parentDevice != null) {
            OpenDeviceVo parentVo = new OpenDeviceVo();
            BeanUtils.copyProperties(parentDevice, parentVo);
            openDeviceDetailsVo.setParentDevice(parentVo);
        }

        //当前设备的下级设备
        List<DeviceVo> childrenDeviceList = deviceDetails.getChildrenDeviceList();
        if (childrenDeviceList != null && childrenDeviceList.size() > 0) {
            List<OpenDeviceVo> childrenList = childrenDeviceList.stream()
                    .map(vo -> {
                        OpenDeviceVo childrenVo = new OpenDeviceVo();
                        BeanUtils.copyProperties(vo, childrenVo);
                        return childrenVo;
                    }).collect(Collectors.toList());
            openDeviceDetailsVo.setChildrenDeviceList(childrenList);
        }

        return new ResponseBean<>(openDeviceDetailsVo);
    }

    @ApiOperation(value = "设置信息", notes = "设置信息")
    @PostMapping(value = "/statusData/updateBath")
    public ResponseBean
            <Boolean> updateBath(@RequestBody DeviceSettingsListDto dto) {
        // 先删除原数据
        if (dto.getDeviceSettingsDtoList().get(0).getDeviceId() != null) {
            deviceSettingsService.deleteBath(dto.getDeviceSettingsDtoList());
        }
        if (dto.getDeviceSettingsDtoList() == null || dto.getDeviceSettingsDtoList().size() == 0) {
            return new ResponseBean<>(Boolean.TRUE);
        } else {
            // 新增数据
            return new ResponseBean<>(deviceSettingsService.insertBatch(dto.getDeviceSettingsDtoList()) > 0);
        }
    }


    /**
     * 设备监控模块设备列表
     */

    @ApiOperation(value = "设备监控模块设备列表")
    @GetMapping("monitorDeviceList")
    public ResponseBean<PageInfo<DeviceAlarmVo>> monitorDeviceList(@RequestParam(value = "pageNum",required = false)String pageNum,@RequestParam(value = "pageSize",required = false)String pageSize,DeviceAlarmConditionDto deviceAlarmConditionDto){
        return new ResponseBean<>(deviceService.monitorDeviceList(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmConditionDto));
    }

    /**
     * 端口关联设备信息
     */
    @GetMapping("portDevice")
    @ApiOperation(value = "网络口,电源口,光纤口关联设备")
    public ResponseBean<Map> portDevice(@RequestParam("id") String id){
        return new ResponseBean<>(deviceService.portDevice(id));
    }

    /**
     * 市级平台设备详情-->设备状态
     *
     * @param id 设备ID
     * @return ResponseBean
     */
    @GetMapping("/cityStatusData/{id}")
    @ApiOperation(value = "查询设备状态")
    public ResponseBean<List<DeviceStatusVo>> getCityStatusData(@PathVariable("id") String id) {
        return new ResponseBean<>(deviceEventAlarmService.getDeviceStatusData(id));
    }

}