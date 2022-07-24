package com.aswl.as.metadata.mapper;

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

    List<UserAuths> findUserList(@Param("userAuths") UserAuths userAuths);
}
