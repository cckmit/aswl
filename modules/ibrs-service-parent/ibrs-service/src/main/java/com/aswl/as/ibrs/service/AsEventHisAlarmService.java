package com.aswl.as.ibrs.service;


import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AsEventHisAlarm;
import com.aswl.as.ibrs.api.module.DeviceEventHisAlarm;
import com.aswl.as.ibrs.api.vo.DeviceEventHisAlarmVo;
import com.aswl.as.ibrs.mapper.AsEventHisAlarmMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Slf4j
@Service
public class AsEventHisAlarmService extends CrudService<AsEventHisAlarmMapper, DeviceEventHisAlarm> {
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
/*    public List<AsEventHisAlarm> findByParentId(){
        return asEventHisAlarmMapper.findAllList();
    }*/
/*    public Map findByParentId() {
        return asEventHisAlarmMapper.findByParentId();
    }*/

    public List<DeviceEventHisAlarmVo> finByinfoTable(List list){
        return asEventHisAlarmMapper.finByinfoTable(list);
    }

    public List<DeviceEventHisAlarmVo> finByalarmlevel(List list, DeviceAlarmDto deviceAlarmDto){
        return asEventHisAlarmMapper.finByalarmlevel(list,deviceAlarmDto);
    }




}
