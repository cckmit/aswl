/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service.impl;


import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.sys.dao.SysMenuDao;
import com.aswl.as.asos.modules.sys.entity.SysMenuEntity;
import com.aswl.as.asos.modules.sys.service.SysMenuService;
import com.aswl.as.asos.modules.sys.service.SysRoleMenuService;
import com.aswl.as.asos.modules.sys.service.SysUserService;
import com.aswl.as.asos.common.utils.MapUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.removeById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.removeByMap(new MapUtils().put("menu_id", menuId));
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		
		return menuList;
	}

	public List<SysMenuEntity> getAllMenuListForEdit(List<Long> menuIdList)
	{
		List<SysMenuEntity> list=getAllMenuList(null);

		List<SysMenuEntity> bList=baseMapper.queryButtonList();

		for(SysMenuEntity b:bList)
		{
			for(SysMenuEntity temp:list)
			{
				setList(temp,b);
			}
		}
		return list;
	}

	private boolean setList(SysMenuEntity parent,SysMenuEntity b)
	{
		boolean isAdd=false;
		if (parent.getMenuId().equals(b.getParentId()))
		{
			if(parent.getList()==null)
			{
				parent.setList(new ArrayList<SysMenuEntity>());
			}
			((List<SysMenuEntity>)parent.getList()).add(b);
			return true;
		}

		if(parent.getList()!=null&& parent.getList().size()>0)
		{
			for(Object o:parent.getList())
			{
				SysMenuEntity temp=(SysMenuEntity)o;
				if(setList(temp,b))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}
}
