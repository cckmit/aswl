/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service;


import com.aswl.as.asos.modules.sys.entity.SysMenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 菜单管理
 *
 * @author admin@gzaswl.net
 */
public interface SysMenuService extends IService<SysMenuEntity> {

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<SysMenuEntity> queryNotButtonList();
	
	/**
	 * 获取用户菜单列表
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 删除
	 */
	void delete(Long menuId);

	/**
	 * 获取所有菜单
	 * @param menuIdList
	 * @return
	 */
	public List<SysMenuEntity> getAllMenuListForEdit(List<Long> menuIdList);

}
