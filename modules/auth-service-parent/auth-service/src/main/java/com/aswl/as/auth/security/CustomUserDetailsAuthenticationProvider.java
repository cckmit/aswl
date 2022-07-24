package com.aswl.as.auth.security;

import com.aswl.as.common.core.exceptions.TenantNotFoundException;
import com.aswl.as.common.security.login.LoginContextHolder;
import com.aswl.as.common.security.tenant.TenantContextHolder;
import com.aswl.as.common.security.core.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 认证Provider，提供获取用户信息、认证、授权等功能
 *
 * @author aswl.com
 * @date 2019/5/28 21:26
 */
@Slf4j
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    private CustomUserDetailsService userDetailsService;

    private String userNotFoundEncodedPassword;

    public CustomUserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            log.debug("认证失败: 密码为空.");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码为空."));
        }
        // 获取密码
        String presentedPassword = authentication.getCredentials().toString();
        // 匹配密码
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            log.debug("认证失败: 密码错误.");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "密码错误."));
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        if (this.userDetailsService == null)
            throw new IllegalArgumentException("UserDetailsService不能为空.");
        this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }

    /**
     * 加载用户信息
     *
     * @param username       username
     * @param authentication authentication
     * @return UserDetails
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException, TenantNotFoundException{
        UserDetails loadedUser;
        try {
            // 登录改为不需要传tenantCode，因为登录时用户没传tenantCode过来
            // 加载用户信息
            loadedUser = this.userDetailsService.loadUserByIdentifierAndTenantCode(authentication.getPrincipal().toString(), TenantContextHolder.getTenantCode(), LoginContextHolder.getIsAdmin());
        } catch (TenantNotFoundException tenantNotFound) {
            throw tenantNotFound;
        } catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("获取用户信息失败.");
        }
        return loadedUser;
    }
}
