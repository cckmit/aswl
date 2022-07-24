package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.DeviceEventHisAlarm;
import com.aswl.as.metadata.api.vo.DeviceEventHisAlarmVO;
import com.aswl.as.metadata.mapper.DeviceEventHisAlarmMapper;
import com.aswl.as.metadata.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceEventHisAlarmService extends CrudService<DeviceEventHisAlarmMapper, DeviceEventHisAlarm> {
private final DeviceEventHisAlarmMapper deviceEventHisAlarmMapper;

    /**
    * 新增设备事件历史报警
    * @param  deviceEventHisAlarm
    * @return  int
    */
    @Transactional
    @Override
    public int insert(DeviceEventHisAlarm deviceEventHisAlarm) {
        return super.insert(deviceEventHisAlarm);
    }

    /**
     * 新增设备事件历史报警
     * @param  deviceEventHisAlarmVO
     * @return  int
     */
    @Transactional
    public int insert(DeviceEventHisAlarmVO deviceEventHisAlarmVO) {
        String yearMonth = DateUtils.formatDate(new Date(), "yyyyMM");
        deviceEventHisAlarmVO.setYearMonth(yearMonth);
        deviceEventHisAlarmVO.setCommonValue_meta("admin", deviceEventHisAlarmVO.getApplicationCode(), deviceEventHisAlarmVO.getTenantCode(),deviceEventHisAlarmVO.getProjectId());
        return deviceEventHisAlarmMapper.insert(deviceEventHisAlarmVO);
    }

    /**
    * 删除设备事件历史报警
    * @param deviceEventHisAlarm
    * @return int
    */
    @Transactional
    @Override
    public int delete(DeviceEventHisAlarm deviceEventHisAlarm) {
    return super.delete(deviceEventHisAlarm);
}
}