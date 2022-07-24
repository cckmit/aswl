package com.aswl.as.common.security.filter;

import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.login.LoginContextHolder;
import com.aswl.as.common.security.project.ProjectContextHolder;
import com.aswl.as.common.security.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 获取请求头里的租户code
 *
 * @author aswl.com
 * @date 2019/5/28 22:53
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 获取请求头里的TENANT_CODE
        String tenantCode = request.getHeader(SecurityConstant.TENANT_CODE_HEADER);
        String projectId = request.getHeader(SecurityConstant.PROJECT_ID_HEADER);
        String isAdmin = request.getHeader(SecurityConstant.IS_ADMIN_HEADER);

        // 这里需要删除
//        if(!"/actuator/health".equals(request.getRequestURI()))
//        {
//            System.out.println("servletRequest地址为=="+request.getRequestURI()+" , 获得的头为 "+new JSONObject(request.getHeaderNames())+"，获得的tenantCode==="+tenantCode);
//        }

        // 没有携带tenantCode则采用默认的tenantCode
        if (tenantCode == null)
            tenantCode = SecurityConstant.DEFAULT_TENANT_CODE;

        TenantContextHolder.setTenantCode(tenantCode);
        ProjectContextHolder.setProjectId(projectId);
        LoginContextHolder.setIsAdmin(isAdmin);
        filterChain.doFilter(request, response);
        TenantContextHolder.clear();
        ProjectContextHolder.clear();
        LoginContextHolder.clear();
    }
}
