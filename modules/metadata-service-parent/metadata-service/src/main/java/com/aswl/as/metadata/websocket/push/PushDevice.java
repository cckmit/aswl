package com.aswl.as.metadata.websocket.push;

import java.io.Serializable;
import java.util.Date;

/**
 * 推送的设备信息消息体
 * @date
 *
 */
public class PushDevice implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String type;
	private String icon;
	private Date date;
	
	/** 设备所属区域名称	 */
	private String regionName;
	/** 设备的IP */
	private String ip;

	public PushDevice() {

	}

	public PushDevice(String id, String name, String type, String icon,
					  Date date, String regionName, String ip) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.icon = icon;
		this.date = date;
		this.regionName = regionName;
		this.ip = ip;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
