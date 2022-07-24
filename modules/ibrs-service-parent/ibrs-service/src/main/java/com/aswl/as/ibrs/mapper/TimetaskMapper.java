package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.Timetask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务Mapper
 *
 * @author dingfei
 * @date 2019-11-13 10:19
 */

@Mapper
public interface TimetaskMapper extends CrudMapper<Timetask> {

    int updateScheduler(Timetask timetask);

    int deleteScheduler(Timetask timetask);

    Timetask findById(String id);

}
