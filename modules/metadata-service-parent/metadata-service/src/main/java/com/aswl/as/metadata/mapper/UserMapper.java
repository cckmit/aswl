package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.user.api.module.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper接口
 *
 * @author aswl.com
 * @date 2018-08-25 15:27
 */
@Mapper
public interface UserMapper extends CrudMapper<User> {

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return Integer
     */
    Integer userCount(UserVo userVo);


    /**
     * 根据项目ID和租户获取用户
     * @return list
     */
    List<UserVo> findUsersByProjectId(@Param("projectId") String projectId, @Param("tenantCode") String tenantCode);


    /**
     * 获取发送APP通知的项目管理员/项目值班员
     * @param projectId
     * @return list
     */
    List<User> findSendNoticeUsers(String projectId);

    
}
