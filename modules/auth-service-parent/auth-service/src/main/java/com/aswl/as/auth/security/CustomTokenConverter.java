package com.aswl.as.auth.security;

import cn.hutool.json.JSONObject;
import com.aswl.as.auth.model.CustomUserDetails;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.LoginType;
import com.aswl.as.common.security.tenant.TenantContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展JwtAccessTokenConverter，增加租户code
 *
 * @author aswl.com
 * @date 2019/5/28 22:53
 */
public class CustomTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        String grantType = authentication.getOAuth2Request().getGrantType();
        // 加入tenantCode
        // 这里登陆的时候会调用，登陆的时候不知道tenantCode， TenantContextHolder.getTenantCode()如果头里面没有Tenant-Code的话
        //会自动取默认的aswl，所以一直返回aswl，现在如果authentication里面有，就用authentication里面的
        String tenantCode=null;
        JSONObject json=new JSONObject(authentication.getUserAuthentication().getPrincipal());
        if(json.containsKey("tenantCode"))
        {
            String tempTenantCode=json.getStr("tenantCode");
            if(!StringUtils.isEmpty(tempTenantCode))
            {
                tenantCode=tempTenantCode;
            }
        }
        if(tenantCode==null)
        {
            tenantCode=TenantContextHolder.getTenantCode();
        }

        additionalInfo.put("tenantCode", tenantCode);

        // 加入登录类型，用户名/手机号
        if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_PASSWORD)) {
            additionalInfo.put("loginType", LoginType.PWD.getType());
        } else if (grantType.equalsIgnoreCase(CommonConstant.GRANT_TYPE_MOBILE)) {
            additionalInfo.put("loginType", LoginType.SMS.getType());
        } else if (grantType.equalsIgnoreCase(LoginType.WECHAT.getType())) {
            additionalInfo.put("loginType", LoginType.WECHAT.getType());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return super.enhance(accessToken, authentication);
    }
}
