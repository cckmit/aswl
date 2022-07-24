package com.aswl.as.ibrs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.ibrs.api.vo.UserWatchVo;
import com.aswl.as.ibrs.mapper.AlarmTypeDeviceFavoriteMapper;
import com.aswl.as.ibrs.mapper.EventUcMetadataMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeDeviceFavoriteService extends CrudService<AlarmTypeDeviceFavoriteMapper, AlarmTypeDeviceFavorite> {
    private final AlarmTypeDeviceFavoriteMapper alarmTypeDeviceFavoriteMapper;
    private final EventUcMetadataMapper metadataMapper;

    /**
     * 新增用户关注的设备报警类型
     *
     * @param alarmTypeDeviceFavorite
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmTypeDeviceFavorite alarmTypeDeviceFavorite) {
        return super.insert(alarmTypeDeviceFavorite);
    }

    /**
     * 删除用户关注的设备报警类型
     *
     * @param alarmTypeDeviceFavorite
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmTypeDeviceFavorite alarmTypeDeviceFavorite) {
        return super.delete(alarmTypeDeviceFavorite);
    }

    /**
     * 获取用户的关注设备报警类型
     * @param userId
     * @param deviceId
     * @return
     */
    public List<UserWatchVo> findUserWatchDeviceAlarmType(String userId,String deviceId) {
       UserWatchVo userWatchVo = alarmTypeDeviceFavoriteMapper.findUserWatchDeviceAlarmType(userId,deviceId);
        List<UserWatchVo> list = new ArrayList<>();
        if (userWatchVo != null) {
            String[] alarmTypes = userWatchVo.getAlarmType().split(",");
            for (String alarmType : alarmTypes) {
                UserWatchVo group = metadataMapper.findGroup(alarmType);
                if(group == null)
                    continue;
                group.setId(userWatchVo.getId());
                list.add(group);
            }
            return list;
        } else {
            userWatchVo = alarmTypeDeviceFavoriteMapper.findGlobalDeviceAlarmType();
            if (userWatchVo == null) {
                return null;
            }
            String[] alarmTypes = userWatchVo.getAlarmType().split(",");
            for (String alarmType : alarmTypes) {
                UserWatchVo group = metadataMapper.findGroup(alarmType);
                if(group == null)
                    continue;
                list.add(group);
            }
            return list;
        }

    }

    @Transactional
    public int batchInsert(List<AlarmTypeDeviceFavorite> alarmTypeDeviceFavorite) {
        return alarmTypeDeviceFavoriteMapper.batchInsert(alarmTypeDeviceFavorite);
    }

    @Transactional
    public int batchUpdate(List<AlarmTypeDeviceFavorite> alarmTypeDeviceFavorite) {
        return alarmTypeDeviceFavoriteMapper.batchUpdate(alarmTypeDeviceFavorite);
    }

    public List<Map> findUserWatchDevices(String userId) {
       return alarmTypeDeviceFavoriteMapper.findUserWatchDevices(userId);
    }
    
    public UserWatchVo getAlarmTypeDeviceFavoriteBydeviceId(String userId,String deviceId) {
    	
        return alarmTypeDeviceFavoriteMapper.findUserWatchDeviceAlarmType(userId,deviceId);
     }

    public AlarmTypeDeviceFavorite getFavorite(String userId,String deviceId) {
        return alarmTypeDeviceFavoriteMapper.getFavorite(userId,deviceId);
    }
}