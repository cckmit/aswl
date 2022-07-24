package com.aswl.as.ibrs.filter;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.RoleVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.security.core.CustomUserDetailsService;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.service.RegionService;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Order(2147483646)
public class RegionCodeFilter implements Filter{

    private final UserServiceClient userServiceClient;
    private final RegionService regionService;

    @Autowired
    public RegionCodeFilter(UserServiceClient userServiceClient,RegionService regionService) {
        this.userServiceClient = userServiceClient;
        this.regionService = regionService;
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//            HttpServletRequest request = (HttpServletRequest) servletRequest;
//            HttpServletResponse response = (HttpServletResponse) servletResponse;
//            if(!request.getRequestURI().contains("/actuator/health")){
//                String user = SysUtil.getUser();
//                String regionCode = request.getParameter("regionCode");
//                String role = null;
//                if(regionCode == null || "".equals(regionCode)){
//                    //ResponseBean<UserInfoDto> info = userServiceClient.info();
//                    ResponseBean<UserVo> info = userServiceClient.findUserByIdentifier(user, "");
//                    if(info != null && info.getData() != null && info.getStatus() == 200){
//                        String regionId = info.getData().getRegionId();
//                        Region queryRegion = new Region();
//                        queryRegion.setId(regionId);
//                        Region region = regionService.get(queryRegion);
//                        if(region != null){
//                            regionCode = region.getRegionCode();
//                        }
//                        List<RoleVo> roleList = info.getData().getRoleList();
//                        if(roleList != null && roleList.size() > 0){
//                            List<String> roles = roleList.stream().map(RoleVo::getRoleCode).collect(Collectors.toList());
//                            role = StringUtils.join(roles,",");
//                        }
//                    }
//                }else {
//
//                }
//                RegionCodeContextHolder.setRegionCode(regionCode);
//                RoleContextHolder.setRole(role);
//            }
//            filterChain.doFilter(request,response);
//            RegionCodeContextHolder.clear();


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(!request.getRequestURI().contains("/actuator/health")){
            String user = SysUtil.getUser();
            String regionCode = request.getParameter("regionCode");
            String role = null;
                ResponseBean<UserVo> info = userServiceClient.findUserByIdentifier(user, "");
                if(info != null && info.getData() != null && info.getStatus() == 200){
                    List<RoleVo> roleList = info.getData().getRoleList();
                    if(roleList != null && roleList.size() > 0){
                        List<String> roles = roleList.stream().map(RoleVo::getRoleCode).collect(Collectors.toList());
                        role = StringUtils.join(roles,",");
                    }
                    if(regionCode == null || "".equals(regionCode)){
                        String regionId = info.getData().getRegionId();
                        Region queryRegion = new Region();
                        queryRegion.setId(regionId);
                        Region region = regionService.get(queryRegion);
                        if(region != null){
                            regionCode = region.getRegionCode();
                        }
                    }
                }
            RegionCodeContextHolder.setRegionCode(regionCode);
            RoleContextHolder.setRole(role);
        }
        filterChain.doFilter(request,response);
        RegionCodeContextHolder.clear();
    }
}
