/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.oss.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.oss.entity.SysOssEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 文件上传
 *
 * @author admin@gzaswl.net
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
