package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.MsgReadBusinessKeyEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.MsgReadRecordDto;
import com.aswl.as.ibrs.api.module.MsgReadRecord;
import com.aswl.as.ibrs.mapper.MsgReadRecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class MsgReadRecordService extends CrudService<MsgReadRecordMapper, MsgReadRecord> {
    private final MsgReadRecordMapper msgReadRecordMapper;

    /**
     * 新增消息阅读表
     *
     * @param msgReadRecordDto
     * @return int
     */
    @Transactional
    public int insert(MsgReadRecordDto msgReadRecordDto) {
        String eventId = msgReadRecordDto.getRelatedId();
        String userId = msgReadRecordDto.getUserId();
        MsgReadRecord msgReadRecord = msgReadRecordMapper.findByEventId(eventId);
        // 不存在添加
        if (msgReadRecord ==  null) {
            msgReadRecord  = new MsgReadRecord();
            msgReadRecord.setId(IdGen.snowflakeId());
            msgReadRecord.setBusinessKey(MsgReadBusinessKeyEnum.MSG_READ_ALARM.getCode());
            msgReadRecord.setRelatedId(eventId);
            msgReadRecord.setReadUserIds(userId);
            msgReadRecordMapper.insert(msgReadRecord);
        }else{
            //更新
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(userId);
            stringBuilder.append(",");
            stringBuilder.append(msgReadRecord.getReadUserIds());
            msgReadRecordMapper.updateByEventId(eventId,stringBuilder.toString());
        }
        return 1;
    }

    /**
     * 删除消息阅读表
     *
     * @param msgReadRecord
     * @return int
     */
    @Transactional
    @Override
    public int delete(MsgReadRecord msgReadRecord) {
        return msgReadRecordMapper.delete(msgReadRecord);
    }
}