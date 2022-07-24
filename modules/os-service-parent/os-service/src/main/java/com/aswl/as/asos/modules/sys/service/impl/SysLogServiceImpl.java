/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.asos.modules.sys.entity.SysPostEntity;
import com.aswl.as.asos.modules.sys.service.SysLogService;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.modules.sys.dao.SysLogDao;
import com.aswl.as.asos.modules.sys.entity.SysLogEntity;
import com.aswl.as.common.core.constant.CommonConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String query = (String)params.get("query");
        String startTime = (String)params.get("startTime");
        String endTime = (String)params.get("startTime");

        IPage<SysLogEntity> page = this.page(
            new Query<SysLogEntity>().getPage(params),
            new QueryWrapper<SysLogEntity>()
                .eq("del_flag",CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(query),"operation", query)
                .gt(StringUtils.isNotBlank(startTime),"create_date",startTime)
                .lt(StringUtils.isNotBlank(endTime),"create_date",endTime)
        );

        return new PageUtils(page);
    }

    /**
     * 删除日志（隐藏）
     * @param ids
     * @return
     */
    public boolean deleteEntityByIds(String[] ids)
    {
        //设置为删除
        SysLogEntity entity=new SysLogEntity();
        entity.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return this.update(entity, new QueryWrapper<SysLogEntity>().in("id", ids));
    }

    /**
     * 清除全部（隐藏）
     * @return
     */
    public boolean deleteAll()
    {
        //设置为删除
        SysLogEntity entity=new SysLogEntity();
        entity.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return this.update(entity, new QueryWrapper<SysLogEntity>());
    }

}
