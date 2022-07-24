/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.job.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.job.entity.ScheduleJobLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author admin@gzaswl.net
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
}
