package com.aswl.as.metadata.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import cc.eguid.commandManager.CommandManagerImpl;
import cc.eguid.commandManager.commandbuidler.CommandBuidlerFactory;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.enums.StreamType;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.metadata.api.dto.CollectDataDto;
import com.aswl.as.metadata.api.module.EventUcId;
import com.aswl.as.metadata.enums.MQExchange;
import com.aswl.as.metadata.service.*;
import com.aswl.as.metadata.websocket.push.PushEventData;
import com.aswl.iot.dto.DeviceCommand;
import com.aswl.iot.dto.constant.MQConstants;
import com.aswl.iot.dto.constant.OperationMoudle;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import com.aswl.as.common.core.enums.DeviceSettingsType;
import com.aswl.as.common.core.utils.SysUtil;

import lombok.extern.slf4j.Slf4j;
import rx.internal.schedulers.ExecutorScheduler;

@Slf4j
@Component
@EnableAsync
public class PicVedioHandler {

	@Autowired
	private OpenCameraService openCameraService;
	@Autowired
	private OpenDoorRecodeService openDoorRecodeService;
	@Autowired
	private DeviceSettingsService deviceSettingsService;
	@Autowired
	private RegionLeaderService regionLeaderService;

	@Autowired
	private ResPowerCallBackHandler resPowerCallBackHandler;
	/**
	 * 抓拍保存路径
	 */
	private static String bizPath = "files/openDoorRecord/";

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


	@Autowired
	private AlarmSettingsService alarmSettingsService;

	@Autowired
	private DeviceService deviceService;

	private static final String ALARM_CAPTURE = "files/alarmCapture/";

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private AlarmCaptureService alarmCaptureService;

	@Autowired
	@Qualifier(value = "rabbitTemplate")
	private AmqpTemplate amqpTemplate;

	@Autowired
	private EventEswitchService eswitchService;

	@Autowired
	private EventNetworkService networkService;
	/**
	 * 开箱记录
	 * 
	 * @param device
	 * @param alarmType
	 */
	@Async
	public void openCameraRecord(Device device, String alarmType) {
		String picPaths = null;
		String videoPaths = null;
		if (alarmType.equals("DoorExtPrompt0")) {
			List<OpenDoorRecode> list = openDoorRecodeService.findListByDeviceId(device.getId());
			if (list.size() > 0) {
				list.get(0).setCloseDoorTime(new Date());
				openDoorRecodeService.update(list.get(0));
			}

		} else {
			// 查询该设备是否布防 布防就抓拍或者录像 否则不操作摄像头
			DeviceSettings deviceSettings = deviceSettingsService.findDeviceSettings(device.getId(),
					DeviceSettingsType.DOOR_DEPLOY.getMode());
			if (deviceSettings != null) {
				if (Boolean.parseBoolean(isPic)
						&& deviceSettings.getMode().equals(DeviceSettingsType.DOOR_DEPLOY.getType())) {
					picPaths = openCameraRecordPic(device, alarmType);
				}
				if (Boolean.parseBoolean(isVedio)
						&& deviceSettings.getMode().equals(DeviceSettingsType.DOOR_DEPLOY.getType())) {
					videoPaths = openCameraRecordVedio(device, alarmType);
				}
			}

			OpenDoorRecode openDoorRecode = new OpenDoorRecode();
			openDoorRecode.setCommonValue_meta("admin", SysUtil.getSysCode(), "aswl", null);
			openDoorRecode.setDeviceId(device.getId());
			openDoorRecode.setAlarmType(alarmType);
			openDoorRecode.setOpenDoorTime(new Date());
			openDoorRecode.setPicPaths(picPaths);
			openDoorRecode.setVideoPath(videoPaths);
			openDoorRecode.setMaintainUserId(getMaintainUserId(device.getRegionCode()));
			openDoorRecode.setStoreTime(new Date());
			openDoorRecodeService.insert(openDoorRecode);
		}

	}

	/***
	 * 开箱抓拍截图
	 */
	public String openCameraRecordPic(Device device, String alarmType) {
		// 查询报障箱的摄像头
		String picPaths_jpg = null;
		OpenCamera openCamera = openCameraService.findOpenCameraByDeviceId(device.getId());
		if (openCamera != null) {
			Calendar date = Calendar.getInstance();
			String fileName_jpg = openCamera.getCameraIp() + "_" + System.currentTimeMillis() + ".jpg";
			String filePaths = filePath + bizPath + String.valueOf(date.get(Calendar.YEAR)) + "/"
					+ String.valueOf(date.get(Calendar.MONTH) + 1);
			picPaths_jpg = bizPath + String.valueOf(date.get(Calendar.YEAR)) + "/"
					+ String.valueOf(date.get(Calendar.MONTH) + 1) + "/" + fileName_jpg;
			isChartPathExist(filePaths);
			List<String> commend = new ArrayList<String>();
			String streamPath = "rtsp://" + openCamera.getCameraUserName() + ":" + openCamera.getCameraPwd() + "@"
					+ openCamera.getCameraIp() + ":554/ch1/main/av_stream";
			commend.add("ffmpeg");
			commend.add("-i");
			commend.add(streamPath);
			commend.add("-s");
			commend.add(resolution);
			commend.add("-b");
			commend.add("1M");
			commend.add("-y");
			commend.add("-f");
			commend.add("image2");
			commend.add("-an");
			commend.add("-loglevel");
			commend.add(time);
			commend.add(filePaths + "/" + fileName_jpg);
			Process p = null;
			try {
				ProcessBuilder builder = new ProcessBuilder(commend);
				builder.command(commend);
				p = builder.start();
				p.getOutputStream().close();
				int i = doWaitFor(p);
				if (i > 0) {
					if(p != null)
						p.destroy();
					return picPaths_jpg;
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(p != null)
					p.destroy();
			}
		}
		return picPaths_jpg;
	}

	/***
	 * 开箱录制
	 */
	public String openCameraRecordVedio(Device device, String alarmType) {
		// 查询报障箱的摄像头
		String picPaths_mp4 = null;
		OpenCamera openCamera = openCameraService.findOpenCameraByDeviceId(device.getId());
		if (openCamera != null) {
			Calendar date = Calendar.getInstance();
			String fileName = openCamera.getCameraIp() + "_" + System.currentTimeMillis();
			String fileName_mp4 = fileName + ".mp4";
			String filePaths = filePath + bizPath + date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH) + 1);
			isChartPathExist(filePaths);
			try {
				String streamPath = "rtsp://" + openCamera.getCameraUserName() + ":" + openCamera.getCameraPwd() + "@"
						+ openCamera.getCameraIp() + ":554/h265/ch1/main/av_stream";
				CommandManagerImpl manager = CommandManagerImpl.getInstance();

				manager.start(fileName, CommandBuidlerFactory.createBuidler()
						.add("ffmpeg")
						.add("-rtsp_transport", "tcp")
						.add("-i", streamPath)
						.add("-r", "25")
						.add("-s", resolution)
						.add("-ar", "48000")
//						.add("-vcodec", "copy")
						.add("-t", vedioTime/1000 + "")
						.add("-f", "mp4")
						.add(filePaths + "/" + fileName_mp4)
				);
				Thread.sleep(vedioTime + 15000);
				manager.stop(fileName);
				manager.destory();
				picPaths_mp4 = bizPath + date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + fileName_mp4;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return picPaths_mp4;
	}

	// 获取开箱截图并存储开箱图片
	public int doWaitFor(Process p) {
		InputStream in = null;
		InputStream err = null;
		int exitValue = -1;

		try {
			in = p.getInputStream();
			err = p.getErrorStream();
			boolean finished = false;

			while (!finished) {
				try {
					Character c;
					while (in.available() > 0) {
						c = new Character((char) in.read());
						System.out.print(c);
					}

					while (err.available() > 0) {
						c = new Character((char) err.read());
						System.out.print(c);
					}

					exitValue = p.exitValue();
					// Thread.sleep(3000L);
					finished = true;
				} catch (IllegalThreadStateException var19) {
					//Thread.currentThread();
					//Thread.sleep(500L);
					finished = true;
				}
			}
		} catch (Exception var20) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException var18) {
			}

			if (err != null) {
				try {
					err.close();
				} catch (IOException var17) {
				}
			}

		}

		return exitValue;
	}

//	/***
//	 * 开箱录制
//	 */
//	public String openCameraRecordVedio_old(Device device, String alarmType) {
//		// 查询报障箱的摄像头
//		String picPaths_mp4 = null;
//		OpenCamera openCamera = openCameraService.findOpenCameraByDeviceId(device.getId());
//		if (openCamera != null) {
//			Calendar date = Calendar.getInstance();
//			String fileName_mp4 = openCamera.getCameraIp() + "_" + System.currentTimeMillis() + ".mp4";
//			String filePaths = filePath + bizPath + String.valueOf(date.get(Calendar.YEAR)) + "/"
//					+ String.valueOf(date.get(Calendar.MONTH) + 1);
//			picPaths_mp4 = bizPath + String.valueOf(date.get(Calendar.YEAR)) + "/"
//					+ String.valueOf(date.get(Calendar.MONTH) + 1) + "/" + fileName_mp4;
//			isChartPathExist(filePaths);
//
//			try {
//				String streamPath = "rtsp://" + openCamera.getCameraUserName() + ":" + openCamera.getCameraPwd() + "@"
//						+ openCamera.getCameraIp() + ":554/ch1/main/av_stream";
//				int i = frameRecord(streamPath, filePaths + "/" + fileName_mp4, 1);
//				if (i > 0) {
//					return picPaths_mp4;
//				}
//			} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return picPaths_mp4;
//	}
//
//	/**
//	 * 按帧录制视频
//	 *
//	 * @param inputFile-该地址可以是网络直播/录播地址，也可以是远程/本地文件路径
//	 * @param outputFile                              -该地址只能是文件地址，如果使用该方法推送流媒体服务器会报错，原因是没有设置编码格式
//	 * @throws FrameGrabber.Exception
//	 * @throws FrameRecorder.Exception
//	 * @throws org.bytedeco.javacv.FrameRecorder.Exception
//	 */
//	public int frameRecord(String inputFile, String outputFile, int audioChannel)
//			throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
//
//		boolean isStart = true;// 该变量建议设置为全局控制变量，用于控制录制结束
//		// 获取视频源
//		FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
//		grabber.setOption("rtsp_transport", "tcp");
//		grabber.setOption("stimeout", vedioTime * 1000 + "");
//		grabber.setFrameRate(30);
//		grabber.setVideoBitrate(48000);
//		// 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
//		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720, audioChannel);
//		recorder.setFrameRate(30);
//		recorder.setVideoBitrate(48000);
//		return recordByFrame(grabber, recorder, isStart);
//	}
//
//	private int recordByFrame(FFmpegFrameGrabber grabber, FFmpegFrameRecorder recorder, Boolean status)
//			throws Exception, org.bytedeco.javacv.FrameRecorder.Exception {
//		try {// 建议在线程中使用该方法
//			grabber.start();
//			recorder.start();
//			Frame frame = null;
//			Long startTime = System.currentTimeMillis();
//			while (status && (frame = grabber.grabFrame()) != null) {
//				recorder.record(frame);
//				if (System.currentTimeMillis() - startTime >= vedioTime) {
//					status = false;
//					System.out.println("stop");
//				}
//			}
//			recorder.stop();
//			grabber.stop();
//			return 1;
//		} finally {
//			if (grabber != null) {
//				grabber.stop();
//				return 0;
//			}
//		}
//	}

	// 判断文件是否存在 不存在创建文件夹
	private static void isChartPathExist(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	// 查询区域负责人
	public String getMaintainUserId(String regionId) {
		// 查询区域负责人ID
		String maintainUserIds = null;
		List<RegionLeader> regionLeaders = getRegionLeaders(regionId);
		if (regionLeaders != null && regionLeaders.size() > 0) {
			maintainUserIds = regionLeaders.get(0).getId();
		} else {

		}
		return maintainUserIds;
	}

	// 递归获取区域负责人
	public List<RegionLeader> getRegionLeaders(String regionIds) {
		List<RegionLeader> regionLeaders_ = new ArrayList<RegionLeader>();
		if (regionIds != null) {
			for (int i = regionIds.length(); i >= 3; i = i - 3) {
				/*List<RegionLeader> regionLeaders_list = regionServiceClient
						.getRegionLeaderByRegionId(regionIds.substring(0, i));*/
				List<RegionLeader> regionLeaders_list =
						regionLeaderService.getRegionLeaderByRegionId(regionIds.substring(0, i));
				if (regionLeaders_list != null && regionLeaders_list.size() > 0) {
					regionLeaders_ = regionLeaders_list;
					break;
				}
			}
		}
		return regionLeaders_;
	}

	/**
	 * 告警抓拍
	 */
	@Async("getTaskExecutor")
	public void alarmCapture(Device parentDevice, AlarmType alarmType,EventUcId eventUcId) {
		String currentDate = FORMAT.format(new Date());
		String tenantCode = parentDevice.getTenantCode();
		String projectId = parentDevice.getProjectId();
		String deviceId = parentDevice.getId();
		//查询告警抓拍配置
		AlarmSettings settings = alarmSettingsService.getByTenantCode(projectId, tenantCode);
		if (settings != null) {
			String captureAlarmType = settings.getAlarmType();
			if (captureAlarmType != null && !"".equals(captureAlarmType)
					&& (captureAlarmType.contains(alarmType.getAlarmType()) || captureAlarmType.contains(alarmType.getStatusName().replaceAll("\\d+[^\\d+]*$", "")))) { //有配置抓拍类型并且包含此次的报警类型,要抓拍
				//获取当前设备的在线的下级摄像头
				List<Device> devices = deviceService.findChild(deviceId, DeviceKindType.CAMERA.getType());
				if (devices.size() > 0) {
					AlarmCapture alarmCapture = new AlarmCapture();
					for (Device childDevice : devices) {
						String picName = childDevice.getId() + "_" + childDevice.getIp() + "_" + System.currentTimeMillis() + ".jpg";
						String picPath = ALARM_CAPTURE + alarmType.getAlarmType() + "/" + currentDate;
						String fullPath = (filePath + picPath + "/" + picName);
						File file = new File(filePath + picPath);
						if(!file.exists()){
							file.mkdirs();
						}
                        List<String> commend = new ArrayList<String>();
						StringBuilder input = new StringBuilder("rtsp://" + childDevice.getUserName() + ":" + childDevice.getPassword() + "@" + childDevice.getIp() + ":" + (childDevice.getPort() == null ? "554" : childDevice.getPort()));
                        commend.add("ffmpeg");
                        commend.add("-ss");
                        commend.add("10");
                        commend.add("-rtsp_transport");
                        commend.add("tcp");
                        commend.add("-i");
                        if(childDevice.getDeviceType().toUpperCase().startsWith(StreamType.DH.getType())){
							input.append("/cam/realmonitor?channel=1&subtype=0");
						}else {
							input.append("/ch1/main/av_stream");
						}
						commend.add(input.toString());
						commend.add("-s");
                        commend.add("1024x768");
                        commend.add("-y");
                        commend.add("-f");
                        commend.add("image2");
                        commend.add("-vframes");
                        commend.add("1");
                        commend.add("-an");
                        commend.add("-loglevel");
                        commend.add("info");
                        commend.add(fullPath);

//						CommandManagerImpl manager = CommandManagerImpl.getInstance();
//						String result = manager.start(picName, ffmpeg);
//						manager.stop(picName);
//						manager.destory();
//                        Runtime r = Runtime.getRuntime();
//						Process p = null;
//						try {
//							p = r.exec(ffmpeg);
//							InputStream inputStream = p.getInputStream();
//							while (inputStream.read() != -1){
//
//							}
//							inputStream.close();
////							int i = doWaitFor(p);
////							if (i > 0) {
////								p.destroy();
////							}
//						} catch (Exception e) {
//							p.destroy();
//							e.printStackTrace();
//						}
                        Process p = null;
                        try {
                            ProcessBuilder builder = new ProcessBuilder();
                            builder.command(commend);
                            p = builder.start();
                            p.getOutputStream().close();
                            int i = doWaitFor(p);
                            if (i >= 0) {
                            	if(p != null)
                                	p.destroy();
                                log.info(i+" : doWaitFor");
                                //return picPaths_jpg;
                            }
                        } catch (Exception e) {
                        	e.printStackTrace();
                        	if(p != null)
                            	p.destroy();
                        }
						alarmCapture.setId(IdGen.snowflakeId());
						alarmCapture.setDeviceId(deviceId);
						alarmCapture.setAlarmType(alarmType.getAlarmType());
						alarmCapture.setAlarmTypeDes(alarmType.getAlarmTypeName());
						alarmCapture.setPicPath(picPath.replaceAll("/","\\\\"));
						alarmCapture.setPicName(picName);
						alarmCapture.setStoreTime(new Date());
						alarmCapture.setApplicationCode(SysUtil.getSysCode());
						alarmCapture.setTenantCode(tenantCode);
						alarmCapture.setUEventId(eventUcId.getId());
                        alarmCaptureService.insert(alarmCapture);
					}
				}
			}
		}
	}




	/**
	 * 外接设备离线,重启上级设备对应电源
	 * @param parentDevice
	 */
	//@Async("getTaskExecutor")
	public void resPower(Device parentDevice,Integer parentDcpowerNo, Device device, AlarmSettings settings, CollectDataDto collectDataDto, EventNetwork eventNetwork_new, EventUcId eventUcId, AlarmLevel alarmLevel_highest, PushEventData pushEventData,List<AlarmType> alarmTypeList) {
		//根据数据库动态设置的间隔时间,执行指定的次数,执行完指定次数后,停止任务
		Timer timer = new Timer();
		timer.schedule(new ResPower(timer,settings.getTimes(),amqpTemplate,parentDevice,parentDcpowerNo,device,resPowerCallBackHandler,collectDataDto,eventNetwork_new,eventUcId,alarmLevel_highest,pushEventData,alarmTypeList),new Date(),settings.getIntervalTime()*1000);
	}

	interface ResPowerCallBack{
		void callBack(Device device,CollectDataDto collectDataDto, EventNetwork eventNetwork_new, EventUcId eventUcId, AlarmLevel alarmLevel_highest, PushEventData pushEventData,List<AlarmType> alarmTypeList);
	}

	class ResPower extends TimerTask{

		private Timer timer;
		private int exeCount;
		private AmqpTemplate amqpTemplate;
		private Device parentDevice;
		private Integer parentDcpowerNo;
		private Device chilDevice;
		private ResPowerCallBack resPowerCallBack;
		private CollectDataDto collectDataDto;
		private EventNetwork eventNetworkNew;
		private EventUcId eventUcId;
		private AlarmLevel alarmLevelHighest;
		private PushEventData pushEventData;
		private List<AlarmType> alarmTypeList;
		ResPower() {
		}
		ResPower(Timer timer,int exeCount, AmqpTemplate amqpTemplate, Device parentDevice, Integer parentDcpowerNo, Device chilDevice, ResPowerCallBack resPowerCallBack,
				 CollectDataDto collectDataDto,EventNetwork eventNetworkNew,EventUcId eventUcId,AlarmLevel alarmLevelHighest,PushEventData pushEventData,List<AlarmType> alarmTypeList) {
			this.timer = timer;
			this.exeCount = exeCount;
			this.amqpTemplate = amqpTemplate;
			this.parentDevice = parentDevice;
			this.parentDcpowerNo = parentDcpowerNo;
			this.chilDevice = chilDevice;
			this.resPowerCallBack = resPowerCallBackHandler;
			this.collectDataDto = collectDataDto;
			this.eventNetworkNew = eventNetworkNew;
			this.eventUcId = eventUcId;
			this.alarmLevelHighest = alarmLevelHighest;
			this.pushEventData = pushEventData;
			this.alarmTypeList = alarmTypeList;
		}
		private int i = 1;
		@Override
		public void run() {
			EventNetwork network = networkService.findByDeviceId(chilDevice.getId());
			if(network.getNetworkState() == 1){  //外接设备上线了，终止重启
				timer.cancel();
				return;
			}
			if(i>exeCount){
				timer.cancel();
				resPowerCallBack.callBack(chilDevice, collectDataDto,  eventNetworkNew,  eventUcId,  alarmLevelHighest,  pushEventData, alarmTypeList);
				return;
			}
//			if(i == exeCount){
//				DeviceCommand command = new DeviceCommand();
//				com.aswl.iot.dto.Device iotDevice = new com.aswl.iot.dto.Device();
//				BeanUtil.copyProperties(parentDevice,iotDevice);
//				command.setDevice(iotDevice);
//				command.setOperationMoudle(OperationMoudle.POWER);
//				command.setOperationCodeA(3);
//				command.setOperationCodeB(parentDcpowerNo);
//				amqpTemplate.convertAndSend(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, JSONUtil.toJsonStr(command));
//				log.info("第:" + i + "次执行");
//				//timer.cancel();
//				//return;
//			}
			DeviceCommand command = new DeviceCommand();
			com.aswl.iot.dto.Device iotDevice = new com.aswl.iot.dto.Device();
			BeanUtil.copyProperties(parentDevice,iotDevice);
			command.setDevice(iotDevice);
			command.setOperationMoudle(OperationMoudle.POWER);
			command.setOperationCodeA(3);
			command.setOperationCodeB(parentDcpowerNo);
			amqpTemplate.convertAndSend(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, JSONUtil.toJsonStr(command));
			log.info("第:" + i + "次执行");
			i++;
		}
	}

	public static void main(String[] args) {

		Timer t = new Timer();
		t.schedule(new Test(t,3),new Date(),5*1000);
	}

	static class Test extends TimerTask{

		private Timer timer;
		private int exeCount;
		Test() {
		}
		Test(Timer timer, int exeCount) {
			this.timer = timer;
			this.exeCount = exeCount;
		}
		private int i = 1;
		@Override
		public void run() {
			if(i>exeCount){
				timer.cancel();
				return;
			}
			if(i == exeCount){
				log.info("第:" + i + "次执行");
				timer.cancel();
				return;
			}
			log.info("第:" + i + "次执行");
			i++;
		}
	}
}
