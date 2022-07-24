/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service;


import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * 系统日志
 *
 * @author admin@gzaswl.net
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public boolean deleteEntityByIds(String[] ids);

    public boolean deleteAll();

}
