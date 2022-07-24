/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service.impl;

import cn.hutool.json.JSONObject;
import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.modules.sys.dao.SysUserDao;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.asos.modules.sys.service.SysRoleService;
import com.aswl.as.asos.modules.sys.service.SysUserRoleService;
import com.aswl.as.asos.modules.sys.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 系统用户
 *
 * @author admin@gzaswl.net
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		/*
		String username = (String)params.get("username");
		Long createUserId = (Long)params.get("createUserId");

		 */

		// 修改成用sql查出来的东西
		IPage<SysUserEntity> page=new Query<SysUserEntity>().getPage(params);

		Map<String, Object> temp=new HashMap<>(params);
		temp.put(Constant.PAGE,null);
		temp.put(Constant.LIMIT,null);
//		temp.put("deptId",params.get("deptId"));

		temp.put("deptCode",params.get("deptCode"));

//		String p=String.valueOf(params.get("params"));
//		if(StringUtils.isNotEmpty(p) && !"null".equals(p))
//		{
//			JSONObject json=new JSONObject(p);
//			if(json.containsKey("deptCode"))
//			{
//
//			}
//		}

		int totalCount=baseMapper.countUsers(temp);
		page.setTotal(totalCount);
		page.setPages((totalCount+page.getSize()-1)/page.getSize());
		if(totalCount<1)
		{
			page.setRecords(new ArrayList<>());
		}
		else
		{
			temp.put("limit1",(page.getCurrent()-1)*page.getSize());
			temp.put("limit2",page.getSize());

			List<SysUserEntity>list=baseMapper.queryUsers(temp);
			page.setRecords(list);
		}

		/*
		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.eq(createUserId != null,"create_user_id", createUserId)
		);
		 */


		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.save(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}

		this.updateById(user);
		
		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

	// 更新用户部门code
	public boolean updateDeptCode(String deptId,String deptCode)
	{
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setDeptCode(deptCode);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("dept_id", deptId));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}

	// 算分页
	/**
	 1)       limit分页公式
	 （1）limit分页公式：curPage是当前第几页；pageSize是一页多少条记录
	 limit (curPage-1)*pageSize,pageSize
	 （2）用的地方：sql语句中
	 select * from student limit(curPage-1)*pageSize,pageSize;

	 2)       总页数公式
	 （1）总页数公式：totalRecord是总记录数；pageSize是一页分多少条记录
	 int totalPageNum = (totalRecord +pageSize - 1) / pageSize;
	 （2）用的地方：前台UI分页插件显示分页码
	 （3）查询总条数：totalRecord是总记录数
	 SELECT COUNT(*) FROM tablename
	 */

}