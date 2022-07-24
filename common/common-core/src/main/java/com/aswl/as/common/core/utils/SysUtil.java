package com.aswl.as.common.core.utils;

import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.common.security.project.ProjectContextHolder;
import com.aswl.as.common.security.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

/**
 * 系统工具类
 *
 * @author aswl.com
 * @date 2018-09-13 20:50
 */
@Slf4j
public class SysUtil {

    private static final String KEY_ALGORITHM = "AES";

    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";

    /**
     * 获取当前登录的用户名
     *
     * @return String
     * @author aswl.com
     * @date 2019/03/17 11:46
     */
    public static String getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();
        if (principal instanceof Principal)
            return ((Principal) principal).getName();
        return String.valueOf(principal);
    }

    /**
     * 判断是否是用户管理员
     *
     * @return String
     * @author aswl.com
     * @date 2019/03/17 11:46
     */
    public static boolean isAdmin()
    {
        //return false;
        String user=SysUtil.getUser();
        if(StringUtils.isEmpty(user))
        {
            return false;
        }
        return user.equalsIgnoreCase("admin");
    }

    /**
     * 获取系统编号
     *
     * @return String
     */
    public static String getSysCode() {
        return SecurityConstant.SYS_CODE;
    }



    /**
     * 获取租户编号
     *
     * @return String
     */
    public static String getTenantCode() {
        String tenantCode = TenantContextHolder.getTenantCode();
        if (StringUtils.isBlank(tenantCode))
            tenantCode = getCurrentUserTenantCode();
        if (StringUtils.isBlank(tenantCode))
            tenantCode = SecurityConstant.DEFAULT_TENANT_CODE;
        log.debug("租户code：{}", tenantCode);
        return tenantCode;
    }

    /**
     * 获取项目ID （甚至加个校验，放到缓存里）
     *
     * @return String
     */
    public static String getProjectId() {
        String projectId = ProjectContextHolder.getProjectId();

        //||NULL_STRING.equals(projectId)

        // 如果是局域网并且为空的时候，设置为默认项目1 实际上直接return也行，先测试好
        if(!CloudCommon.isCloud() && StringUtils.isEmpty(projectId))
            projectId=SecurityConstant.DEFAULT_PROJECT_ID;

        log.debug("projectId：{}", projectId);

        if(StringUtils.isEmpty(projectId))
        {
            return null;
        }
        return projectId;
    }

    /**
     * 获取当前登录的租户code
     *
     * @return String
     */
    private static String getCurrentUserTenantCode() {
        String tenantCode = "";
        try {
            ResourceServerTokenServices resourceServerTokenServices = SpringContextHolder.getApplicationContext().getBean(ResourceServerTokenServices.class);
            if(SecurityContextHolder.getContext().getAuthentication() != null){
            Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) details;
                OAuth2AccessToken oAuth2AccessToken = resourceServerTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue());
                Object tenantObj = oAuth2AccessToken.getAdditionalInformation().get(SecurityConstant.TENANT_CODE);
                tenantCode = tenantObj == null ? "" : tenantObj.toString();
            } else if (details instanceof WebAuthenticationDetails) {
                // 未认证
                Object requestObj = RequestContextHolder.getRequestAttributes();
                if (requestObj != null) {
                    HttpServletRequest request = ((ServletRequestAttributes) requestObj).getRequest();
                    tenantCode = request.getParameter(SecurityConstant.TENANT_CODE);
                }
            }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return tenantCode;
    }


    /**
     * des解密
     *
     * @param data data
     * @param pass pass
     * @return String
     * @author aswl.com
     * @date 2019/03/18 11:39
     */
    public static String decryptAES(String data, String pass) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM), new IvParameterSpec(pass.getBytes()));
        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }
}
