package com.aswl.as.auth.mapper;

import com.aswl.as.auth.api.module.OauthClientDetails;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Oauth2客户端mapper
 *
 * @author aswl.com
 * @date 2019/3/30 16:39
 */
@Mapper
public interface OauthClientDetailsMapper extends CrudMapper<OauthClientDetails> {
}
