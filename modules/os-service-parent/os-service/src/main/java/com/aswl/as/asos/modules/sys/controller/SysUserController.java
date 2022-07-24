/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.controller;

import com.aswl.as.asos.common.validator.Assert;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.validator.group.UpdateGroup;
import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.aswl.as.asos.modules.sys.entity.SysRoleEntity;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.validator.group.AddGroup;
import com.aswl.as.asos.modules.sys.form.PasswordForm;
import com.aswl.as.asos.modules.sys.service.SysDeptService;
import com.aswl.as.asos.modules.sys.service.SysRoleService;
import com.aswl.as.asos.modules.sys.service.SysUserRoleService;
import com.aswl.as.asos.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author admin@gzaswl.net
 */
@RestController
@RequestMapping("/sys/user")
@Api("系统用户")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	@ApiOperation("所有用户列表")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	@ApiOperation("获取登录的用户信息")
	public R info(){
		return R.ok().put("user", getUser());
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	@ApiOperation("修改密码")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");

		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}

		return R.ok();
	}

	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	@ApiOperation("用户信息")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);

		if(user==null)
		{
			return R.error("没有该用户");
		}

		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		if(roleIdList!=null && roleIdList.size()>0)
		{
			SysRoleEntity role=sysRoleService.getById(roleIdList.get(0));
			if(role!=null)
			{
				user.setRoleName(role.getRoleName());
			}
		}

		// 查找用户的部门
		if(!StringUtils.isEmpty(user.getDeptId()))
		{
			SysDeptEntity dept=sysDeptService.getById(user.getDeptId());
			if(dept!=null)
			{
				user.setDeptName(dept.getDeptName());
			}
		}

		return R.ok().put("user", user);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	@ApiOperation("保存用户")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

		String reuslt=validateEntity(user);
		if(reuslt!=null)
		{
			return R.error(reuslt);
		}

		user.setCreateUserId(getUserId());
		user.setCreateTime(new Date());
		user.setStatus(1);
		sysUserService.saveUser(user);

		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	@ApiOperation("修改用户")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		String reuslt=validateEntity(user);
		if(reuslt!=null)
		{
			return R.error(reuslt);
		}

//		user.setCreateUserId(getUserId());
		user.setModifierUserId(getUserId());
		user.setModifyTime(new Date());
		sysUserService.update(user);

		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	@ApiOperation("删除用户")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}

		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);

		return R.ok();
	}

	private String validateEntity(SysUserEntity user)
	{
		//确认过，一个用户只能有一个角色
		if(user.getRoleIdList()!=null)
		{
			if(user.getRoleIdList().size()>1)
			{
				return "一个用户只能设置一个角色";
			}
		}
		return null;
	}

}
