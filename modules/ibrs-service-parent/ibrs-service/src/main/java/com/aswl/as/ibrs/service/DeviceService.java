package com.aswl.as.ibrs.service;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.config.CityPlatformSender;
import com.aswl.as.common.core.constant.CityPlatformConstant;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.constant.StateGroup;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.enums.AlarmLevelType;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.ibrs.api.dto.DeviceAlarmConditionDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.DeviceFaultDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.enums.MQExchange;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.*;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.task.DeviceThread;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import com.aswl.as.ibrs.utils.DeviceUtil;
import com.aswl.as.ibrs.utils.FileUtil;
import com.aswl.as.common.core.utils.PasswordUtil;
import com.aswl.as.ibrs.utils.ValueComponent;
import com.aswl.as.user.api.dto.ConfigDto;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Config;
import com.aswl.as.user.api.module.Tenant;
import com.aswl.iot.dto.DeviceEventPatrol;
import com.aswl.iot.dto.constant.MQConstants;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.quartz.Scheduler;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceService extends CrudService<DeviceMapper, Device> {
    private final DeviceMapper deviceMapper;
    private final UserServiceClient userServiceClient;
    private final MQSender mqSender;
    private final DeviceEventAlarmMapper deviceEventAlarmMapper;
    private final EventAlarmMapper alarmMapper;
    private final EventBaseMapper baseMapper;
    private final EventEoutletMapper eoutletMapper;
    private final EventEcurrentMapper ecurrentMapper;
    private final EventEswitchMapper eswitchMapper;
    private final EventEvoltageMapper evoltageMapper;
    private final EventNetworkMapper networkMapper;
    private final EventSfpMapper sfpMapper;
    private final RegionMapper regionMapper;
    private final DeviceModelMapper modelMapper;
    private final FlowRunMapper runMapper;
    private final DeviceDiscoverMapper deviceDiscoverMapper;
    private final ProjectMapper projectMapper;
    private final AddressBaseMapper addressBaseMapper;
    private final AlarmTypeDeviceFavoriteMapper deviceFavoriteMapper;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final EventUcStatusGroupModelService eventUcStatusGroupModelService;
    private final DeviceQrcodeMapper deviceQrcodeMapper;
    private final EventPatrolMapper eventPatrolMapper;
    private final EventElectricityMapper eventElectricityMapper;
    private  final EventSecCtlPowerMapper eventSecCtlPowerMapper;
    private final EventSecCtlPowerOutputMapper eventSecCtlPowerOutputMapper;
    private  final  EventSecCtlPowerSetMapper eventSecCtlPowerSetMapper;
    private  final EventSfpInfoMapper eventSfpInfoMapper;
    private final EventPostureMapper eventPostureMapper;
    private final EventInputMapper eventInputMapper;

    @Autowired
    ValueComponent valueComponent;

    private final RedisTemplate redisTemplate;

    @Autowired
    private final AlarmCaptureService alarmCaptureService;

    @Autowired
    private CityPlatformSender  cityPlatformSender;

    @Autowired
    private CityPlatformUtil cityPlatformUtil;

    /**
     * ????????????????????????
     *
     * @param page
     * @param deviceDto
     * @return
     */
    public PageInfo<DeviceVo> findPage(PageInfo<DeviceDto> page, DeviceDto deviceDto,String isAsos) {
        String roles = RoleContextHolder.getRole();
        String tenantCode =deviceDto.getTenantCode();
        String projectId =deviceDto.getProjectId();
        String regionCode = deviceDto.getRegionCode();
      /*  if(deviceDto.getProjectId() != null && !"".equals(deviceDto.getProjectId())){
            regionCode = null;
        }*/
        if (!CommonConstant.IS_ASOS_TRUE.equals(isAsos)) {
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
                //??????????????????
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
                //???????????????????????????????????????
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
                //???????????????????????????????????????SysUtil.getProjectId()?????????
            } else {
                if (regionCode == null || "".equals(regionCode)) {
                    String userRegionCode = RegionCodeContextHolder.getRegionCode();
                    if (userRegionCode == null || "".equals(userRegionCode)) {
                        return new PageInfo<>(new ArrayList<>());
                    }
                    regionCode = userRegionCode;
                }
            }
        }else{
            tenantCode=deviceDto.getTenantCode();
            projectId=deviceDto.getProjectId();
        }
        deviceDto.setTenantCode(tenantCode);
        deviceDto.setProjectId(projectId);
        deviceDto.setRegionCode(regionCode);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(deviceMapper.findDeviceInfo(deviceDto));
    }

    /**
     * ??????id????????????
     *
     * @param id
     * @return DeviceVo
     */
    public DeviceVo findById(String id) {

        DeviceVo deviceVo = deviceMapper.findById(id);
        if(deviceVo != null && (deviceVo.getDeviceModelId() != null && !"".equals(deviceVo.getDeviceModelId()))){
            String deviceModelId = deviceVo.getDeviceModelId();
            deviceVo.setDcpowerNumber(eventUcStatusGroupModelService.findPortNum(StateGroup.DCPOWERGROUP,deviceModelId));
            deviceVo.setRj45Number(eventUcStatusGroupModelService.findPortNum(StateGroup.RJ45EXTGROUP,deviceModelId));
            deviceVo.setFibreOpticalNumber(eventUcStatusGroupModelService.findPortNum(StateGroup.FIBOPTGroup,deviceModelId));
        }
        return deviceVo;
    }

    /**
     * ????????????
     *
     * @param deviceDto
     * @return int
     */
    @Transactional
    public int insert(DeviceDto deviceDto) {
        // ????????????
        Device device = this.insertDevice(deviceDto);
        // ??????mq??????
        if(device != null){
            List<Device> deviceList = new ArrayList<>();
            deviceList.add(device);
            List<com.aswl.iot.dto.Device> list = this.generateIotDeviceList(deviceList);
            if(list != null){
                //
                sendMq(list);
            }
            //??????MQ???????????????
            List<Device> cityDevices = new ArrayList<>();
            cityDevices.add(device);
            sendCityPlatformMq(cityDevices);
        }
        return 1;
    }

    /**
     *  ????????????
     * @param deviceDto
     * @return
     */
    public Device insertDevice(DeviceDto deviceDto){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //?????????????????????????????????????????????
        String user = SysUtil.getUser();
        String sysCode = SysUtil.getSysCode();
        String tenantCode = SysUtil.getTenantCode();
        if (CommonConstant.IS_ASOS_TRUE.equals(deviceDto.getIsAsOs())) {
            //??????????????????????????????
//            regionCode=deviceDto.getRegionCode();
            user = deviceDto.getCreator();
            sysCode = deviceDto.getApplicationCode();
            tenantCode = deviceDto.getTenantCode();
        }
        String ids = deviceDto.getChildrenDeviceIds();
        Device device = new Device();
        BeanUtils.copyProperties(deviceDto, device);
        if (device.getDeviceCode() != null) {
            Device uniqueKey = deviceMapper.findUniqueDeviceCode(device.getDeviceCode(), device.getProjectId());
            if (uniqueKey != null) {
                throw new CommonException("?????????????????????");
            }
        }

        //TODO ???????????????????????????IP ??????????????????

        //??????IP???????????????
        if (StringUtils.isNotBlank(device.getIp())) {
            int num  = deviceMapper.findUniqueIp(null,device.getIp(), device.getProjectId());
            if (num  > 0) {
                throw new CommonException("??????IP?????????");
            }
        }

        device.setCommonValue(user, sysCode, tenantCode, deviceDto.getProjectId());
//        device.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        try {
            if (StringUtils.isNotBlank(deviceDto.getPurchaseDate())) {
                device.setPurchaseDate(sdf.parse(deviceDto.getPurchaseDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ??????????????????????????????????????????????????????????????????????????? -----
        //?????????????????????
        Integer maxCount=getTenantMaxDeviceCount(tenantCode);
        Integer devicesCount=deviceMapper.getDevicesCount(null,tenantCode,null);
        if(maxCount !=null && devicesCount>=maxCount)
        {
            throw new CommonException("??????????????????????????????????????????????????????");
        }

        int count = super.insert(device);
        //???????????????,??????????????????????????????
        deviceDiscoverMapper.deleteById(deviceDto.getDiscoverId());
        // ??????????????????????????????
        if (count > 0 && StringUtils.isNotEmpty(ids)) {
            List<Device> lists = deviceMapper.getDevicesByIds(ids);
            for (Device dev : lists) {
                dev.setParentDeviceId(device.getId());
                deviceMapper.update(dev);
            }
        }
        //??????????????????-?????????????????????????????????????????????
        DeviceEventAlarm deviceEventAlarm = new DeviceEventAlarm();
        deviceEventAlarm.setId(IdGen.snowflakeId());
        deviceEventAlarm.setDeviceId(device.getId());
        deviceEventAlarm.setRegionNo(device.getRegionCode());
        deviceEventAlarm.setIsAlarm(1);
        deviceEventAlarm.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        deviceEventAlarm.setStoreTime(new Date());
        deviceEventAlarm.setApplicationCode(SysUtil.getSysCode());
//        deviceEventAlarm.setTenantCode(SysUtil.getTenantCode());
        deviceEventAlarm.setTenantCode(tenantCode);
        deviceEventAlarm.setAlarmLevel(AlarmLevelType.FAULT.getType());
        deviceEventAlarm.setAlarmTypes("IsOnlineAlarm0");
        deviceEventAlarm.setAlarmTypesDes("??????");
        deviceEventAlarm.setAlarmLevels("??????");
        deviceEventAlarm.setAlarmDates(DateUtil.formatDateTime(new Date()));
        deviceEventAlarm.setUEventId("0");
        deviceEventAlarmMapper.insert(deviceEventAlarm);

        // ????????????-??????
        EventAlarm eventAlarm = new EventAlarm();
        eventAlarm.setId(IdGen.snowflakeId());
        eventAlarm.setDeviceId(device.getId());
        eventAlarm.setRegionNo(device.getRegionCode());
//        eventAlarm.setRecTime(Long.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventAlarm.setRecTime(0L);
        eventAlarm.setStoreTime(new Date());
        eventAlarm.setApplicationCode(SysUtil.getSysCode());
//        eventAlarm.setTenantCode(SysUtil.getTenantCode());
        eventAlarm.setTenantCode(tenantCode);
        alarmMapper.insert(eventAlarm);
        // ????????????-??????
        EventBase eventBase = new EventBase();
        eventBase.setId(IdGen.snowflakeId());
        eventBase.setDeviceId(device.getId());
        eventBase.setRegionNo(device.getRegionCode());
        //eventBase.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventBase.setRecTime(0);
        eventBase.setStoreTime(new Date());
        eventBase.setApplicationCode(SysUtil.getSysCode());
//        eventBase.setTenantCode(SysUtil.getTenantCode());
        eventBase.setTenantCode(tenantCode);
        baseMapper.insert(eventBase);

        // ????????????-????????????

        EventEoutlet eventEoutlet = new EventEoutlet();
        eventEoutlet.setId(IdGen.snowflakeId());
        eventEoutlet.setDeviceId(device.getId());
        eventEoutlet.setRegionNo(device.getRegionCode());
        //eventEoutlet.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventEoutlet.setRecTime(0);
        eventEoutlet.setStoreTime(new Date());
        eventEoutlet.setApplicationCode(SysUtil.getSysCode());
//        eventEoutlet.setTenantCode(SysUtil.getTenantCode());
        eventEoutlet.setTenantCode(tenantCode);
        eoutletMapper.insert(eventEoutlet);

        //????????????-????????????
        EventEcurrent eventEcurrent = new EventEcurrent();
        eventEcurrent.setId(IdGen.snowflakeId());
        eventEcurrent.setDeviceId(device.getId());
        eventEcurrent.setRegionNo(device.getRegionCode());
        // eventEcurrent.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventEcurrent.setRecTime(0);
        eventEcurrent.setStoreTime(new Date());
        eventEcurrent.setApplicationCode(SysUtil.getSysCode());
//        eventEcurrent.setTenantCode(SysUtil.getTenantCode());
        eventEcurrent.setTenantCode(tenantCode);
        ecurrentMapper.insert(eventEcurrent);
        // ????????????-????????????
        EventEswitch eventEswitch = new EventEswitch();
        eventEswitch.setId(IdGen.snowflakeId());
        eventEswitch.setDeviceId(device.getId());
        eventEswitch.setRegionNo(device.getRegionCode());
        //eventEswitch.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventEswitch.setRecTime(0);
        eventEswitch.setStoreTime(new Date());
        eventEswitch.setApplicationCode(SysUtil.getSysCode());
//        eventEswitch.setTenantCode(SysUtil.getTenantCode());
        eventEswitch.setTenantCode(tenantCode);
        eswitchMapper.insert(eventEswitch);

        // ????????????-??????
        EventEvoltage evoltage = new EventEvoltage();
        evoltage.setId(IdGen.snowflakeId());
        evoltage.setDeviceId(device.getId());
        evoltage.setRegionNo(device.getRegionCode());
        //evoltage.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        evoltage.setRecTime(0);
        evoltage.setStoreTime(new Date());
        evoltage.setApplicationCode(SysUtil.getSysCode());
//        evoltage.setTenantCode(SysUtil.getTenantCode());
        evoltage.setTenantCode(tenantCode);
        evoltageMapper.insert(evoltage);

        // ????????????-??????
        EventNetwork eventNetwork = new EventNetwork();
        eventNetwork.setId(IdGen.snowflakeId());
        eventNetwork.setDeviceId(device.getId());
        eventNetwork.setRegionNo(device.getRegionCode());
        eventNetwork.setNetworkState(0);
        //eventNetwork.setStoreTime(new Date());
        eventNetwork.setApplicationCode(SysUtil.getSysCode());
//        eventNetwork.setTenantCode(SysUtil.getTenantCode());
        eventNetwork.setTenantCode(tenantCode);
        networkMapper.insert(eventNetwork);

        //????????????-??????
        EventSfp eventSfp = new EventSfp();
        eventSfp.setId(IdGen.snowflakeId());
        eventSfp.setDeviceId(device.getId());
        eventSfp.setRegionNo(device.getRegionCode());
//        eventSfp.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
        eventSfp.setRecTime(0);
        eventSfp.setStoreTime(new Date());
        eventSfp.setApplicationCode(SysUtil.getSysCode());
//        eventSfp.setTenantCode(SysUtil.getTenantCode());
        eventSfp.setTenantCode(tenantCode);
        sfpMapper.insert(eventSfp);

        //?????????
        EventElectricity eventElectricity = new EventElectricity();
        eventElectricity.setId(IdGen.snowflakeId());
        eventElectricity.setDeviceId(device.getId());
        eventElectricity.setRegionNo(device.getRegionCode());
        eventElectricity.setRecTime(0);
        eventElectricity.setStoreTime(new Date());
        eventElectricity.setApplicationCode(SysUtil.getSysCode());
        eventElectricity.setTenantCode(device.getTenantCode());
        eventElectricity.setPassway("00000000");
        eventElectricity.setLeakage(0d);
        eventElectricity.setSwitchNum(0);
        eventElectricity.setOverLoad(0d);
        eventElectricity.setUnderVoltage(0d);
        eventElectricity.setElectricity(0d);
        eventElectricityMapper.insert(eventElectricity);

        // ???????????????-??????
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        eventSecCtlPower.setId(IdGen.snowflakeId());
        eventSecCtlPower.setRecTime(0);
        eventSecCtlPower.setDeviceId(device.getId());
        eventSecCtlPower.setRegionNo(device.getRegionCode());
        eventSecCtlPower.setStoreTime(new Date());
        eventSecCtlPower.setApplicationCode(SysUtil.getSysCode());
        eventSecCtlPower.setTenantCode(device.getTenantCode());
        eventSecCtlPowerMapper.insert(eventSecCtlPower);

        // ???????????????-????????????
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setId(IdGen.snowflakeId());
        eventSecCtlPowerOutput.setRecTime(0);
        eventSecCtlPowerOutput.setDeviceId(device.getId());
        eventSecCtlPowerOutput.setRegionNo(device.getRegionCode());
        eventSecCtlPowerOutput.setStoreTime(new Date());
        eventSecCtlPowerOutput.setApplicationCode(SysUtil.getSysCode());
        eventSecCtlPowerOutput.setTenantCode(device.getTenantCode());
        eventSecCtlPowerOutputMapper.insert(eventSecCtlPowerOutput);

        //???????????????-????????????
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setId(IdGen.snowflakeId());
        eventSecCtlPowerSet.setRecTime(0);
        eventSecCtlPowerSet.setDeviceId(device.getId());
        eventSecCtlPowerSet.setRegionNo(device.getRegionCode());
        eventSecCtlPowerSet.setStoreTime(new Date());
        eventSecCtlPowerSet.setApplicationCode(SysUtil.getSysCode());
        eventSecCtlPowerSet.setTenantCode(device.getTenantCode());
        eventSecCtlPowerSetMapper.insert(eventSecCtlPowerSet);

        //SFP??????
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        eventSfpInfo.setId(IdGen.snowflakeId());
        eventSfpInfo.setRecTime(0);
        eventSfpInfo.setDeviceId(device.getId());
        eventSfpInfo.setRegionNo(device.getRegionCode());
        eventSfpInfo.setStoreTime(new Date());
        eventSfpInfo.setApplicationCode(SysUtil.getSysCode());
        eventSfpInfo.setTenantCode(device.getTenantCode());
        eventSfpInfoMapper.insert(eventSfpInfo);

        //????????????
        EventPosture eventPosture = new EventPosture();
        eventPosture.setId(IdGen.snowflakeId());
        eventPosture.setRecTime(0);
        eventPosture.setDeviceId(device.getId());
        eventPosture.setRegionNo(device.getRegionCode());
        eventPosture.setStoreTime(new Date());
        eventPosture.setApplicationCode(SysUtil.getSysCode());
        eventPosture.setTenantCode(device.getTenantCode());
        eventPostureMapper.insert(eventPosture);

        //????????????-??????
        EventInput eventInput = new EventInput();
        eventInput.setId(IdGen.snowflakeId());
        eventInput.setRegionNo(device.getRegionCode());
        eventInput.setRecTime(0);
        eventInput.setDeviceId(device.getId());
        eventInput.setStoreTime(new Date());
        eventInput.setApplicationCode(SysUtil.getSysCode());
        eventInput.setTenantCode(device.getTenantCode());
        eventInputMapper.insert(eventInput);
        
        return device;
    }

    /**
     * iot??????????????????
     * @param deviceList
     * @return
     */
    public List<com.aswl.iot.dto.Device> generateIotDeviceList(List<Device> deviceList){
        if(deviceList == null){
            return null;
        }
        List<com.aswl.iot.dto.Device> list = new ArrayList<>();
        com.aswl.iot.dto.Device mqDevice = new com.aswl.iot.dto.Device();
        for(Device device : deviceList){
            mqDevice.setId(device.getId());
            mqDevice.setDeviceCode(device.getDeviceCode());
            mqDevice.setDeviceName(device.getDeviceName());
            mqDevice.setIp(device.getIp());
            mqDevice.setPort(device.getPort());
            mqDevice.setDeviceKind(device.getDeviceKindId());
            mqDevice.setDeviceType(device.getDeviceType());
            mqDevice.setDeviceModel(device.getDeviceModelId());
            mqDevice.setUserName(device.getUserName());
            mqDevice.setPassword(device.getPassword());
            mqDevice.setRemark(device.getRemark());
            mqDevice.setTenantCode(device.getTenantCode());
            mqDevice.setProjectCode(projectMapper.findById(device.getProjectId()));
            list.add(mqDevice);
        }
        return list;
    }


    /**
     * ??????????????????????????????
     * @param tenantCode
     * @return
     */
    private Integer getTenantMaxDeviceCount(String tenantCode)
    {
        // ??????????????????????????????????????????????????????????????????????????? -----
        ResponseBean<Tenant> r = userServiceClient.findTenantByTenantCode(tenantCode);
        if(r != null && ResponseBean.SUCCESS == r.getCode())
        {
            Tenant tenant=r.getData();
            if( tenant != null )
            {
//                if(tenant.getMaxDeviceNumber()==null)
//                {
//                    return 0;
//                }
//                else
//                {
//                    return tenant.getMaxDeviceNumber();
//                }
                return tenant.getMaxDeviceCount();
            }
            else
            {
                throw new CommonException("?????????????????????");
            }
        }
        else
        {
            throw new CommonException("????????????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param deviceDto
     * @return int
     */
    @Transactional
    public int update(DeviceDto deviceDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int count = 0;
        String isAsOs = deviceDto.getIsAsOs();
        Device device = new Device();
        BeanUtils.copyProperties(deviceDto, device);
        //??????IP???????????????
        if (StringUtils.isNotBlank(device.getIp())) {
            int num = deviceMapper.findUniqueIp(device.getId(),device.getIp(), device.getProjectId());
            if (num > 0 ) {
                throw new CommonException("??????IP?????????");
            }
        }
        if (CommonConstant.IS_ASOS_TRUE.equals(deviceDto.getIsAsOs())) {
            device.setModifier(deviceDto.getModifier());
            device.setModifyDate(new Date());
            device.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
            device.setApplicationCode(deviceDto.getApplicationCode());
            device.setTenantCode(deviceDto.getTenantCode());
        } else {
            device.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(), device.getProjectId());
        }
        if (deviceDto.getParentDeviceId() != null && !"".equals(deviceDto.getParentDeviceId())) {
            device.setParentDeviceId(deviceDto.getParentDeviceId());
        }
        if (deviceDto.getPurchaseDate() != null){
            try {
                device.setPurchaseDate(sdf.parse(deviceDto.getPurchaseDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        count = deviceMapper.update(device);
        if (deviceDto.getChilDevice().size() > 0) {
            List<Map> chilDevice = deviceDto.getChilDevice();
            for (Map chilMap : chilDevice) {
                String childrenId = (String) chilMap.get("childrenId");
                Device query = new Device();
                query.setId(childrenId);
                query = deviceMapper.get(query);
                query.setParentDeviceId(deviceDto.getId());
                Integer parentDcpowerNo = (Integer) chilMap.get("parentDcpowerNo");
                query.setParentDcpowerNo(parentDcpowerNo);
                Integer parentFibreOpticalNo = (Integer) chilMap.get("parentFibreOpticalNo");
                query.setParentFibreOpticalNo(parentFibreOpticalNo);
                Integer parentRj45No = (Integer) chilMap.get("parentRj45No");
                query.setParentRj45No(parentRj45No);
                deviceMapper.update(query);
            }
        }
        List<com.aswl.iot.dto.Device> mqList = new ArrayList<>();
        com.aswl.iot.dto.Device mqDevice = new com.aswl.iot.dto.Device();
        mqDevice.setId(device.getId());
        mqDevice.setDeviceCode(device.getDeviceCode());
        mqDevice.setDeviceName(device.getDeviceName());
        mqDevice.setIp(device.getIp());
        mqDevice.setPort(device.getPort());
        mqDevice.setDeviceKind(device.getDeviceKindId());
        mqDevice.setDeviceType(device.getDeviceType());
        mqDevice.setDeviceModel(device.getDeviceModelId());
        mqDevice.setUserName(device.getUserName());
        mqDevice.setPassword(device.getPassword());
        mqDevice.setRemark(device.getRemark());
        mqDevice.setTenantCode(device.getTenantCode());
        mqDevice.setProjectCode(projectMapper.findById(device.getProjectId()));
        mqList.add(mqDevice);
        mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.UPDATE_DEVICE_QUEUE, JSON.toJSONString(mqList), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        //???????????????????????????

        if(cityPlatformUtil.isCityPlatform()){
            List<Device> cityDevices = new ArrayList<>();
            cityDevices.add(device);
            if (cityDevices.size() > 0) {
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                cityPlatformDto.setFlag(CityPlatformConstant.UPDATE_DEVICE);
                cityPlatformDto.setData(cityDevices);
                byte[] bytes = JSON.toJSONString(cityPlatformDto).getBytes(StandardCharsets.UTF_8);
                cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_QUEUE, bytes);
            }
        }
        return count;
    }

    /**
     * ????????????
     *
     * @param device
     * @return int
     */
    @Transactional
    @Override
    public int delete(Device device) {
        return super.delete(device);
    }


    /**
     * ??????????????????
     *
     * @param parentId ??????ID
     * @return DeviceVo
     */
    public DeviceVo getSuperiorDevice(String parentId) {

        return deviceMapper.getSuperiorDevice(parentId);
    }

    /**
     * ??????????????????
     *
     * @param id ??????id
     * @return DeviceVo
     */
    public List<DeviceVo> getSubordinateDevice(String id) {

        return deviceMapper.getSubordinateDevice(id);
    }

    /**
     * ?????????????????????????????????
     * @param regionCode ????????????
     * @param lonMin ???????????????
     * @param lonMax ???????????????
     * @param latMin ???????????????
     * @param latMax ???????????????
     * @param devType ????????????
     * @param query ?????????
     * @param alarmLevels  ??????????????????
     * @param offlineFlag  ??????????????????????????????
     * @return  list
     */
    public List<Object> getDevicesByLonLat(String regionCode,double lonMin, double lonMax, double latMin, double latMax, String devType, String query,String [] alarmLevels,String queryDebug,
                                           String offlineFlag,String kind,String queryProjctId,String isAsOs,String loadType) {
        String projectId = null;
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        if (queryProjctId != null){
            projectId =queryProjctId;
        }else{
           projectId = SysUtil.getProjectId();
        }
        if(CommonConstant.IS_ASOS_TRUE.equals(isAsOs)){
            tenantCode = null;
            projectId = null;
        }else {
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
                //??????????????????
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
                //???????????????????????????????????????
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
                //???????????????????????????????????????SysUtil.getProjectId()?????????
            } else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        
        if ("2".equals(loadType)){
            Project project = new Project();
            project.setProjectId(projectId);
            project.setRegionCode(regionCode);
            project.setTenantCode(tenantCode);
            project.setLonMin(lonMin);
            project.setLonMax(lonMax);
            project.setLatMin(latMin);
            project.setLatMax(latMax);
            List<ProjectVo> projects = projectMapper.findProjectAlarmLevel(project);
            return  new ArrayList<>(projects);

        }else if("3".equals(loadType)){
            Region  region = new Region();
            region.setProjectId(projectId);
            region.setRegionCode(regionCode);
            region.setTenantCode(tenantCode);
            region.setLonMin(lonMin);
            region.setLonMax(lonMax);
            region.setLatMin(latMin);
            region.setLatMax(latMax);
            List<RegionVo> regions = regionMapper.findRegionAlarmLevel(region);
            return  new ArrayList<>(regions);
        }else {
            List<DeviceVo> deviceVoList = deviceMapper.getDevicesByLonLat(regionCode, lonMin, lonMax, latMin, latMax, devType, query, tenantCode, projectId, alarmLevels, queryDebug, offlineFlag, kind);

            //?????????????????? ??????????????????????????????
//       setIsVideo();

            /*  */
            if (deviceVoList != null && deviceVoList.size() > 0) {
                for (DeviceVo deviceVo : deviceVoList) {

                    deviceVo.setIsVideo(getIsVideoValue(deviceVo.getId(), deviceVo.getType(), deviceVo.getNetworkState()));

               /*
//               if ("2".equals(deviceVo.getType()) && "1".equals(deviceVo.getNetworkState())) {
               if (deviceVo.getType()==2 && "1".equals(deviceVo.getNetworkState())) {
                   List<RegionDeviceVo> video = regionMapper.findVideo(deviceVo.getId());
                   if (video != null && video.size() > 0) {
                       for (RegionDeviceVo regionDeviceVo : video) {
                           if (regionDeviceVo.getType() == 3) {
                               deviceVo.setIsVideo(1);
                           } else {
                               deviceVo.setIsVideo(0);
                           }
                       }
                   }
//               } else if ("3".equals(deviceVo.getType()) && "1".equals(deviceVo.getNetworkState())) {
               } else if (deviceVo.getType()==3 && "1".equals(deviceVo.getNetworkState())) {
                   deviceVo.setIsVideo(1);
               } else {
                   deviceVo.setIsVideo(0);
               }

           */

                }
            }
            return  new ArrayList<>(deviceVoList);
        }
       
    }

    /**
     * ?????? ?????????????????? ???????????????????????????????????????????????????
     *
     * @param id           ??????id
     * @param type         ??????
     * @param networkState ??????????????????
     * @return
     */
    public int getIsVideoValue(String id, int type, int networkState) {
        // ????????????
        int result = 0;

        if (type == 2 && networkState == 1){
            List<RegionDeviceVo> video = regionMapper.findVideo(id);
            if (video == null || video.size() == 0) {
                result = 0;
            }
            for (RegionDeviceVo regionDeviceVo : video) {
                if (regionDeviceVo.getType() == 3) {
                    result = 1;
                    break;
                }
            }
        } else if (type == 3 && networkState == 1) {
            result = 1;
        }
        return result;
    }

    //????????????id?????? ??? NetworkState (?????????????????????????????????????????????sql???????????????????????????????????????NetworkState)
    public Map<String, String> getDeviceNetworkState(List<String> list) {
        Map<String, String> map = new HashMap<String, String>();
        if (list != null || list.size() > 0) {
            String sb=list.stream().collect(Collectors.joining(","));
            List<DeviceVo> dList = deviceMapper.getDeviceNetworkState(sb);
            for (DeviceVo d : dList) {
                 map.put(d.getId(),String.valueOf(d.getNetworkState()));
            }
        }
        return map;
    }

    /**
     * ????????????????????????
     *
     * @param device ????????????
     * @return int
     */
    @Transactional
    public int deleteAllDevice(Device device) {
        if (StringUtils.isNotEmpty(device.getRegionId())) {
            String[] regionIds = device.getRegionId().split(",");
            for (int i = 0; i < regionIds.length; i++) {
                Region region = new Region();
                region.setId(regionIds[i]);
                Region upRegion = regionMapper.get(region);
                upRegion.setId(regionIds[i]);
                upRegion.setBoxNum(upRegion.getBoxNum() - 1);
                regionMapper.update(upRegion);
            }
        }
        if (StringUtils.isNotEmpty(device.getIdString())) {
            String[] ids = device.getIdString().split(",");
            deviceEventAlarmMapper.deleteByDeviceIds(device.getIdString());
            alarmMapper.deleteByDeviceIds(device.getIdString());
            baseMapper.deleteByDeviceIds(device.getIdString());
            eoutletMapper.deleteByDeviceIds(device.getIdString());
            ecurrentMapper.deleteByDeviceIds(device.getIdString());
            eswitchMapper.deleteByDeviceIds(device.getIdString());
            evoltageMapper.deleteByDeviceIds(device.getIdString());
            networkMapper.deleteByDeviceIds(device.getIdString());
            sfpMapper.deleteByDeviceIds(device.getIdString());
            eventSecCtlPowerMapper.deleteByDeviceIds(device.getIdString());
            eventSecCtlPowerOutputMapper.deleteByDeviceIds(device.getIdString());
            eventSecCtlPowerSetMapper.deleteByDeviceIds(device.getIdString());
            eventSfpInfoMapper.deleteByDeviceIds(device.getIdString());
            runMapper.deleteFlowRunByDeviceIds(device.getIdString());
            deviceFavoriteMapper.deleteByDeviceIds(device.getIdString());
            addressBaseMapper.updateAddressBaseAfterDeleteDevices(device.getIdString());
            eventElectricityMapper.deleteByDeviceIds(device.getIdString());
            eventPostureMapper.deleteByDeviceIds(device.getIdString());
            eventInputMapper.deleteByDeviceIds(device.getIdString());
            List idsList = Arrays.asList(ids);
            new Thread(() -> {
                //???????????????????????????
                List<String> tables = asEventHisAlarmMapper.findListTab(1);
                if(tables.size()>0){
                    for (String tab: tables) {
                        deviceEventAlarmMapper.deleteHisByDeviceIds(tab,idsList);
                    }
                }
            }).start();
            List<Map<String,String>> deviceMap = deviceMapper.getTenantCodeByIds(idsList);
            //mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.DELETE_DEVICE_QUEUE, JSON.toJSONString(idsList), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.DELETE_DEVICE_QUEUE, JSON.toJSONString(deviceMap), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
            if(cityPlatformUtil.isCityPlatform()){
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                cityPlatformDto.setFlag(CityPlatformConstant.DELETE_DEVICE);
                cityPlatformDto.setData(device.getIdString());
                byte[] bytes = JSON.toJSONString(cityPlatformDto).getBytes(StandardCharsets.UTF_8);
                cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_QUEUE, bytes);
            }
            return deviceMapper.deleteAll(ids);
        }
        return 0;
    }


    /**
     * ?????? excel
     *
     * @param file
     * @param projectId
     * @param tag  1:????????? 2:?????????
     * @return ResponseBean
     * @throws Exception
     */
    @Transactional
    public ResponseBean importDevice(MultipartFile file,String projectId,String tag) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("deviceName", "??????");
        map.put("deviceCode", "????????????");
        map.put("regionCode", "????????????");
        map.put("deviceModelName", "????????????");
        map.put("deviceType","????????????");
        map.put("address", "??????");
        map.put("ip", "IP");
        map.put("port", "??????");
        map.put("userName", "?????????");
        map.put("password", "??????");
        map.put("longitude", "??????");
        map.put("latitude", "??????");
        map.put("longitudeA", "??????A");
        map.put("latitudeA", "??????A");
        map.put("rj45No", "?????????");
        map.put("fibreOpticalNo", "?????????");
        map.put("parentDeviceCode", "??????????????????");
        map.put("parentRj45No", "???????????????");
        map.put("parentFibreOpticalNo", "???????????????");
        map.put("parentDcpowerNo", "?????????????????????");
        map.put("parentAcpowerNo", "??????220V?????????");
        map.put("purchaseDate", "????????????");
        map.put("guaranteeTime", "?????????");
        map.put("remark", "??????");
        List<DeviceDto> deviceDtos = MapUtil.map2Java(DeviceDto.class, ExcelToolUtil.importExcel(file.getInputStream(),map));
        return importDeviceByDeviceDtoList(deviceDtos,projectId,tag);
    }

    /**
     * ?????? ???????????? excel (??????????????????????????????????????????)
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Transactional
    public ResponseBean<JSONObject> importOsDevice(MultipartFile file) throws Exception {

        List<DeviceDto> deviceDtos = MapUtil.map2Java(DeviceDto.class,
                ExcelToolUtil.importExcel(file.getInputStream(), DeviceUtil.getOsDeviceMap()));

        return importDeviceByDeviceDtoList(deviceDtos,null,null);
    }

    /**
     * ?????? excel ????????????
     *
     * @param deviceDtos
     * @param projectId
     * @return
     * @throws Exception
     */
    @Transactional
    public ResponseBean<JSONObject> importDeviceByDeviceDtoList(List<DeviceDto> deviceDtos,String projectId,String tag) throws Exception {
        // ????????????ID??????????????????
        Project project = new Project();
        project.setProjectId(projectId);
        Project project1 = projectMapper.get(project);
        String isAsOs = "0";
        if (deviceDtos != null && "1".equals(deviceDtos.get(0).getIsAsOs())) {
            isAsOs = "1";
        }

        ResponseBean<JSONObject> responseBean = new ResponseBean<JSONObject>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //????????????
        Map<String, String> regionMap = new HashMap<>();
        Map<String, String> regionProjectMap = new HashMap<>();
        Region r = new Region(); // ????????????tenantCode???projectId??????
        List<Region> regions = regionMapper.findList(r);
        for (Region region : regions) {
            regionMap.put(region.getRegionCode(), region.getId());
            regionProjectMap.put(region.getRegionCode(), region.getProjectId());
        }
        //??????????????????
        Map<String, String> modelMap = new HashMap();
        List<DeviceModel> deviceModels = modelMapper.findAll();
        for (DeviceModel deviceModel : deviceModels) {
            modelMap.put(deviceModel.getDeviceModelName(), deviceModel.getId());
        }


        Map<String, String> projectIdMap = new HashMap<String, String>();
        Map<String, String> projectTenantMap = new HashMap<>();

        int totalCount = deviceDtos.size();
        List<String> errorStrs = new ArrayList<>();
        Project p = new Project();
        List<Project> pList;



        // ?????? deviceDtos ??????????????????
        List<DeviceDto> delList = new ArrayList<>();
        for (int i = 0; i < deviceDtos.size(); i++) {

            DeviceDto temp = deviceDtos.get(i);

            // ??????????????????
            if (StringUtils.isBlank(temp.getDeviceName())) {
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                delList.add(temp);
                continue;
            }
            //????????????
            if (StringUtils.isBlank(temp.getDeviceCode())) {
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                delList.add(temp);
                continue;
            }
            //??????????????????
            if (StringUtils.isBlank(temp.getDeviceModelName())) {
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                delList.add(temp);
                continue;
            }
            // ??????????????????????????????
            if (StringUtils.isNotEmpty(temp.getDeviceModelName()) && !modelMap.containsKey(temp.getDeviceModelName())) {
                errorStrs.add("??? " + (i + 1) + " ?????????????????? " + temp.getDeviceModelName() + " ?????????,????????????");
                delList.add(temp);
                continue;
            }
            
            //????????????
            if (StringUtils.isBlank(temp.getRegionCode())) {
                errorStrs.add("??? " + (i + 1) + " ???????????????????????????,????????????");
                delList.add(temp);
                continue;
            }
            //IP
            if (StringUtils.isBlank(temp.getIp())) {
                errorStrs.add("??? " + (i + 1) + " ??????IP?????????,????????????");
                delList.add(temp);
                continue;
            }
            String deviceCodeI = temp.getDeviceCode();
            for (int j = i + 1; j < deviceDtos.size(); j++) {
                String deviceCodeJ = deviceDtos.get(j).getDeviceCode();
                // ????????????????????????
                if (deviceCodeI.equals(deviceCodeJ)) {
                    errorStrs.add("??? " + (j + 1) + " ?????? deviceCode ??????" + deviceCodeI + " ????????????????????????");
                    deviceDtos.remove(j);
                    break;
                }
            }

            // ?????????????????????????????????
            if (StringUtils.isNotEmpty(temp.getRegionCode()) && !regionMap.containsKey(temp.getRegionCode())) {
                errorStrs.add("??? " + (i + 1) + " ?????? ???????????? ?????????????????????,????????????");
                delList.add(temp);
                continue;
            }

            // ??????????????????????????????????????????????????????????????????????????????????????????
            if (!projectId.equals(regionProjectMap.get(temp.getRegionCode()))) {
                errorStrs.add("??? " + (i + 1) + " ?????? ???????????? " + temp.getRegionCode() + " ??????????????? ???" +project1.getProjectName() +" ?????????" + ",????????????");
                delList.add(temp);
                continue;
            }
        }
        //?????????????????????????????????
        deviceDtos.removeAll(delList);


        //??????????????????????????????????????????????????????<?????????????????????????????????>
        Map<String,Integer> tenantMaxCountMap=new HashMap<>();
        //????????????????????????????????????????????????????????????????????????????????????+1????????????<??????????????????????????????????????????>
        Map<String,Integer> tenantDeviceCountMap=new HashMap<>();
        // ??????mq??????
        List<com.aswl.iot.dto.Device> list = new ArrayList<>();
        // ??????????????????????????????
        Map<String,Integer> regionBoxSetNumberMap= new HashMap<>();
        Map<String,Integer> regionDeviceIpNotNullNumberMap= new HashMap<>();
        Map<String,List<Device>> regionIpNullDevicesMap = new HashMap<>();
        List<Device> cityDevices = new ArrayList<>();
        // ?????? sql ??????????????????
        for (int i = 0; i < deviceDtos.size(); i++) {
            DeviceDto deviceDtoExcel = deviceDtos.get(i);
            deviceDtoExcel.setProjectId(projectId);

            //????????????????????????????????????
            if (StringUtils.isNotEmpty(deviceDtoExcel.getParentDeviceCode())) {
                Device parentDevice = deviceMapper.findUniqueDeviceCode(deviceDtoExcel.getParentDeviceCode(), deviceDtoExcel.getProjectId());
                if(parentDevice == null){
                    errorStrs.add("??? " + (i + 1) + " ?????? ?????????????????? ??????" + deviceDtoExcel.getParentDeviceCode() + " ????????????????????????");
                    continue;
                }
            }
            // ??????IP?????????????????????
            int num = deviceMapper.findUniqueIp(null,deviceDtoExcel.getIp(), deviceDtoExcel.getProjectId());
            if (num > 0) {
                // ????????????ip??????
                Device device = deviceMapper.findDevice(deviceDtoExcel.getIp(),deviceDtoExcel.getProjectId());
                BeanUtil.copyProperties(deviceDtoExcel,device,CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
                device.setId(device.getId());
                int update = deviceMapper.updateAddress(device);
                if (update  > 0 ){
                    continue;
                }
                errorStrs.add("??? " + (i + 1) + " ?????? IP ??????" + deviceDtoExcel.getIp() + " ????????????????????????");
                continue;
            }
            // ???????????????????????????????????????
            Device code = deviceMapper.findUniqueDeviceCode(deviceDtoExcel.getDeviceCode(), deviceDtoExcel.getProjectId());
            if (code != null) {
                errorStrs.add("??? " + (i + 1) + " ?????? ???????????? ??????" + deviceDtoExcel.getDeviceCode() + " ????????????????????????");
                continue;
            }

            String tenantCode = SysUtil.getTenantCode();
            // ?????????????????????????????????????????????id?????????????????????id??????????????????
            if( "1".equals(isAsOs) && deviceDtoExcel.getProjectId()!=null)
            {
                tenantCode=projectTenantMap.get(deviceDtoExcel.getProjectId());
            }


            // ????????????????????????????????????,????????????????????????????????????????????????
            Integer tenantMaxCount;
            Integer deviceCount;
            if(tenantMaxCountMap.containsKey(tenantCode))
            {
                tenantMaxCount=tenantMaxCountMap.get(tenantCode);
                deviceCount=tenantDeviceCountMap.get(tenantCode);
            }
            else
            {
                //????????????????????????????????????????????????????????????
                tenantMaxCount=getTenantMaxDeviceCount(tenantCode);
                deviceCount=deviceMapper.getDevicesCount(null,tenantCode,null);
                tenantMaxCountMap.put(tenantCode,tenantMaxCount);
                tenantDeviceCountMap.put(tenantCode,deviceCount);
            }
            if(tenantMaxCount !=null && deviceCount>=tenantMaxCount)
            {
                errorStrs.add("??? " + (i + 1) + " ?????? ??????????????????????????????????????????"+deviceCount+"????????????"+tenantMaxCount+"???????????????????????????????????????");
                continue;
            }
            tenantDeviceCountMap.put(tenantCode,deviceCount+1);
            //??????????????????????????????????????????
            String regionCode = deviceDtoExcel.getRegionCode();
            int regionBoxSetNumber = 0;
            int regionDeviceIpNotNullNumber = 0;
            List<Device> regionIpNullDevices = null;
            if ("1" .equals(tag)) {
                if (regionBoxSetNumberMap.containsKey(regionCode)) {
                    regionBoxSetNumber = regionBoxSetNumberMap.get(regionCode);
                    regionDeviceIpNotNullNumber = regionDeviceIpNotNullNumberMap.get(regionCode);
                    regionIpNullDevices = regionIpNullDevicesMap.get(regionCode);
                } else {
                    Region dbRegion = regionMapper.findByRegionCode(regionCode);
                    regionBoxSetNumber = dbRegion.getBoxNum();
                    regionBoxSetNumberMap.put(regionCode, regionBoxSetNumber);
                    int ipNotNullDevice = deviceMapper.findDeviceByIpNotNull(regionMap.get(regionCode),"1");
                    regionDeviceIpNotNullNumber = ipNotNullDevice;
                    regionDeviceIpNotNullNumberMap.put(regionCode, regionDeviceIpNotNullNumber);

                    regionIpNullDevices = deviceMapper.findDeviceByIpNull(regionMap.get(regionCode));
                    regionIpNullDevicesMap.put(regionCode, regionIpNullDevices);
                }
                //????????????????????????????????????????????????????????????
                if (regionDeviceIpNotNullNumber >= regionBoxSetNumber) {
                    errorStrs.add("??? " + (i + 1) + " ?????????????????????????????? " + regionCode + " ???????????????????????????" + regionDeviceIpNotNullNumber + "????????????" + regionBoxSetNumber + "???????????????????????????????????????");
                    continue;
                }
                //??????IP??????????????????
                if (regionIpNullDevices != null && regionIpNullDevices.size() > 0) {
                    Device ipNullDevice = regionIpNullDevices.get(0);
                    ipNullDevice.setIdString(ipNullDevice.getId());
                    this.deleteAllDevice(ipNullDevice);
                    regionIpNullDevices.remove(ipNullDevice);
                }
            }else{
                int regionCameraSetNumber = 0;
               if (regionBoxSetNumberMap.containsKey(regionCode)){
                   regionCameraSetNumber = regionBoxSetNumberMap.get(regionCode);
               }else{
                   Region dbRegion = regionMapper.findByRegionCode(regionCode);
                   regionCameraSetNumber = dbRegion.getCameraNum();
                   regionBoxSetNumberMap.put(regionCode, regionCameraSetNumber);
               }
                if (regionDeviceIpNotNullNumber >= regionCameraSetNumber) {
                    errorStrs.add("??? " + (i + 1) + " ?????????????????????????????? " + regionCode + " ???????????????????????????" + regionDeviceIpNotNullNumber + "????????????" + regionCameraSetNumber + "???????????????????????????????????????");
                    continue;
                }
                
            }
            regionDeviceIpNotNullNumberMap.put(regionCode, regionDeviceIpNotNullNumber + 1);

            Device device = new Device();
            BeanUtils.copyProperties(deviceDtoExcel, device);

            try {


                String id = IdGen.snowflakeId();
                device.setId(id);
                device.setProjectId(projectId);
//                deviceDtoExcel.setId(id);
                if (StringUtils.isNotEmpty(deviceDtoExcel.getRegionCode())) {
                    device.setRegionId(regionMap.get(deviceDtoExcel.getRegionCode()));
//                    deviceDtoExcel.setRegionId(regionMap.get(deviceDtos.get(i).getRegionCode()));
                }
                if (StringUtils.isNotEmpty(deviceDtoExcel.getDeviceModelName())) {
                    device.setDeviceModelId(modelMap.get(deviceDtoExcel.getDeviceModelName()));
//                    deviceDtoExcel.setDeviceModelId(modelMap.get(deviceDtos.get(i).getDeviceModelName()));
                }
                // ????????????????????????
                if (StringUtils.isNotEmpty(deviceDtoExcel.getParentDeviceCode())) {
                    Device parentDevice = deviceMapper.findUniqueDeviceCode(deviceDtoExcel.getParentDeviceCode(), deviceDtoExcel.getProjectId());
                    device.setParentDeviceId(parentDevice.getId());
//                    deviceDtoExcel.setParentDeviceId(parentDevice.getId());
                }
                device.setDelFlag(0);
                device.setCreator(SysUtil.getUser());
                device.setCreateDate(new Date());
//                deviceDtoExcel.setDelFlag(0);
//                deviceDtoExcel.setCreator(SysUtil.getUser());
//                deviceDtoExcel.setCreateDate(new Date());
                if (deviceDtos.get(i).getPurchaseDate() != null) {
                    device.setPurchaseDate(sdf.parse(deviceDtos.get(i).getPurchaseDate()));
//                    deviceDtoExcel.setPurchaseDate(deviceDtos.get(i).getPurchaseDate());
                }
                device.setApplicationCode(SysUtil.getSysCode());
//                deviceDtoExcel.setApplicationCode(SysUtil.getSysCode());
//                device.setTenantCode(SysUtil.getTenantCode());
                device.setTenantCode(tenantCode);
//                deviceDtoExcel.setTenantCode(tenantCode);
//                Thread thread = new Thread(new DeviceThread(deviceMapper, device));
                deviceMapper.insert(device);
                cityDevices.add(device);
                //??????????????????-?????????????????????????????????????????????
                DeviceEventAlarm deviceEventAlarm = new DeviceEventAlarm();
                deviceEventAlarm.setId(IdGen.snowflakeId());
                deviceEventAlarm.setDeviceId(device.getId());
                deviceEventAlarm.setRegionNo(device.getRegionCode());
                deviceEventAlarm.setIsAlarm(1);
                deviceEventAlarm.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                deviceEventAlarm.setStoreTime(new Date());
                deviceEventAlarm.setApplicationCode(SysUtil.getSysCode());
//                 deviceEventAlarm.setTenantCode(SysUtil.getTenantCode());
                deviceEventAlarm.setTenantCode(tenantCode);
                deviceEventAlarm.setAlarmLevel(AlarmLevelType.FAULT.getType());
                deviceEventAlarm.setAlarmTypes("IsOnlineAlarm0");
                deviceEventAlarm.setAlarmTypesDes("??????");
                deviceEventAlarm.setAlarmLevels("??????");
                deviceEventAlarm.setAlarmDates(DateUtil.formatDateTime(new Date()));
                deviceEventAlarm.setUEventId("0");
                deviceEventAlarmMapper.insert(deviceEventAlarm);

                // ????????????-??????
                EventAlarm eventAlarm = new EventAlarm();
                eventAlarm.setId(IdGen.snowflakeId());
                eventAlarm.setDeviceId(device.getId());
                eventAlarm.setRegionNo(device.getRegionCode());
//                eventAlarm.setRecTime(Long.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventAlarm.setRecTime(0L);
                eventAlarm.setStoreTime(new Date());
                eventAlarm.setApplicationCode(SysUtil.getSysCode());
//                eventAlarm.setTenantCode(SysUtil.getTenantCode());
                eventAlarm.setTenantCode(tenantCode);
                alarmMapper.insert(eventAlarm);
                // ????????????-??????
                EventBase eventBase = new EventBase();
                eventBase.setId(IdGen.snowflakeId());
                eventBase.setDeviceId(device.getId());
                eventBase.setRegionNo(device.getRegionCode());
//                eventBase.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventBase.setRecTime(0);
                eventBase.setStoreTime(new Date());
                eventBase.setApplicationCode(SysUtil.getSysCode());
//                eventBase.setTenantCode(SysUtil.getTenantCode());
                eventBase.setTenantCode(tenantCode);
                baseMapper.insert(eventBase);
                // ????????????-????????????
                EventEoutlet eventEoutlet = new EventEoutlet();
                eventEoutlet.setId(IdGen.snowflakeId());
                eventEoutlet.setDeviceId(device.getId());
                eventEoutlet.setRegionNo(device.getRegionCode());
//                eventEoutlet.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventEoutlet.setRecTime(0);
                eventEoutlet.setStoreTime(new Date());
                eventEoutlet.setApplicationCode(SysUtil.getSysCode());
//                eventEoutlet.setTenantCode(SysUtil.getTenantCode());
                eventEoutlet.setTenantCode(tenantCode);
                eoutletMapper.insert(eventEoutlet);
                //????????????-????????????
                EventEcurrent eventEcurrent = new EventEcurrent();
                eventEcurrent.setId(IdGen.snowflakeId());
                eventEcurrent.setDeviceId(device.getId());
                eventEcurrent.setRegionNo(device.getRegionCode());
//                eventEcurrent.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventEcurrent.setRecTime(0);
                eventEcurrent.setStoreTime(new Date());
                eventEcurrent.setApplicationCode(SysUtil.getSysCode());
//                eventEcurrent.setTenantCode(SysUtil.getTenantCode());
                eventEcurrent.setTenantCode(tenantCode);
                ecurrentMapper.insert(eventEcurrent);
                // ????????????-????????????
                EventEswitch eventEswitch = new EventEswitch();
                eventEswitch.setId(IdGen.snowflakeId());
                eventEswitch.setDeviceId(device.getId());
                eventEswitch.setRegionNo(device.getRegionCode());
//                eventEswitch.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventEswitch.setRecTime(0);
                eventEswitch.setStoreTime(new Date());
                eventEswitch.setApplicationCode(SysUtil.getSysCode());
//                eventEswitch.setTenantCode(SysUtil.getTenantCode());
                eventEswitch.setTenantCode(tenantCode);
                eswitchMapper.insert(eventEswitch);
                // ????????????-??????
                EventEvoltage evoltage = new EventEvoltage();
                evoltage.setId(IdGen.snowflakeId());
                evoltage.setDeviceId(device.getId());
                evoltage.setRegionNo(device.getRegionCode());
//                evoltage.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                evoltage.setRecTime(0);
                evoltage.setStoreTime(new Date());
                evoltage.setApplicationCode(SysUtil.getSysCode());
//                evoltage.setTenantCode(SysUtil.getTenantCode());
                evoltage.setTenantCode(tenantCode);
                evoltageMapper.insert(evoltage);
                // ????????????-??????
                EventNetwork eventNetwork = new EventNetwork();
                eventNetwork.setId(IdGen.snowflakeId());
                eventNetwork.setDeviceId(device.getId());
                eventNetwork.setRegionNo(device.getRegionCode());
                eventNetwork.setNetworkState(0);
//                eventNetwork.setStoreTime(new Date());
                eventNetwork.setApplicationCode(SysUtil.getSysCode());
//                eventNetwork.setTenantCode(SysUtil.getTenantCode());
                eventNetwork.setTenantCode(tenantCode);
                networkMapper.insert(eventNetwork);
                //????????????-??????
                EventSfp eventSfp = new EventSfp();
                eventSfp.setId(IdGen.snowflakeId());
                eventSfp.setDeviceId(device.getId());
                eventSfp.setRegionNo(device.getRegionCode());
//                eventSfp.setRecTime(Integer.valueOf(String.valueOf(new Date().getTime()).substring(0, 10)));
                eventSfp.setRecTime(0);
                eventSfp.setStoreTime(new Date());
                eventSfp.setApplicationCode(SysUtil.getSysCode());
//                eventSfp.setTenantCode(SysUtil.getTenantCode());
                eventSfp.setTenantCode(tenantCode);
                sfpMapper.insert(eventSfp);
               
                // ?????????
                EventElectricity eventElectricity = new EventElectricity();
                eventElectricity.setId(IdGen.snowflakeId());
                eventElectricity.setDeviceId(device.getId());
                eventElectricity.setRegionNo(device.getRegionCode());
                eventElectricity.setRecTime(0);
                eventElectricity.setStoreTime(new Date());
                eventElectricity.setApplicationCode(SysUtil.getSysCode());
                eventElectricity.setTenantCode(device.getTenantCode());
                eventElectricity.setPassway("00000000");
                eventElectricity.setLeakage(0d);
                eventElectricity.setSwitchNum(0);
                eventElectricity.setOverLoad(0d);
                eventElectricity.setUnderVoltage(0d);
                eventElectricity.setElectricity(0d);
                eventElectricityMapper.insert(eventElectricity);

                // ???????????????-??????
                EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
                eventSecCtlPower.setId(IdGen.snowflakeId());
                eventSecCtlPower.setRecTime(0);
                eventSecCtlPower.setDeviceId(device.getId());
                eventSecCtlPower.setRegionNo(device.getRegionCode());
                eventSecCtlPower.setStoreTime(new Date());
                eventSecCtlPower.setApplicationCode(SysUtil.getSysCode());
                eventSecCtlPower.setTenantCode(device.getTenantCode());
                eventSecCtlPowerMapper.insert(eventSecCtlPower);

                // ???????????????-????????????
                EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
                eventSecCtlPowerOutput.setId(IdGen.snowflakeId());
                eventSecCtlPowerOutput.setRecTime(0);
                eventSecCtlPowerOutput.setDeviceId(device.getId());
                eventSecCtlPowerOutput.setRegionNo(device.getRegionCode());
                eventSecCtlPowerOutput.setStoreTime(new Date());
                eventSecCtlPowerOutput.setApplicationCode(SysUtil.getSysCode());
                eventSecCtlPowerOutput.setTenantCode(device.getTenantCode());
                eventSecCtlPowerOutputMapper.insert(eventSecCtlPowerOutput);

                //???????????????-????????????
                EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
                eventSecCtlPowerSet.setId(IdGen.snowflakeId());
                eventSecCtlPowerSet.setRecTime(0);
                eventSecCtlPowerSet.setDeviceId(device.getId());
                eventSecCtlPowerSet.setRegionNo(device.getRegionCode());
                eventSecCtlPowerSet.setStoreTime(new Date());
                eventSecCtlPowerSet.setApplicationCode(SysUtil.getSysCode());
                eventSecCtlPowerSet.setTenantCode(device.getTenantCode());
                eventSecCtlPowerSetMapper.insert(eventSecCtlPowerSet);
                //SFP??????
                EventSfpInfo eventSfpInfo = new EventSfpInfo();
                eventSfpInfo.setId(IdGen.snowflakeId());
                eventSfpInfo.setRecTime(0);
                eventSfpInfo.setDeviceId(device.getId());
                eventSfpInfo.setRegionNo(device.getRegionCode());
                eventSfpInfo.setStoreTime(new Date());
                eventSfpInfo.setApplicationCode(SysUtil.getSysCode());
                eventSfpInfo.setTenantCode(device.getTenantCode());
                eventSfpInfoMapper.insert(eventSfpInfo);

                //????????????
                EventPosture eventPosture = new EventPosture();
                eventPosture.setId(IdGen.snowflakeId());
                eventPosture.setRecTime(0);
                eventPosture.setDeviceId(device.getId());
                eventPosture.setRegionNo(device.getRegionCode());
                eventPosture.setStoreTime(new Date());
                eventPosture.setApplicationCode(SysUtil.getSysCode());
                eventPosture.setTenantCode(device.getTenantCode());
                eventPostureMapper.insert(eventPosture);

                //????????????-??????
                EventInput eventInput = new EventInput();
                eventInput.setId(IdGen.snowflakeId());
                eventInput.setRegionNo(device.getRegionCode());
                eventInput.setRecTime(0);
                eventInput.setDeviceId(device.getId());
                eventInput.setStoreTime(new Date());
                eventInput.setApplicationCode(SysUtil.getSysCode());
                eventInput.setTenantCode(device.getTenantCode());
                eventInputMapper.insert(eventInput);
                
//                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.aswl.iot.dto.Device mqDevice = new com.aswl.iot.dto.Device();
            mqDevice.setId(device.getId());
            mqDevice.setDeviceCode(device.getDeviceCode());
            mqDevice.setDeviceName(device.getDeviceName());
            mqDevice.setIp(device.getIp());
            mqDevice.setPort(device.getPort());
            mqDevice.setDeviceKind(device.getDeviceKindId());
            mqDevice.setDeviceType(device.getDeviceType());
            mqDevice.setDeviceModel(device.getDeviceModelId());
            mqDevice.setUserName(device.getUserName());
            mqDevice.setPassword(device.getPassword());
            mqDevice.setRemark(device.getRemark());
            mqDevice.setTenantCode(device.getTenantCode());
            mqDevice.setProjectCode(projectMapper.findById(device.getProjectId()));
            list.add(mqDevice);
        }
        if (errorStrs.size() == 0) {
            responseBean.setCode(200);
//            responseBean.setData(Boolean.TRUE);
            JSONObject result = new JSONObject();
            result.put("isAllTrue", Boolean.TRUE);
            responseBean.setData(result);
            // responseBean.setData("???????????????????????????????????????" + totalCount);
            sendMq(list);
            sendCityPlatformMq(cityDevices);
            return responseBean;
        }
        JSONObject result = new JSONObject(16);
        result.put("totalCount", totalCount);
        result.put("errorCount", errorStrs.size());
        result.put("successCount", (totalCount - (errorStrs.size())));
        result.put("title", "????????????????????????????????????");
        result.put("msg", "??????????????????" + totalCount + "?????????????????????" + (totalCount - (errorStrs.size())) + "??????????????????" + errorStrs.size());
        String fileUrl = FileUtil.saveErrorTxtByList(errorStrs, "deviceImportExcelErrorLog");
        int lastIndex = fileUrl.lastIndexOf(File.separator);
        String fileName = fileUrl.substring(lastIndex + 1);
        if ("1".equals(isAsOs)) {
            // ??????ibrs?????????????????????
            //TODO ????????????????????????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????
            // ???????????????????????????????????????????????????????????????errorStrs??????????????????????????????
            // ????????????????????????????????????????????????
            result.put("winUploadPath", valueComponent.winUploadPath);
            result.put("linuxUploadPath", valueComponent.linuxUploadPath);
            result.put("errorStrs", errorStrs);
            result.put("originalFileUrl", fileUrl);
        }
        result.put("fileUrl", "/v1/sys/file/download/" + fileUrl);
        result.put("fileName", fileName);
        result.put("isAllTrue", Boolean.FALSE);
        responseBean.setData(result);
        responseBean.setCode(200);
//        if (list.size() > 0) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(3000);
//                    mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.ADD_DEVICE_QUEUE, JSON.toJSONString(list), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
        sendMq(list);
        sendCityPlatformMq(cityDevices);
        return responseBean;
    }

    /**
     * ??????excel
     *
     * @param deviceDto ????????????
     * @param request   ????????????
     * @param response  ????????????
     * @return ResponseBean
     */
    public ResponseBean<Boolean> exportDevice(@RequestBody DeviceDto deviceDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            // ??????response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "????????????" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<DeviceVo> devices = deviceMapper.findDeviceInfo(deviceDto);
            if (CollectionUtils.isEmpty(devices)) {
                throw new CommonException("?????????????????????.");
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(devices), DeviceUtil.getDeviceMap());
            return new ResponseBean<>(true, "????????????");
        } catch (Exception e) {
            log.error("???????????????????????????????????????", e);
            throw new CommonException("???????????????????????????????????????");
        }
    }

    public List<VideoStreamVo> findVideoVo(String id) {
        return deviceMapper.findVideoVo(id);
    }

    public VideoStreamVo findVideoStreamVo(String id) {
        return deviceMapper.findVideoStreamVo(id);
    }

    /**
     * ????????????id??????????????????ID??????
     *
     * @param id
     * @return
     */
    public Integer updateParentDeviceId(String id) {
        return deviceMapper.updateParentDeviceId(id);
    }

    /**
     * ???????????????????????????????????????Id??????
     */
    public List<String> findDeviceIdsByRegionAndKind(String regionCode, String deviceKind) {
        return deviceMapper.findDeviceIdsByRegionAndKind(regionCode, deviceKind);
    }

    /**
     * ???????????????????????????
     *
     * @param deviceDto
     * @return
     */
    public PageInfo<LinkedHashMap> openDeviceList(PageInfo<OpenDeviceVo> page, DeviceDto deviceDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(deviceMapper.openDeviceList(deviceDto));
    }

    /**
     *??????????????????????????????Ids????????????id
     * @return
     */
    public List<String> findByRegionIds(List<String> regionIds) {
        return deviceMapper.findByRegionIds(regionIds);
    }

    /**
     *
     */
    @Async
    @Scheduled(cron = "0 50 0/1 * * ?")
    public void systemAuthentication(){
        try{
            ResponseBean<Config> response = userServiceClient.findConfigByKey(CommonConstant.SysValidityAuth.SYS_VALIDITY_AUTH_KEY, SysUtil.getTenantCode());   //??????????????????
            Config config = null;
            if(response != null && response.getCode() == ResponseBean.SUCCESS){
                config = response.getData();
            }
            if(config == null){
//                sysAuthForbidden(new Config());
                return;
            }
            String authValue = config.getParamValue();
            authValue = PasswordUtil.decrypt(authValue, CommonConstant.SysValidityAuth.ENCRYPT_SECRET, PasswordUtil.getStaticSalt());
            String[] keys = authValue.split("_");
            String validDay = keys[0];
            String startDate = keys[1];
            String hours = keys[2];
            //?????????????????????
            hours = String.valueOf(Integer.parseInt(hours) + 1);
            //??????key
            authValue = validDay+"_"+startDate+"_"+hours;
            authValue = PasswordUtil.encrypt(authValue, CommonConstant.SysValidityAuth.ENCRYPT_SECRET, PasswordUtil.getStaticSalt());
            config.setParamValue(authValue);
            //??????config
            ConfigDto configDto = JSON.parseObject(JSON.toJSONString(config), ConfigDto.class);
            userServiceClient.updateConfig(configDto);

            if("*".equals(validDay)) {

            } else if("?".equals(startDate)) {
                startDate = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
                authValue = validDay+"_"+startDate+"_0";
                authValue = PasswordUtil.encrypt(authValue, CommonConstant.SysValidityAuth.ENCRYPT_SECRET, PasswordUtil.getStaticSalt());
                config.setParamValue(authValue);
                configDto = JSON.parseObject(JSON.toJSONString(config), ConfigDto.class);
                userServiceClient.updateConfig(configDto);
            } else {
                long startTime = DateUtils.parseDate(startDate, "yyyyMMddHHmmss").getTime();
//                if (startTime > new Date().getTime()) {    //???????????????????????????????????????
//                    System.exit(0);
//                }
                if ((startTime + 1000 * 60 * 60 * 24 * Long.parseLong(validDay)) <= new Date().getTime()
                        || (Integer.parseInt(hours) / 24) >= Integer.parseInt(validDay)) {    //??????????????????
                    sysAuthForbidden(config);
                    return;
                }
            }
            sysAuthActive();
        }catch (Exception e){
//            sysAuthForbidden(new Config());
        }
    }

    @Transactional
    public int updateAddressBaseAfterDeleteDevice(String deviceId) {
        //???????????????id????????????????????????????????????????????????????????????
        AddressBase addressBase = new AddressBase();
        addressBase.setBindDeviceId(deviceId);
        List<AddressBase> list = addressBaseMapper.findList(addressBase);
        if (list != null && list.size() > 0) {
            AddressBase ab = list.get(0);
            AddressBase temp = new AddressBase();
            temp.setId(ab.getId());
            temp.setBindDeviceId("");
            temp.setIsUsed(0);
            int update = addressBaseMapper.update(temp);
            return update;
        }
        return -1;
    }

    /**
     * ??????????????????????????????
     */
    private void sysAuthForbidden(Config config){
        redisTemplate.opsForValue().set(CommonConstant.SysValidityAuth.SYS_VALIDITY_AUTH_KEY, config.getParamValue());
    }

    private void sysAuthActive(){
        redisTemplate.delete(CommonConstant.SysValidityAuth.SYS_VALIDITY_AUTH_KEY);
    }


    /**
     * ????????????????????????
     * @param info
     * @param deviceAlarmConditionDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> monitorDeviceList(PageInfo info, DeviceAlarmConditionDto deviceAlarmConditionDto) {

        //??????: ????????????????????????????????????????????????,?????????????????????????????????
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmConditionDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);
        String queryProjctId =deviceAlarmConditionDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjctId)){
            deviceAlarmConditionDto.setProjectId(queryProjctId);
        }
        deviceAlarmConditionDto.setRegionCode(regionCode);
        //???pageSize????????????
//        PageHelper.startPage(info.getPageNum(), info.getPageSize());
        List<DeviceAlarmVo> topicList = deviceMapper.monitorDeviceList(deviceAlarmConditionDto, "parent");

        if (topicList.size() > 0) {
            //??????????????????
            List<DeviceAlarmVo> childrenList = deviceMapper.monitorDeviceList(deviceAlarmConditionDto, "children");
            List<DeviceAlarmVo> removeList = new ArrayList<>();
            if (childrenList.size() > 0) {
                for (DeviceAlarmVo deviceAlarmVo : childrenList) {
                    List<DeviceAlarmVo> newList = topicList.stream().filter(parent -> parent.getId().equals(deviceAlarmVo.getParentId())).collect(Collectors.toList());
                    if (newList.size() <= 0) {
                        removeList.add(deviceAlarmVo);
                        if (deviceAlarmVo.getType() == 3 && deviceAlarmVo.getIsOnline() == 1) {
                            deviceAlarmVo.setIsVideo(1);
                        }
                    }
                }
                if (removeList.size()>0) {
                    topicList.addAll(removeList);
                    childrenList.removeAll(removeList);
                }
                Map<String, List<DeviceAlarmVo>> childrenMap = new HashMap<>();
                for (DeviceAlarmVo deviceAlarmVo : childrenList) {
                    if (childrenMap.containsKey(deviceAlarmVo.getParentId())) {
                        childrenMap.get(deviceAlarmVo.getParentId()).add(deviceAlarmVo);
                    } else {
                        List<DeviceAlarmVo> voList = new ArrayList<>();
                        voList.add(deviceAlarmVo);
                        childrenMap.put(deviceAlarmVo.getParentId(), voList);
                    }
                }
                Object[] keySet = childrenMap.keySet().toArray();
                for (Object parentObject : keySet) {
                    String parentId = parentObject.toString();
                    if (childrenMap.containsKey(parentId)) {
                        List<DeviceAlarmVo> voList = childrenMap.get(parentId);
                        searchParent(voList, childrenMap);
//                        if("1".equals(deviceAlarmConditionDto.getOfflineFlag())) {
//                            topicList.addAll(voList);
//                        }
                    }
                }
                for (DeviceAlarmVo deviceAlarmVo : topicList) {
                    if (childrenMap.containsKey(deviceAlarmVo.getId())) {
                        List<DeviceAlarmVo> list = childrenMap.get(deviceAlarmVo.getId());
                        deviceAlarmVo.getChildren().addAll(list);
                        if (deviceAlarmVo.getType() != 3 || (deviceAlarmVo.getType() == 3 && deviceAlarmVo.getIsOnline() != 1)) {
                            if (list.size() > 0) {
                                deviceAlarmVo.setIsVideo(getIsVideoValue(list));
                            }
                        }
                    }
                }
            }
        }
        else {
            topicList = deviceMapper.monitorDeviceList(deviceAlarmConditionDto, null);
        }
//        return new PageInfo<>(topicList);
        return getPageInfo(info.getPageNum(),info.getPageSize(),topicList);
    }

    private void searchParent(List<DeviceAlarmVo> deviceAlarmVoList, Map<String, List<DeviceAlarmVo>> childrenMap){
        if(deviceAlarmVoList != null && deviceAlarmVoList.size() > 0){
            for(DeviceAlarmVo vo : deviceAlarmVoList){
                if(childrenMap.containsKey(vo.getId())){
                    List<DeviceAlarmVo> childList = childrenMap.get(vo.getId());
                    vo.getChildren().addAll(childList);
                    childrenMap.remove(vo.getId());
                    searchParent(childList, childrenMap);
                }
            }
        }
    }


    private int getIsVideoValue(List<DeviceAlarmVo> list){
        int result = 0;
        for (DeviceAlarmVo deviceAlarmVo : list) {
            if(deviceAlarmVo.getType() == 3 && deviceAlarmVo.getIsOnline() == 1){
                result = 1;
                break;
            }else {
                List<DeviceAlarmVo> children = deviceAlarmVo.getChildren();
                if(children.size() > 0){
                    getIsVideoValue(children);
                }
            }
        }
        return result;
    }

    /**
     * ??????????????????
     */
    private DeviceAlarmVo findParent(DeviceAlarmVo deviceAlarmVo,Map<String,DeviceAlarmVo> deviceAlarmVoMap){
        DeviceAlarmVo parentDevice = deviceAlarmVoMap.get(deviceAlarmVo.getParentId());
        if(parentDevice != null){
            List<DeviceAlarmVo> children = parentDevice.getChildren();
            if(!children.contains(deviceAlarmVo)){
                children.add(deviceAlarmVo);
            }
            deviceAlarmVoMap.put(parentDevice.getId(),parentDevice);
            if(parentDevice.getParentId() == null || "".equals(parentDevice.getParentId())){
                return parentDevice;
            }
            findParent(parentDevice,deviceAlarmVoMap);
        }
        return null;
    }

    /**
     * pagehelper????????????
     * @param currentPage ?????????
     * @param pageSize
     * @param list
     * @param <T>
     * @return
     */
    private static <T> PageInfo<T> getPageInfo(int currentPage, int pageSize, List<T> list) {
        int total = list.size();
        if (total > pageSize) {
            int toIndex = pageSize * currentPage;
            if (toIndex > total) {
                toIndex = total;
            }
            list = list.subList(pageSize * (currentPage - 1), toIndex);
        }
        Page<T> page = new Page<>(currentPage, pageSize);
        page.addAll(list);
        page.setPages((total + pageSize - 1) / pageSize);
        page.setTotal(total);
        return new PageInfo<>(page);
    }

    private void sendMq(List<com.aswl.iot.dto.Device> list){
        new Thread(() -> {
            if(list.size() > 0){
                try {
                    Thread.sleep(3000);
                    mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.ADD_DEVICE_QUEUE, JSON.toJSONString(list), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ????????????????????????
     * @param info
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> getCurrentAlarm(PageInfo<DeviceAlarmVo> info, DeviceAlarmDto deviceAlarmDto) {
        //???????????????????????????id
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        PageHelper.startPage(info.getPageNum(),info.getPageSize());
        List<DeviceAlarmVo> currentAlarm = deviceMapper.getCurrentAlarm(deviceAlarmDto);
        DeviceFaultDto deviceFaultDto = new DeviceFaultDto();
        deviceFaultDto.setTenantCode(tenantCode);
        deviceFaultDto.setProjectId(projectId);
        deviceFaultDto.setRegionCode(regionCode);
        if(currentAlarm.size() > 0){
            for (DeviceAlarmVo deviceAlarmVo : currentAlarm) {
                List<AlarmCapture> alarmCaptures = alarmCaptureService.findByEventId(deviceAlarmVo.getUEventId());
                deviceAlarmVo.setAlarmCaptures(alarmCaptures);
                deviceAlarmVo.setIntervalTime(DateUtils.formatDateTime(deviceAlarmVo.getIntervalTime()));
                if(deviceAlarmVo.getStatus() != null && (deviceAlarmVo.getStatus() == 1)){
                    if (deviceAlarmVo.getAlarmTypes() != null && !"".equals(deviceAlarmVo.getAlarmTypes())) {
                        deviceFaultDto.setDeviceId(deviceAlarmVo.getId());
                        String alarmTypes = deviceAlarmVo.getAlarmTypes();
                        for (String type : alarmTypes.split(",")) {
                            deviceFaultDto.setAlarmType(type);
                            List<DeviceFaultVo> faultVos  = runMapper.findNoFinish(deviceFaultDto);
                            if(faultVos ==  null || faultVos.size() == 0){
                                deviceAlarmVo.setIsManual(1);
                                break;
                            }
                        }
                    }else {
                        deviceAlarmVo.setIsManual(0);
                    }
                }
            }
        }
        return new PageInfo<>(currentAlarm);
    }


    /**
     * ????????????????????????
     * @param info
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> getCurrentStatus(PageInfo<DeviceAlarmVo> info, DeviceAlarmDto deviceAlarmDto) {
        //???????????????????????????id
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        PageHelper.startPage(info.getPageNum(),info.getPageSize());
        List<DeviceAlarmVo> currentStatus = deviceMapper.getCurrentStatus(deviceAlarmDto);
        if (currentStatus.size() > 0) {
                Date date = new Date();
            for (DeviceAlarmVo deviceAlarmVo : currentStatus) {
                List<AlarmCapture> alarmCaptures = alarmCaptureService.findByEventId(deviceAlarmVo.getUEventId());
                deviceAlarmVo.setAlarmCaptures(alarmCaptures);
                String promptTypes = deviceAlarmVo.getPromptTypes();
                if(promptTypes != null && !"".equals(promptTypes)){
                    int length = promptTypes.split(";").length;
                    StringBuilder propmtTime = new StringBuilder();
                    String time = DateUtil.formatDateTime(date);
                    for (int i = 0; i < length; i++) {
                        propmtTime.append(";").append(time);
                    }
                    deviceAlarmVo.setAlarmTimes(deviceAlarmVo.getAlarmTimes() != null && !"".equals(deviceAlarmVo.getAlarmTimes()) ? deviceAlarmVo.getAlarmTimes()+propmtTime.toString() : propmtTime.toString().substring(1));
                }
                if(deviceAlarmVo.getAlarmTime() == null || "".equals(deviceAlarmVo.getAlarmTime())){
                    deviceAlarmVo.setAlarmTime(DateUtil.formatDateTime(date));
                }
            }
        }
        return new PageInfo<>(currentStatus);
    }


    /**
     * ????????????????????????
     * @param info
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> getHistoryAlarm(PageInfo<DeviceAlarmVo> info, DeviceAlarmDto deviceAlarmDto) {
        //????????????7???
      if(deviceAlarmDto.getStartTime() == null || deviceAlarmDto.getEndTime() == null){
            Date date = new Date();
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            deviceAlarmDto.setEndTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
            instance.add(Calendar.DAY_OF_MONTH,-6);
            deviceAlarmDto.setStartTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        //???????????????????????????id
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        List<String> months = getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        String tablePrefix = "as_device_event_his_alarm_";
        List<String> tableNames = new ArrayList<>();
        for (String month : months) {
            tableNames.add(tablePrefix+month.replaceAll("-",""));
        }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
        if(tables.size() <= 0){
            return null;
        }
        deviceAlarmDto.setTableNames(tables);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        PageHelper.startPage(info.getPageNum(),info.getPageSize());
        List<DeviceAlarmVo> historyAlarm = deviceMapper.getHistoryAlarm(deviceAlarmDto);
        if(historyAlarm.size() > 0){
            for (DeviceAlarmVo deviceAlarmVo : historyAlarm) {
                deviceAlarmVo.setIntervalTime(DateUtils.formatDateTime(deviceAlarmVo.getIntervalTime()));
                deviceAlarmVo.setAlarmCaptures(alarmCaptureService.findByEventId(deviceAlarmVo.getUEventId()));
                String alarmTypeName = deviceAlarmVo.getAlarmTypeName();
                if(alarmTypeName != null && !"".equals(alarmTypeName)){
                    int length = alarmTypeName.split(";").length;
                    StringBuilder times = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        times.append(";").append(deviceAlarmVo.getAlarmTime());
                    }
                    deviceAlarmVo.setAlarmTimes(times.toString().substring(1));
                }
            }
        }
        return new PageInfo<>(historyAlarm);
    }

    /**
     * ????????????????????????
     * @param info
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> getHistoryStatus(PageInfo<Object> info, DeviceAlarmDto deviceAlarmDto) {

        //????????????7???
        if(deviceAlarmDto.getStartTime() == null || deviceAlarmDto.getEndTime() == null){
            Date date = new Date();
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            deviceAlarmDto.setEndTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
            instance.add(Calendar.DAY_OF_MONTH,-6);
            deviceAlarmDto.setStartTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        //???????????????????????????id
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        List<String> months = getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        String tablePrefix = "as_device_event_his_alarm_";
        List<String> tableNames = new ArrayList<>();
        for (String month : months) {
            tableNames.add(tablePrefix+month.replaceAll("-",""));
        }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
        if(tables.size() <= 0){
            return null;
        }
        deviceAlarmDto.setTableNames(tables);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        PageHelper.startPage(info.getPageNum(),info.getPageSize());
        List<DeviceAlarmVo> historyStatus = deviceMapper.getHistoryStatus(deviceAlarmDto);
        if(historyStatus.size() > 0){
            for (DeviceAlarmVo status : historyStatus) {
                status.setIntervalTime(DateUtils.formatDateTime(status.getIntervalTime()));
                status.setAlarmCaptures(alarmCaptureService.findByEventId(status.getUEventId()));
                String alarmTypeName = status.getAlarmTypeName();
                if(alarmTypeName != null && !"".equals(alarmTypeName)){
                    int length = alarmTypeName.split(";").length;
                    StringBuilder times = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        times.append(";").append(status.getAlarmTime());
                    }
                    status.setAlarmTimes(times.toString().substring(1));
                }
            }
        }
        return new PageInfo<>(historyStatus);
    }


    private static List<String> getMonthBetween(String minDate, String maxDate){
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//??????????????????
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            max.setTime(sdf.parse(maxDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * ??????????????????????????????
     * @param deviceCode
     * @return
     */
    public com.aswl.iot.dto.Device getByDeviceCode(String deviceCode) {
        return deviceMapper.getByDeviceCode(deviceCode);
    }


    /**
     * ????????????????????????
     * @param id
     * @return
     */
    public Map portDevice(String id) {
        DeviceModel model = modelMapper.getByDeviceId(id);
        Map result = new HashMap();
        if(model.getRj45Number() > 0){
            List<Map> rj45Device = deviceMapper.portDevice(id,"parent_rj45_no",model.getRj45Number());
            if(rj45Device.size() > 0){
                result.put("rj45",rj45Device);
            }
        }
        if(model.getDcpowerNumber() > 0){
            List<Map> dcpowerDevice = deviceMapper.portDevice(id,"parent_dcpower_no",model.getDcpowerNumber());
            if(dcpowerDevice.size() > 0){
                result.put("dcpower",dcpowerDevice);
            }
        }
        if(model.getFibreOpticalNumber() > 0){
            List<Map> fibreOpticalDevice = deviceMapper.portDevice(id,"parent_fibre_optical_no",model.getFibreOpticalNumber());
            if(fibreOpticalDevice.size() > 0){
                result.put("fibreOptical",fibreOpticalDevice);
            }
        }
        return result;
    }

    /**
     * ????????????????????????????????????
     * @param projectId
     * @return list
     */
    public List<ModelStatisticsVo> findModelStatistics (String projectId){
        return deviceMapper.findModelStatistics(projectId);
    }

    /**
     * ???????????????????????????????????????
     * @param id
     * @param port
     * @param portNum
     * @return
     */
    public Device findSubsetDevice(String id, String port, Integer portNum) {
        return deviceMapper.findSubsetDevice(id,port,portNum);
    }

    /**
     * ?????????????????????
     * @param dto
     * @return int
     */
    public int relationDevice (DeviceDto dto){
        deviceQrcodeMapper.updateByQrCode(dto.getFactoryCode());
        return deviceMapper.relationDevice(dto);
    }

    /**
     * ????????????????????????
     */
    private void sendCityPlatformMq(List<Device> devices) {

        if(cityPlatformUtil.isCityPlatform()){
            if (devices.size() > 0) {
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                cityPlatformDto.setFlag(CityPlatformConstant.ADD_DEVCICE);
                cityPlatformDto.setData(devices);
                byte[] bytes = JSON.toJSONString(cityPlatformDto).getBytes(StandardCharsets.UTF_8);
                cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_QUEUE, bytes);
            }
        }
    }

    /**
     * ?????????????????????????????????
     */
    @Transactional
    public void delCityDevice(String ids){
        String[] idsArr = ids.split(",");
        deviceMapper.deleteAll(idsArr);
        //???????????????
        deviceEventAlarmMapper.deleteByDeviceIds(ids);
        baseMapper.deleteByDeviceIds(ids);
        eventPatrolMapper.deleteByDeviceIds(ids);
        ecurrentMapper.deleteByDeviceIds(ids);
        eoutletMapper.deleteByDeviceIds(ids);
        eswitchMapper.deleteByDeviceIds(ids);
        evoltageMapper.deleteByDeviceIds(ids);
        sfpMapper.deleteByDeviceIds(ids);
        networkMapper.deleteByDeviceIds(ids);
        deviceEventAlarmMapper.deleteByDeviceIds(ids);
    }
    public Map getMapDeviceCount(DeviceAlarmConditionDto deviceAlarmConditionDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmConditionDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmConditionDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceAlarmConditionDto.setProjectId(queryProjectId);
        } 
        deviceAlarmConditionDto.setRegionCode(regionCode);
        return deviceMapper.getMapDeviceCount(deviceAlarmConditionDto);
    }

    public Device findByIpAndProjectCode(Device device) {
        return deviceMapper.findByIpAndProjectCode(device);
    }

    /**
     * ???????????????
     * @return
     */
    public List<DeviceVo> debugDeviceList(DeviceDto deviceDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    throw new CommonException("??????????????????????????????");
                }
                regionCode = userRegionCode;
            }
        }
        deviceDto.setRegionCode(regionCode);
        deviceDto.setTenantCode(tenantCode);
        deviceDto.setProjectId(projectId);
        return deviceMapper.debugDevice(deviceDto);
    }

    public Device getDeviceVo(String id){
        return deviceMapper.getDeviceVo(id);
    }

    /**
     * ??????????????????-??????
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> getCurrentAlarmExport(DeviceAlarmDto deviceAlarmDto) {
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        List<DeviceAlarmVo> currentAlarm = deviceMapper.getCurrentAlarm(deviceAlarmDto);
        if(currentAlarm != null && currentAlarm.size() > 0) {
            int num = 1;
            for (DeviceAlarmVo deviceAlarmVo : currentAlarm) {
                deviceAlarmVo.setNum(num);
                num++;
                deviceAlarmVo.setIntervalTime(DateUtils.formatDateTime(deviceAlarmVo.getIntervalTime()));
            }
        }
        return currentAlarm;
    }

    /**
     * ??????????????????-??????
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> getCurrentStatusExport(DeviceAlarmDto deviceAlarmDto) {
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        List<DeviceAlarmVo> currentStatus = deviceMapper.getCurrentStatus(deviceAlarmDto);
        if(currentStatus != null && currentStatus.size() > 0) {
            Date date = new Date();
            int num = 1;
            for (DeviceAlarmVo deviceAlarmVo : currentStatus) {
                deviceAlarmVo.setNum(num);
                num++;
                if(deviceAlarmVo.getAlarmTime() == null || "".equals(deviceAlarmVo.getAlarmTime())){
                    deviceAlarmVo.setAlarmTime(DateUtil.formatDateTime(date));
                }

            }
        }
        return currentStatus;
    }

    /**
     * ??????????????????-??????
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> getHistoryAlarmExport(DeviceAlarmDto deviceAlarmDto) {
        //????????????7???
        if(deviceAlarmDto.getStartTime() == null || deviceAlarmDto.getEndTime() == null){
            Date date = new Date();
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            deviceAlarmDto.setEndTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
            instance.add(Calendar.DAY_OF_MONTH,-6);
            deviceAlarmDto.setStartTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
        }

        List<String> months = getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        String tablePrefix = "as_device_event_his_alarm_";
        List<String> tableNames = new ArrayList<>();
        for (String month : months) {
            tableNames.add(tablePrefix+month.replaceAll("-",""));
        }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
        if(tables.size() <= 0){
            return null;
        }
        deviceAlarmDto.setTableNames(tables);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        List<DeviceAlarmVo> historyAlarm = deviceMapper.getHistoryAlarm(deviceAlarmDto);
        if(historyAlarm != null && historyAlarm.size() > 0){
            int num = 1;
            for (DeviceAlarmVo deviceAlarmVo : historyAlarm) {
                deviceAlarmVo.setNum(num);
                num++;
                deviceAlarmVo.setIntervalTime(DateUtils.formatDateTime(deviceAlarmVo.getIntervalTime()));
            }
        }
        return historyAlarm;
    }

    /**
     * ??????????????????-??????
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> getHistoryStatusExport(DeviceAlarmDto deviceAlarmDto) {
        //????????????7???
        if(deviceAlarmDto.getStartTime() == null || deviceAlarmDto.getEndTime() == null){
            Date date = new Date();
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            deviceAlarmDto.setEndTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
            instance.add(Calendar.DAY_OF_MONTH,-6);
            deviceAlarmDto.setStartTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        List<String> months = getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        String tablePrefix = "as_device_event_his_alarm_";
        List<String> tableNames = new ArrayList<>();
        for (String month : months) {
            tableNames.add(tablePrefix+month.replaceAll("-",""));
        }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
        if(tables.size() <= 0){
            return null;
        }
        deviceAlarmDto.setTableNames(tables);
        if(deviceAlarmDto.getAlarmTypeName() != null && !"".equals(deviceAlarmDto.getAlarmTypeName())){
            deviceAlarmDto.setAlarmTypeNames(Arrays.asList(deviceAlarmDto.getAlarmTypeName().split(",")));
        }
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        List<DeviceAlarmVo> historyStatus = deviceMapper.getHistoryStatus(deviceAlarmDto);
        if(historyStatus != null && historyStatus.size() > 0){
            int num = 1;
            for (DeviceAlarmVo status : historyStatus) {
                status.setNum(num);
                num++;
                status.setIntervalTime(DateUtils.formatDateTime(status.getIntervalTime()));
            }
        }
        return historyStatus;
    }
    /**
     * ????????????ID????????????IP???????????????
     * @param  regionId
     * @return list
     */
    public List<Device> findDeviceByIpNull(String regionId){
        return deviceMapper.findDeviceByIpNull(regionId);
    }

    /**
     * ????????????ID????????????IP???????????????????????????
     * @param  regionId
     * @return list
     */
    public int findDeviceByIpNotNull(String regionId,String tag){
        return deviceMapper.findDeviceByIpNotNull(regionId,tag);
    }

    /**
     * ??????????????????????????????????????????
     * @param device
     * @return
     */
    public int updateAddress(Device device){
        return deviceMapper.updateAddress(device);
    }

}