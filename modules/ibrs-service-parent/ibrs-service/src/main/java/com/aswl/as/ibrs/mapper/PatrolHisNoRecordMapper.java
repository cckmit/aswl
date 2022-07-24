package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.module.PatrolHisNoRecord;
import com.aswl.as.ibrs.api.vo.PatrolHisNoRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 未巡更Mapper
 */
@Mapper
public interface PatrolHisNoRecordMapper extends CrudMapper<PatrolHisNoRecord> {

    /**
     * 查询未巡更记录
     * @param patrolKeyId
     * @param periodBeginTime
     * @param periodEndTime
     * @param tables
     * @return
     */
    List<PatrolHisNoRecord> findByPatrolKeyIdAndPeriod(@Param("regionCode")String regionCode,@Param("patrolKeyId") String patrolKeyId,
                                                       @Param("periodBeginTime") String periodBeginTime,
                                                       @Param("periodEndTime") String periodEndTime, @Param("tables") List<String> tables);

    /**
     * 批量新增未巡更记录
     * @param patrolHisNoRecordList
     */
    void batchInsert(@Param("patrolHisNoRecordList") List<PatrolHisNoRecord> patrolHisNoRecordList,
                     @Param("noRecordHisTable") String noRecordHisTable);

    /**
     * 未巡更列表
     * @return
     */
    List<PatrolHisNoRecordVo> findByPage(EventPatrolDto eventPatrolDto);

    /**
     * 上个月有多少未巡更记录
     * @param tableName
     * @param previousMonthStart
     * @param previousMonthEnd
     * @return
     */
    List<PatrolHisNoRecordVo> getNoRecordExamineTime(@Param("tableName") String tableName, @Param("previousMonthStart") String previousMonthStart,
                                                     @Param("previousMonthEnd") String previousMonthEnd,@Param("regionCode") String regionCode,
                                                        @Param("tenantCode") String tenantCode,@Param("projectId") String projectId);
}
