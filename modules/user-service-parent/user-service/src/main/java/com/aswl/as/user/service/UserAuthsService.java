package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.user.api.module.User;
import com.aswl.as.user.api.module.UserAuths;
import com.aswl.as.user.mapper.UserAuthsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户授权Service
 *
 * @author aswl.com
 * @date 2019/07/03 11:45
 */
@AllArgsConstructor
@Slf4j
@Service
public class UserAuthsService extends CrudService<UserAuthsMapper, UserAuths> {

    /**
     * 根据唯一标识查询
     *
     * @param userAuths userAuths
     * @return UserAuths
     * @author aswl.com
     * @date 2019/07/03 11:52:27
     */
    public UserAuths getByIdentifier(UserAuths userAuths) {
        return this.dao.getByIdentifier(userAuths);
    }

    /**
     * 根据用户批量查询用户权限
     *
     * @param userList userList
     * @return List
     * @author aswl.com
     * @date 2019/07/03 21:58:31
     */
    public List<UserAuths> getListByUsers(List<User> userList) {
        return this.dao.getListByUserIds(userList.stream().map(User::getId).distinct().toArray(String[]::new));
    }

    /**
     * 根据唯一标识删除
     *
     * @param userAuths userAuths
     * @return int
     * @author aswl.com
     * @date 2019/07/04 11:39:50
     */
    @Transactional
    public int deleteByIdentifier(UserAuths userAuths) {
        return this.dao.deleteByIdentifier(userAuths);
    }

    /**
     * 根据用户ID删除
     *
     * @param userAuths userAuths
     * @return int
     * @author aswl.com
     * @date 2019/07/04 11:42:50
     */
    @Transactional
    public int deleteByUserId(UserAuths userAuths) {
        return this.dao.deleteByUserId(userAuths);
    }


    /**
     * 根据用户ID查询
     *
     * @param userAuths userAuths
     * @return UserAuths
     */
    public UserAuths getByUserId(UserAuths userAuths) {
        return this.dao.getByUserId(userAuths);
    }

    /**
     * 根据用户ID更新
     *
     * @param userAuths userAuths
     * @return UserAuths
     */
    int updateByUserId(UserAuths userAuths){
        return this.dao.updateByUserId(userAuths);
    }
}
