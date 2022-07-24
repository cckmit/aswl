package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.AlarmTypeUserFavorite;
import com.aswl.as.ibrs.api.vo.UserWatchVo;
import com.aswl.as.ibrs.mapper.AlarmTypeUserFavoriteMapper;
import com.aswl.as.ibrs.mapper.EventUcMetadataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeUserFavoriteService extends CrudService<AlarmTypeUserFavoriteMapper, AlarmTypeUserFavorite> {
    private final AlarmTypeUserFavoriteMapper alarmTypeUserFavoriteMapper;
    private final EventUcMetadataMapper metadataMapper;

    /**
     * 新增用户关注的系统报警类型
     *
     * @param alarmTypeUserFavorite
     * @return int
     */
    @Transactional
    public int insertAlarmTypeUserFavorite(AlarmTypeUserFavorite alarmTypeUserFavorite) {
        int update;
        String userId = alarmTypeUserFavorite.getUserId();
        List<UserWatchVo> list = alarmTypeUserFavoriteMapper.findUserWatchAlarmType(userId);
        if (list!= null && list.size() > 0 ){
            update = alarmTypeUserFavoriteMapper.updateAlarmTypeUserFavorite(alarmTypeUserFavorite);
        }else{
            alarmTypeUserFavorite.setId(IdGen.snowflakeId());
            update = super.insert(alarmTypeUserFavorite);
        }
        return update;
    }

    /**
     * 删除用户关注的系统报警类型
     *
     * @param alarmTypeUserFavorite
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmTypeUserFavorite alarmTypeUserFavorite) {
        return super.delete(alarmTypeUserFavorite);
    }

    /**
     * 获取用户的关注报警类型
     *
     * @param userId
     * @return
     */
    public List<UserWatchVo> findUserWatchAlarmType(String userId) {
        List<UserWatchVo> userWatchAlarmType = alarmTypeUserFavoriteMapper.findUserWatchAlarmType(userId);
        List<UserWatchVo> list = new ArrayList<>();
        if (userWatchAlarmType != null && userWatchAlarmType.size() > 0) {
            for (UserWatchVo userWatchVo : userWatchAlarmType) {
                UserWatchVo group = metadataMapper.findGroup(userWatchVo.getAlarmType());
                if (group != null) {
                    group.setId(userWatchVo.getId());
                    list.add(group);
                }
            }
            return list;
        } else {
            userWatchAlarmType = alarmTypeUserFavoriteMapper.findGlobalAlarmType();
            if (userWatchAlarmType != null && userWatchAlarmType.size() > 0) {
                for (UserWatchVo vo : userWatchAlarmType) {
                    UserWatchVo group = metadataMapper.findGroup(vo.getAlarmType());
                    if (group != null) {
                        // group.setId(vo.getId());
                        list.add(group);
                    }
                }
            }
            return list;
        }
       /* UserWatchVo userWatchVo = alarmTypeUserFavoriteMapper.findUserWatchAlarmType(userId);
        List<UserWatchVo> list = new ArrayList<>();
        if (userWatchVo != null) {
            String[] alarmTypes = userWatchVo.getAlarmType().split(",");
            for (String alarmType : alarmTypes) {
                UserWatchVo group = metadataMapper.findGroup(alarmType);
                if (group != null) {
                    group.setId(userWatchVo.getId());
                    list.add(group);
                }
            }
            return list;
        } else {
            userWatchVo = alarmTypeUserFavoriteMapper.findGlobalAlarmType();
            String[] alarmTypes = userWatchVo.getAlarmType().split(",");
            for (String alarmType : alarmTypes) {
                UserWatchVo group = metadataMapper.findGroup(alarmType);
                if (group != null) {
                    list.add(group);
                }
            }
            return list;
        }*/
    }

    @Transactional
    public int batchInsert(List<AlarmTypeUserFavorite> typeDeviceFavorites) {
        return alarmTypeUserFavoriteMapper.batchInsert(typeDeviceFavorites);
    }

    @Transactional
    public int batchUpdate(List<AlarmTypeUserFavorite> typeDeviceFavorites) {
        alarmTypeUserFavoriteMapper.deleteByUserId(typeDeviceFavorites.get(0).getUserId());
        return alarmTypeUserFavoriteMapper.batchInsert(typeDeviceFavorites);
    }

    public AlarmTypeUserFavorite findByUserId(String userId){
        return alarmTypeUserFavoriteMapper.findByUserId(userId);
    }
}