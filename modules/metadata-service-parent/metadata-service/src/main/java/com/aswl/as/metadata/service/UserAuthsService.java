package com.aswl.as.metadata.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.datasource.annotation.DataSource;
import com.aswl.as.metadata.mapper.UserAuthsMapper;
import com.aswl.as.user.api.module.User;
import com.aswl.as.user.api.module.UserAuths;
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
    private final UserAuthsMapper userAuthsMapper;

    @DataSource(value = CommonConstant.DataBaseConstant.USER_DATABASE)
    public List<UserAuths> findUserList(UserAuths userAuths) {
        return userAuthsMapper.findList(userAuths);
    }
}
