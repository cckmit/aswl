/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.controller;

import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.modules.sys.service.SysLogService;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 系统日志
 *
 * @author admin@gzaswl.net
 */
//@Controller
@RestController
@RequestMapping("/sys/log")
@Api("系统日志")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("sys:log:list")
	@ApiOperation("列表")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		return R.ok().put("page", page);
	}

	//将现在所有日志都隐藏掉

	/**
	 * 删除所有日志
	 */
	@SysLog("删除所有日志")
	@PostMapping("/deleteAll")
	@RequiresPermissions("sys:log:list")
	@ApiOperation("删除所有日志")
	public R deleteAll(){
		sysLogService.deleteAll();
		return R.ok("数据已全部清除");
	}

	/**
	 * 删除系统日志
	 */
	@SysLog("删除系统日志")
	@PostMapping("/delete")
	@RequiresPermissions("sys:log:list")
	@ApiOperation("删除系统日志")
	public R delete(@RequestBody String[] ids){
		sysLogService.deleteEntityByIds(ids);
		return R.ok();
	}

}
