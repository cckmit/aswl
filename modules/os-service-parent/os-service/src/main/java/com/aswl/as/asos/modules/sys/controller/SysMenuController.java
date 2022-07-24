/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.controller;

import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.modules.sys.entity.SysMenuEntity;
import com.aswl.as.asos.modules.sys.service.ShiroService;
import com.aswl.as.asos.modules.sys.service.SysMenuService;
import com.aswl.as.user.api.dto.MenuDto;
import com.aswl.as.user.api.module.Menu;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author admin@gzaswl.net
 */
@RestController
@RequestMapping("/sys/menu")
@Api("系统菜单")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	@ApiOperation("导航菜单")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());

		// 这里改一下，设置为类型是2，按钮的话，就隐藏掉那些菜单
		//系统管理员，拥有最高权限
		if(getUserId() == Constant.SUPER_ADMIN){
			removeButtonMenuList(menuList);
		}

		Set<String> permissions = shiroService.getUserPermissions(getUserId());

		// 封装成list传给前端
//		return R.ok().put("menuList", menuList).put("permissions", permissions).put("menuDtoList",getMenuDtoList(menuList));
		return R.ok().put("menuList", getMenuDtoList(menuList)).put("permissions", permissions).put("oldMenuList",menuList);
	}

	/**
	 * 根据菜单列表返回原来的格式的菜单
	 * @param list
	 * @return
	 */
	private List<MenuDto> getMenuDtoList(List<SysMenuEntity> list)
	{
		List<MenuDto> dtoList=new ArrayList<>();
		for(SysMenuEntity temp:list)
		{
			dtoList.add(getMenuDtoBySysMenu(temp,null));
		}
		return dtoList;
	}

	/**
	 * 设置菜单的列表
	 * @param m 对象
	 * @param p 对象的父对象
	 * @return
	 */
	private MenuDto getMenuDtoBySysMenu(SysMenuEntity m,MenuDto p)
	{
		Menu menu=new Menu();
		menu.setSort("0");
		MenuDto temp=new MenuDto(menu);

		temp.setId(String.valueOf(m.getMenuId()));
		temp.setParentId(String.valueOf(m.getParentId()));
		//temp.setParent(p);
		temp.setName(m.getName());
		temp.setUrl(m.getUrl());
		//m.getPerms();//TODO 这里可能需要添加一个权限
		temp.setType(String.valueOf(m.getType()));
		temp.setIcon(m.getIcon());
		temp.setSort(m.getOrderNum()==null?0:m.getOrderNum());
		temp.setSpread(m.getOpen()==null?false:m.getOpen());
		temp.setComponent(m.getComponent());

		if(m.getList()!=null && m.getList().size()>0)
		{
//			List<MenuDto> list=new ArrayList<>();
			//递归设置下面的菜单
			for(Object o:m.getList())
			{
				if(o!=null && o instanceof SysMenuEntity)
				{
					SysMenuEntity entity=(SysMenuEntity)o;
					//list.add(getMenuDtoBySysMenu(entity,temp));
					temp.getChildren().add(getMenuDtoBySysMenu(entity,temp));
				}
			}
		}

		return temp;
	}
	
	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	@ApiOperation("所有菜单列表")
	public List<SysMenuEntity> list(){
		List<SysMenuEntity> menuList = sysMenuService.list();
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
			}
		}

		return menuList;
	}

	@GetMapping("/getAllMenuList")
	@RequiresPermissions("sys:menu:list")
	@ApiOperation("获取所有菜单列表，用作菜单管理")
	public List<SysMenuEntity> getAllMenuList()
	{
		// 设置
		return sysMenuService.getAllMenuListForEdit(null);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
//	@GetMapping("/select")
//	@RequiresPermissions("sys:menu:select")
//	@ApiOperation("选择菜单(添加、修改菜单)")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
		
		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	
	/**
	 * 菜单信息
	 */
//	@GetMapping("/info/{menuId}")
//	@RequiresPermissions("sys:menu:info")
//	@ApiOperation("菜单信息")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@PostMapping("/save")
	@RequiresPermissions("sys:menu:save")
	@ApiOperation("保存菜单")
	public R save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
		
		sysMenuService.save(menu);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@PostMapping("/update")
	@RequiresPermissions("sys:menu:update")
	@ApiOperation("修改")
	public R update(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
				
		sysMenuService.updateById(menu);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete/{menuId}")
	@RequiresPermissions("sys:menu:delete")
	@ApiOperation("删除")
	public R delete(@PathVariable("menuId") long menuId){
		if(menuId <= 31){
			return R.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new RRException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RRException("菜单URL不能为空");
			}
		}
		
		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("上级菜单只能为目录类型");
			}
			return ;
		}
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new RRException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}

	//递归删除类型是2按钮的菜单
	private void removeButtonMenuList(List<SysMenuEntity> menuList)
	{
		//空就直接返回
		if(menuList==null)
			return ;

		List<SysMenuEntity> delList=new ArrayList<SysMenuEntity>();
		for(SysMenuEntity m:menuList)
		{
			//按钮
			if(m.getType() == Constant.MenuType.BUTTON.getValue() )
			{
				delList.add(m);
			}
			removeButtonMenuList((List<SysMenuEntity>)m.getList());
		}
		menuList.removeAll(delList);
	}

}
