package com.aswl.as.metadata.websocket.push;

import java.io.Serializable;

public class PushLevel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String icon;
	private String alarmName;
	private String alarmNameEn;

	public PushLevel(String icon, String alarmName, String alarmNameEn) {
		super();
		this.icon = icon;
		this.alarmName = alarmName;
		this.alarmNameEn = alarmNameEn;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmNameEn() {
		return alarmNameEn;
	}

	public void setAlarmNameEn(String alarmNameEn) {
		this.alarmNameEn = alarmNameEn;
	}
}
