package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.ReportHisInfoDto;
import com.aswl.as.ibrs.api.module.ReportHisInfo;
import com.aswl.as.ibrs.api.vo.ReportHisInfoVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.ReportHisInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@AllArgsConstructor
@Slf4j
@Service
public class ReportHisInfoService extends CrudService<ReportHisInfoMapper, ReportHisInfo> {
    private final ReportHisInfoMapper reportHisInfoMapper;
    
    /**
     * 分页查询统计报表记录
     * @param page
     * @param regionLeaderDto
     * @return
     */
    public PageInfo<ReportHisInfoVo> findPage(PageInfo<ReportHisInfoDto> page, ReportHisInfoDto regionLeaderDto) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String regionCode = regionLeaderDto.getRegionCode();
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
                if(regionCode == null || "".equals(regionCode)){
                    return new PageInfo<>(new ArrayList<>());
                }
                regionCode = userRegionCode;
            }
        }
        regionLeaderDto.setTenantCode(tenantCode);
        regionLeaderDto.setProjectId(projectId);
        regionLeaderDto.setRegionCode(regionCode);
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(reportHisInfoMapper.findPageInfo(regionLeaderDto));
    }

    /**
     * 新增统计报表
     *
     * @param reportHisInfo
     * @return int
     */
    @Transactional
    @Override
    public int insert(ReportHisInfo reportHisInfo) {
        return reportHisInfoMapper.insert(reportHisInfo);
    }

    /**
     * 删除统计报表
     *
     * @param reportHisInfo
     * @return int
     */
    @Transactional
    @Override
    public int delete(ReportHisInfo reportHisInfo) {
        return reportHisInfoMapper.delete(reportHisInfo);
    }
}