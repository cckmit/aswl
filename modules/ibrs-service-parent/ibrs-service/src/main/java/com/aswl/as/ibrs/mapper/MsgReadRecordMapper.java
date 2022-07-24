package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.MsgReadRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 消息阅读表Mapper
 *
 * @author df
 * @date 2021/12/08 10:08
 */

@Mapper
public interface MsgReadRecordMapper extends CrudMapper<MsgReadRecord> {
    

    /**
     * 根据元数据ID查询
     * @param eventId
     * @return
     */
    MsgReadRecord findByEventId(@Param("eventId") String eventId);

    /**
     * 根据设备ID 和用户ID更新
     * @param eventId
     * @param userId
     * @return int
     */
    int updateByEventId(@Param("eventId") String eventId,@Param("userId") String userId);
    
}
