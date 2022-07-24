package com.aswl.as.user.filter;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.RoleVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.user.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(2147483646)
public class RegionCodeFilter implements Filter{

    private final UserService userService;

    @Autowired
    public RegionCodeFilter(UserService userService) {
        this.userService = userService;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(!request.getRequestURI().contains("/actuator/health")){
            String user = SysUtil.getUser();
            String regionCode = request.getParameter("regionCode");
            String role = null;
            UserVo userVo = userService.findUserByIdentifier(user, "");
            if (userVo != null){
                if (userVo.getRoleList() != null && userVo.getRoleList().size() > 0){
                    List<String> roles = userVo.getRoleList().stream().map(RoleVo::getRoleCode).collect(Collectors.toList());
                    role = StringUtils.join(roles,",");
                }
                regionCode = userVo.getRegionCode();
            }
            RegionCodeContextHolder.setRegionCode(regionCode);
            RoleContextHolder.setRole(role);
        }
        filterChain.doFilter(request,response);
        RegionCodeContextHolder.clear();
    }
}
