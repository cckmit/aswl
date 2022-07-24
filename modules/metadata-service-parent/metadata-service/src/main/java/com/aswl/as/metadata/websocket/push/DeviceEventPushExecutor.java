package com.aswl.as.metadata.websocket.push;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.aswl.as.user.api.module.User;

@Component("deviceEventPushExecutor")
public class DeviceEventPushExecutor {

	private static final String ONLINESTATUS_DESTINATION = "/queue/onlineStatus";
	private static final String TROUBLEREPORT_DESTINATION = "/queue/troubleReport";
	private static final String DIALOGREPORT_DESTINATION = "/queue/dialogReport";
	private static final String TOPIC_COMMON_MSG_DESTINATION = "/topic/commonMsg";

	@Autowired
	private SimpMessagingTemplate template;

	public void pushTopicMsg(Object object) {
		try {
			template.convertAndSend(TOPIC_COMMON_MSG_DESTINATION, object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushOnlineStatus(List<User> users, PushDeviceNetwork pushDeviceNetwork) {
		try {
//			for (User user : users) {
//				if (isLogin(user)) {
//					template.convertAndSendToUser(user.getName(), ONLINESTATUS_DESTINATION, pushSimpleDeviceNetwork);
					template.convertAndSend(ONLINESTATUS_DESTINATION, pushDeviceNetwork);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushTroubleStatus(List<String> userNames, PushEventData pushEventData) {
		try {
			for (String userName : userNames) {
//				boolean userOnline = isLogin(user);
//				if (userOnline) {
					template.convertAndSendToUser(userName, TROUBLEREPORT_DESTINATION, pushEventData);
//					template.convertAndSend(TROUBLEREPORT_DESTINATION, pushEventData);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushMsg(String userName, PushEventData pushEventData) {
		try {

//				boolean userOnline = isLogin(user);
//				if (userOnline) {
				template.convertAndSendToUser(userName, TROUBLEREPORT_DESTINATION, pushEventData);
//					template.convertAndSend(TROUBLEREPORT_DESTINATION, pushEventData);
//				}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pushDialogStatus(List<User> users, PushEventData pushEventData) {
		try {
//			for (User user : users) {
//				boolean userOnline = isLogin(user);
//				if (userOnline) {
//					template.convertAndSend(user.getName(), DIALOGREPORT_DESTINATION, pushEventData);
					template.convertAndSend(DIALOGREPORT_DESTINATION, pushEventData);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isLogin(User user){
		if(user == null){
			return false;
		}

		//判断是否登录

		return true;
	}
}
