package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.UserAuths;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserAuthsMapper
 *
 * @author aswl.com
 * @date 2019/07/03 11:44
 */
@Mapper
public interface UserAuthsMapper extends CrudMapper<UserAuths> {

    /**
     * 根据唯一标识查询
     *
     * @param userAuths userAuths
     * @return UserAuths
     * @author aswl.com
     * @date 2019/07/03 11:52:27
     */
    UserAuths getByIdentifier(UserAuths userAuths);

    /**
     * 根据用户id批量查询
     *
     * @param userIds userIds
     * @return List
     * @author aswl.com
     * @date 2019/07/03 22:02:13
     */
    List<UserAuths> getListByUserIds(@Param("userIds") String[] userIds);

    /**
     * 根据唯一标识删除
     *
     * @param userAuths userAuths
     * @return int
     * @author aswl.com
     * @date 2019/07/04 11:39:50
     */
    int deleteByIdentifier(UserAuths userAuths);


    /**
     * 根据用户ID删除
     *
     * @param userAuths userAuths
     * @return int
     * @author aswl.com
     * @date 2019/07/04 11:43:50
     */
    int deleteByUserId(UserAuths userAuths);


    /**
     * 根据用户ID查询
     *
     * @param userAuths userAuths
     * @return UserAuths
     */
    UserAuths getByUserId(UserAuths userAuths);


    /**
     * 根据用户ID更新
     *
     * @param userAuths userAuths
     * @return UserAuths
     */
    int updateByUserId(UserAuths userAuths);


    /**
     * 根据租户编码删除
     *
     * @param tenantCode
     * @return int
     * @author aswl.com
     */
    int deleteByTenantCode(String tenantCode);
}
