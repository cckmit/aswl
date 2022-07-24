package com.aswl.as.auth.service;

import com.aswl.as.auth.api.module.OauthClientDetails;
import com.aswl.as.auth.mapper.OauthClientDetailsMapper;
import com.aswl.as.common.core.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * Oauth2客户端Service
 *
 * @author aswl.com
 * @date 2019/3/30 16:48
 */
@Service
public class OauthClientDetailsService extends CrudService<OauthClientDetailsMapper, OauthClientDetails> {
}
