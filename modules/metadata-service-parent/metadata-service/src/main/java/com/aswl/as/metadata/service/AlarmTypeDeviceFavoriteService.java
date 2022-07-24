package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.metadata.mapper.AlarmTypeDeviceFavoriteMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeDeviceFavoriteService extends CrudService<AlarmTypeDeviceFavoriteMapper, AlarmTypeDeviceFavorite> {
    private final AlarmTypeDeviceFavoriteMapper alarmTypeDeviceFavoriteMapper;


    public AlarmTypeDeviceFavorite getFavorite(String userId,String deviceId) {
        return alarmTypeDeviceFavoriteMapper.getFavorite(userId,deviceId);
    }

    public List<AlarmTypeDeviceFavorite> getAlarmTypeDeviceFavoriteByUserId(String userId) {
        AlarmTypeDeviceFavorite entity = new AlarmTypeDeviceFavorite();
        entity.setUserId(userId);
        return alarmTypeDeviceFavoriteMapper.findList(entity);
    }
}