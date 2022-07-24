/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysConfigEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 系统配置信息
 *
 * @author admin@gzaswl.net
 */
public interface SysConfigService extends IService<SysConfigEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 保存配置信息
	 */
	public void saveConfig(SysConfigEntity config);
	
	/**
	 * 更新配置信息
	 */
	public void update(SysConfigEntity config);
	
	/**
	 * 根据key，更新value
	 */
	public void updateValueByKey(String key, String value);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] ids);
	
	/**
	 * 根据key，获取配置的value值
	 * 
	 * @param key           key
	 */
	public String getValue(String key);
	
	/**
	 * 根据key，获取value的Object对象
	 * @param key    key
	 * @param clazz  Object对象
	 */
	public <T> T getConfigObject(String key, Class<T> clazz);
	
}
