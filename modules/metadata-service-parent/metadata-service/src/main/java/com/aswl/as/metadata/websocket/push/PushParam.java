package com.aswl.as.metadata.websocket.push;

import java.io.Serializable;

public class PushParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String nameEn;
	private String alias;
	private Integer priority;
	private Object value;
	private String icon;

	public PushParam() {
		super();
	}

	/**
	 * 
	 * @param name 名称 
	 * @param nameEn 名称(英文)
	 * @param alias	别名
	 * @param priority 优先级
	 * @param value 值
	 * @param icon	图标
	 * @param date 时间
	 */
	public PushParam(String name, String nameEn, String alias, Integer priority, Object value, String icon) {
		super();
		this.name = name;
		this.nameEn = nameEn;
		this.alias = alias;
		this.priority = priority;
		this.value = value;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}
