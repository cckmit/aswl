package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.OpenDoorRecodeDto;
import com.aswl.as.ibrs.api.module.OpenDoorRecode;
import com.aswl.as.ibrs.api.vo.DeviceAlarmVo;
import com.aswl.as.ibrs.api.vo.OpenDoorVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.OpenDoorRecodeMapper;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class OpenDoorRecodeService extends CrudService<OpenDoorRecodeMapper, OpenDoorRecode> {
    private final OpenDoorRecodeMapper openDoorRecodeMapper;
    private final UserServiceClient userServiceClient;
    /**
     * 新增开箱记录表
     *
     * @param openDoorRecode
     * @return int
     */
    @Transactional
    @Override
    public int insert(OpenDoorRecode openDoorRecode) {
        return super.insert(openDoorRecode);
    }

    /**
     * 删除开箱记录表
     *
     * @param openDoorRecode
     * @return int
     */
    @Transactional
    @Override
    public int delete(OpenDoorRecode openDoorRecode) {
        return super.delete(openDoorRecode);
    }

    public PageInfo<OpenDoorVo> findOpenDoorInfo(PageInfo<OpenDoorVo> pageInfo, OpenDoorRecodeDto openDoorRecodeDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = openDoorRecodeDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        openDoorRecodeDto.setProjectId(projectId);
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return  null;
                }
                regionCode = userRegionCode;
            }
        }
        openDoorRecodeDto.setTenantCode(tenantCode);
        openDoorRecodeDto.setProjectId(projectId);
        openDoorRecodeDto.setRegionCode(regionCode);
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        return new PageInfo<>(openDoorRecodeMapper.findOpenDoorInfo(openDoorRecodeDto));
    }

    public Map findOpenDoorList(PageInfo<OpenDoorRecodeDto> pageInfo, OpenDoorRecodeDto openDoorRecodeDto) {

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<OpenDoorVo> info = new PageInfo<>(openDoorRecodeMapper.findOpenDoorInfo(openDoorRecodeDto));
        List<OpenDoorVo> list = new ArrayList<>();
        if (info.getList() != null && info.getList().size() > 0) {
            for (OpenDoorVo vo : info.getList()) {
                vo.setIntervalTime(formatDateTime(vo.getIntervalTime()));
                list.add(vo);
            }
        }
        info.setList(list);
        Map map = new LinkedHashMap();
        int total = openDoorRecodeMapper.findOpenDoorTotal(openDoorRecodeDto);
        int illegalTotal = openDoorRecodeMapper.findIllegalOpenDoorTotal(openDoorRecodeDto);
        map.put("total", total);
        map.put("illegalTotal", illegalTotal);
        map.put("info", info);
        return map;
    }

    //时长转换
    public String formatDateTime(String time) {
        if (time == null) {
            return "";
        }
        long ss = Long.valueOf(time);
        String intervalTime = null;
        long days = ss / (60 * 60 * 24);
        long hours = (ss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (ss % (60 * 60)) / 60;
        long seconds = ss % 60;
        if (days > 0) {
            intervalTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            intervalTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            intervalTime = minutes + "分钟";
        } else {
            intervalTime = "1分钟内";
        }
        return intervalTime;
    }
}