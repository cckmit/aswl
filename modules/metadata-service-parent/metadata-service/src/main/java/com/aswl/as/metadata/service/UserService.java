package com.aswl.as.metadata.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.datasource.annotation.DataSource;
import com.aswl.as.metadata.mapper.UserMapper;
import com.aswl.as.user.api.module.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户Service
 *
 * @author aswl.com
 * @date 2021/11/05 11:45
 */
@AllArgsConstructor
@Slf4j
@Service
@DataSource(value = CommonConstant.DataBaseConstant.USER_DATABASE)
public class UserService extends CrudService<UserMapper, User> {

    private  final UserMapper userMapper;

    public User get(String id){
        User user = new User();
        user.setId(id);
        return userMapper.get(user);
    }

    /**
     * 加载指定固定角色和项目的用户
     * @param sysRoles
     * @param projectIds
     * @return
     */
//    @DataSource(value = CommonConstant.DataBaseConstant.USER_DATABASE)
    public List<User> findBySysRolesAndProjectId(List<String> sysRoles, String projectIds){
        if(sysRoles == null || sysRoles.size() <= 0){
            return new ArrayList<>();
        }
        User userDto = new User();
        userDto.setProjectId(projectIds);
        userDto.setSysRole(String.join(",", sysRoles));
        return userMapper.findList(userDto);
    }

    /**
     * 加载指定项目的项目管理员、项目值班员用户
     * @param projectIds
     * @return
     */
//    @DataSource(value = CommonConstant.DataBaseConstant.USER_DATABASE)
    public List<User> findProjectUserByProjectId(String projectIds){
       List<String> sysRoles = new ArrayList<>();
       sysRoles.add(RoleEnum.ROLE_PROJECT_ADMIN.getType());
       sysRoles.add(RoleEnum.ROLE_PROJECT_WATCHER.getType());

       return this.findBySysRolesAndProjectId(sysRoles, projectIds);
    }

    /**
     * 加载指定项目ID集的用户
     * @param projectIds
     * @return
     */
    public List<User> findByProjectIds(String projectIds){
        User userDto = new User();
        userDto.setProjectId(projectIds);
        return userMapper.findList(userDto);
    }
}
