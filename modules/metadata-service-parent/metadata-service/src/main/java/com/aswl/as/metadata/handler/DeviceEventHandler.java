package com.aswl.as.metadata.handler;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aswl.as.common.core.config.CityPlatformSender;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.DtuPushConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.enums.*;
import com.aswl.as.common.core.model.DtuBodyBean;
import com.aswl.as.common.core.persistence.BaseEntity;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.AlarmOrderHandleVo;
import com.aswl.as.metadata.api.dto.CollectDataDto;
import com.aswl.as.metadata.api.module.EventHisPatrol;
import com.aswl.as.metadata.mapper.EventEoutletMapper;
import com.aswl.as.metadata.service.*;
import com.aswl.as.metadata.utils.CityPlatformUtil;
import com.aswl.as.metadata.vo.PushMobilePhoneDataVO;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.module.SmsResponse;
import com.aswl.as.user.api.module.User;
import com.aswl.iot.dto.*;
import com.aswl.iot.dto.DeviceEventAlarm;
import com.aswl.iot.dto.constant.MQConstants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.metadata.api.module.EventUcId;
import com.aswl.as.metadata.api.vo.DeviceEventHisAlarmVO;
import com.aswl.as.metadata.common.Globals;
import com.aswl.as.metadata.enums.AlarmTypeKindEnum;
import com.aswl.as.metadata.utils.DateUtils;
import com.aswl.as.metadata.vo.ChangeDeviceVo;
import com.aswl.as.metadata.vo.ChangeStatusVo;
import com.aswl.as.metadata.vo.IfaceCallbackDeviceStatusVO;
import com.aswl.as.metadata.websocket.push.DeviceEventPushExecutor;
import com.aswl.as.metadata.websocket.push.PushDevice;
import com.aswl.as.metadata.websocket.push.PushDeviceNetwork;
import com.aswl.as.metadata.websocket.push.PushEventData;
import com.aswl.as.metadata.websocket.push.PushLevel;
import com.aswl.as.metadata.websocket.push.PushParam;
import com.aswl.as.user.api.module.UserAuths;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableAsync
public class DeviceEventHandler {

	@Autowired
	@Qualifier(value = "rabbitTemplate")
	private AmqpTemplate amqpTemplate;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private DeviceKindService deviceKindService;
	@Autowired
	private AlarmLevelService alarmLevelService;
	@Autowired
	private AlarmTypeService alarmTypeService;
	@Autowired
	private DeviceEventService deviceEventService;
	@Autowired
	private EventEswitchService eventEswitchService;
	@Autowired
	private EventNetworkService eventNetworkService;
	@Autowired
	private DeviceEventAlarmService deviceEventAlarmService;
	@Autowired
	private DeviceEventHisAlarmService deviceEventHisAlarmService;
	@Autowired
	private AlarmWayService alarmWayService;
	@Autowired
	private DeviceEventBuilder deviceEventBuilder;
	@Autowired
	private DeviceEventJudge deviceEventJudge;
	@Autowired
	private DeviceEventPushExecutor deviceEventPushExecutor;
	@Autowired
	private EventEoutletService eventEoutletService;
	@Autowired
	private EventEvoltageService eventEvoltageService;
	@Autowired
	private EventSfpService eventSfpService;
	@Autowired
	private EventBaseService eventBaseService;
	@Autowired
	private EventEcurrentService eventEcurrentService;
	@Autowired
	private FlowRunService flowRunService;
	@Autowired
	private FlowRunPrcsService flowRunPrcsService;
	@Autowired
	private AlarmStatisticsService alarmStatisticsService;
	@Autowired
	private DeviceModelAlarmThresholdService deviceModelAlarmThresholdService;
	@Autowired
	private EventAlarmService eventAlarmService;
	@Autowired
	private EventUcIdService eventUcIdService;
	@Autowired
	private FlowRunLogService flowRunLogService;
	@Autowired
	private PicVedioHandler picVedioHandler;
	@Autowired
	private EventUcMetadataService eventUcMetadataService;
	@Autowired
	private SuperPlatformService superPlatformService;
	@Autowired
	private EventPatrolService eventPatrolService;
	@Autowired
	private EventHisPatrolService eventHisPatrolService;
	@Autowired
	private PatrolHisNoRecordService patrolHisNoRecordService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private AlarmTypeDeviceFavoriteService alarmTypeDeviceFavoriteService;
	@Autowired
	private RegionLeaderService regionLeaderService;
	@Autowired
	private UserAuthsService userAuthsService;
	@Autowired
	private JPushService jPushService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private EventDataExtService eventDataExtService;
	@Autowired
	private AlarmWaySettingService alarmWaySettingService;
	@Autowired
	private AlarmTypeUserFavoriteService alarmTypeUserFavoriteService;
	@Autowired
	private EventPostureService eventPostureService;
	@Autowired
	private EventInputService eventInputService;
	

	@Autowired
	@Qualifier(value = "redisTemplateJson")
	private RedisTemplate redisTemplate;

	private static final String DAY = "day";
	private static final String MONTH = "month";
	private static final String YEAR = "year";

	@Value("${ffmpeg.address}")
	private String ffmpegAddress;

	@Value("${ffmpeg.resolution}")
	private String resolution;

	@Value("${ffmpeg.time}")
	private String time;

	@Value("${ffmpeg.vedioTime}")
	private Long vedioTime;

	@Value("${ffmpeg.filePath}")
	private String filePath;

	@Value("${ffmpeg.isVedio}")
	private String isVedio;

	@Value("${ffmpeg.isPic}")
	private String isPic;

	@Value("${sys.projectCode}")
	private String projectCode;

    @Value("${sms.signName}")
    private String signName;
    @Value("${sms.alarmTemplateCode}")
    private String alarmTemplateCode;
    @Value("${sms.orderTemplateCode}")
    private String orderTemplateCode;

	@Autowired
	private AlarmSettingsService alarmSettingsService;

	@Autowired
	private CityPlatformSender cityPlatformSender;

	@Autowired
	private CityPlatformUtil cityPlatformUtil;

	@Autowired
	private EventElectricityService eventElectricityService;

	@Autowired
	private EventSfpInfoService eventSfpInfoService;

	@Autowired
	private EventSecCtlPowerService eventSecCtlPowerService;

	@Autowired
	private EventSecCtlPowerOutputService eventSecCtlPowerOutputService;

	@Autowired
	private EventSecCtlPowerSetService eventSecCtlPowerSetService;

	@Autowired
	private AlarmOrderHandleService alarmOrderHandleService;

	@Autowired
	private UserService userService;

	@Autowired
    private SmsService smsService;

	private static ConcurrentHashMap<String, com.aswl.iot.dto.Device> currentDeviceMap = new ConcurrentHashMap<>();

	@Async("getTaskExecutor")
	public void handler(DeviceEvent deviceEvent) throws Exception {
		com.aswl.iot.dto.Device syncDevice = null;
		synchronized (this) {
			com.aswl.iot.dto.Device device = deviceEvent.getDevice();
			if(device == null){
				return;
			}
			if(!currentDeviceMap.containsKey(device.getId())){
				currentDeviceMap.put(device.getId(), device);
				syncDevice = device;
			}else {
				syncDevice = currentDeviceMap.get(device.getId());
			}
		}
		synchronized (syncDevice) {
			if (deviceEvent instanceof DeviceEventAlarm) {
				eventAlarmProcess((DeviceEventAlarm) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventEcurrent) {
				eventEcurrentProcess((DeviceEventEcurrent) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventEswitch) {
				eventEswitchProcess((DeviceEventEswitch) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventEvoltage) {
			    eventEvoltageProcess((DeviceEventEvoltage) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventEoutlet) {
				eventEoutletProcess((DeviceEventEoutlet) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventSfp) {
				eventSfpProcess((DeviceEventSfp) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventIot) {
				eventIotProcess((DeviceEventIot) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventGPS) {
				eventGpsProcess((DeviceEventGPS) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventNetwork) {
				eventNetworkProcess((DeviceEventNetwork) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventPatrol) {
				eventPatrolProcess((DeviceEventPatrol) deviceEvent);
			} else if (deviceEvent instanceof DeviceEventAuthorize) {
				eventAuthorizeProcess((DeviceEventAuthorize) deviceEvent);
			}else if (deviceEvent instanceof DeviceElectricity) {
				eventElectricityProcess((DeviceElectricity) deviceEvent);
			}else if (deviceEvent instanceof DeviceEventSfpInfo) {	//sfp????????????
				eventSfpInfoProcess((DeviceEventSfpInfo) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPower) {	//?????????????????????
				eventSecCtlPowerProcess((DeviceSecCtlPower) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerAlarm) {	//???????????????????????????
				eventSecCtlPowerAlarmProcess((DeviceSecCtlPowerAlarm) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerVol) {	//?????????????????????????????????
				eventSecCtlPowerVolProcess((DeviceSecCtlPowerVol) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerRate) {	//?????????????????????????????????
				eventSecCtlPowerRateProcess((DeviceSecCtlPowerRate) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerElectric) {	//???????????????????????????????????????
				eventSecCtlPowerElectricProcess((DeviceSecCtlPowerElectric) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerOverVol) {	//???????????????????????????
				eventSecCtlPowerOverVolProcess((DeviceSecCtlPowerOverVol) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerUnderVol) {	//???????????????????????????
				eventSecCtlPowerUnderVolProcess((DeviceSecCtlPowerUnderVol) deviceEvent);
			}else if (deviceEvent instanceof DeviceSecCtlPowerOverElec) {	//???????????????????????????
				eventSecCtlPowerOverElecProcess((DeviceSecCtlPowerOverElec) deviceEvent);
			}else if(deviceEvent instanceof  DeviceEventPosture){ //????????????-????????????
				eventPostureProcess((DeviceEventPosture) deviceEvent);
			}else if(deviceEvent instanceof  DeviceEventInputStatus){ //????????????-????????????
				eventInputProcess((DeviceEventInputStatus) deviceEvent);
			}
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventAlarm
	 * @throws Exception
	 */
//	@Async
	public void eventAlarmProcess(DeviceEventAlarm deviceEventAlarm) throws Exception {
		// ??????????????????
		if (deviceEventAlarm == null || deviceEventAlarm.getDevice() == null) {
			log.info("deviceEventAlarm??????????????????");
			return;
		}
		String deviceId = deviceEventAlarm.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventAlarm.getDevice().getId());
			return;
		}
		// ??????EventAlarm
		EventAlarm eventAlarm_new = deviceEventBuilder.buildEventAlarm(deviceEventAlarm);
		//???????????????1
		if(eventAlarm_new.getPassway1() != null) {
			String passWay1 = new StringBuilder(eventAlarm_new.getPassway1()).reverse().toString();
			//???????????????????????????,???1??????,0??????
			if(passWay1.charAt(1) == '1'){
				if (eventAlarm_new.getFld01() != 0 && eventAlarm_new.getFld02() == 1) { // ????????????????????????
					eventAlarm_new.setFld01(2);
				}
			}
		}
		// ???EventAlarm??????
		EventAlarm eventAlarm = deviceEventService.findEventAlarmByDeviceId(deviceId);
		if (eventAlarm == null) { // ????????????????????????????????????
			eventAlarm_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventAlarmService.save(eventAlarm_new);
			return;
		}else if(eventAlarm.getRecTime() == 0){  //????????????
			eventAlarm_new.setId(eventAlarm.getId());
			eventAlarm_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventAlarmService.update(eventAlarm_new);
			initState(EventTableEnum.ALARM.getTableName(), eventAlarm_new, eventAlarm);
			return;
		}
		Map<String, Object> changeEventMap = deviceEventJudge.eventAlarmJudge(eventAlarm_new, eventAlarm);
		if (changeEventMap.size() > 0) { // ???????????????
			// ??????????????????????????????
			if(device.getDebug() != null && 1 == device.getDebug()){ // ????????????
				debugDeviceEvent(deviceEventAlarm,device,EventTableEnum.ALARM.getTableName(),eventAlarm_new,eventAlarm,changeEventMap,eventAlarm_new.getStoreTime(),eventAlarm_new.getRecTime());
				return;
			}
			eventLogicHandler(deviceEventAlarm,device, EventTableEnum.ALARM.getTableName(), eventAlarm_new, eventAlarm, changeEventMap,
					eventAlarm_new.getStoreTime(), eventAlarm_new.getRecTime());
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventEcurrent
	 */
//	@Async
	public void eventEcurrentProcess(DeviceEventEcurrent deviceEventEcurrent) {
		// ??????????????????
		if (deviceEventEcurrent == null || deviceEventEcurrent.getDevice() == null) {
			log.info("deviceEventEcurrent ??????????????????");
			return;
		}
		String deviceId = deviceEventEcurrent.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventEcurrent.getDevice().getId());
			return;
		}
		// ??????EventEcurrent
		EventEcurrent eventEcurrent_new = deviceEventBuilder.builderEventEcurrent(deviceEventEcurrent);
		// ???????????????EventEswitch
		EventEcurrent eventEcurrent_old = null;
		eventEcurrent_old = eventEcurrentService.findByDeviceId(deviceId);
		if (eventEcurrent_old == null) {
			eventEcurrent_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEcurrentService.save(eventEcurrent_new);
			return;
		}else if(eventEcurrent_old.getRecTime() == 0){
			eventEcurrent_new.setId(eventEcurrent_old.getId());
			eventEcurrent_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEcurrentService.update(eventEcurrent_new);
			initState(EventTableEnum.CURRENT.getTableName(), eventEcurrent_new, eventEcurrent_old);
			return;
		}
		// ????????????
		Map<String, Object> changeEventMap = deviceEventJudge.eventEcurrentJudge(eventEcurrent_new, eventEcurrent_old);

		if (changeEventMap.size() > 0) {
			if(device.getDebug() != null && 1 == device.getDebug()){ //?????????????????????,???????????????????????????
				this.debugDeviceEvent(deviceEventEcurrent,device, EventTableEnum.CURRENT.getTableName(), eventEcurrent_new, eventEcurrent_old, changeEventMap,
						eventEcurrent_new.getStoreTime(), Long.valueOf(eventEcurrent_new.getRecTime()));
				return;
			}
			// ??????????????????????????????
			eventLogicHandler(deviceEventEcurrent,device, EventTableEnum.CURRENT.getTableName(), eventEcurrent_new, eventEcurrent_old, changeEventMap,
					eventEcurrent_new.getStoreTime(), Long.valueOf(eventEcurrent_new.getRecTime()));
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventEswitch
	 */
//	@Async
	public void eventEswitchProcess(DeviceEventEswitch deviceEventEswitch) throws Exception {
		// ??????????????????
		if (deviceEventEswitch == null || deviceEventEswitch.getDevice() == null) {
			log.info("deviceEventEswitch ??????????????????");
			return;
		}
		String deviceId = deviceEventEswitch.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventEswitch.getDevice().getId());
			return;
		}
		// ??????EventEswitch
		EventEswitch eventEswitch_new = deviceEventBuilder.buildEventEswitch(deviceEventEswitch);
		// ???????????????EventEswitch
		EventEswitch eventEswitch_old = null;
//        eventEswitch_old.setDeviceId(deviceId);
		eventEswitch_old = eventEswitchService.findByDeviceId(deviceId);
		if (eventEswitch_old == null) {
			eventEswitch_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEswitchService.save(eventEswitch_new);
			return;
		}else if(eventEswitch_old.getRecTime() == 0){
			eventEswitch_new.setId(eventEswitch_old.getId());
			eventEswitch_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEswitchService.update(eventEswitch_new);
			initState(EventTableEnum.SWITCH.getTableName(), eventEswitch_new, eventEswitch_old);
			return;
		}
		// ????????????
		Map<String, Object> changeEventMap = deviceEventJudge.eventEswitchJudge(eventEswitch_new, eventEswitch_old);
		if (changeEventMap.size() > 0) {
			if(device.getDebug() != null && 1 == device.getDebug()){ //?????????????????????,???????????????????????????
				this.debugDeviceEvent(deviceEventEswitch,device, EventTableEnum.SWITCH.getTableName(), eventEswitch_new, eventEswitch_old, changeEventMap,
						eventEswitch_new.getStoreTime(), Long.valueOf(eventEswitch_new.getRecTime()));
				return;
			}
			// ??????????????????????????????
			eventLogicHandler(deviceEventEswitch,device, EventTableEnum.SWITCH.getTableName(), eventEswitch_new, eventEswitch_old, changeEventMap,
					eventEswitch_new.getStoreTime(), Long.valueOf(eventEswitch_new.getRecTime()));
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventEvoltage
	 */
//	@Async
	public void eventEvoltageProcess(DeviceEventEvoltage deviceEventEvoltage) {
		// ??????????????????
		if (deviceEventEvoltage == null || deviceEventEvoltage.getDevice() == null) {
			log.info("deviceEventEvoltage ??????????????????");
			return;
		}
		String deviceId = deviceEventEvoltage.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventEvoltage.getDevice().getId());
			return;
		}
		// ??????EventEvoltage
		EventEvoltage eventEvoltage_new = deviceEventBuilder.buildEventEvoltage(deviceEventEvoltage);
		// ??????????????????EventEvoltage
		EventEvoltage eventEvoltage_old = eventEvoltageService.findByDeviceId(deviceId);
		// null???????????????
		if (eventEvoltage_old == null) {
			eventEvoltage_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEvoltageService.save(eventEvoltage_new);
			return;
		}else if(eventEvoltage_old.getRecTime() == 0){
			eventEvoltage_new.setId(eventEvoltage_old.getId());
			eventEvoltage_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEvoltageService.update(eventEvoltage_new);
			initState(EventTableEnum.VOLTAGE.getTableName(), eventEvoltage_new, eventEvoltage_old);
			return;
		}
		// ?????????????????????
		Map<String, Object> changeEventMap = deviceEventJudge.eventEvoltageJudge(eventEvoltage_new, eventEvoltage_old);
		if (changeEventMap.size() > 0) {
			if(device.getDebug() != null && 1 == device.getDebug()){ //?????????????????????,???????????????????????????
				this.debugDeviceEvent(deviceEventEvoltage,device, EventTableEnum.VOLTAGE.getTableName(), eventEvoltage_new, eventEvoltage_old, changeEventMap,
						eventEvoltage_new.getStoreTime(), Long.valueOf(eventEvoltage_new.getRecTime()));
				return;
			}
			// ??????????????????????????????
			eventLogicHandler(deviceEventEvoltage,device, EventTableEnum.VOLTAGE.getTableName(), eventEvoltage_new, eventEvoltage_old, changeEventMap,
					eventEvoltage_new.getStoreTime(), Long.valueOf(eventEvoltage_new.getRecTime()));
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventEoutlet
	 */
//	@Async
	public void eventEoutletProcess(DeviceEventEoutlet deviceEventEoutlet) {
		// ??????????????????
		if (deviceEventEoutlet == null || deviceEventEoutlet.getDevice() == null) {
			log.info("deviceEventEoutlet ??????????????????");
			return;
		}
		String deviceId = deviceEventEoutlet.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventEoutlet.getDevice().getId());
			return;
		}
		// ??????????????????
		EventEoutlet eventEoutlet_new = deviceEventBuilder.builderEventEoutlet(deviceEventEoutlet);
		// ??????????????????EventEoutlet
		EventEoutlet eventEoutlet_old = eventEoutletService.findByDeviceId(deviceId);
		if (eventEoutlet_old == null) {
			// ????????????????????????,???????????????
			eventEoutlet_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventEoutletService.save(eventEoutlet_new);
			return;
		}else if(eventEoutlet_old.getRecTime() == 0){
//			eventEoutlet_new.setId(eventEoutlet_old.getId());
			eventEoutlet_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
//			eventEoutletService.update(eventEoutlet_new);
			initState(EventTableEnum.OUTLET.getTableName(), eventEoutlet_new, eventEoutlet_old);
            saveBean(EventTableEnum.OUTLET.getTableName(), eventEoutlet_new, eventEoutlet_old);
			return;
		}
		// ?????????????????????,????????????????????????
		Map<String, Object> changeEventMap = deviceEventJudge.eventEoutletJudge(eventEoutlet_new, eventEoutlet_old);
        if (changeEventMap.size() > 0) {
            changeEventMap = outletSfpForbiddenProcess(device, changeEventMap, EventTableEnum.OUTLET.getTableName(), eventEoutlet_old);
            if (changeEventMap.size()>0) {
            	if(device.getDebug() != null && 1 == device.getDebug()){
            		this.debugDeviceEvent(deviceEventEoutlet,device, EventTableEnum.OUTLET.getTableName(), eventEoutlet_new, eventEoutlet_old, changeEventMap,
							eventEoutlet_new.getStoreTime(), Long.valueOf(eventEoutlet_new.getRecTime()));
            		return;
				}
                // ??????????????????????????????
                eventLogicHandler(deviceEventEoutlet,device, EventTableEnum.OUTLET.getTableName(), eventEoutlet_new, eventEoutlet_old, changeEventMap,
                        eventEoutlet_new.getStoreTime(), Long.valueOf(eventEoutlet_new.getRecTime()));
            }
        }
	}

	/**
	 * ????????????????????????
	 *
	 * @param deviceEventSfp
	 */
//	@Async
	public void eventSfpProcess(DeviceEventSfp deviceEventSfp) {
		// ??????????????????
		if (deviceEventSfp == null || deviceEventSfp.getDevice() == null) {
			log.info("deviceEventSfp ??????????????????");
			return;
		}
		String deviceId = deviceEventSfp.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventSfp.getDevice().getId());
			return;
		}
		// ??????????????????EventSfp
		EventSfp eventSfp_new = deviceEventBuilder.builderEventSfp(deviceEventSfp);
		// ??????????????????EventSfp
		EventSfp eventSfp_old = eventSfpService.findByDeviceId(deviceId);
		if (eventSfp_old == null) {
			// ????????????????????????,???????????????
			eventSfp_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventSfpService.save(eventSfp_new);
			return;
		}else if(eventSfp_old.getRecTime() == 0){
//			eventSfp_new.setId(eventSfp_old.getId());
			eventSfp_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
//			eventSfpService.update(eventSfp_new);
			initState(EventTableEnum.SFP.getTableName(), eventSfp_new, eventSfp_old);
			saveBean(EventTableEnum.SFP.getTableName(), eventSfp_new, eventSfp_old);
			return;
		}
		// ??????
		Map<String, Object> changeEventMap = deviceEventJudge.eventSfpJudge(eventSfp_new, eventSfp_old);
        if (changeEventMap.size() > 0) {
			changeEventMap = outletSfpForbiddenProcess(device, changeEventMap, EventTableEnum.SFP.getTableName(), eventSfp_old);
            if (changeEventMap.size()>0) {
				if(device.getDebug() != null && 1 == device.getDebug()){
					this.debugDeviceEvent(deviceEventSfp,device, EventTableEnum.SFP.getTableName(), eventSfp_new, eventSfp_old, changeEventMap,
							eventSfp_new.getStoreTime(), Long.valueOf(eventSfp_new.getRecTime()));
					return;
				}
                // ??????????????????????????????
                eventLogicHandler(deviceEventSfp,device, EventTableEnum.SFP.getTableName(), eventSfp_new, eventSfp_old, changeEventMap,
                        eventSfp_new.getStoreTime(), Long.valueOf(eventSfp_new.getRecTime()));
            }
        }

	}

	/**
	 * ???????????????????????????
	 *
	 * @param deviceEventIot
	 */
//	@Async
	public void eventIotProcess(DeviceEventIot deviceEventIot) {
		// ??????????????????
		if (deviceEventIot == null || deviceEventIot.getDevice() == null) {
			log.info("deviceEventIot ??????????????????");
			return;
		}
		String deviceId = deviceEventIot.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventIot.getDevice().getId());
			return;
		}
		// ???????????????EventIot
		EventBase eventBase_new = deviceEventBuilder.builderEventBase(deviceEventIot);
		// ?????????????????????????????????
		EventBase eventBase_old = eventBaseService.findByDeviceId(deviceId);
		if (eventBase_old == null) { // ???????????????,?????????
			eventBase_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventBaseService.save(eventBase_new);
			return;
		}else if(eventBase_old.getRecTime() == 0){
			eventBase_new.setId(eventBase_old.getId());
			eventBase_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventBaseService.update(eventBase_new);
			initState("as_event_iot", eventBase_new, eventBase_old);
			return;
		}
		Map<String, Object> changeEventMap = deviceEventJudge.eventIotJudge(eventBase_new, eventBase_old);
		if (changeEventMap.size() > 0) {
			if(device.getDebug() != null && 1 == device.getDebug()){  //?????????????????????
				this.debugDeviceEvent(deviceEventIot,device, EventTableEnum.BASE.getTableName(), eventBase_new, eventBase_old, changeEventMap,
						eventBase_new.getStoreTime(), Long.valueOf(eventBase_new.getRecTime()));
				return;
			}
			// ??????????????????????????????
			eventLogicHandler(deviceEventIot,device, EventTableEnum.BASE.getTableName(), eventBase_new, eventBase_old, changeEventMap,
					eventBase_new.getStoreTime(), Long.valueOf(eventBase_new.getRecTime()));
		}

	}

	/**
	 * ??????GPS????????????
	 *
	 * @param deviceEventGPS
	 */
//	@Async
	public void eventGpsProcess(DeviceEventGPS deviceEventGPS) {
		// ??????????????????
		if (deviceEventGPS == null || deviceEventGPS.getDevice() == null) {
			log.info("deviceEventGPS ??????????????????");
			return;
		}
		String deviceId = deviceEventGPS.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventGPS.getDevice().getId());
			return;
		}
		// ??????EventBase
		EventBase eventBase_new = deviceEventBuilder.builderEventBase(deviceEventGPS);
		// ???????????????eventBase
		EventBase eventBase_old = eventBaseService.findByDeviceId(deviceId);
		if (eventBase_old == null) { // ???????????????,??????????????????
			eventBase_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventBaseService.save(eventBase_new);
			return;
		}else if(eventBase_old.getRecTime() == 0){
			eventBase_new.setId(eventBase_old.getId());
			eventBase_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventBaseService.update(eventBase_new);
			return;
		}
		// ??????
		Map<String, Object> map = deviceEventJudge.eventGPSJudge(eventBase_new, eventBase_old);
		if (map.size() > 0) { // size??????0,??????GPS???????????????,????????????,GPS??????????????????
			eventBase_new.setDeviceId(eventBase_old.getDeviceId());
			BeanUtil.copyProperties(eventBase_new, eventBase_old,
					CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventBaseService.update(eventBase_old);
		}
	}

	//	@Async
	public void eventNetworkProcess(DeviceEventNetwork deviceEventNetwork) {
		// ??????????????????
		if (deviceEventNetwork == null || deviceEventNetwork.getDevice() == null) {
			log.info("deviceEventNetwork ??????????????????");
			return;
		}
		String deviceId = deviceEventNetwork.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventNetwork.getDevice().getId());
			return;
		}
		// ???????????????EventIot
		EventNetwork eventNetwork_new = deviceEventBuilder.buildEventNetwork(deviceEventNetwork);
		// ?????????????????????????????????
		EventNetwork eventNetwork_old = eventNetworkService.findByDeviceId(deviceId);
		if (eventNetwork_old == null) { // ???????????????,?????????
			eventNetwork_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventNetworkService.save(eventNetwork_new);
			syncCityNetstat(eventNetwork_new);
			return;
		}else if(eventNetwork_old.getStoreTime() == null){
			eventNetwork_new.setId(eventNetwork_old.getId());
			eventNetwork_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			eventNetworkService.update(eventNetwork_new);
            //?????????????????????????????????
            initNetwork(eventNetwork_new,device.getTenantCode());
            syncCityNetstat(eventNetwork_new);
			return;
		}

		Map<String, Object> changeEventMap = deviceEventJudge.eventNetworkJudge(eventNetwork_new, eventNetwork_old);
		if (changeEventMap.size() > 0) {
			boolean isOnline = eventNetwork_new.getNetworkState().intValue() == 1;
			//??????????????????
			if (!isOnline) {
				if (!device.getDeviceType().contains("IPC")) {
					EventAlarm eventAlarm = eventAlarmService.findByDeviceId(deviceId);
					if (eventAlarm.getFld08() == 1) {
						eventNetwork_new.setNetworkState(2); //????????????
					}
					if (eventAlarm.getFld08() != 1) {
						eventNetwork_new.setNetworkState(3);	//??????????????????
					}
				}
			}

			if(device.getDebug() != null && 1 == device.getDebug()){  //????????????
				this.debugDeviceEvent(deviceEventNetwork,device,EventTableEnum.NETWORK.getTableName(),eventNetwork_new,eventNetwork_old,changeEventMap,eventNetwork_new.getStoreTime(),(eventNetwork_new.getStoreTime().getTime() / 1000));
				return;
			}
			// ??????pushDeviceNetwork
			PushDeviceNetwork pushDeviceNetwork = new PushDeviceNetwork(deviceId, deviceEventNetwork.getRecTime(),
					isOnline);
			deviceEventPushExecutor.pushOnlineStatus(new ArrayList<>(), pushDeviceNetwork);

			// ????????????????????????
			String regionName = "";
			// ???????????????
			PushEventData pushEventData = new PushEventData();
			// ??????????????????
//			Region region = regionServiceClient.metaGetRegionById(device.getRegionCode());
			Region region = regionService.findByRegionCode(device.getRegionCode());
			if (region != null) {
				regionName = region.getFullName();
			}
			// ????????????
			PushDevice pushDevice = new PushDevice(device.getId(), device.getDeviceName(), device.getDeviceType(), "",
					eventNetwork_new.getStoreTime(), regionName, device.getIp());
			pushEventData.setPushDevice(pushDevice);
			// ???????????????
			List<PushParam> pushParamList = new ArrayList<PushParam>();

			// ??????????????????
			EventUcId eventUcId = new EventUcId();
			eventUcId.setId(IdGen.snowflakeId());
			eventUcIdService.insert(eventUcId);

			// ?????????????????????
			AlarmLevel alarmLevel_highest = null;
			StringBuffer pushMessageTips = new StringBuffer();
			StringBuffer pushMessageTipsEn = new StringBuffer();
			StringBuffer statusNames = new StringBuffer(); // statusName???
			StringBuffer alarmTypes = new StringBuffer(); // ??????????????????
			StringBuffer alarmTypesDes = new StringBuffer(); // ????????????????????????????????????
			StringBuffer alarmLevels = new StringBuffer(); // ?????????
			StringBuffer alarmDates = new StringBuffer(); // ???????????????
			StringBuffer promptTypes = new StringBuffer(); // ??????????????????
			StringBuffer promptTypesDes = new StringBuffer(); // ????????????????????????????????????
			StringBuffer changeTypes = new StringBuffer(); // ????????????????????????
			StringBuffer changeTypesDes = new StringBuffer(); // ??????????????????????????????????????????
			StringBuffer uEventIds = new StringBuffer(); // ??????????????????ID??????
			List<AlarmType> alarmTypeList = new ArrayList<>();
			// ????????????????????????
			AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(eventNetwork_new.getNetworkState(),
					"as_event_network", "network_state");
			if (alarmType != null) {
				alarmTypeList.add(alarmType);
				AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
				statusNames.append(statusNames.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
						.append(alarmType.getStatusName()); // ??????statusName???
				changeTypes.append(changeTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
						.append(alarmType.getAlarmType()); // ??????????????????????????????
				changeTypesDes.append(changeTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
						.append(alarmType.getAlarmTypeName());
				if (AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())) { // ???????????????
					alarmTypes.append(alarmTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
							.append(alarmType.getAlarmType());
					alarmTypesDes.append(alarmTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmType.getAlarmTypeName());
					alarmLevels.append(alarmLevels.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmLevel.getAlarmLevelName());
					alarmDates.append(alarmDates.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(DateUtils.formatDate(eventNetwork_new.getStoreTime(), "yyyy-MM-dd HH:mm:ss"));
					uEventIds.append(uEventIds.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(eventUcId.getId());
				} else {
					promptTypes.append(promptTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
							.append(alarmType.getAlarmType());
					promptTypesDes.append(promptTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmType.getAlarmTypeName());
				}
				// pushMessageTips
				pushMessageTips.append(pushMessageTips.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "").append(alarmType.getAlarmTypeName());
				pushMessageTipsEn.append(pushMessageTipsEn.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "").append(alarmType.getAlarmTypeNameEn());
				// ????????????
				PushParam pushParam = new PushParam(alarmType.getAlarmTypeName(), alarmType.getAlarmTypeNameEn(), alarmType.getAlarmType(),
						alarmType.getPriority(), alarmType.getCode(), alarmType.getIconPath());
				pushParamList.add(pushParam);
				// ??????????????????
				if (alarmLevel_highest == null
						|| alarmLevel_highest.getAlarmLevel().intValue() > alarmLevel.getAlarmLevel().intValue()) {
					alarmLevel_highest = alarmLevel;
				}

			}

			pushEventData.setPushParams(pushParamList);
			// ??????
			PushLevel pushLevel = new PushLevel("", alarmLevel_highest.getAlarmLevelName(), alarmLevel_highest.getAlarmLevelNameEn());
			pushEventData.setPushLevel(pushLevel);
			// pushMessage
			pushEventData.createPushMessage(device, pushMessageTips.toString(), alarmLevel_highest.getAlarmLevelName());
			pushEventData.createPushMessageEn(device, pushMessageTipsEn.toString(), alarmLevel_highest.getAlarmLevelNameEn());
			pushEventData.setPushMessageTips(pushMessageTips.toString());
			pushEventData.setPushMessageTipsEn(pushMessageTipsEn.toString());
			// ????????????
			List<String> pushMethod = new ArrayList<>();
			AlarmWay alarmWaySearch = new AlarmWay();
			alarmWaySearch.setIsOn(1);
			List<AlarmWay> alarmWayList = alarmWayService.findList(alarmWaySearch);
			Boolean sendMsgAlarm = false;   //??????????????????
			Boolean pushAppAlarm = false;   //????????????APP
			List<RegionLeader> regionLeaders = picVedioHandler.getRegionLeaders(device.getRegionCode());

			//??????????????????
			AlarmWaySetting alarmWaySetting = alarmWaySettingService.findByProjectIdAndAlarmLevel(device.getProjectId(), alarmLevel_highest.getAlarmLevel().intValue());
			if(alarmWaySetting != null){
				if(alarmWaySetting.getPopupWindow() == 1){	//??????
					pushMethod.add(Globals.AlarmMethodConsts.POPUP_WINDOW);
				}
				if(alarmWaySetting.getVoice() == 1){	//??????
					pushMethod.add(Globals.AlarmMethodConsts.VOICE);
				}
				if(alarmWaySetting.getNotice() == 1){	//APP??????
					pushAppAlarm = true;
				}
				if(alarmWaySetting.getMessage() == 1){	//??????
					if (regionLeaders.size() > 0) {
						sendMsgAlarm = true;
					}
				}
				if(alarmWaySetting.getSoundLight() == 1){	//????????????

				}
			}

			pushEventData.setPushAlarmMethod(pushMethod);

			// ????????????
			eventNetwork_new.setId(eventNetwork_old.getId());
			BeanUtil.copyProperties(eventNetwork_new, eventNetwork_old,
					CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventNetworkService.update(eventNetwork_old);
			syncCityNetstat(eventNetwork_old);

			AlarmSettings settings = alarmSettingsService.getByTenantCode(device.getProjectId(), device.getTenantCode());
			Boolean nextAlarm = true;
			if(device.getDeviceType().contains("IPC") && !isOnline && settings != null && settings.getTimes() != null && settings.getIntervalTime() != null){
				//?????????????????????
				//??????Id???????????????
				Device parentDevice = deviceService.findParent(deviceId);
				if(parentDevice != null){
					Integer parentDcpowerNo = device.getParentDcpowerNo();
					if(parentDcpowerNo != null){
						//???????????????????????????????????????
						Integer fldValue = eventEswitchService.findFldValue(parentDevice.getId(),"fld"+(parentDcpowerNo>10?parentDcpowerNo:"0"+parentDcpowerNo));
						if(fldValue == 1){
							//??????????????????????????????????????????,?????????????????????,????????????????????????,????????????
							nextAlarm = false;
							CollectDataDto collectDataDto = new CollectDataDto();
							collectDataDto.setStatusNames(statusNames.toString());
							collectDataDto.setAlarmTypes(alarmTypes.toString());
							collectDataDto.setAlarmTypesDes(alarmTypesDes.toString());
							collectDataDto.setAlarmLevels(alarmLevels.toString());
							collectDataDto.setAlarmDates(alarmDates.toString());
							collectDataDto.setuEventIds(uEventIds.toString());
							collectDataDto.setPromptTypes(promptTypes.toString());
							collectDataDto.setChangeTypes(changeTypes.toString());
							collectDataDto.setChangeTypesDes(changeTypesDes.toString());
							collectDataDto.setRegionName(regionName);
							picVedioHandler.resPower(parentDevice,parentDcpowerNo,device,settings,collectDataDto,eventNetwork_new,eventUcId,alarmLevel_highest,pushEventData,alarmTypeList);	//?????????????????????
						}
					}
				}
			}
			if(!nextAlarm){
				return;
			}
			// ????????????
			//pushTroubleStatus(device, pushEventData, alarmTypes);
			pushMsg(device,pushEventData);
			//????????????redis
			try {
				updateDeviceDynamic(device,regionName,alarmTypeList);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ??????????????????????????????
			String removeAlarmTypes = this.updateDeviceEventAlarm(device, statusNames.toString(), alarmTypes.toString(), alarmTypesDes.toString(),
					alarmLevels.toString(), alarmDates.toString(), uEventIds.toString(), promptTypes.toString(), eventNetwork_new.getStoreTime(),
					(eventNetwork_new.getStoreTime().getTime() / 1000));


			// ??????????????????
			this.addDeviceEventHisAlarm(device, eventUcId, alarmLevel_highest, alarmTypes.toString(), changeTypes.toString(), changeTypesDes.toString(),
					(eventNetwork_new.getStoreTime().getTime() / 1000));
			// ????????????????????????
			deviceEventService.generateEventHisRecord(device, eventUcId);
			// ??????????????????
			if (alarmTypes.length() > 0 && alarmLevel_highest.getAlarmLevel() <= Globals.AlarmLevelConsts.level2) { // ??????????????????
				this.updateAlarmStatistics(device, alarmTypes.toString(), eventNetwork_new.getStoreTime());
			}
			// ????????????
			List<FlowRun> flowRuns = this.generateWorkFlow(device, alarmLevel_highest, pushEventData.getPushMessage(), alarmTypes.toString(), uEventIds.toString(), eventNetwork_new.getStoreTime().getTime()/1000, removeAlarmTypes);

			try {
				//??????-????????????
				if(sendMsgAlarm){
//				if (flowRuns.size()>0) {
//					RegionLeader regionLeader = regionLeaders.get(0);
//					Map map = new HashMap();
//					map.put("telephoneNo",regionLeader.getUserMobile());
//					map.put("msg",pushEventData.getPushMessage());
//					map.put("flag","sendMsg");
//					map.put("data",flowRuns);
//					map.put("ip",device.getIp());
//					map.put("projectCode",projectCode);
//					amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE,MQConstants.SEND_DTU_QUEUE,JSON.toJSONString(map,SerializerFeature.WriteDateUseDateFormat));
//				}
					this.sendAlarmSmsToPhone(device, pushEventData);
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			try{
				//??????-APP??????
				if(pushAppAlarm){
//					PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO(pushEventData.getPushMessage(), pushMessageTips.toString(), new HashMap());
					this.pushAlarmToMobileApp(device, pushEventData, changeTypes.toString());
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			try{
				//??????-????????????
				this.sendOrderSmsToPhone(device, flowRuns);
			}catch (Exception e){
				e.printStackTrace();
			}

			try {
				//??????-APP??????
				this.pushOrderToMobileApp(device, flowRuns);
			}catch (Exception e){
				e.printStackTrace();
			}

			String[] arrTypes_ = (changeTypes.toString()).split(Globals.EventAlarmSeparateConsts.COMMA);
			//?????????????????????????????????
			notifyThirdParties(device,arrTypes_,null);

		}
	}

	/**
	 * ??????????????????
	 *
	 * @param alarmTypeStr
	 * @return
	 */
	public Integer levelSort(String alarmTypeStr) {
		List<AlarmType> alarmTypes = alarmTypeService.findAlarmTypes(alarmTypeStr);
		if (alarmTypes.size() > 0) {
			Integer[] arr = new Integer[alarmTypes.size()];
			for (int i = 0; i < alarmTypes.size(); i++) {
				arr[i] = alarmTypes.get(i).getAlarmLevel();
			}
			if (arr.length > 0) {
				for (int i = 0; i < arr.length; i++) {
					int tem = 0;
					// ??????for?????????????????????????????????????????????
					for (int j = i + 1; j < arr.length; j++) {
						if (arr[i] > arr[j]) {
							tem = arr[j];
							arr[j] = arr[i];
							arr[i] = tem;
						}
					}
				}
				return arr[0];
			}
		}
		return null;
	}

	/**
	 * ????????????????????????
	 * @param alarmTypes
	 * @return
	 */
	public Integer getHighestAlarmLevel(String alarmTypes){
		List<AlarmType> alarmTypeList = alarmTypeService.findAlarmTypes(alarmTypes);
		Integer highLevel = null;
		if(alarmTypeList != null && alarmTypeList.size() > 0){
			for(AlarmType alarmType : alarmTypeList){
				if(highLevel == null || highLevel.intValue() > alarmType.getAlarmLevel().intValue()){
					highLevel = alarmType.getAlarmLevel();
				}
			}
		}
		return highLevel;
	}

	/**
	 * ????????????????????????
	 *
	 * @param flowRun
	 */
	public void addFlowRunPrcs(FlowRun flowRun, Device device) {
		FlowRunPrcs flowRunPrcs = new FlowRunPrcs();
		flowRunPrcs.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
		flowRunPrcs.setRunId(flowRun.getId());
		flowRunPrcs.setTenantCode(device.getTenantCode());
		flowRunPrcs.setCreateTime(Integer.parseInt(System.currentTimeMillis() / 1000 + ""));
		flowRunPrcsService.insert(flowRunPrcs);
	}

	/**
	 * ????????????????????????
	 *
	 * @param flowRun
	 */
	public void addFlowRunLog(FlowRun flowRun, Device device, UserAuths userAuths) {
		FlowRunLog flowRunLog = new FlowRunLog();
		flowRunLog.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
		flowRunLog.setUserId(userAuths.getUserId());
		flowRunLog.setRunId(flowRun.getId());
		flowRunLog.setRunStatus(flowRun.getRunStatus());
		flowRunLog.setLogTime(new Date());
		flowRunLog.setContent("??????????????????");
		flowRunLogService.insert(flowRunLog);
	}

	/***
	 * ??????????????????
	 *
	 * @param device
	 * @param eventTableCode
	 * @param bean_new
	 * @param bean_old
	 * @param changeEventMap
	 * @param storeTime
	 * @param recTime
	 */
	public void eventLogicHandler(DeviceEvent deviceEvent,Device device, String eventTableCode, Object bean_new, Object bean_old,
								  Map<String, Object> changeEventMap, Date storeTime, Long recTime) {
		// ?????????????????? ????????????????????????????????????
		EventNetwork eventNetwork = eventNetworkService.findByDeviceId(device.getId());
		if (!eventNetwork.getNetworkState().equals(Globals.DeviceIsOnlineConsts.ON_LINE)) {
			saveBean(eventTableCode, bean_new, bean_old);
			return;
		}
		// ?????????????????????
		String regionName = "";
		// ???????????????
		PushEventData pushEventData = new PushEventData();
		// ??????????????????
//		Region region = regionServiceClient.metaGetRegionById(device.getRegionCode());
		Region region = regionService.findByRegionCode(device.getRegionCode());
		if (region != null) {
			regionName = region.getFullName();
		}
		// ????????????
		PushDevice pushDevice = new PushDevice(device.getId(), device.getDeviceName(), device.getDeviceType(), "",
				storeTime, regionName, device.getIp());
		pushEventData.setPushDevice(pushDevice);
		// ???????????????
		List<PushParam> pushParamList = new ArrayList<PushParam>();

		// ??????????????????
		EventUcId eventUcId = new EventUcId();
		eventUcId.setId(IdGen.snowflakeId());
		eventUcIdService.insert(eventUcId);

		// ????????????
		AlarmLevel alarmLevel_highest = null;
		StringBuffer pushMessageTips = new StringBuffer();
		StringBuffer pushMessageTipsEn = new StringBuffer();
		StringBuffer statusNames = new StringBuffer(); // ???????????????
		StringBuffer alarmTypes = new StringBuffer(); // ??????????????????
		StringBuffer alarmTypesDes = new StringBuffer(); // ????????????????????????????????????
		StringBuffer alarmLevels = new StringBuffer(); // ?????????
		StringBuffer alarmDates = new StringBuffer(); // ???????????????
		StringBuffer promptTypes = new StringBuffer(); // ??????????????????
		StringBuffer promptTypesDes = new StringBuffer(); // ????????????????????????????????????
		StringBuffer changeTypes = new StringBuffer(); // ????????????????????????
		StringBuffer changeTypesDes = new StringBuffer(); // ??????????????????????????????????????????
		StringBuffer uEventIds = new StringBuffer(); // ??????????????????ID??????
		List<Integer> allValue = new ArrayList<Integer>();
		List<AlarmType> alarmTypeList = new ArrayList<>();
		// ??????Map????????????????????????
		for (String key : changeEventMap.keySet()) {
			Integer value = null;
			// ????????????????????????
			if (eventTableCode.equals(EventTableEnum.CURRENT.getTableName()) || eventTableCode.equals(EventTableEnum.VOLTAGE.getTableName())) {

				// 1?????????key??? ???as_event_ecurrent ?????? ??????????????????ID 2???????????????ID?????? ????????????ID 3???????????????ID ??? ?????????
				// ???????????????????????????
				Double values = Double.valueOf(changeEventMap.get(key).toString());
				DeviceModelAlarmThreshold deviceModelAlarmThreshold = deviceModelAlarmThresholdService
						.findStatusValueByCode(device.getDeviceModelId(), values, eventTableCode, key);
				if (deviceModelAlarmThreshold == null) {
					continue;
				}
				value = deviceModelAlarmThreshold.getStatusValue();
				// ??????????????????
				Integer his_value = eventVoltageAndEcurrent(eventTableCode, device, key);
				if (value == his_value) {
					continue;
				}
				allValue.add(value);
			} else {
				value = Integer.parseInt(changeEventMap.get(key).toString());
			}
			// ?????????????????????????????????????????? ???????????????????????????????????????
			AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(value, eventTableCode, key);
			if (alarmType != null) {
				alarmTypeList.add(alarmType);
				// ????????????????????????
				AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
//				// ??????????????????????????? ??????????????????????????? ?????????????????? ???????????????
//				com.aswl.as.ibrs.api.module.DeviceEventAlarm deviceEventAlarm = deviceEventAlarmService
//						.findByDeviceId(device.getId());
//				String alarmTypes_his = deviceEventAlarm.getAlarmTypes() == null ? ""
//						: deviceEventAlarm.getAlarmTypes();
//				String promptTypes_his = deviceEventAlarm.getPromptTypes() == null ? ""
//						: deviceEventAlarm.getPromptTypes();
//				if (alarmTypes_his.contains(alarmType.getAlarmType())) {
//					if (promptTypes_his != null) {
//						String[] arrList = promptTypes_his.split(Globals.EventAlarmSeparateConsts.COMMA);
//						for (int i = 0; i < arrList.length; i++) {
//							if (arrList[i].startsWith(alarmType.getStatusName())) {
//								if (promptTypes_his
//										.indexOf(arrList[i] + Globals.EventAlarmSeparateConsts.COMMA) != -1) { // ?????????????????????????????????????????????
//									promptTypes_his = promptTypes_his
//											.replace(arrList[i] + Globals.EventAlarmSeparateConsts.COMMA, "");
//								} else {
//									promptTypes_his = promptTypes_his.replace(arrList[i], "");
//								}
//							}
//						}
//					}
//					deviceEventAlarm.setPromptTypes(promptTypes_his);
//					deviceEventAlarmService.update(deviceEventAlarm);
//					saveBean(alarmTypeStr, bean_new, bean_old);
//					continue;
//				} else if (promptTypes_his.contains(alarmType.getAlarmType())) {
//					if (alarmTypes_his != null) {
//						String alarmTypesDes_old_his = deviceEventAlarm.getAlarmTypesDes();
//						String alarmLevels_old_his = deviceEventAlarm.getAlarmLevels();
//						String alarmDates_old_his = deviceEventAlarm.getAlarmDates();
//						String uEventIds_old_his = deviceEventAlarm.getUEventId();
//						String[] arrList = alarmTypes_his.split(Globals.EventAlarmSeparateConsts.COMMA);
//						String[] arrList_des = deviceEventAlarm.getAlarmTypesDes()
//								.split(Globals.EventAlarmSeparateConsts.SEMICOLON);
//						String[] arrList_uid = deviceEventAlarm.getUEventId()
//								.split(Globals.EventAlarmSeparateConsts.SEMICOLON);
//						String[] arrList_date = deviceEventAlarm.getAlarmDates()
//								.split(Globals.EventAlarmSeparateConsts.SEMICOLON);
//						String[] arrList_leves = deviceEventAlarm.getAlarmLevels()
//								.split(Globals.EventAlarmSeparateConsts.SEMICOLON);
//
//						for (int i = 0; i < arrList.length; i++) {
//							if (arrList[i].startsWith(alarmType.getStatusName())) {
//								if (arrList.length > 0) {
//									ArrayList<String> at = new ArrayList<String>(Arrays.asList(arrList));
//									at.remove(i);
//									alarmTypes_his = String.join(Globals.EventAlarmSeparateConsts.COMMA, at);
//								}
//								if (arrList_des.length > 0) {
//									ArrayList<String> at = new ArrayList<String>(Arrays.asList(arrList_des));
//									at.remove(i);
//									alarmTypesDes_old_his = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, at);
//								}
//								if (arrList_leves.length > 0) {
//									ArrayList<String> at = new ArrayList<String>(Arrays.asList(arrList_leves));
//									at.remove(i);
//									alarmLevels_old_his = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, at);
//								}
//								if (arrList_date.length > 0) {
//									ArrayList<String> at = new ArrayList<String>(Arrays.asList(arrList_date));
//									at.remove(i);
//									alarmDates_old_his = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, at);
//								}
//								if (arrList_uid.length > 0) {
//									ArrayList<String> at = new ArrayList<String>(Arrays.asList(arrList_uid));
//									at.remove(i);
//									uEventIds_old_his = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, at);
//								}
//							}
//						}
//						if (alarmTypes_his.length() == 0) {
//							deviceEventAlarm.setAlarmLevel(3);
//						}
//						deviceEventAlarm.setAlarmTypes(alarmTypes_his);
//						deviceEventAlarm.setAlarmTypesDes(alarmTypesDes_old_his);
//						deviceEventAlarm.setAlarmDates(alarmDates_old_his);
//						deviceEventAlarm.setAlarmLevels(alarmLevels_old_his);
//						deviceEventAlarm.setUEventId(uEventIds_old_his);
//						deviceEventAlarmService.update(deviceEventAlarm);
//					}
//					saveBean(alarmTypeStr, bean_new, bean_old);
//					continue;
//				}
				statusNames.append(statusNames.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
						.append(alarmType.getStatusName()); // ??????????????????
				changeTypes.append(changeTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
						.append(alarmType.getAlarmType()); // ??????????????????????????????
				changeTypesDes.append(changeTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
						.append(alarmType.getAlarmTypeName()); // ?????????????????????????????????
				if (AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())) { // ???????????????
					alarmTypes.append(alarmTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
							.append(alarmType.getAlarmType());
					alarmTypesDes.append(alarmTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmType.getAlarmTypeName());
					alarmLevels.append(alarmLevels.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmLevel.getAlarmLevelName());
					alarmDates.append(alarmDates.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(DateUtils.formatDate(storeTime, "yyyy-MM-dd HH:mm:ss"));
					uEventIds.append(uEventIds.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(eventUcId.getId());
				} else {
					promptTypes.append(promptTypes.length() > 0 ? Globals.EventAlarmSeparateConsts.COMMA : "")
							.append(alarmType.getAlarmType());
					promptTypesDes.append(promptTypesDes.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "")
							.append(alarmType.getAlarmTypeName());
				}
				// ?????????????????????
				pushMessageTips.append(pushMessageTips.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "").append(alarmType.getAlarmTypeName());
				pushMessageTipsEn.append(pushMessageTipsEn.length() > 0 ? Globals.EventAlarmSeparateConsts.SEMICOLON : "").append(alarmType.getAlarmTypeNameEn());
				// ????????????
				PushParam pushParam = new PushParam(alarmType.getAlarmTypeName(), alarmType.getAlarmTypeNameEn(), alarmType.getAlarmType(),
						alarmType.getPriority(), alarmType.getCode(), alarmType.getIconPath());
				pushParamList.add(pushParam);
				// ??????????????????
				if (alarmLevel_highest == null || alarmLevel_highest.getAlarmLevel().intValue() > alarmLevel.getAlarmLevel().intValue()) {
					alarmLevel_highest = alarmLevel;
				}

				try {
					if (AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())) { // ????????????????????????
						picVedioHandler.alarmCapture(device, alarmType, eventUcId);
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e.getCause());
				}

				//???????????????????????????????????????
				try {
					this.specialAlarmTypeUpdateEventDataExt(device, alarmType, eventTableCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (pushParamList.size() <= 0) {
			saveBean(eventTableCode, bean_new, bean_old);
			return;
		}
		pushEventData.setPushParams(pushParamList);
		// ??????
		PushLevel pushLevel = new PushLevel("", alarmLevel_highest.getAlarmLevelName(), alarmLevel_highest.getAlarmLevelNameEn());
		pushEventData.setPushLevel(pushLevel);
		// ????????????
		String pushMessage = pushEventData.createPushMessage(device, pushMessageTips.toString(), alarmLevel_highest.getAlarmLevelName());
		String pushMessageEn = pushEventData.createPushMessageEn(device, pushMessageTipsEn.toString(), alarmLevel_highest.getAlarmLevelNameEn());
		pushEventData.setPushMessage(pushMessage);
		pushEventData.setPushMessageTips(pushMessageTips.toString());
		pushEventData.setPushMessageTipsEn(pushMessageTipsEn.toString());
		// ????????????
		List<String> pushMethod = new ArrayList<>();
//		AlarmWay alarmWaySearch = new AlarmWay();
//		alarmWaySearch.setIsOn(1);
//		List<AlarmWay> alarmWayList = alarmWayService.findList(alarmWaySearch);
		Boolean sendMsgAlarm = false;   //??????????????????
		Boolean pushAppAlarm = false;   //????????????APP
		List<RegionLeader> regionLeaders = picVedioHandler.getRegionLeaders(device.getRegionCode());

		//??????????????????
		AlarmWaySetting alarmWaySetting = alarmWaySettingService.findByProjectIdAndAlarmLevel(device.getProjectId(), alarmLevel_highest.getAlarmLevel().intValue());
		if(alarmWaySetting != null){
			if(alarmWaySetting.getPopupWindow() == 1){	//??????
				pushMethod.add(Globals.AlarmMethodConsts.POPUP_WINDOW);
			}
			if(alarmWaySetting.getVoice() == 1){	//??????
				pushMethod.add(Globals.AlarmMethodConsts.VOICE);
			}
			if(alarmWaySetting.getNotice() == 1){	//APP??????
				pushAppAlarm = true;
			}
			if(alarmWaySetting.getMessage() == 1){	//??????
				if (regionLeaders.size() > 0) {
					sendMsgAlarm = true;
				}
			}
			if(alarmWaySetting.getSoundLight() == 1){	//????????????

			}
		}
//		for (AlarmWay alarmWay : alarmWayList) {
//			if (alarmWay.getIsOn() == null || alarmWay.getIsOn() == 0 || alarmWay.getLowestAlarmLevel().intValue() < alarmLevel_highest.getAlarmLevel().intValue()) {
//				continue;
//			}
//			if (Globals.AlarmMethodConsts.POPUP_WINDOW.equals(alarmWay.getAlarmMethod())
//					|| Globals.AlarmMethodConsts.VOICE.equals(alarmWay.getAlarmMethod())) { // ???????????????
//				pushMethod.add(alarmWay.getAlarmMethod());
//			}
//			if (Globals.AlarmMethodConsts.SOUND_LIGHT.equals(alarmWay.getAlarmMethod())) { // ??????
//
//			}
//			if (Globals.AlarmMethodConsts.MESSAGE.equals(alarmWay.getAlarmMethod())) { // ??????
////				if (regionLeaders.size() > 0) {
////					RegionLeader regionLeader = regionLeaders.get(0);
////					MessageCatSendMsg messageCatSendMsg = new MessageCatSendMsg();
////					messageCatSendMsg.setTelphoneNo("+86"+regionLeader.getUserMobile());
////					messageCatSendMsg.setMsg(pushEventData.getPushMessage());
////					messageCatSendMsg.setFlag("sendMsg");
////						amqpTemplate.convertAndSend("messageExchange","sendMessageQueue",JSON.toJSONString(messageCatSendMsg));
////				}
//
//				if (regionLeaders.size() > 0) {
//					sendMsg = true;
//				}
//			}
//			if (Globals.AlarmMethodConsts.NOTICE.equals(alarmWay.getAlarmMethod())) { // ??????
//				if (configService.findIsCloud()) {
//					PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO(pushMessage, pushMessageTips.toString(), new HashMap());
//					pushAlarmToMobileApp(device, pushMobilePhoneDataVO, changeTypes.toString());
//				}else {
//					if (regionLeaders.size() > 0) {
//						pushApp = true;
//					}
//				}
//			}
//		}
		pushEventData.setPushAlarmMethod(pushMethod);

		// ????????????
		//pushTroubleStatus(device, pushEventData, changeTypes);
		pushMsg(device,pushEventData);
		//??????????????????redis
		try {
			updateDeviceDynamic(device,regionName,alarmTypeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ??????????????????????????????
		String removeAlarmTypes = this.updateDeviceEventAlarm(device,  statusNames.toString(), alarmTypes.toString(), alarmTypesDes.toString(),
				alarmLevels.toString(), alarmDates.toString(), uEventIds.toString(), promptTypes.toString(), storeTime, recTime);

		// ????????????????????????
		saveBean(eventTableCode, bean_new, bean_old);
		// ??????????????????
		this.addDeviceEventHisAlarm(device, eventUcId, alarmLevel_highest, alarmTypes.toString(), changeTypes.toString(), changeTypesDes.toString(), recTime);
		// ????????????????????????
		deviceEventService.generateEventHisRecord(device, eventUcId);
		// ??????????????????
		if (alarmTypes.length() > 0 && alarmLevel_highest.getAlarmLevel() <= Globals.AlarmLevelConsts.level2) { // ??????????????????
			this.updateAlarmStatistics(device, alarmTypes.toString(), storeTime);
		}
		// ????????????
		List<FlowRun> flowRuns = this.generateWorkFlow(device, alarmLevel_highest, pushEventData.getPushMessage(), alarmTypes.toString(), uEventIds.toString(), recTime, removeAlarmTypes);

		try{
			//??????-????????????
			if(sendMsgAlarm){
//            if (flowRuns.size()>0) {
//                RegionLeader regionLeader = regionLeaders.get(0);
//				DtuBodyBean dtuBodyBean = new DtuBodyBean();
//				dtuBodyBean.setFlag(DtuPushConstant.DtuFlagEnum.SEND_SMS_ALARM.getFlag());
//				dtuBodyBean.setMsg(pushEventData.getPushMessage());
//				dtuBodyBean.setTelephoneNo(regionLeader.getUserMobile());
//				dtuBodyBean.setIp(device.getIp());
//				dtuBodyBean.setProjectCode(projectCode);
//                amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE,MQConstants.SEND_DTU_QUEUE,JSON.toJSONString(dtuBodyBean,SerializerFeature.WriteDateUseDateFormat));
//            }
				this.sendAlarmSmsToPhone(device, pushEventData);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		try{
			//??????-APP??????
			if(pushAppAlarm){
//				PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO(pushMessage, pushMessageTips.toString(), new HashMap());
				this.pushAlarmToMobileApp(device, pushEventData, changeTypes.toString());
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		try{
			//??????-????????????
			this.sendOrderSmsToPhone(device, flowRuns);
		}catch (Exception e){
			e.printStackTrace();
		}

		try {
			//??????-APP??????
			this.pushOrderToMobileApp(device, flowRuns);
		}catch (Exception e){
			e.printStackTrace();
		}

		String[] arrTypes_ = (alarmTypes.toString()).split(Globals.EventAlarmSeparateConsts.COMMA);
		String[] arrPrompt_ = (promptTypes.toString()).split(Globals.EventAlarmSeparateConsts.SEMICOLON);
		for (String val : arrTypes_) {
			// ????????????
			if (val.equals("DoorExtAlarm1") || val.equals("DoorExtAlarm2")) {
				picVedioHandler.openCameraRecord(device, val);
			}
		}
		for (String val : arrPrompt_) {
			// ????????????
			if (val.equals("DoorExtPrompt0")) {
				picVedioHandler.openCameraRecord(device, val);
			}
		}

		//?????????????????????????????????
		notifyThirdParties(device,(changeTypes.toString()).split(Globals.EventAlarmSeparateConsts.COMMA),allValue);

	}

	/**
	 * ????????????????????????????????????
	 * @param device
	 * @param statusNames
	 * @param alarmTypes
	 * @param alarmTypesDes
	 * @param alarmLevels
	 * @param alarmDates
	 * @param uEventIds
	 * @param promptTypes
	 * @param storeTime
	 * @param recTime
	 * @return
	 */
	public String updateDeviceEventAlarm(Device device, String statusNames, String alarmTypes, String alarmTypesDes, String alarmLevels,
										 String alarmDates, String uEventIds, String promptTypes, Date storeTime, Long recTime){
		String removeAlarmTypes = "";	//????????????????????????
		com.aswl.as.ibrs.api.module.DeviceEventAlarm deviceEventAlarm_ibrs = deviceEventAlarmService.findByDeviceId(device.getId());
		if (deviceEventAlarm_ibrs == null) { // ???????????????????????? ?????????????????????
			deviceEventAlarm_ibrs = new com.aswl.as.ibrs.api.module.DeviceEventAlarm();
			deviceEventAlarm_ibrs.setDeviceId(device.getId());
			deviceEventAlarm_ibrs.setAlarmTypes("");
			deviceEventAlarm_ibrs.setAlarmTypesDes("");
			deviceEventAlarm_ibrs.setPromptTypes("");
			deviceEventAlarm_ibrs.setCommonValue_meta("", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			deviceEventAlarm_ibrs.setStoreTime(storeTime);
		}
		// deviceEventAlarm_ibrs.setUEventId(eventUcId.getId());
		deviceEventAlarm_ibrs.setRecTime(recTime.intValue());
		// ??????---???????????????????????? start
		String alarmTypes_old = deviceEventAlarm_ibrs.getAlarmTypes(); // ????????????
		String alarmTypesDes_old = deviceEventAlarm_ibrs.getAlarmTypesDes();
		String promptTypes_old = deviceEventAlarm_ibrs.getPromptTypes();
		String alarmLevels_old = deviceEventAlarm_ibrs.getAlarmLevels();
		String alarmDates_old = deviceEventAlarm_ibrs.getAlarmDates();
		String uEventIds_old = deviceEventAlarm_ibrs.getUEventId();

		List<String> alarmTypesList_old = alarmTypes_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(alarmTypes_old.split(Globals.EventAlarmSeparateConsts.COMMA)));
		List<String> alarmTypesDesList_old = alarmTypesDes_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(alarmTypesDes_old.split(Globals.EventAlarmSeparateConsts.SEMICOLON)));
		List<String> alarmLevelsList_old = alarmLevels_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(alarmLevels_old.split(Globals.EventAlarmSeparateConsts.SEMICOLON)));
		List<String> alarmDatesList_old = alarmDates_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(alarmDates_old.split(Globals.EventAlarmSeparateConsts.SEMICOLON)));
		List<String> uEventIdsList_old = uEventIds_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(uEventIds_old.split(Globals.EventAlarmSeparateConsts.SEMICOLON)));
		List<String> promptTypesList_old = promptTypes_old == null ? new ArrayList<>()
				: new ArrayList<>(Arrays.asList(promptTypes_old.split(Globals.EventAlarmSeparateConsts.COMMA)));

		for (String statusName : statusNames.split(Globals.EventAlarmSeparateConsts.SEMICOLON)) {
			if (StringUtils.isEmpty(statusName)) {
				continue;
			}
			// ????????????????????????????????????
			for (int i = 0; i < alarmTypesList_old.size(); i++) {
				String alarmType_old = alarmTypesList_old.get(i);
				if (StringUtils.isEmpty(alarmType_old)) {
					continue;
				}
				if (alarmType_old.startsWith(statusName)) {
					alarmTypesList_old.remove(i);
					if(alarmTypesDesList_old.size() > 0)
						alarmTypesDesList_old.remove(i);
					if(alarmLevelsList_old.size() > 0)
						alarmLevelsList_old.remove(i);
					if(alarmDatesList_old.size() > 0)
						alarmDatesList_old.remove(i);
					if(uEventIdsList_old.size() > 0)
						uEventIdsList_old.remove(i);
					// ??????????????????????????????
					removeAlarmTypes = (removeAlarmTypes == null || "".equals(removeAlarmTypes)) ? alarmType_old
							: removeAlarmTypes + Globals.EventAlarmSeparateConsts.COMMA + alarmType_old;
					break;
				}
			}
			// ????????????????????????????????????
			for (int i = 0; i < promptTypesList_old.size(); i++) {
				String promptType_old = promptTypesList_old.get(i);
				if (StringUtils.isEmpty(promptType_old)) {
					continue;
				}
				if (promptType_old.startsWith(statusName)) {
					promptTypesList_old.remove(i);
					break;
				}
			}
		}
		alarmTypes_old = String.join(Globals.EventAlarmSeparateConsts.COMMA, alarmTypesList_old);
		alarmTypesDes_old = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, alarmTypesDesList_old);
		alarmLevels_old = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, alarmLevelsList_old);
		alarmDates_old = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, alarmDatesList_old);
		uEventIds_old = String.join(Globals.EventAlarmSeparateConsts.SEMICOLON, uEventIdsList_old);
		promptTypes_old = String.join(Globals.EventAlarmSeparateConsts.COMMA, promptTypesList_old);
		// ??????---???????????????????????? end

		// ???????????????????????? start
		if (StringUtils.isEmpty(alarmTypes_old)) {
			alarmTypes_old = alarmTypes;
		} else if (!StringUtils.isEmpty(alarmTypes)) {
			alarmTypes_old = alarmTypes + Globals.EventAlarmSeparateConsts.COMMA + alarmTypes_old;
		}
		if (StringUtils.isEmpty(alarmTypesDes_old)) {
			alarmTypesDes_old = alarmTypesDes;
		} else if (!StringUtils.isEmpty(alarmTypesDes)) {
			alarmTypesDes_old = alarmTypesDes + Globals.EventAlarmSeparateConsts.SEMICOLON
					+ alarmTypesDes_old;
		}
		if (StringUtils.isEmpty(alarmLevels_old)) {
			alarmLevels_old = alarmLevels;
		} else if (!StringUtils.isEmpty(alarmLevels)) {
			alarmLevels_old = alarmLevels + Globals.EventAlarmSeparateConsts.SEMICOLON + alarmLevels_old;
		}
		if (StringUtils.isEmpty(alarmDates_old)) {
			alarmDates_old = alarmDates;
		} else if (!StringUtils.isEmpty(alarmDates)) {
			alarmDates_old = alarmDates + Globals.EventAlarmSeparateConsts.SEMICOLON + alarmDates_old;
		}
		if (StringUtils.isEmpty(uEventIds_old)) {
			uEventIds_old = uEventIds;
		} else if (!StringUtils.isEmpty(uEventIds)) {
			uEventIds_old = uEventIds + Globals.EventAlarmSeparateConsts.SEMICOLON + uEventIds_old;
		}
		// ????????????????????????
		if (StringUtils.isEmpty(promptTypes_old)) {
			promptTypes_old = promptTypes;
		} else if (!StringUtils.isEmpty(promptTypes)) {
			promptTypes_old = promptTypes + Globals.EventAlarmSeparateConsts.COMMA + promptTypes_old;
		}
		// ???????????????????????? end

		// ??????????????????
		Integer levels = getHighestAlarmLevel(alarmTypes_old); // ????????????????????????
		if (levels != null) {
			deviceEventAlarm_ibrs.setAlarmLevel(levels);
		} else {
			deviceEventAlarm_ibrs.setAlarmLevel(AlarmLevelType.NORMAL.getType());
		}

		deviceEventAlarm_ibrs.setRegionNo(device.getRegionCode());
		deviceEventAlarm_ibrs.setAlarmTypes(alarmTypes_old.endsWith(Globals.EventAlarmSeparateConsts.COMMA)
				? alarmTypes_old.substring(0, alarmTypes_old.length() - 1)
				: alarmTypes_old);
		deviceEventAlarm_ibrs.setAlarmTypesDes(alarmTypesDes_old.endsWith(Globals.EventAlarmSeparateConsts.SEMICOLON)
				? alarmTypesDes_old.substring(0, alarmTypesDes_old.length() - 1)
				: alarmTypesDes_old);
		deviceEventAlarm_ibrs.setPromptTypes(promptTypes_old.endsWith(Globals.EventAlarmSeparateConsts.SEMICOLON)
				? promptTypes_old.substring(0, promptTypes_old.length() - 1)
				: promptTypes_old);
		deviceEventAlarm_ibrs.setAlarmDates(alarmDates_old.endsWith(Globals.EventAlarmSeparateConsts.SEMICOLON)
				? alarmDates_old.substring(0, alarmDates_old.length() - 1)
				: alarmDates_old);
		deviceEventAlarm_ibrs.setAlarmLevels(alarmLevels_old.endsWith(Globals.EventAlarmSeparateConsts.SEMICOLON)
				? alarmLevels_old.substring(0, alarmLevels_old.length() - 1)
				: alarmLevels_old);
		deviceEventAlarm_ibrs.setUEventId(uEventIds_old.endsWith(Globals.EventAlarmSeparateConsts.SEMICOLON)
				? uEventIds_old.substring(0, uEventIds_old.length() - 1)
				: uEventIds_old);
		deviceEventAlarm_ibrs.setRecTime(recTime.intValue());
		deviceEventAlarm_ibrs.setIsAlarm(StringUtils.isEmpty(alarmTypes_old) ? 0 : 1);
		deviceEventAlarmService.save(deviceEventAlarm_ibrs);
		//????????????????????????????????????

		if(cityPlatformUtil.isCityPlatform()){
				CityPlatformDto cityPlatformDto = new CityPlatformDto();
				cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
				cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
				cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
				cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
				cityPlatformDto.setData(deviceEventAlarm_ibrs);
			byte[] bytes = new byte[0];
			try {
				bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_DEVICEEVENT_QUEUE, bytes);
		}
		return removeAlarmTypes;
	}

	/**
	 * ????????????????????????
	 * @param device
	 * @param eventUcId
	 * @param alarmLevelHighest
	 * @param alarmTypes
	 * @param changeTypes
	 * @param changeTypesDes
	 * @param recTime
	 */
	public void addDeviceEventHisAlarm(Device device, EventUcId eventUcId, AlarmLevel alarmLevelHighest, String alarmTypes, String changeTypes, String changeTypesDes,
									   Long recTime){
		DeviceEventHisAlarmVO deviceEventHisAlarmVO = new DeviceEventHisAlarmVO();
		deviceEventHisAlarmVO.setDeviceId(device.getId());
		deviceEventHisAlarmVO.setUEventId(eventUcId.getId());
		deviceEventHisAlarmVO.setRegionNo(device.getRegionCode());
		deviceEventHisAlarmVO.setAlarmLevel(alarmLevelHighest.getAlarmLevel());
		deviceEventHisAlarmVO.setAlarmTypes(changeTypes); // ??????????????????????????????
		deviceEventHisAlarmVO.setAlarmTypesDes(changeTypesDes);
		deviceEventHisAlarmVO.setIsAlarm(StringUtils.isEmpty(alarmTypes) ? 0 : 1);
		deviceEventHisAlarmVO.setRecTime(recTime.intValue());
		deviceEventHisAlarmVO.setStoreTime(new Date());
		deviceEventHisAlarmVO.setProjectId(device.getProjectId());
		deviceEventHisAlarmVO.setTenantCode(device.getTenantCode());
		deviceEventHisAlarmVO.setApplicationCode(device.getApplicationCode());
		deviceEventHisAlarmService.insert(deviceEventHisAlarmVO);
	}

	/**
	 * ????????????????????????
	 * @param device
	 * @param alarmTypes
	 * @param storeTime
	 */
	public void updateAlarmStatistics(Device device, String alarmTypes, Date storeTime){
		String[] alarmTypeArr = alarmTypes.split(Globals.EventAlarmSeparateConsts.COMMA);
		for (String alarmType : alarmTypeArr) {
			AlarmStatistics alarmStatistics = alarmStatisticsService.findByDeviceIdWithDate(device.getId(), alarmType, storeTime);
			AlarmType type = alarmTypeService.findAlarmType(alarmType);
			if (alarmStatistics == null) {
				alarmStatistics = new AlarmStatistics();
				alarmStatistics.setDeviceId(device.getId());
				alarmStatistics.setDeviceModelId(device.getDeviceModelId());
				alarmStatistics.setAlarmType(alarmType);
				alarmStatistics.setRegionNo(device.getRegionCode());
				alarmStatistics.setCreateDate(storeTime);
				DeviceKind deviceKind = deviceKindService.findByDeviceId(device.getId());
				alarmStatistics.setType(Integer.parseInt(deviceKind.getType()));
				alarmStatistics.setProjectId(device.getProjectId());
				if (type.getAlarmLevel() == (Globals.AlarmLevelConsts.level2)) { //??????
					alarmStatistics.setAlarmNum(1);
					alarmStatistics.setFaultNum(0);
				} else if (type.getAlarmLevel() == (Globals.AlarmLevelConsts.level1)) {  //??????
					alarmStatistics.setAlarmNum(1);
					alarmStatistics.setFaultNum(1);
				}
			} else {
				if (type.getAlarmLevel() == (Globals.AlarmLevelConsts.level2)) { //??????
					alarmStatistics.setAlarmNum(alarmStatistics.getAlarmNum() + 1);
				} else if (type.getAlarmLevel() == (Globals.AlarmLevelConsts.level1)) {  //??????
					alarmStatistics.setFaultNum(alarmStatistics.getFaultNum() + 1);
					alarmStatistics.setAlarmNum(alarmStatistics.getAlarmNum() + 1);
				}
			}
			alarmStatisticsService.save(alarmStatistics);

			//????????????????????????????????????
			if(cityPlatformUtil.isCityPlatform()){
				CityPlatformDto cityPlatformDto = new CityPlatformDto();
				cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
				cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
				cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
				cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
				cityPlatformDto.setData(alarmStatistics);
				byte[] bytes = new byte[0];
				try {
					bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
				} catch (Exception e) {
					e.printStackTrace();
				}
				cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_ALARM_COUNT_QUEUE, bytes);
			}
		}
	}

	/**
	 * ????????????
	 * @param device
	 * @param alarmLevelHighest
	 * @param runName
	 * @param alarmTypes
	 * @param uEventIds
	 * @param alarmTime
	 * @param removeAlarmType
	 */
	public List<FlowRun> generateWorkFlow(Device device, AlarmLevel alarmLevelHighest, String runName, String alarmTypes, String uEventIds, Long alarmTime, String removeAlarmType){
		// ?????????????????????ID
		UserAuths userAuths = new UserAuths();
		List<FlowRun> flowRuns = new ArrayList<>();
		userAuths.setIdentifier("admin");
		List<UserAuths> userAuths_ = userAuthsService.findUserList(userAuths);
		String[] arrTypes = (alarmTypes).split(Globals.EventAlarmSeparateConsts.COMMA);
		String[] uEventId = (uEventIds).split(Globals.EventAlarmSeparateConsts.SEMICOLON);
		if (arrTypes.length > 0) {
			//??????????????????
			AlarmOrderHandleVo alarmOrderHandleVo = alarmOrderHandleService.findByProjectId(device.getProjectId());
			if(SendOrderSettingEnum.SEND_ORDER_TYPE_AUTO.getValue() == alarmOrderHandleVo.getSendOrderType()){  //?????????????????????
				for (int j = 0; j < arrTypes.length; j++) {
					if(StringUtils.isEmpty(arrTypes[j])){
						continue;
					}
					AlarmType alarmType = alarmTypeService.findAlarmType(arrTypes[j]);
					if(alarmType != null && alarmType.getAlarmLevel() <= Globals.AlarmLevelConsts.level1){	//??????????????????????????????
						FlowRun flowRun = new FlowRun();
						flowRun.setRunNo(IdGen.getFlowRunNum(arrTypes[j]));
						flowRun.setRunName(runName);
						flowRun.setBeginDeviceId(device.getId());
						flowRun.setBeginUEventId(uEventId[j]);
						flowRun.setBeginTime((int) (System.currentTimeMillis() / 1000));
						flowRun.setAlarmTime(DateUtils.getDate(alarmTime * 1000));
						flowRun.setRunStatus(FlowRunStatus.INIT.getValue());
						flowRun.setAlarmLevel(alarmType.getAlarmLevel());
						flowRun.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
						flowRun.setAlarmType(arrTypes[j]);
						flowRun.setType(alarmOrderHandleVo.getOrderHandleType());
						flowRun.setPriority("0");
						// ?????????????????????ID
						String maintainUserId = picVedioHandler.getMaintainUserId(device.getRegionCode());
						if (maintainUserId == null) {
							continue;
						}
						flowRun.setMaintainUserId(maintainUserId);
						flowRun.setBeginUserId(userAuths_.get(0).getUserId());
						int q = flowRunService.insert(flowRun);
						if (q > 0) {
							flowRuns.add(flowRun);
							addFlowRunPrcs(flowRun, device);
							addFlowRunLog(flowRun, device, userAuths_.get(0));	//????????????????????????
						}
					}
				}
			}
		}
		if (removeAlarmType != null) {
			String[] arr = removeAlarmType.split(Globals.EventAlarmSeparateConsts.COMMA);
			if (arr.length > 0) {
				for (String alarmType : arr) {
					List<FlowRun> list = flowRunService.findListByDeviceIdAndAlarmType(device.getId(), alarmType);
					if (list.size() > 0) {
						for (FlowRun flowRun : list) {
							if(SendOrderSettingEnum.ORDER_HANDLE_TYPE_AUTO.getValue() == flowRun.getType()){	//???????????????????????????
								flowRun.setRunStatus(FlowRunStatus.MAINTENANCE_COMPLETED.getValue());	//??????????????????????????????????????????
								flowRun.setEndTime((int) (System.currentTimeMillis() / 1000));
								int i = flowRunService.update(flowRun);
								if (i > 0) {
									FlowRunLog flowRunLog = new FlowRunLog();
									flowRunLog.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
									flowRunLog.setUserId(userAuths_.get(0).getUserId());
									flowRunLog.setRunId(flowRun.getId());
									flowRunLog.setRunStatus(1);
									flowRunLog.setLogTime(new Date());
									flowRunLog.setContent("??????????????????");
									flowRunLogService.insert(flowRunLog);
								}
							}
						}
					}
				}
			}
		}
		return flowRuns;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param alarmTypeStr
	 * @param bean_new
	 * @param bean_old
	 */
	public void saveBean(String alarmTypeStr, Object bean_new, Object bean_old) {
		switch (alarmTypeStr) {
			case "as_event_alarm":
				EventAlarm eventAlarm_new = (EventAlarm) bean_new;
				EventAlarm eventAlarm_old = (EventAlarm) bean_old;
				eventAlarm_new.setId(eventAlarm_old.getId());
				BeanUtil.copyProperties(eventAlarm_new, eventAlarm_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				deviceEventService.update(eventAlarm_old);
				break;
			case "as_event_base":
				EventBase eventBase_new = (EventBase) bean_new;
				EventBase eventBase_old = (EventBase) bean_old;
				eventBase_new.setId(eventBase_old.getId());
				BeanUtil.copyProperties(eventBase_new, eventBase_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				eventBaseService.update(eventBase_old);
				break;
			case "as_event_ecurrent":
				EventEcurrent eventEcurrent_new = (EventEcurrent) bean_new;
				EventEcurrent eventEcurrent_old = (EventEcurrent) bean_old;
				eventEcurrent_new.setId(eventEcurrent_old.getId());
				BeanUtil.copyProperties(eventEcurrent_new, eventEcurrent_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				eventEcurrentService.update(eventEcurrent_old);
				break;
			case "as_event_eoutlet":
				EventEoutlet eventEoutlet_new = (EventEoutlet) bean_new;
				EventEoutlet eventEoutlet_old = (EventEoutlet) bean_old;
				eventEoutlet_new.setId(eventEoutlet_old.getId());
				List<String> ignoreField = deviceEventJudge.ignoreField(eventEoutlet_new, eventEoutlet_old);
				BeanUtil.copyProperties(eventEoutlet_new, eventEoutlet_old,
						CopyOptions.create().setIgnoreProperties(ignoreField.toArray(new String[ignoreField.size()])).setIgnoreNullValue(true).setIgnoreError(true));
				eventEoutletService.update(eventEoutlet_old);
				break;
			case "as_event_eswitch":
				EventEswitch eventEswitch_new = (EventEswitch) bean_new;
				EventEswitch eventEswitch_old = (EventEswitch) bean_old;
				eventEswitch_new.setId(eventEswitch_old.getId());
				BeanUtil.copyProperties(eventEswitch_new, eventEswitch_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				eventEswitchService.update(eventEswitch_old);
				break;
			case "as_event_evoltage":
				EventEvoltage eventEvoltage_new = (EventEvoltage) bean_new;
				EventEvoltage eventEvoltage_old = (EventEvoltage) bean_old;
				eventEvoltage_new.setId(eventEvoltage_old.getId());
				BeanUtil.copyProperties(eventEvoltage_new, eventEvoltage_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				eventEvoltageService.update(eventEvoltage_old);
				break;
			case "as_event_sfp":
				EventSfp eventSfp_new = (EventSfp) bean_new;
				EventSfp eventSfp_old = (EventSfp) bean_old;
				eventSfp_new.setId(eventSfp_old.getId());
				List<String> ignoreFields = deviceEventJudge.ignoreField(eventSfp_new, eventSfp_old);
				BeanUtil.copyProperties(eventSfp_new, eventSfp_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true).setIgnoreProperties(ignoreFields.toArray(new String[ignoreFields.size()])));
				eventSfpService.update(eventSfp_old);
				break;
			case "as_event_network":
				EventNetwork eventNetwork_new = (EventNetwork) bean_new;
				EventNetwork eventNetwork_old = (EventNetwork) bean_old;
				eventNetwork_new.setId(eventNetwork_old.getId());
				BeanUtil.copyProperties(eventNetwork_new, eventNetwork_old,
						CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
				eventNetworkService.update(eventNetwork_old);
				break;
			default:
				break;
		}
	}



	// ???????????? ??????????????????
	public Integer eventVoltageAndEcurrent(String eventTableCode, Device device, String key) {
		Integer value = null;
		BaseEntity eventEntity = null;
		if (eventTableCode.equals(EventTableEnum.CURRENT.getTableName())) {
			eventEntity = eventEcurrentService.findByDeviceId(device.getId());
		} else if (eventTableCode.equals(EventTableEnum.VOLTAGE.getTableName())) {
			eventEntity = eventEvoltageService.findByDeviceId(device.getId());
		}
		if(eventEntity != null){
			Object object = getClassValue(eventEntity, key);
			// 1?????????key??? ???as_event_ecurrent ?????? ??????????????????ID 2???????????????ID?????? ????????????ID 3???????????????ID ??? ?????????
			// ???????????????????????????
			if (object != null) {
				Double values = Double.valueOf(object.toString());
				DeviceModelAlarmThreshold deviceModelAlarmThreshold = deviceModelAlarmThresholdService
						.findStatusValueByCode(device.getDeviceModelId(), values, eventTableCode, key);
				if (deviceModelAlarmThreshold == null) {
					return null;
				}
				value = deviceModelAlarmThreshold.getStatusValue();
			}
		}
		return value;
	}

	/**
	 * ????????????????????????
	 *
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Object getClassValue(Object obj, String fieldName) {
		if (obj == null) {
			return null;
		}
		try {
			Class<? extends Object> beanClass = obj.getClass();
			Method[] ms = beanClass.getMethods();
			for (int i = 0; i < ms.length; i++) {
				// ???get????????????
				if (!ms[i].getName().startsWith("get")) {
					continue;
				}
				Object objValue = null;
				try {
					objValue = ms[i].invoke(obj, new Object[] {});
				} catch (Exception e) {
					continue;
				}
				if (objValue == null) {
					continue;
				}
				if (ms[i].getName().toUpperCase().equals(fieldName.toUpperCase())
						|| ms[i].getName().substring(3).toUpperCase().equals(fieldName.toUpperCase())) {
					return objValue;
				} else if (fieldName.toUpperCase().equals("SID") && (ms[i].getName().toUpperCase().equals("ID")
						|| ms[i].getName().substring(3).toUpperCase().equals("ID"))) {
					return objValue;
				}
			}
		} catch (Exception e) {
			// logger.info("??????????????????" + e.toString());
		}
		return null;
	}

	// ????????????????????????????????????
	public void pushTroubleStatus(Device device, PushEventData pushEventData, StringBuffer alarmTypes) {
		List<String> userNames = new ArrayList<String>();
		UserAuths eAuths = new UserAuths();
//		List<UserAuths> userAuths = userServiceClient.findUserList(eAuths);
		List<UserAuths> userAuths = userAuthsService.findUserList(eAuths);
		if (userAuths != null && userAuths.size() > 0) {
			for (UserAuths userAuth : userAuths) {
				/*List<AlarmTypeDeviceFavorite> lists = regionServiceClient
						.getAlarmTypeDeviceFavoriteByUserId(userAuth.getUserId());*/
				List<AlarmTypeDeviceFavorite> lists =
						alarmTypeDeviceFavoriteService.getAlarmTypeDeviceFavoriteByUserId(userAuth.getUserId());
				if (lists != null && lists.size() > 0) {
					for (AlarmTypeDeviceFavorite alarmTypeDeviceFavorite : lists) {
						if (alarmTypeDeviceFavorite.getDeviceId().equals(device.getId())) {
							String[] arrs = alarmTypes.toString().split(",");
							if (arrs.length > 0) {
								for (String arr : arrs) {
									if (alarmTypeDeviceFavorite.getAlarmType().contains(arr)) {
										// ????????????????????????????????????????????? ??????
										userNames.add(userAuth.getIdentifier());
										break;
									}
								}
							}
						}

					}
				} else {
					userNames.add(userAuth.getIdentifier());
				}
			}
		}
		// ??????
		deviceEventPushExecutor.pushTroubleStatus(userNames, pushEventData);
	}

	/**
	 * ????????????
	 * @param device ??????
	 * @param pushEventData ???????????????
	 */
	public void pushMsg(Device device, PushEventData pushEventData) {
		//??????Id
		String deviceId = device.getId();
		//???????????????????????????????????????
		List<PushParam> allPushParams = pushEventData.getPushParams();
		//???????????????
//		List<UserAuths> userAuths = userServiceClient.findUserList(new UserAuths());
		List<UserAuths> userAuths = userAuthsService.findUserList(new UserAuths());
		if(userAuths != null && userAuths.size() > 0){
			for (UserAuths userAuth : userAuths) {
				List<PushParam> userPushParams = new ArrayList<>();
				//????????????id????????????????????????????????????
//				AlarmTypeDeviceFavorite favorite = regionServiceClient.getFavorite(userAuth.getUserId(),deviceId);
				AlarmTypeDeviceFavorite favorite = alarmTypeDeviceFavoriteService.getFavorite(userAuth.getUserId(),deviceId);
				if(favorite != null){ //???????????????????????????
					List<String> alarmTypes = Arrays.asList(favorite.getAlarmType().split(","));
					for (PushParam allPushParam : allPushParams) {
						String alias = allPushParam.getAlias();
//						for (String alarmType : alarmTypes) {
						if (alarmTypes.contains(alias)) {
							userPushParams.add(allPushParam);
//							break;
						}
//							if(alias.equals(alarmType)){
//								userPushParams.add(allPushParam);
//								break;
//							}
//						}
					}
					if(userPushParams.size() > 0){
						pushEventData.setPushParams(userPushParams);
						deviceEventPushExecutor.pushMsg(userAuth.getIdentifier(), pushEventData);
					}
				}
			}
		}
	}

	public void notifyThirdParties(Device device,String[] arrTypes,List<Integer> allValue) {
		List<SuperPlatform> superPlatforms = superPlatformService.findByRegionCode(device.getRegionCode());
		for (SuperPlatform superPlatform : superPlatforms) {
			IfaceCallbackDeviceStatusVO ifaceCallbackDeviceStatusVO = new IfaceCallbackDeviceStatusVO();
			ChangeDeviceVo changeDeviceVo = new ChangeDeviceVo();
			List<ChangeStatusVo> listStatusVos =  new ArrayList<ChangeStatusVo>();
			for (int i = 0; i < arrTypes.length; i++) {
				ChangeStatusVo changeStatusVo = new ChangeStatusVo();
				List<AlarmType> list = alarmTypeService.findAlarmTypes(arrTypes[i]);
				if(list.size() >0) {
					EventUcMetadata eventUcMetadata = eventUcMetadataService.findByStatusName(list.get(0).getStatusName());
					changeStatusVo.setCode(list.get(0).getCode());
					changeStatusVo.setCodeCN(list.get(0).getCodeCn());
					if(eventUcMetadata != null) {
						changeStatusVo.setName(eventUcMetadata.getFldName());//
						changeStatusVo.setUnit(eventUcMetadata.getFldUnit());//??????
					}
					if(allValue != null && allValue.size() > i) {
						changeStatusVo.setValue(new Double(allValue.get(i)));
					}
				}
				listStatusVos.add(changeStatusVo);
			}
			changeDeviceVo.setDeviceCode(device.getDeviceCode());//??????Code
			changeDeviceVo.setDeviceName(device.getDeviceName());//????????????
			changeDeviceVo.setChangeStatuses(listStatusVos);
			ifaceCallbackDeviceStatusVO.setCallbackUrl(superPlatform.getCallbackUrl());//?????????????????????
			ifaceCallbackDeviceStatusVO.setChangeDevice(changeDeviceVo);

			amqpTemplate.convertAndSend("ifaceDeviceStatusExchange","ifaceCallbackQueue",JSONUtil.toJsonStr(ifaceCallbackDeviceStatusVO));
		}
	}

	/**
	 * ????????????
	 * @param deviceEventPatrol
	 * @throws ParseException
	 */
	public void eventPatrolProcess(DeviceEventPatrol deviceEventPatrol) throws ParseException {
		// ??????????????????
		if (deviceEventPatrol == null || deviceEventPatrol.getDevice() == null) {
			log.info("deviceEventPatrol ??????????????????");
			return;
		}
		String deviceId = deviceEventPatrol.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventPatrol.getDevice().getId());
			return;
		}
		// ??????EventPatrol
		EventPatrol eventPatrol_new = deviceEventBuilder.buildEventEPatrol(deviceEventPatrol);
		// ??????????????????EventPatrol
		EventPatrol eventPatrol_old = eventPatrolService.findByDeviceId(deviceId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		//long recTime = eventPatrol_new.getRecTime();
		//String tableTime = sdf.format(new Date(recTime));
		String tableTime = sdf.format(eventPatrol_new.getAuthTime());
		String hisTableName = "as_event_his_bt_patrol_"+tableTime;
		//??????region_no
		eventPatrol_new.setRegionNo(device.getRegionCode());
		eventPatrol_new.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
		eventPatrol_new.setId(IdGen.snowflakeId());

		// null???????????????,???????????????????????????
		if (eventPatrol_old == null) {
			eventPatrolService.save(eventPatrol_new);
		}else {
			//??????
			eventPatrol_new.setId(eventPatrol_old.getId());
			eventPatrolService.update(eventPatrol_new);
		}
		//???????????????
		EventHisPatrol eventHisPatrol = new EventHisPatrol();
		BeanUtil.copyProperties(eventPatrol_new,eventHisPatrol);
		eventHisPatrol.setId(IdGen.snowflakeId());
		eventHisPatrol.setStoreTime(new Date());
		eventHisPatrolService.save(eventHisPatrol,hisTableName);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//???????????????????????????
		//???????????????????????????
//		ResponseBean<List<RegionLeader>> leader = regionServiceClient.getRegionLeaderByPatrolKeyId(eventPatrol_new.getIdCipher());
		List<RegionLeader> leaders = regionLeaderService.getRegionLeaderByPatrolKeyId(eventPatrol_new.getIdCipher());
		if (leaders.size() > 0 && leaders.get(0) != null) {
			RegionLeader regionLeader = leaders.get(0);
			//???????????????????????????????????????
			Date periodBeginTime = regionLeader.getPatrolPeriodBeginTime();
			Integer patrolPeriod = regionLeader.getPatrolPeriod();
			Calendar calendar = Calendar.getInstance();

//		calendar.setTime(periodBeginTime);
//		calendar.add(Calendar.DATE,patrolPeriod/60/60/24);
//		String periodEnd = format.format(calendar.getTime());
//		String periodBegin = format.format(periodBeginTime);
			//?????????????????????????????????
//		??????????????????????????????
			calendar.setTime(periodBeginTime);
			calendar.add(Calendar.SECOND,patrolPeriod);
			for(;;) {
				if (calendar.getTimeInMillis() > new Date().getTime()) {
					break;
				}
				calendar.add(Calendar.SECOND, patrolPeriod);
			}
			//???????????????????????????????????????
			String periodEnd = format.format(calendar.getTime());
			//???????????????????????????????????????
			calendar.add(Calendar.SECOND,-patrolPeriod);
			String periodBegin = format.format(calendar.getTime());
			//??????????????????????????????????????????
			List<String> monthBetween = getMonthBetween(periodBegin, format.format(new Date()));
			List<String> hisTables = new ArrayList<>();
			String tableName = "as_patrol_his_no_record_";
			for (String month : monthBetween) {
				hisTables.add(tableName+month.replaceAll("-",""));
			}
			//?????????????????????
			patrolHisNoRecordService.deleteByDeviceIdAndTime(deviceId,periodBegin,periodEnd,hisTables);
		}
	}

	/**
	 * ??????????????????
	 * @param deviceEventAuthorize
	 */
	public void eventAuthorizeProcess(DeviceEventAuthorize deviceEventAuthorize) {
		if(deviceEventAuthorize == null || deviceEventAuthorize.getDevice() == null){
			log.info("deviceEventAuthorize ??????????????????");
			return;
		}
		String deviceId = deviceEventAuthorize.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if(device == null){
			log.info("?????????????????????id[{}], deviceEventAuthorize.getDevice().getId()");
			return;
		}
		//????????????Id??????eventBase
		EventBase eventBase_old = eventBaseService.findByDeviceId(deviceId);
		if(eventBase_old != null){
			EventBase eventBase_new = new EventBase();
			eventBase_new.setId(eventBase_old.getId());
			//????????????
			eventBase_new.setRecTime(Integer.parseInt(deviceEventAuthorize.getRecTime()/1000 + ""));
			//????????????
			eventBase_new.setKeyMac(deviceEventAuthorize.getKeyMac());
			//id??????
			eventBase_new.setCiphertextId(deviceEventAuthorize.getIdCipher());
			//????????????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date date = sdf.parse(deviceEventAuthorize.getAuthTime());
				eventBase_new.setAuthTime(Integer.parseInt(date.getTime()/1000+""));
				//????????????
				eventBase_new.setAuthStatus(deviceEventAuthorize.isAuthorizeStatus() ? 1 : 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			eventBaseService.update(eventBase_new);
		}
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param minDate
	 * @param maxDate
	 * @return
	 * @throws ParseException
	 */
	private static List<String> getMonthBetween(String minDate, String maxDate) {
		ArrayList<String> result = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//??????????????????

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();
		try {
			min.setTime(sdf.parse(minDate));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
			max.setTime(sdf.parse(maxDate));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		while (min.before(max)) {
			result.add(sdf.format(min.getTime()));
			min.add(Calendar.MONTH, 1);
		}
		return result;
	}

	/**
	 * ???????????????????????????
	 * @param device
	 * @param pushEventData
	 */
	public void sendAlarmSmsToPhone(Device device, PushEventData pushEventData){
		if (configService.findIsCloud(device.getTenantCode())) {    //?????????
			//??????Id
			String deviceId = device.getId();
			//???????????????????????????????????????
			List<PushParam> allPushParams = pushEventData.getPushParams();
			//???????????????
//			List<UserAuths> userAuths = userAuthsService.findUserList(new UserAuths());
			List<User> userList = userService.findByProjectIds(device.getProjectId());
			if(userList != null && userList.size() > 0){
				for (User user : userList) {
				    if(StringUtils.isEmpty(user.getPhone())){   //??????????????????????????????????????????
				        continue;
                    }
					List<PushParam> userPushParams = new ArrayList<>(); //???????????????????????????
					//????????????id????????????????????????????????????
					AlarmTypeDeviceFavorite favorite = alarmTypeDeviceFavoriteService.getFavorite(user.getId(),deviceId);
					AlarmTypeUserFavorite userFavorite = alarmTypeUserFavoriteService.findByUserId(user.getId());	//????????????????????? 20220322
					if ((favorite != null && StringUtils.isNotEmpty(favorite.getAlarmType()))
							|| (userFavorite != null && StringUtils.isNotEmpty(userFavorite.getSmsAlarmType()))) { //???????????????????????????
						List<String> alarmTypes = favorite != null ? Arrays.asList(favorite.getAlarmType().split(",")) : new ArrayList<>();
						List<String> alarmTypes2 = userFavorite != null ? Arrays.asList(userFavorite.getSmsAlarmType().split(",")) : new ArrayList<>();
						for (PushParam allPushParam : allPushParams) {
							String alias = allPushParam.getAlias();
							if (alarmTypes.contains(alias) || alarmTypes2.contains(alias)) {   //??????????????????????????????????????????????????????
								userPushParams.add(allPushParam);
							}
						}
						if(userPushParams.size() > 0){
							PushEventData userPushEventData = new PushEventData();
							String userPushTips = userPushParams.stream().map(PushParam::getName).collect(Collectors.joining(";"));
							//????????????????????????
							AlarmLevel alarmLevel = alarmLevelService.loadMinLevelByAlarmTypes(userPushParams.stream().map(PushParam::getAlias).collect(Collectors.toList()), device.getTenantCode());
							if(alarmLevel != null){
								userPushEventData.createPushMessage(device, userPushTips, alarmLevel.getAlarmLevelName());	//????????????????????????
                                SmsDto smsDto = new SmsDto();
                                smsDto.setSignName(signName);
                                smsDto.setTemplateCode(alarmTemplateCode);
                                smsDto.setContent(userPushEventData.getPushMessage());
                                smsDto.setReceiver(user.getPhone());
								SmsResponse smsResponse = smsService.sendSms(smsDto);
								log.info("????????????????????????????????????{}", smsResponse.toString());
							}
						}
					}
				}
			}
		}else{	//?????????

			List<RegionLeader> regionLeaders = picVedioHandler.getRegionLeaders(device.getRegionCode());
			if (regionLeaders != null && regionLeaders.size() > 0) {
				RegionLeader regionLeader = regionLeaders.get(0);
				DtuBodyBean dtuBodyBean = new DtuBodyBean();
				dtuBodyBean.setFlag(DtuPushConstant.DtuFlagEnum.SEND_SMS_ALARM.getFlag());
				dtuBodyBean.setMsg(pushEventData.getPushMessage());
				dtuBodyBean.setTelephoneNo(regionLeader.getUserMobile());
				dtuBodyBean.setIp(device.getIp());
				dtuBodyBean.setProjectCode(projectCode);
				amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE, MQConstants.SEND_DTU_QUEUE, JSON.toJSONString(dtuBodyBean, SerializerFeature.WriteDateUseDateFormat));
			}
		}
	}

	/**
	 * ???????????????????????????
	 * @param device
	 * @param flowRunList
	 */
	public void sendOrderSmsToPhone(Device device, List<FlowRun> flowRunList) throws Exception {
		if(flowRunList == null || flowRunList.size() <= 0){
			return;
		}

		AlarmOrderHandleVo alarmOrderHandleVo = alarmOrderHandleService.findByProjectId(device.getProjectId());
		if(alarmOrderHandleVo.getSmsNotice() == 1){
			List<RegionLeader> regionLeaders = picVedioHandler.getRegionLeaders(device.getRegionCode());
			if (configService.findIsCloud(device.getTenantCode())) {    //?????????
				PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO("???????????????", "", new HashMap());
				Map extrasMap = pushMobilePhoneDataVO.getExtras();
				extrasMap.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.WORK_DETAIL_NOTICE.getCode());

				List<String> userPhoneNos = new ArrayList<>();	//?????????????????????
				if(regionLeaders != null && regionLeaders.size() > 0 && regionLeaders.get(0) != null) {
					User regionLeaderUser = userService.get(regionLeaders.get(0).getUserId());	//???????????????
					if(regionLeaderUser != null && StringUtils.isNotEmpty(regionLeaderUser.getPhone())) {
						userPhoneNos.add(regionLeaderUser.getPhone());
					}
				}

				//?????????????????????????????????????????????
				List<User> userList = userService.findProjectUserByProjectId(device.getProjectId());
				userPhoneNos.addAll(userList.stream().filter(user -> ((user.getSmsNotice() == null || user.getSmsNotice() == 1) && StringUtils.isNotEmpty(user.getPhone())))
						.map(User::getPhone).collect(Collectors.toList()));

				for(FlowRun flowRun : flowRunList){
					SmsDto smsDto = new SmsDto();
					smsDto.setSignName(signName);
					smsDto.setTemplateCode(orderTemplateCode);
					smsDto.setContent(flowRun.getRunNo());
					smsDto.setReceiver(String.join(",", userPhoneNos));
					SmsResponse smsResponse = smsService.sendSms(smsDto);
					log.info("??????[" + flowRun.getRunNo() + "]????????????????????????????????????{}", smsResponse.toString());
				}
			}else{	//?????????
				if (regionLeaders != null && regionLeaders.size() > 0 && regionLeaders.get(0) != null) {
					RegionLeader regionLeader = regionLeaders.get(0);
					DtuBodyBean dtuBodyBean = new DtuBodyBean();
					dtuBodyBean.setFlag(DtuPushConstant.DtuFlagEnum.SEND_SMS_WORK_ORDER.getFlag());
					dtuBodyBean.setMsg("???????????????");
					dtuBodyBean.setData(flowRunList);
					dtuBodyBean.setTelephoneNo(regionLeader.getUserMobile());
					dtuBodyBean.setIp(device.getIp());
					dtuBodyBean.setProjectCode(projectCode);
					amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE,MQConstants.SEND_DTU_QUEUE,JSON.toJSONString(dtuBodyBean,SerializerFeature.WriteDateUseDateFormat));
				}
			}
		}
	}

	/**
	 * ????????????????????????
	 * @param device
	 * @param pushEventData
	 */
	public void pushAlarmToMobileApp(Device device, PushEventData pushEventData, String alarmTypes){
		//??????????????????????????????
		if (configService.findIsCloud(device.getTenantCode())) {    //?????????
			List<String> userIds = new ArrayList<>();
			String deviceId = device.getId();
            //???????????????????????????????????????
            List<PushParam> allPushParams = pushEventData.getPushParams();
            UserAuths eAuths = new UserAuths();
//			List<UserAuths> userAuths = userServiceClient.findUserList(eAuths);
			List<UserAuths> userAuths = userAuthsService.findUserList(eAuths);
			if (userAuths != null && userAuths.size() > 0) {
				for (UserAuths userAuth : userAuths) {
//				List<AlarmTypeDeviceFavorite> lists = regionServiceClient
//						.getAlarmTypeDeviceFavoriteByUserId(userAuth.getUserId());
                    List<PushParam> userPushParams = new ArrayList<>(); //???????????????????????????
					AlarmTypeDeviceFavorite favorite = alarmTypeDeviceFavoriteService.getFavorite(userAuth.getUserId(), deviceId);  //???????????????????????????
					AlarmTypeUserFavorite userFavorite = alarmTypeUserFavoriteService.findByUserId(userAuth.getUserId());	//????????????????????? 20220322
					if ((favorite != null && StringUtils.isNotEmpty(favorite.getAlarmType()))
							|| (userFavorite != null && StringUtils.isNotEmpty(userFavorite.getAppAlarmType()))) { //???????????????????????????
                        List<String> alarmTypeList = favorite != null ? Arrays.asList(favorite.getAlarmType().split(",")) : new ArrayList<>();
                        List<String> alarmTypeList2 = userFavorite != null ? Arrays.asList(userFavorite.getAppAlarmType().split(",")) : new ArrayList<>();
                        for (PushParam allPushParam : allPushParams) {
                            String alias = allPushParam.getAlias();
                            if (alarmTypeList.contains(alias) || alarmTypeList2.contains(alias)) {   //??????????????????????????????????????????????????????
                                userPushParams.add(allPushParam);
                            }
                        }
                        if(userPushParams.size() > 0){
                            PushEventData userPushEventData = new PushEventData();
                            String userPushTips = userPushParams.stream().map(PushParam::getName).collect(Collectors.joining(";"));
                            //????????????????????????
                            AlarmLevel alarmLevel = alarmLevelService.loadMinLevelByAlarmTypes(userPushParams.stream().map(PushParam::getAlias).collect(Collectors.toList()), device.getTenantCode());
                            if(alarmLevel != null){
                                userPushEventData.createPushMessage(device, userPushTips, alarmLevel.getAlarmLevelName());	//????????????????????????
                                PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO(userPushEventData.getPushMessage(), userPushTips, new HashMap());
                                Map extrasMap = pushMobilePhoneDataVO.getExtras();
                                extrasMap.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.DEVICE_STATUS_Notice.getCode());
                                extrasMap.put("deviceId", deviceId);
                                JPushService.JPushResult jPushResult = jPushService.sendAliasAndroid(pushMobilePhoneDataVO.getAlert(), pushMobilePhoneDataVO.getTitle(), extrasMap, userAuth.getUserId());
                                log.info("?????????????????????????????????" + (jPushResult.getIsSuccess() ? "?????????" : "?????????"));
                            }
                        }
					}
				}
			}
		}
	}

	/**
	 * ????????????????????????
	 * @param device
	 * @param flowRunList
	 */
	public void pushOrderToMobileApp(Device device, List<FlowRun> flowRunList){
		if(flowRunList == null || flowRunList.size() <= 0){
			return;
		}

		AlarmOrderHandleVo alarmOrderHandleVo = alarmOrderHandleService.findByProjectId(device.getProjectId());
		if(alarmOrderHandleVo.getAppNotice() == 1){
			List<RegionLeader> regionLeaders = picVedioHandler.getRegionLeaders(device.getRegionCode());
			if (configService.findIsCloud(device.getTenantCode())) {    //?????????
				PushMobilePhoneDataVO pushMobilePhoneDataVO = new PushMobilePhoneDataVO("???????????????", "", new HashMap());
				Map extrasMap = pushMobilePhoneDataVO.getExtras();
				extrasMap.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.WORK_DETAIL_NOTICE.getCode());

				List<String> userIds = new ArrayList<>();	//?????????????????????
				if(regionLeaders != null && regionLeaders.size() > 0 && regionLeaders.get(0) != null) {
					userIds.add(regionLeaders.get(0).getUserId());
				}

				//?????????????????????????????????????????????
				List<User> userList = userService.findProjectUserByProjectId(device.getProjectId());
				userIds.addAll(userList.stream().filter(user -> (user.getAppNotice() == null || user.getAppNotice() == 1)).map(User::getId).collect(Collectors.toList()));

				for(FlowRun flowRun : flowRunList){
					extrasMap.put("workOrderId", flowRun.getId());
					JPushService.JPushResult jPushResult = jPushService.sendAliasAndroid(pushMobilePhoneDataVO.getAlert(), flowRun.getRunName(), extrasMap, userIds);
					log.info("??????[NO:" + flowRun.getRunNo() + "]??????????????????" + (jPushResult.getIsSuccess() ? "?????????" : "?????????"));
				}
			}else{	//?????????
				if (regionLeaders != null && regionLeaders.size() > 0 && regionLeaders.get(0) != null) {
					RegionLeader regionLeader = regionLeaders.get(0);
					DtuBodyBean dtuBodyBean = new DtuBodyBean();
					dtuBodyBean.setFlag(DtuPushConstant.DtuFlagEnum.PUSH_APP_WORK_ORDER.getFlag());
					dtuBodyBean.setMsg("???????????????");
					dtuBodyBean.setData(flowRunList);
					dtuBodyBean.setTelephoneNo(regionLeader.getUserMobile());
					dtuBodyBean.setIp(device.getIp());
					dtuBodyBean.setProjectCode(projectCode);
					amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE,MQConstants.SEND_DTU_QUEUE,JSON.toJSONString(dtuBodyBean,SerializerFeature.WriteDateUseDateFormat));
				}
			}
		}
	}

	/**
	 *
	 * @param device
	 * @param regionName
	 * @param alarmTypeList
	 */
	public void updateDeviceDynamic(Device device, String regionName, List<AlarmType> alarmTypeList) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String id = device.getId();
		String deviceName = device.getDeviceName();
		for (AlarmType alarmType : alarmTypeList) {
			String statusName = alarmType.getStatusName();
			String alarmTypeName = alarmType.getAlarmTypeName();
			BoundListOperations faultListOperations = redisTemplate.boundListOps("fault");
			if (faultListOperations != null && faultListOperations.size() > 0) {
				for (int i = 0; i < faultListOperations.size(); i++) {
					DynamicMessage message = (DynamicMessage) faultListOperations.index(i);
					if (message != null) {
						String deviceId = message.getDeviceId();
						String redisStatusName = message.getStatusName();
						if (id.equals(deviceId) && statusName.equals(redisStatusName)) {
							faultListOperations.remove(0, message);
							break;
						}
					}
				}
			}
			BoundListOperations warnListOperations = redisTemplate.boundListOps("warn");
			if (warnListOperations != null && warnListOperations.size() > 0) {
				for (int i = 0; i < warnListOperations.size(); i++) {
					DynamicMessage message = (DynamicMessage) warnListOperations.index(i);
					if (message != null) {
						String deviceId = message.getDeviceId();
						String redisStatusName = message.getStatusName();
						if (id.equals(deviceId) && statusName.equals(redisStatusName)) {
							warnListOperations.remove(0,message);
							break;
						}
					}
				}
			}
			BoundListOperations normalListOperations = redisTemplate.boundListOps("normal");
			if (normalListOperations != null && normalListOperations.size() > 0) {
				for (int i = 0; i < normalListOperations.size(); i++) {
					DynamicMessage message = (DynamicMessage) normalListOperations.index(i);
					if (message != null) {
						String deviceId = message.getDeviceId();
						String redisStatusName = message.getStatusName();
						if (id.equals(deviceId) && statusName.equals(redisStatusName)) {
							normalListOperations.remove(0,message);
							break;
						}
					}
				}
			}
			DynamicMessage message = new DynamicMessage();
//				message.setDeviceKindType("");
			message.setDeviceId(id);
			message.setStatusName(statusName);
			message.setDeviceName(deviceName);
			message.setRegionName(regionName);
			message.setAlarmType(alarmTypeName);
			message.setAlarmTime(format.format(new Date()));
			message.setTenantCode(device.getTenantCode());
			message.setProjectId(device.getProjectId());
			message.setRegionCode(device.getRegionCode());
			message.setDeviceCode(device.getDeviceCode());
			message.setIp(device.getIp());
			if(alarmType.getAlarmLevel() == 1) { //??????
				message.setAlarmLevel(1);
				message.setAlarmLevelName(AlarmLevelType.FAULT.getDescription());
				if(faultListOperations.size() >= 10){
					faultListOperations.rightPop();
					faultListOperations.leftPush(message);
				}else {
					faultListOperations.leftPush(message);
				}
			}
			if(alarmType.getAlarmLevel() == 2){ //??????
				message.setAlarmLevel(2);
				message.setAlarmLevelName(AlarmLevelType.WARNING.getDescription());
				if(warnListOperations.size() >= 10){
					warnListOperations.rightPop();
					warnListOperations.leftPush(message);
				}else {
					warnListOperations.leftPush(message);
				}
			}
			if(alarmType.getAlarmLevel() == 3){ //??????
				message.setAlarmLevel(3);
				message.setAlarmLevelName(AlarmLevelType.NORMAL.getDescription());
				if(normalListOperations.size() >= 10){
					normalListOperations.rightPop();
					normalListOperations.leftPush(message);
				}else {
					normalListOperations.leftPush(message);
				}
			}
		}
	}


    /**
     * ??????????????????????????????
     */
    private void initNetwork(EventNetwork eventNetwork,String tenantCode){
        if(eventNetwork != null){
            String deviceId = eventNetwork.getDeviceId();
            if(deviceId != null && !"".equals(deviceId)){
                com.aswl.as.ibrs.api.module.DeviceEventAlarm deviceEventAlarm = deviceEventAlarmService.findByDeviceId(deviceId); //????????????
                AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(eventNetwork.getNetworkState(), EventTableEnum.NETWORK.getTableName(), "network_state"); //??????????????????
                if(alarmType != null){
                    deviceEventAlarm.setAlarmLevel(alarmType.getAlarmLevel());  //??????????????????
                    if(alarmType.getAlarmLevel() == 3){  //??????
                        deviceEventAlarm.setIsAlarm(0);  //?????????
                        deviceEventAlarm.setPromptTypes((deviceEventAlarm.getPromptTypes() == null || "".equals(deviceEventAlarm.getPromptTypes())) ? alarmType.getAlarmType() : alarmType.getAlarmType() + Globals.EventAlarmSeparateConsts.COMMA + deviceEventAlarm.getPromptTypes());//???????????????
						deviceEventAlarm.setAlarmTypes("");
						deviceEventAlarm.setAlarmTypesDes(""); //????????????
						deviceEventAlarm.setAlarmDates("");  //????????????
						deviceEventAlarm.setAlarmLevels("");  //????????????
						deviceEventAlarm.setUEventId("0");
                    }
//                    else{  //??????
//                        AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), tenantCode);  //???????????????????????????
//                        deviceEventAlarm.setIsAlarm(1);  //??????
//                        deviceEventAlarm.setAlarmTypes((deviceEventAlarm.getAlarmTypes() == null || "".equals(deviceEventAlarm.getAlarmTypes())) ? alarmType.getAlarmType() : alarmType.getAlarmType() + Globals.EventAlarmSeparateConsts.COMMA + deviceEventAlarm.getAlarmTypes()); //????????????
//                        deviceEventAlarm.setAlarmTypesDes((deviceEventAlarm.getAlarmTypesDes() == null || "".equals(deviceEventAlarm.getAlarmTypesDes())) ? alarmType.getAlarmTypeName() : alarmType.getAlarmTypeName() + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmTypesDes()); //????????????
//                        deviceEventAlarm.setAlarmDates((deviceEventAlarm.getAlarmDates() == null || "".equals(deviceEventAlarm.getAlarmDates())) ? DateUtil.formatDateTime(eventNetwork.getStoreTime()) : DateUtil.formatDateTime(eventNetwork.getStoreTime()) + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmDates());  //????????????
//                        deviceEventAlarm.setAlarmLevels((deviceEventAlarm.getAlarmLevels() == null || "".equals(deviceEventAlarm.getAlarmLevels())) ? alarmLevel.getAlarmLevelName() : alarmLevel.getAlarmLevelName() + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmLevels());  //????????????
//                        deviceEventAlarm.setUEventId((deviceEventAlarm.getUEventId() == null || "".equals(deviceEventAlarm.getUEventId())) ? "0" : "0" + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getUEventId());
//                    }
					deviceEventAlarm.setRecTime(((Long)(eventNetwork.getStoreTime().getTime() / 1000)).intValue());
                    deviceEventAlarmService.update(deviceEventAlarm);
                }
            }
        }
    }

    /**
     * ?????????????????????????????????
     * @param event
     * @param newDeviceEvent
     */
    private void initState(String event, Object newDeviceEvent, Object dbDeviceEvent){
		Map<String,Object> initMap = new HashMap<>();
		String deviceId = null;
		Integer recTime = null;
    	if(EventTableEnum.ALARM.getTableName().equals(event)){
    		EventAlarm eventAlarm_new = (EventAlarm)newDeviceEvent;
    		deviceId = eventAlarm_new.getDeviceId();
			recTime = eventAlarm_new.getRecTime().intValue();
    		initMap.put("fld01",eventAlarm_new.getFld01());
    		initMap.put("fld02",eventAlarm_new.getFld02());
    		initMap.put("fld03",eventAlarm_new.getFld03());
    		initMap.put("fld04",eventAlarm_new.getFld04());
    		initMap.put("fld05",eventAlarm_new.getFld05());
    		initMap.put("fld06",eventAlarm_new.getFld06());
    		initMap.put("fld07",eventAlarm_new.getFld07());
    		initMap.put("fld08",eventAlarm_new.getFld08());
    		initMap.put("fld09",eventAlarm_new.getFld09());
    		initMap.put("fld10",eventAlarm_new.getFld10());
    		initMap.put("fld11",eventAlarm_new.getFld11());
    		initMap.put("fld12",eventAlarm_new.getFld12());
    		initMap.put("fld13",eventAlarm_new.getFld13());
    		initMap.put("fld14",eventAlarm_new.getFld14());
    		initMap.put("fld15",eventAlarm_new.getFld15());
    		initMap.put("fld16",eventAlarm_new.getFld16());
		}
		else if(EventTableEnum.CURRENT.getTableName().equals(event)){
			EventEcurrent eventEcurrent_new = (EventEcurrent)newDeviceEvent;
			deviceId = eventEcurrent_new.getDeviceId();
			recTime = eventEcurrent_new.getRecTime();
			initMap.put("fldx",eventEcurrent_new.getFldx());
			initMap.put("fldy",eventEcurrent_new.getFldy());
			initMap.put("fldall",eventEcurrent_new.getFldall());
			initMap.put("fld01",eventEcurrent_new.getFld01());
			initMap.put("fld02",eventEcurrent_new.getFld02());
			initMap.put("fld03",eventEcurrent_new.getFld03());
			initMap.put("fld04",eventEcurrent_new.getFld04());
			initMap.put("fld05",eventEcurrent_new.getFld05());
			initMap.put("fld06",eventEcurrent_new.getFld06());
			initMap.put("fld07",eventEcurrent_new.getFld07());
			initMap.put("fld08",eventEcurrent_new.getFld08());
			initMap.put("fld09",eventEcurrent_new.getFld09());
			initMap.put("fld10",eventEcurrent_new.getFld10());
			initMap.put("fld11",eventEcurrent_new.getFld11());
			initMap.put("fld12",eventEcurrent_new.getFld12());
			initMap.put("fld13",eventEcurrent_new.getFld13());
			initMap.put("fld14",eventEcurrent_new.getFld14());
			initMap.put("fld15",eventEcurrent_new.getFld15());
			initMap.put("fld16",eventEcurrent_new.getFld16());
		}
		else if(EventTableEnum.SWITCH.getTableName().equals(event)){
    		EventEswitch eventEswitch_new = (EventEswitch)newDeviceEvent;
			deviceId = eventEswitch_new.getDeviceId();
			recTime = eventEswitch_new.getRecTime();
			initMap.put("fldx",eventEswitch_new.getFldx());
			initMap.put("fldy",eventEswitch_new.getFldy());
			initMap.put("fld01",eventEswitch_new.getFld01());
			initMap.put("fld02",eventEswitch_new.getFld02());
			initMap.put("fld03",eventEswitch_new.getFld03());
			initMap.put("fld04",eventEswitch_new.getFld04());
			initMap.put("fld05",eventEswitch_new.getFld05());
			initMap.put("fld06",eventEswitch_new.getFld06());
			initMap.put("fld07",eventEswitch_new.getFld07());
			initMap.put("fld08",eventEswitch_new.getFld08());
			initMap.put("fld09",eventEswitch_new.getFld09());
			initMap.put("fld10",eventEswitch_new.getFld10());
			initMap.put("fld11",eventEswitch_new.getFld11());
			initMap.put("fld12",eventEswitch_new.getFld12());
			initMap.put("fld13",eventEswitch_new.getFld13());
			initMap.put("fld14",eventEswitch_new.getFld14());
			initMap.put("fld15",eventEswitch_new.getFld15());
			initMap.put("fld16",eventEswitch_new.getFld16());

		}
		else if(EventTableEnum.OUTLET.getTableName().equals(event)){
			EventEoutlet eventEoutlet_new = (EventEoutlet)newDeviceEvent;
			EventEoutlet eventEoutlet_db = (EventEoutlet)dbDeviceEvent;
			deviceId = eventEoutlet_new.getDeviceId();
			recTime = eventEoutlet_new.getRecTime();
			if(eventEoutlet_new.getFld01() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld01()){
                initMap.put("fld01",eventEoutlet_new.getFld01());
            }
            if(eventEoutlet_new.getFld02() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld02()) {
                initMap.put("fld02", eventEoutlet_new.getFld02());
            }
            if(eventEoutlet_new.getFld03() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld03()) {
                initMap.put("fld03", eventEoutlet_new.getFld03());
            }
            if(eventEoutlet_new.getFld04() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld04()) {
                initMap.put("fld04", eventEoutlet_new.getFld04());
            }
            if(eventEoutlet_new.getFld05() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld05()) {
                initMap.put("fld05", eventEoutlet_new.getFld05());
            }
            if(eventEoutlet_new.getFld06() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld06()) {
                initMap.put("fld06", eventEoutlet_new.getFld06());
            }
            if(eventEoutlet_new.getFld07() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld07()) {
                initMap.put("fld07", eventEoutlet_new.getFld07());
            }
            if(eventEoutlet_new.getFld08() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld08()) {
                initMap.put("fld08", eventEoutlet_new.getFld08());
            }
            if(eventEoutlet_new.getFld09() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld09()) {
                initMap.put("fld09", eventEoutlet_new.getFld09());
            }
            if(eventEoutlet_new.getFld10() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld10()) {
                initMap.put("fld10", eventEoutlet_new.getFld10());
            }
            if(eventEoutlet_new.getFld11() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld11()) {
                initMap.put("fld11", eventEoutlet_new.getFld11());
            }
            if(eventEoutlet_new.getFld12() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld12()) {
                initMap.put("fld12", eventEoutlet_new.getFld12());
            }
            if(eventEoutlet_new.getFld13() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld13()) {
                initMap.put("fld13", eventEoutlet_new.getFld13());
            }
            if(eventEoutlet_new.getFld14() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld14()) {
                initMap.put("fld14", eventEoutlet_new.getFld14());
            }
            if(eventEoutlet_new.getFld15() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld15()) {
                initMap.put("fld15", eventEoutlet_new.getFld15());
            }
            if(eventEoutlet_new.getFld16() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventEoutlet_db.getFld16()) {
                initMap.put("fld16", eventEoutlet_new.getFld16());
            }
		}
		else if(EventTableEnum.SFP.getTableName().equals(event)){
			EventSfp eventSfp_new = (EventSfp)newDeviceEvent;
			EventSfp eventSfp_db = (EventSfp)dbDeviceEvent;
			deviceId = eventSfp_new.getDeviceId();
			recTime = eventSfp_new.getRecTime();
            if(eventSfp_new.getFld01() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld01()) {
                initMap.put("fld01", eventSfp_new.getFld01());
            }
            if(eventSfp_new.getFld02() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld02()) {
                initMap.put("fld02", eventSfp_new.getFld02());
            }
            if(eventSfp_new.getFld03() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld03()) {
                initMap.put("fld03", eventSfp_new.getFld03());
            }
            if(eventSfp_new.getFld04() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld04()) {
                initMap.put("fld04", eventSfp_new.getFld04());
            }
            if(eventSfp_new.getFld05() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld05()) {
                initMap.put("fld05", eventSfp_new.getFld05());
            }
            if(eventSfp_new.getFld06() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld06()) {
                initMap.put("fld06", eventSfp_new.getFld06());
            }
            if(eventSfp_new.getFld07() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld07()) {
                initMap.put("fld07", eventSfp_new.getFld07());
            }
            if(eventSfp_new.getFld08() != null && Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() != eventSfp_db.getFld08()) {
                initMap.put("fld08", eventSfp_new.getFld08());
            }
		}
		else if(EventTableEnum.VOLTAGE.getTableName().equals(event)){
			EventEvoltage eventEvoltage_new = (EventEvoltage)newDeviceEvent;
			deviceId = eventEvoltage_new.getDeviceId();
			recTime = eventEvoltage_new.getRecTime();
			initMap.put("fld01",eventEvoltage_new.getFld01());
			initMap.put("fld02",eventEvoltage_new.getFld02());
			initMap.put("fld03",eventEvoltage_new.getFld03());
			initMap.put("fld04",eventEvoltage_new.getFld04());
			initMap.put("fld05",eventEvoltage_new.getFld05());
			initMap.put("fld06",eventEvoltage_new.getFld06());
			initMap.put("fld07",eventEvoltage_new.getFld07());
			initMap.put("fld08",eventEvoltage_new.getFld08());
			initMap.put("humidity",eventEvoltage_new.getHumidity());
			initMap.put("temperature",eventEvoltage_new.getTemperature());
		}
		else if("as_event_iot".equals(event)){
			EventBase eventBase_new = (EventBase)newDeviceEvent;
			deviceId = eventBase_new.getDeviceId();
			recTime = eventBase_new.getRecTime();
			initMap.put("iotStatus",eventBase_new.getIotStatus());
			initMap.put("useStatus",eventBase_new.getUseStatus());
		}
//		else if("as_event_gps".equals(event)){
//			//TODO Gps?????????
//		}
		saveState(deviceId,recTime,initMap,event);

    }

    /**
     * ???????????????????????????????????????
     * @param deviceId
     * @param recTime
     * @param initMap
     * @param tableCode
     */
	private void saveState(String deviceId, Integer recTime, Map<String, Object> initMap, String tableCode) {
		//??????????????????
		Integer hightLevel = null;
		//???????????????
		List<Integer> alarmLevels = new ArrayList<>();

		//??????????????????,????????????
		StringBuilder alarmTypes = new StringBuilder();
		//??????????????????????????????,????????????
		StringBuilder alarmTypeDes = new StringBuilder();
		//????????????????????????,????????????
		StringBuilder alarmLevelDes = new StringBuilder();
		//??????????????????,????????????
		StringBuilder alarmDates = new StringBuilder();
		//????????????,????????????
		StringBuilder promptTypes = new StringBuilder();
		//eventIds??? , ????????????
        StringBuilder eventIds = new StringBuilder();
		Device device = deviceService.findById(deviceId);
		com.aswl.as.ibrs.api.module.DeviceEventAlarm deviceEventAlarm = deviceEventAlarmService.findByDeviceId(deviceId);
		deviceEventAlarm.setRecTime(recTime);
		Set<String> keySet = initMap.keySet();
		//?????????????????????????????????fld
		String flds = eventUcMetadataService.findByModelTab(device.getDeviceModelId(), tableCode);
		for (String key : keySet) {
			if (flds != null && flds.contains(key)) {
				Integer value;
				if (EventTableEnum.CURRENT.getTableName().equals(tableCode) || EventTableEnum.VOLTAGE.getTableName().equals(tableCode)) {  //?????????????????????
					Double doubleVal = Double.valueOf(initMap.get(key).toString());
					DeviceModelAlarmThreshold deviceModelAlarmThreshold = deviceModelAlarmThresholdService.findStatusValueByCode(device.getDeviceModelId(), doubleVal, tableCode, key);
					if (deviceModelAlarmThreshold == null) {
						continue;
					}
					value = deviceModelAlarmThreshold.getStatusValue();
				} else {
					value = Integer.parseInt(initMap.get(key).toString());
				}

				AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(value, tableCode, key);
				if (alarmType != null) {
					alarmLevels.add(alarmType.getAlarmLevel());
					AlarmLevel dbAlarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
					if (AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())) {  //??????
						alarmTypes.append(alarmType.getAlarmType()).append(Globals.EventAlarmSeparateConsts.COMMA);
						alarmTypeDes.append(alarmType.getAlarmTypeName()).append(Globals.EventAlarmSeparateConsts.SEMICOLON);
						alarmLevelDes.append(dbAlarmLevel.getAlarmLevelName()).append(Globals.EventAlarmSeparateConsts.SEMICOLON);
						alarmDates.append(DateUtil.formatDateTime(new Date((long)recTime*1000))).append(Globals.EventAlarmSeparateConsts.SEMICOLON);
                        eventIds.append("0").append(Globals.EventAlarmSeparateConsts.SEMICOLON);
					} else {  //??????
						promptTypes.append(alarmType.getAlarmType()).append(Globals.EventAlarmSeparateConsts.COMMA);
					}
				}
			}
		}

		if(alarmTypes.toString().length() > 0){
			String alarmTypesStr = alarmTypes.toString().substring(0,alarmTypes.toString().length()-1);
			deviceEventAlarm.setAlarmTypes((deviceEventAlarm.getAlarmTypes() == null || "".equals(deviceEventAlarm.getAlarmTypes())) ? alarmTypesStr : alarmTypesStr + Globals.EventAlarmSeparateConsts.COMMA + deviceEventAlarm.getAlarmTypes());
		}
		if(alarmLevelDes.toString().length() > 0){
			String alarmLevelDesStr = alarmLevelDes.toString().substring(0,alarmLevelDes.toString().length()-1);
			deviceEventAlarm.setAlarmLevels((deviceEventAlarm.getAlarmLevels() == null || "".equals(deviceEventAlarm.getAlarmLevels())) ? alarmLevelDesStr : alarmLevelDesStr + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmLevels());
		}
		if(alarmDates.toString().length() > 0){
			String alarmDatesStr = alarmDates.toString().substring(0,alarmDates.toString().length()-1);
			deviceEventAlarm.setAlarmDates((deviceEventAlarm.getAlarmDates() == null || "".equals(deviceEventAlarm.getAlarmDates())) ? alarmDatesStr : alarmDatesStr + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmDates());
		}
		if(promptTypes.toString().length() > 0){
			String promptTypesStr = promptTypes.toString().substring(0,promptTypes.toString().length()-1);
			deviceEventAlarm.setPromptTypes((deviceEventAlarm.getPromptTypes() == null || "".equals(deviceEventAlarm.getPromptTypes())) ? promptTypesStr : promptTypesStr + Globals.EventAlarmSeparateConsts.COMMA + deviceEventAlarm.getPromptTypes());
		}
		if(alarmTypeDes.toString().length() > 0){
			String alarmTypeDesStr = alarmTypeDes.toString().substring(0,alarmTypeDes.toString().length()-1);
			deviceEventAlarm.setAlarmTypesDes((deviceEventAlarm.getAlarmTypesDes() == null || "".equals(deviceEventAlarm.getAlarmTypesDes())) ? alarmTypeDesStr : alarmTypeDesStr + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getAlarmTypesDes());
		}
        if(eventIds.toString().length() > 0){
            String eventIdsStr = eventIds.toString().substring(0,eventIds.toString().length()-1);
            deviceEventAlarm.setUEventId((deviceEventAlarm.getUEventId() == null || "".equals(deviceEventAlarm.getUEventId())) ? eventIdsStr : eventIdsStr + Globals.EventAlarmSeparateConsts.SEMICOLON + deviceEventAlarm.getUEventId());
        }
		// ??????????????????
		hightLevel = getHighestAlarmLevel(deviceEventAlarm.getAlarmTypes()); // ????????????????????????
		if (hightLevel != null) {
			deviceEventAlarm.setAlarmLevel(hightLevel);
		} else {
			deviceEventAlarm.setAlarmLevel(AlarmLevelType.NORMAL.getType());
		}
		deviceEventAlarm.setIsAlarm(StringUtils.isEmpty(deviceEventAlarm.getAlarmTypes()) ? 0 : 1);
		deviceEventAlarmService.update(deviceEventAlarm);
	}

	/**
	 * ??????????????????????????????
	 */
	public void syncCityNetstat(EventNetwork eventNetwork){
		if(cityPlatformUtil.getEnable() && cityPlatformUtil.getIsCityPlatform()){
			return;
		}
		if(cityPlatformUtil.isCityPlatform()) {
			CityPlatformDto cityPlatformDto = new CityPlatformDto();
			cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
			cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
			cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
			cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
			cityPlatformDto.setData(eventNetwork);
			byte[] bytes = new byte[0];
			try {
				bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
			} catch (Exception e) {
				e.printStackTrace();
			}
			cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE, MqConstant.CITY_PLATFORM_NETWORK_QUEUE,bytes);
		}
	}


	/**
	 * ???????????????
	 */
	public void sendMsg(){


	}


	/**
	 * ??????????????????
	 */
	private void eventElectricityProcess(DeviceElectricity deviceElectricity) {
		// ??????????????????
		if (deviceElectricity == null || deviceElectricity.getDevice() == null) {
			log.info("deviceElectricity??????????????????");
			return;
		}
		String deviceId = deviceElectricity.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceElectricity.getDevice().getId());
			return;
		}
		EventElectricity eventElectricity = deviceEventBuilder.buildEventElectricity(deviceElectricity);
		EventElectricity dbEventElectricity = eventElectricityService.findByDeviceId(deviceId);
		if(dbEventElectricity == null){
			eventElectricity.setId(IdGen.snowflakeId());
			eventElectricity.setRegionNo(device.getRegionCode());
			eventElectricity.setApplicationCode(device.getApplicationCode());
			eventElectricity.setTenantCode(device.getTenantCode());
			eventElectricity.setStoreTime(new Date());
			eventElectricity.setNewRecord(true);
		}else {
			eventElectricity.setId(dbEventElectricity.getId());
		}
		eventElectricityService.save(eventElectricity);
	}

	/**
	 * ?????????,???????????????????????????
	 */
	public Map outletSfpForbiddenProcess(Device device,Map<String,Object> changeEventMap,String agreement,Object eventOld){
        Date date = new Date();
        long recTime = date.getTime() / 1000;
        String deviceId = device.getId();
		Map<String,String> updateKey = new HashMap<>();
//		List<AlarmType> incomingType = new ArrayList<>();
		List<String> alarmTypeStr = new ArrayList<>();
		List<String> statusName = new ArrayList<>();
		List<String> promptTypeStr = new ArrayList<>();
		List<String> alarmDateStr = new ArrayList<>();
		List<String> alarmTypeDesStr = new ArrayList<>();
		List<String> alarmLevelsStr = new ArrayList<>();
		List<String> ueventIds = new ArrayList<>();
        String dateTime = DateUtil.formatDateTime(date);
        if(agreement.equals(EventTableEnum.SFP.getTableName())){
            Set<String> keySet = changeEventMap.keySet();
            Map<String,String> sfpState = eventSfpService.findSfpState(keySet, deviceId);
            for (Map.Entry<String, String> entry : sfpState.entrySet()) {
                String key = entry.getKey();
				String dbValue = String.valueOf(entry.getValue());
                AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(Integer.parseInt(changeEventMap.get(key).toString()), EventTableEnum.SFP.getTableName(), key);
                if(alarmType != null){
                    AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
                    if (alarmLevel != null) {
                        if(PortSettingEnum.Enable.getValue().equals(dbValue)){
                            if(AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())){
                                ueventIds.add("0");
                                alarmTypeStr.add(alarmType.getAlarmType());
                                alarmDateStr.add(dateTime);
                                alarmTypeDesStr.add(alarmType.getAlarmTypeName());
                                alarmLevelsStr.add(alarmLevel.getAlarmLevelName());
                            }else {
                                promptTypeStr.add(alarmType.getAlarmType());
                            }
                            //??????????????????????????????
                            updateKey.put(key,changeEventMap.get(key).toString());
                            //???????????????-1?????????????????????
//                    		incomingType.add(alarmType);
                            //??????Mapkey
                            changeEventMap.remove(key);
                            statusName.add(alarmType.getStatusName());
                        }
                    }
                }
			}
            //??????EOULET???
            updateState(updateKey,device,EventTableEnum.SFP.getTableName(),eventOld);
        }
		if(agreement.equals(EventTableEnum.OUTLET.getTableName())){
            Set<String> keySet = changeEventMap.keySet();
            Map<String,String> outletMap = eventEoutletService.findOutletState(keySet,deviceId);
            for (Map.Entry<String, String> entry : outletMap.entrySet()) {
                String key = entry.getKey();
                String dbValue = String.valueOf(entry.getValue());
                AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(Integer.parseInt(changeEventMap.get(key).toString()), EventTableEnum.OUTLET.getTableName(), key);
                if(alarmType != null) {
                    AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
                    if(alarmLevel != null) {
						if (PortSettingEnum.Enable.getValue().equals(dbValue)) {  //???????????????,?????????????????????
							if (AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())) {
								ueventIds.add("0");
								alarmTypeStr.add(alarmType.getAlarmType());
								alarmDateStr.add(dateTime);
								alarmTypeDesStr.add(alarmType.getAlarmTypeName());
								alarmLevelsStr.add(alarmLevel.getAlarmLevelName());
							} else {
								promptTypeStr.add(alarmType.getAlarmType());
							}
							//??????????????????????????????
							updateKey.put(key, changeEventMap.get(key).toString());
							//???????????????-1?????????????????????
//                    		incomingType.add(alarmType);
							//??????Mapkey
							changeEventMap.remove(key);
							statusName.add(alarmType.getStatusName());
						}
					}
                }
            }
            //??????EOULET???
            updateState(updateKey,device,EventTableEnum.OUTLET.getTableName(),eventOld);
		}
		this.updateDeviceEventAlarm(device,StringUtils.join(statusName.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmTypeStr.toArray(),Globals.EventAlarmSeparateConsts.COMMA),
				StringUtils.join(alarmTypeDesStr.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmLevelsStr.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmDateStr.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(ueventIds.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(promptTypeStr.toArray(),Globals.EventAlarmSeparateConsts.COMMA),date,recTime);
		return changeEventMap;
	}


    /**
     * ????????????????????????
     * @param updateKey
     * @param device
     * @param table
     */
    private void updateState(Map<String, String> updateKey, Device device, String table,Object eventOld) {
	    if(updateKey.size()>0){
//            Set<Map.Entry<String, String>> entries = updateKey.entrySet();
            if(table.equals(EventTableEnum.SFP.getTableName())){  //??????
                EventSfp dbEventSfp = (EventSfp)eventOld;
				try {
					BeanUtils.populate(dbEventSfp, updateKey);	//????????????????????????
				} catch (Exception e) {
					e.printStackTrace();
				}
                eventSfpService.update(dbEventSfp);
            }
            if(table.equals(EventTableEnum.OUTLET.getTableName())){  //??????
                EventEoutlet dbEventEoutlet = (EventEoutlet)eventOld;
				try {
					BeanUtils.populate(dbEventEoutlet, updateKey);	//????????????????????????
				} catch (Exception e) {
					e.printStackTrace();
				}
                eventEoutletService.update(dbEventEoutlet);
            }
        }
    }


	/**
	 * ?????????????????????????????????
	 */

	public void debugDeviceEvent(DeviceEvent deviceEvent,Device device, String alarmTypeStr, Object bean_new, Object bean_old,
								 Map<String, Object> changeEventMap, Date storeTime, Long recTime){
		List<Integer> allValue = new ArrayList<>();
		List<String> statusNames = new ArrayList<>(); // ???????????????
		List<String> alarmTypes = new ArrayList<>(); // ??????????????????
		List<String> alarmTypesDes = new ArrayList<>(); // ????????????????????????????????????
		List<String> alarmLevels = new ArrayList<>(); // ?????????
		List<String> alarmDates = new ArrayList<>(); // ???????????????
		List<String> promptTypes = new ArrayList<>(); // ??????????????????
		List<String> promptTypesDes = new ArrayList<>(); // ????????????????????????????????????
		List<String> changeTypes = new ArrayList<>(); // ????????????????????????
		List<String> changeTypesDes = new ArrayList<>(); // ??????????????????????????????????????????
		List<String> uEventIds = new ArrayList<>(); // ??????????????????ID??????
		List<AlarmType> alarmTypeList = new ArrayList<>();
		for (String key : changeEventMap.keySet()) {
			Integer value = null;
			// ????????????????????????
			if (alarmTypeStr.equals(EventTableEnum.CURRENT.getTableName()) || alarmTypeStr.equals(EventTableEnum.VOLTAGE.getTableName())) {

				// 1?????????key??? ???as_event_ecurrent ?????? ??????????????????ID 2???????????????ID?????? ????????????ID 3???????????????ID ??? ?????????
				// ???????????????????????????
				Double values = Double.valueOf(changeEventMap.get(key).toString());
				DeviceModelAlarmThreshold deviceModelAlarmThreshold = deviceModelAlarmThresholdService
						.findStatusValueByCode(device.getDeviceModelId(), values, alarmTypeStr, key);
				if (deviceModelAlarmThreshold == null) {
					continue;
				}
				value = deviceModelAlarmThreshold.getStatusValue();
				// ??????????????????
				Integer his_value = eventVoltageAndEcurrent(alarmTypeStr, device, key);
				if (value == his_value) {
					continue;
				}
				allValue.add(value);
			} else {
				value = Integer.parseInt(changeEventMap.get(key).toString());
			}
			// ?????????????????????????????????????????? ???????????????????????????????????????
			AlarmType alarmType = alarmTypeService.findByStatusValueWithTabFld(value, alarmTypeStr, key);
			if(alarmType != null){
				alarmTypeList.add(alarmType);
				// ????????????????????????
				AlarmLevel alarmLevel = alarmLevelService.findByAlarmLevel(alarmType.getAlarmLevel(), device.getTenantCode());
				statusNames.add(alarmType.getStatusName());  //?????????
				changeTypes.add(alarmType.getAlarmType()); //???????????????
				changeTypesDes.add(alarmType.getAlarmTypeName()); //?????????????????????
				if(AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())){  //????????????????????????
					alarmTypes.add(alarmType.getAlarmType());
					alarmTypesDes.add(alarmType.getAlarmTypeName());
					alarmLevels.add(alarmLevel.getAlarmLevelName());
					alarmDates.add(DateUtils.formatDate(storeTime, "yyyy-MM-dd HH:mm:ss"));
					uEventIds.add("0");
				}else {
					promptTypes.add(alarmType.getAlarmType());
					promptTypesDes.add(alarmType.getAlarmTypeName());
				}
			}
		}

		this.saveBean(alarmTypeStr,bean_new,bean_old);
		this.updateDeviceEventAlarm(device,StringUtils.join(statusNames.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmTypes.toArray(),Globals.EventAlarmSeparateConsts.COMMA),
				StringUtils.join(alarmTypesDes.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmLevels.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(alarmDates.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(uEventIds.toArray(),Globals.EventAlarmSeparateConsts.SEMICOLON),
				StringUtils.join(promptTypes.toArray(),Globals.EventAlarmSeparateConsts.COMMA),storeTime,recTime);
	}

	/**
	 * ??????SFP????????????
	 * @param deviceEventSfpInfo
	 */
	public void eventSfpInfoProcess(DeviceEventSfpInfo deviceEventSfpInfo){
		// ??????????????????
		if (deviceEventSfpInfo == null || deviceEventSfpInfo.getDevice() == null) {
			log.info("deviceElectricity??????????????????");
			return;
		}
		String deviceId = deviceEventSfpInfo.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventSfpInfo.getDevice().getId());
			return;
		}
		EventSfpInfo eventSfpInfo = deviceEventBuilder.buildEventSfpInfo(deviceEventSfpInfo);
		EventSfpInfo dbEventSfpInfo = eventSfpInfoService.findByDeviceId(deviceId);

		if(dbEventSfpInfo == null){
			eventSfpInfo.setId(IdGen.snowflakeId());
			eventSfpInfo.setRegionNo(device.getRegionCode());
			eventSfpInfo.setApplicationCode(device.getApplicationCode());
			eventSfpInfo.setTenantCode(device.getTenantCode());
			eventSfpInfo.setStoreTime(new Date());
			eventSfpInfo.setNewRecord(true);
			eventSfpInfoService.insert(eventSfpInfo);
		}else{
			BeanUtil.copyProperties(eventSfpInfo, dbEventSfpInfo, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSfpInfoService.update(dbEventSfpInfo);
		}
	}

	/**
	 * ???????????????????????????
	 * @param deviceSecCtlPower
	 */
	public void eventSecCtlPowerProcess(DeviceSecCtlPower deviceSecCtlPower){
		// ??????????????????
		if (deviceSecCtlPower == null || deviceSecCtlPower.getDevice() == null) {
			log.info("deviceElectricity??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPower.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPower.getDevice().getId());
			return;
		}
		EventSecCtlPower eventSecCtlPower = deviceEventBuilder.buildSecCtlPower(deviceSecCtlPower);
		EventSecCtlPower dbEventSecCtlPower = eventSecCtlPowerService.findByDeviceId(deviceId);

		if(dbEventSecCtlPower == null){
			eventSecCtlPower.setId(IdGen.snowflakeId());
			eventSecCtlPower.setRegionNo(device.getRegionCode());
			eventSecCtlPower.setApplicationCode(device.getApplicationCode());
			eventSecCtlPower.setTenantCode(device.getTenantCode());
			eventSecCtlPower.setStoreTime(new Date());
			eventSecCtlPower.setNewRecord(true);
			eventSecCtlPowerService.insert(eventSecCtlPower);
		}else{
			BeanUtil.copyProperties(eventSecCtlPower, dbEventSecCtlPower, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerService.update(dbEventSecCtlPower);
		}
	}

	/**
	 * ?????????????????????????????????
	 * @param deviceSecCtlPowerAlarm
	 */
	public void eventSecCtlPowerAlarmProcess(DeviceSecCtlPowerAlarm deviceSecCtlPowerAlarm){
		// ??????????????????
		if (deviceSecCtlPowerAlarm == null || deviceSecCtlPowerAlarm.getDevice() == null) {
			log.info("deviceSecCtlPowerAlarm??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerAlarm.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerAlarm.getDevice().getId());
			return;
		}
		EventSecCtlPower eventSecCtlPower = deviceEventBuilder.buildSecCtlPower(deviceSecCtlPowerAlarm);
		EventSecCtlPower dbEventSecCtlPower = eventSecCtlPowerService.findByDeviceId(deviceId);

		if(dbEventSecCtlPower == null){
			eventSecCtlPower.setId(IdGen.snowflakeId());
			eventSecCtlPower.setRegionNo(device.getRegionCode());
			eventSecCtlPower.setApplicationCode(device.getApplicationCode());
			eventSecCtlPower.setTenantCode(device.getTenantCode());
			eventSecCtlPower.setStoreTime(new Date());
			eventSecCtlPower.setNewRecord(true);
			eventSecCtlPowerService.insert(eventSecCtlPower);
		}else{
			BeanUtil.copyProperties(eventSecCtlPower, dbEventSecCtlPower, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerService.update(dbEventSecCtlPower);
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param deviceSecCtlPowerVol
	 */
	public void eventSecCtlPowerVolProcess(DeviceSecCtlPowerVol deviceSecCtlPowerVol){
		// ??????????????????
		if (deviceSecCtlPowerVol == null || deviceSecCtlPowerVol.getDevice() == null) {
			log.info("deviceSecCtlPowerVol??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerVol.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerVol.getDevice().getId());
			return;
		}
		EventSecCtlPowerOutput eventSecCtlPowerOutput = deviceEventBuilder.buildSecCtlPowerOutput(deviceSecCtlPowerVol);
		EventSecCtlPowerOutput dbEventSecCtlPowerOutput = eventSecCtlPowerOutputService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerOutput == null){
			eventSecCtlPowerOutput.setId(IdGen.snowflakeId());
			eventSecCtlPowerOutput.setRegionNo(device.getRegionCode());
			eventSecCtlPowerOutput.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerOutput.setTenantCode(device.getTenantCode());
			eventSecCtlPowerOutput.setStoreTime(new Date());
			eventSecCtlPowerOutput.setNewRecord(true);
			eventSecCtlPowerOutputService.insert(eventSecCtlPowerOutput);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerOutput, dbEventSecCtlPowerOutput, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerOutputService.update(dbEventSecCtlPowerOutput);
		}
	}

	/**
	 * ???????????????????????????????????????
	 * @param deviceSecCtlPowerRate
	 */
	public void eventSecCtlPowerRateProcess(DeviceSecCtlPowerRate deviceSecCtlPowerRate){
		// ??????????????????
		if (deviceSecCtlPowerRate == null || deviceSecCtlPowerRate.getDevice() == null) {
			log.info("deviceSecCtlPowerRate??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerRate.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerRate.getDevice().getId());
			return;
		}
		EventSecCtlPowerOutput eventSecCtlPowerOutput = deviceEventBuilder.buildSecCtlPowerOutput(deviceSecCtlPowerRate);
		EventSecCtlPowerOutput dbEventSecCtlPowerOutput = eventSecCtlPowerOutputService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerOutput == null){
			eventSecCtlPowerOutput.setId(IdGen.snowflakeId());
			eventSecCtlPowerOutput.setRegionNo(device.getRegionCode());
			eventSecCtlPowerOutput.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerOutput.setTenantCode(device.getTenantCode());
			eventSecCtlPowerOutput.setStoreTime(new Date());
			eventSecCtlPowerOutput.setNewRecord(true);
			eventSecCtlPowerOutputService.insert(eventSecCtlPowerOutput);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerOutput, dbEventSecCtlPowerOutput, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerOutputService.update(dbEventSecCtlPowerOutput);
		}
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param deviceSecCtlPowerElectric
	 */
	public void eventSecCtlPowerElectricProcess(DeviceSecCtlPowerElectric deviceSecCtlPowerElectric){
		// ??????????????????
		if (deviceSecCtlPowerElectric == null || deviceSecCtlPowerElectric.getDevice() == null) {
			log.info("deviceSecCtlPowerElectric??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerElectric.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerElectric.getDevice().getId());
			return;
		}
		EventSecCtlPowerOutput eventSecCtlPowerOutput = deviceEventBuilder.buildSecCtlPowerOutput(deviceSecCtlPowerElectric);
		EventSecCtlPowerOutput dbEventSecCtlPowerOutput = eventSecCtlPowerOutputService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerOutput == null){
			eventSecCtlPowerOutput.setId(IdGen.snowflakeId());
			eventSecCtlPowerOutput.setRegionNo(device.getRegionCode());
			eventSecCtlPowerOutput.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerOutput.setTenantCode(device.getTenantCode());
			eventSecCtlPowerOutput.setStoreTime(new Date());
			eventSecCtlPowerOutput.setNewRecord(true);
			eventSecCtlPowerOutputService.insert(eventSecCtlPowerOutput);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerOutput, dbEventSecCtlPowerOutput, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerOutputService.update(dbEventSecCtlPowerOutput);
		}
	}

	/**
	 * ?????????????????????????????????
	 * @param deviceSecCtlPowerOverVol
	 */
	public void eventSecCtlPowerOverVolProcess(DeviceSecCtlPowerOverVol deviceSecCtlPowerOverVol){
		// ??????????????????
		if (deviceSecCtlPowerOverVol == null || deviceSecCtlPowerOverVol.getDevice() == null) {
			log.info("deviceSecCtlPowerOverVol??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerOverVol.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerOverVol.getDevice().getId());
			return;
		}
		EventSecCtlPowerSet eventSecCtlPowerSet = deviceEventBuilder.buildSecCtlPowerSet(deviceSecCtlPowerOverVol);
		EventSecCtlPowerSet dbEventSecCtlPowerSet = eventSecCtlPowerSetService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerSet == null){
			eventSecCtlPowerSet.setId(IdGen.snowflakeId());
			eventSecCtlPowerSet.setRegionNo(device.getRegionCode());
			eventSecCtlPowerSet.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerSet.setTenantCode(device.getTenantCode());
			eventSecCtlPowerSet.setStoreTime(new Date());
			eventSecCtlPowerSet.setNewRecord(true);
			eventSecCtlPowerSetService.insert(eventSecCtlPowerSet);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerSet, dbEventSecCtlPowerSet, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerSetService.update(dbEventSecCtlPowerSet);
		}
	}

	/**
	 * ?????????????????????????????????
	 * @param deviceSecCtlPowerUnderVol
	 */
	public void eventSecCtlPowerUnderVolProcess(DeviceSecCtlPowerUnderVol deviceSecCtlPowerUnderVol){
		// ??????????????????
		if (deviceSecCtlPowerUnderVol == null || deviceSecCtlPowerUnderVol.getDevice() == null) {
			log.info("deviceSecCtlPowerUnderVol??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerUnderVol.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerUnderVol.getDevice().getId());
			return;
		}
		EventSecCtlPowerSet eventSecCtlPowerSet = deviceEventBuilder.buildSecCtlPowerSet(deviceSecCtlPowerUnderVol);
		EventSecCtlPowerSet dbEventSecCtlPowerSet = eventSecCtlPowerSetService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerSet == null){
			eventSecCtlPowerSet.setId(IdGen.snowflakeId());
			eventSecCtlPowerSet.setRegionNo(device.getRegionCode());
			eventSecCtlPowerSet.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerSet.setTenantCode(device.getTenantCode());
			eventSecCtlPowerSet.setStoreTime(new Date());
			eventSecCtlPowerSet.setNewRecord(true);
			eventSecCtlPowerSetService.insert(eventSecCtlPowerSet);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerSet, dbEventSecCtlPowerSet, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerSetService.update(dbEventSecCtlPowerSet);
		}
	}

	/**
	 * ?????????????????????????????????
	 * @param deviceSecCtlPowerOverElec
	 */
	public void eventSecCtlPowerOverElecProcess(DeviceSecCtlPowerOverElec deviceSecCtlPowerOverElec){
		// ??????????????????
		if (deviceSecCtlPowerOverElec == null || deviceSecCtlPowerOverElec.getDevice() == null) {
			log.info("deviceSecCtlPowerOverElec??????????????????");
			return;
		}
		String deviceId = deviceSecCtlPowerOverElec.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceSecCtlPowerOverElec.getDevice().getId());
			return;
		}
		EventSecCtlPowerSet eventSecCtlPowerSet = deviceEventBuilder.buildSecCtlPowerSet(deviceSecCtlPowerOverElec);
		EventSecCtlPowerSet dbEventSecCtlPowerSet = eventSecCtlPowerSetService.findByDeviceId(deviceId);

		if(dbEventSecCtlPowerSet == null){
			eventSecCtlPowerSet.setId(IdGen.snowflakeId());
			eventSecCtlPowerSet.setRegionNo(device.getRegionCode());
			eventSecCtlPowerSet.setApplicationCode(device.getApplicationCode());
			eventSecCtlPowerSet.setTenantCode(device.getTenantCode());
			eventSecCtlPowerSet.setStoreTime(new Date());
			eventSecCtlPowerSet.setNewRecord(true);
			eventSecCtlPowerSetService.insert(eventSecCtlPowerSet);
		}else{
			BeanUtil.copyProperties(eventSecCtlPowerSet, dbEventSecCtlPowerSet, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventSecCtlPowerSetService.update(dbEventSecCtlPowerSet);
		}
	}

	/**
	 * ??????????????????????????????????????????
	 * @param device
	 * @param alarmType
	 * @param eventTableCode
	 */
	public void specialAlarmTypeUpdateEventDataExt(Device device, AlarmType alarmType, String eventTableCode) throws Exception{
		if(EventTableEnum.ALARM.getTableName().equals(eventTableCode) &&
				AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind()) &&
				"ViolentVibrationValue".equals(alarmType.getStatusName())){	//??????????????????
			EventDataExt eventDataExt = eventDataExtService.findByDeviceId(device.getId());
			if(eventDataExt == null){
				eventDataExt = new EventDataExt();
				eventDataExt.setDeviceId(device.getId());
				eventDataExt.setData("{}");
				eventDataExt.setCommonValue_meta("admin", SysUtil.getSysCode(), device.getTenantCode(), device.getProjectId());
			}
			JSONObject jsonObject = JSON.parseObject(eventDataExt.getData());
			Long vibrationNum = jsonObject.getLong("ViolentVibrationValue");
			if(vibrationNum == null){
				vibrationNum = 1L;
			}else{
				vibrationNum = vibrationNum + 1;
			}
			jsonObject.put("ViolentVibrationValue", vibrationNum);
			eventDataExt.setData(jsonObject.toJSONString());
			eventDataExtService.save(eventDataExt);
		}
	}

	/**
	 * ??????????????????
	 * @param deviceEventPosture
	 */
	public void eventPostureProcess(DeviceEventPosture deviceEventPosture){
		// ??????????????????
		if (deviceEventPosture == null || deviceEventPosture.getDevice() == null) {
			log.info("deviceEventPosture??????????????????");
			return;
		}
		String deviceId = deviceEventPosture.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventPosture.getDevice().getId());
			return;
		}
		EventPosture eventPosture = deviceEventBuilder.buildEventPosture(deviceEventPosture);
		EventPosture dbEventPosture = eventPostureService.findByDeviceId(deviceId);

		if(dbEventPosture == null){
			eventPosture.setId(IdGen.snowflakeId());
			eventPosture.setRegionNo(device.getRegionCode());
			eventPosture.setApplicationCode(device.getApplicationCode());
			eventPosture.setTenantCode(device.getTenantCode());
			eventPosture.setStoreTime(new Date());
			eventPosture.setNewRecord(true);
			eventPostureService.insert(eventPosture);
		}else{
			BeanUtil.copyProperties(eventPosture, dbEventPosture, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventPostureService.update(dbEventPosture);
		}
	}

	/**
	 * ????????????-????????????
	 * @param deviceEventInputStatus
	 */
	public void eventInputProcess(DeviceEventInputStatus deviceEventInputStatus){
		// ??????????????????
		if (deviceEventInputStatus == null || deviceEventInputStatus.getDevice() == null) {
			log.info("deviceEventInputStatus??????????????????");
			return;
		}
		String deviceId = deviceEventInputStatus.getDevice().getId();
		Device device = deviceService.findById(deviceId);
		if (device == null) {
			log.info("?????????????????????id[{}]", deviceEventInputStatus.getDevice().getId());
			return;
		}
		EventInput eventInput = deviceEventBuilder.buildEventInput(deviceEventInputStatus);
		EventInput eventInput_db = eventInputService.findByDeviceId(deviceId);

		if(eventInput_db == null){
			eventInput.setId(IdGen.snowflakeId());
			eventInput.setRegionNo(device.getRegionCode());
			eventInput.setApplicationCode(device.getApplicationCode());
			eventInput.setTenantCode(device.getTenantCode());
			eventInput.setStoreTime(new Date());
			eventInput.setNewRecord(true);
			eventInputService.insert(eventInput);
		}else{
			BeanUtil.copyProperties(eventInput, eventInput_db, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
			eventInputService.update(eventInput_db);
		}
	}
}
