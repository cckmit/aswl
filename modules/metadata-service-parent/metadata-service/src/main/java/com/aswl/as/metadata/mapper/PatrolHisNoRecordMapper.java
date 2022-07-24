package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.PatrolHisNoRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 未巡更Mapper
 */
@Mapper
public interface PatrolHisNoRecordMapper extends CrudMapper<PatrolHisNoRecord> {

    /**
     * 删除未巡更记录
     * @param deviceId 设备ID
     * @param periodBegin 巡更开始时间
     * @param periodEnd 巡更结束时间
     * @param hisTables 历史表名
     */
    void deleteByDeviceIdAndTime(@Param("deviceId") String deviceId, @Param("periodBegin") String periodBegin,
                                 @Param("periodEnd") String periodEnd, @Param("hisTables") List<String> hisTables);
}
