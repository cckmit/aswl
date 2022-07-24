/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysRoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author admin@gzaswl.net
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
