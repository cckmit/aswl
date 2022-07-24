/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.app.service;


import com.aswl.as.asos.modules.app.form.LoginForm;
import com.aswl.as.asos.modules.app.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户
 *
 * @author admin@gzaswl.net
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	long login(LoginForm form);
}
