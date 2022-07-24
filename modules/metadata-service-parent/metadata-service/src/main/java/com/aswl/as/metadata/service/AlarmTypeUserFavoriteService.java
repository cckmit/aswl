package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmTypeUserFavorite;
import com.aswl.as.metadata.mapper.AlarmTypeUserFavoriteMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeUserFavoriteService extends CrudService<AlarmTypeUserFavoriteMapper, AlarmTypeUserFavorite> {
    private final AlarmTypeUserFavoriteMapper alarmTypeUserFavoriteMapper;

    /**
     * 新增用户关注的系统报警类型
     *
     * @param alarmTypeUserFavorite
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmTypeUserFavorite alarmTypeUserFavorite) {
        return super.insert(alarmTypeUserFavorite);
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
     * 获取用户的关注报警类型数据
     *
     * @param userId
     * @return
     */
    public AlarmTypeUserFavorite findByUserId(String userId) {
        AlarmTypeUserFavorite alarmTypeUserFavorite = null;
        AlarmTypeUserFavorite search = new AlarmTypeUserFavorite();
        search.setUserId(userId);
        List<AlarmTypeUserFavorite> list = alarmTypeUserFavoriteMapper.findList(search);
        if(list != null && list.size() > 0){
            alarmTypeUserFavorite = list.get(0);
        }
        return alarmTypeUserFavorite;
    }

}