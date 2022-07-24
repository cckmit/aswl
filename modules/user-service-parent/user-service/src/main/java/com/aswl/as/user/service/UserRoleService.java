package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.user.api.module.UserRole;
import com.aswl.as.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aswl.com
 * @date 2018/8/26 14:55
 */
@AllArgsConstructor
@Service
public class UserRoleService extends CrudService<UserRoleMapper, UserRole> {

    private final UserRoleMapper userRoleMapper;

    /**
     * 根据用户ID查询
     *
     * @param userId 用户ID
     * @return List
     */
    public List<UserRole> getByUserId(String userId) {
        return userRoleMapper.getByUserId(userId);
    }

    /**
     * 根据用户ID查询
     *
     * @param userIds 用户ID
     * @return List
     */
    public List<UserRole> getByUserIds(List<String> userIds) {
        return userRoleMapper.getByUserIds(userIds);
    }

}
