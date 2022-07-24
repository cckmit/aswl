package com.aswl.as.metadata.service;

import com.aswl.as.metadata.mapper.PatrolHisNoRecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 未巡更Service
 */
@Service
@Slf4j
@AllArgsConstructor
public class PatrolHisNoRecordService {
    private final PatrolHisNoRecordMapper patrolHisNoRecordMapper;

    /**
     * 删除未巡更记录
     * @param deviceId 设备ID
     * @param periodBegin 巡更开始时间
     * @param periodEnd 巡更结束时间
     * @param hisTables 历史表名
     */
    public void deleteByDeviceIdAndTime(String deviceId, String periodBegin, String periodEnd, List<String> hisTables) {

        patrolHisNoRecordMapper.deleteByDeviceIdAndTime(deviceId,periodBegin,periodEnd,hisTables);
    }
}
