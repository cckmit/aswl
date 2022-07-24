package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.ibrs.api.vo.UserWatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
*
* 用户关注的设备报警类型Mapper
* @author dingfei
* @date 2019-10-31 13:58
*/

@Mapper
public interface AlarmTypeDeviceFavoriteMapper extends CrudMapper<AlarmTypeDeviceFavorite> {

    UserWatchVo findUserWatchDeviceAlarmType(@Param("userId") String userId, @Param("deviceId") String deviceId);

    UserWatchVo findGlobalDeviceAlarmType();

    int batchInsert(@Param("alarmTypeDeviceFavorite") List<AlarmTypeDeviceFavorite> deviceFavorite);

    int batchUpdate(@Param("alarmTypeDeviceFavorite") List<AlarmTypeDeviceFavorite> alarmTypeDeviceFavorite);

    List<Map> findUserWatchDevices(@Param("userId") String userId);

    AlarmTypeDeviceFavorite getFavorite(@Param("userId") String userId, @Param("deviceId") String deviceId);

    List<AlarmTypeDeviceFavorite> getAlarmTypeDeviceFavoriteByUserId(@Param("userId") String userId);
}
