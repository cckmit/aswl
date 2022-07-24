package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.user.api.module.BlackList;
import com.aswl.as.user.mapper.BlackListMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class BlackListService extends CrudService<BlackListMapper, BlackList> {
    private final BlackListMapper blackListMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 新增系统IP黑名单
     *
     * @param blackList
     * @return int
     */
    @Transactional
    @Override
    public int insert(BlackList blackList) {
        int rows = super.insert(blackList);
        if (rows > 0) {
            redisTemplate.opsForValue().set(blackList.getIp(),"blackList");
        }
        return rows;
    }

    /**
     * 修改系统黑名单
     *
     * @param blackList
     * @return
     */
    @Transactional
    @Override
    public int update(BlackList blackList) {
        BlackList old = blackListMapper.get(blackList);
        int rows = super.update(blackList);
        if (rows > 0) {
            BlackList update = blackListMapper.get(blackList);
            redisTemplate.delete(old.getIp());
            redisTemplate.opsForValue().set(update.getIp(),"blackList");
        }
        return rows;
    }

    /**
     * 删除系统IP黑名单
     *
     * @param blackList
     * @return int
     */
    @Transactional
    @Override
    public int delete(BlackList blackList) {
        int rows = super.delete(blackList);
        if (rows > 0) {
            BlackList list = blackListMapper.findIpById(blackList.getId());
            redisTemplate.delete(list.getIp());
        }
        return rows;
    }
}