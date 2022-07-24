/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.controller;

import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.utils.RedisUtils;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.asos.modules.sys.form.SysLoginForm;
import com.aswl.as.asos.modules.sys.service.SysCaptchaService;
import com.aswl.as.asos.modules.sys.service.SysUserService;
import com.aswl.as.asos.modules.sys.service.SysUserTokenService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 *
 * @author admin@gzaswl.net
 */
//@Slf4j
@RestController
@RequestMapping("/sys/login")
@Api("系统登录相关")
@AllArgsConstructor
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	@Autowired
	RedisUtils redisUtils;

	@Autowired
	@Qualifier(value = "redisTemplateJson")
	private RedisTemplate redisTemplate;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	@ApiOperation("验证码")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	@ApiOperation("登录")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(form.getUsername());

		//账号不存在
		if(user == null)
		{
			return R.error("账号不存在");
		}

		//密码错误
		if( !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}

	/**
	 * 测试接口
	 */
	@PostMapping("/test")
	@ApiOperation("测试接口")
	public R test() {


//		redisTemplate.opsForValue().set("temp11111fk","123456",5, TimeUnit.SECONDS);
//		redisUtils.
		return R.ok();
	}

//	@PostMapping("/test1")
//	@ApiOperation("测试接口")
//	public R test1() {
//
//		System.out.println("redis获得的temp11111fk值为====="+redisTemplate.opsForValue().get("temp11111fk"));
//
//		return R.ok();
//	}

	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	@ApiOperation("退出")
	public R logout() {
		SysUserEntity user=getUser();
		if(user!=null)
		{
			sysUserTokenService.logout(user.getUserId());
		}
//		sysUserTokenService.logout();
		return R.ok();
	}
	
}
