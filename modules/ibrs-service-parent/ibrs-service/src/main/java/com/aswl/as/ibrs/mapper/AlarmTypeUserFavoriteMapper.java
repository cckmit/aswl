package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmTypeUserFavorite;
import com.aswl.as.ibrs.api.vo.AlarmTypeUserFavoriteVo;
import com.aswl.as.ibrs.api.vo.UserWatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 用户关注的系统报警类型Mapper
* @author dingfei
* @date 2019-10-29 15:54
*/

@Mapper
public interface AlarmTypeUserFavoriteMapper extends CrudMapper<AlarmTypeUserFavorite> {

    /**
     * 查询用户关注的报警类型列表
     * @return
     */
    List<AlarmTypeUserFavoriteVo> findAlarmUserFavoriteTypeLists(@Param("userId") String userId);

    /**
     *
     * @param userId
     * @return
     */
    List<UserWatchVo> findUserWatchAlarmType(@Param("userId") String userId);

    List<UserWatchVo> findGlobalAlarmType();

    int batchInsert(@Param("AlarmTypeUserFavorites") List<AlarmTypeUserFavorite> AlarmTypeUserFavorites);

    int batchUpdate(@Param("AlarmTypeUserFavorites") List<AlarmTypeUserFavorite> AlarmTypeUserFavorites);

    int deleteByUserId(@Param("userId") String userId);
    
    int updateAlarmTypeUserFavorite(AlarmTypeUserFavorite alarmTypeUserFavorite);

    AlarmTypeUserFavorite findByUserId(@Param("userId") String userId);
}
