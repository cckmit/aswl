package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventPatrol;
import com.aswl.as.metadata.api.module.EventHisPatrol;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EventHisPatrolMapper extends CrudMapper<EventHisPatrol> {

    void save(@Param("eventHisPatrol") EventHisPatrol eventHisPatrol, @Param("hisTableName") String hisTableName);
}
