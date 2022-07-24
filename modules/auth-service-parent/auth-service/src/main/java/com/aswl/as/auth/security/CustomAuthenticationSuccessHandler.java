package com.aswl.as.auth.security;

import com.aswl.as.auth.model.CustomUserDetails;
import com.aswl.as.common.core.constant.ServiceConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.security.utils.SecurityUtil;
import com.aswl.as.user.api.dto.UserDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 监听登录成功事件，记录登录信息
 *
 * @author aswl.com
 * @date 2019-05-30 23:23
 */
@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler {

    private final UserServiceClient userServiceClient;
    private final HttpServletRequest request;

    @EventListener({AuthenticationSuccessEvent.class, InteractiveAuthenticationSuccessEvent.class})
    public void processAuthenticationSuccessEvent(AbstractAuthenticationEvent event) {
        // 注意：登录包括oauth2客户端、用户名密码登录都会触发AuthenticationSuccessEvent，这里只记录用户名密码登录的日志
        if (event.getSource() instanceof OAuth2Authentication)
            return;
        if (event.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication().getPrincipal();
            String tenantCode = userDetails.getTenantCode();
            String username = userDetails.getUsername();
            log.info("CustomAuthenticationSuccessHandler->登录成功, tenantCode: {}, username: {}", tenantCode, username);
            // 记录日志
            //获取浏览器信息
            String ua = request.getHeader("User-Agent");
            //转成UserAgent对象
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            //获取浏览器信息
            Browser browser = userAgent.getBrowser();
            //获取系统信息
            OperatingSystem os = userAgent.getOperatingSystem();
            //系统名称
            String system = os.getName();
            //浏览器名称
            String browserName = browser.getName();
            Log logInfo = new Log();
            logInfo.setCommonValue(username, SysUtil.getSysCode(), tenantCode);
            logInfo.setTitle("用户登录");
            logInfo.setMethod(HttpMethod.POST.name());
            logInfo.setTime(String.valueOf(System.currentTimeMillis() - userDetails.getStart()));
            logInfo.setType(BusinessType.LOGIN.getType());
            logInfo.setRequestUri(userDetails.getLoginType().getUri());
            // 获取ip、浏览器信息
            this.initLogInfo(logInfo, event.getSource());
            logInfo.setServiceId(ServiceConstant.AUTH_SERVICE);
            logInfo.setUserAgent(browserName + " / " + os);
            saveLoginSuccessLog(logInfo);
            //更新登录起始时间
            ResponseBean<UserVo> user = userServiceClient.findUserByIdentifier(username,tenantCode);
            if (user.getCode() ==ResponseBean.SUCCESS){
                String userId = user.getData().getUserId();
                UserDto  userDto = new UserDto();
                userDto.setId(userId);
                userDto.setOnlineDurationBegin((int)(System.currentTimeMillis()/1000));
                userServiceClient.updateInfo(userDto);
                
            }
            
        }
    }

    /**
     * 参考源码：
     * org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
     * org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
     *
     * @param logInfo logInfo
     * @param source  source
     */
    private void initLogInfo(Log logInfo, Object source) {
        if (source instanceof UsernamePasswordAuthenticationToken) {
            Object currentAuthentication = SecurityUtil.getCurrentAuthentication();
            if (currentAuthentication instanceof UsernamePasswordAuthenticationToken) {
                Object currentDetails = ((UsernamePasswordAuthenticationToken) currentAuthentication).getDetails();
                if (currentDetails instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) currentDetails;
                    logInfo.setIp(webAuthenticationDetails.getRemoteAddress());
                }
            }
        }
    }

    /**
     * 获取用户名
     *
     * @param authentication authentication
     * @return String
     */
    private String getUsername(Authentication authentication) {
        String username = "";
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof Principal) {
            username = ((Principal) principal).getName();
        }
        return username;
    }

    /**
     * 异步记录登录日志
     *
     * @author aswl.com
     * @date 2019/05/30 23:30
     */
    @Async
    public void saveLoginSuccessLog(Log logInfo) {
        try {
            userServiceClient.saveLog(logInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
