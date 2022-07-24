package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.RegionLeaderDto;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.ibrs.api.vo.RegionLeaderVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.RegionLeaderMapper;
import com.aswl.as.user.api.module.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class RegionLeaderService extends CrudService<RegionLeaderMapper, RegionLeader> {
    private final RegionLeaderMapper regionLeaderMapper;

    private static final String DAY = "day";

    private static final String MONTH = "month";

    private static final String YEAR = "year";


    /**
     * 分页查询区域负责人
     * @param page
     * @param regionLeaderDto
     * @return
     */
    public PageInfo<RegionLeaderVo> findPage(PageInfo<DeviceDto> page, RegionLeaderDto regionLeaderDto) {
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
        return new PageInfo<>(regionLeaderMapper.findRegionLeaderInfo(regionLeaderDto));
    }

    /**
     * 新增区域负责人
     *
     * @param regionLeaderDto
     * @return int
     */
    public int insert(RegionLeaderDto regionLeaderDto) {
        RegionLeader regionLeader = new RegionLeader();
        // 查询该区域下是否添加负责人
        RegionLeader leader = regionLeaderMapper.findByRegionCode(regionLeaderDto.getRegionCode());
        if (leader != null) {
            return 0;
        }
        if (regionLeaderDto.getIsPatrol() == 1) {
            String periodUnit = regionLeaderDto.getPatrolPeriodUnit();
            Integer periodNum = regionLeaderDto.getPatrolPeriodNum();
            Integer second = null;
            Calendar calendar = Calendar.getInstance();
            Date beginTime = regionLeaderDto.getPatrolPeriodBeginTime();
            calendar.setTime(beginTime);
            switch (periodUnit) {
                case DAY:
                    second = periodNum * 24 * 60 * 60;
                    break;
                case MONTH:
                    calendar.add(Calendar.MONTH, periodNum);
                    second = (int) ((calendar.getTimeInMillis() - beginTime.getTime()) / 1000);
                    break;
                case YEAR:
                    calendar.add(Calendar.YEAR, periodNum);
                    second = (int) ((calendar.getTimeInMillis() - beginTime.getTime()) / 1000);
                    break;
            }
            regionLeaderDto.setPatrolPeriod(second);
        } else {
            regionLeaderDto.setPatrolKeyId(null);
            regionLeaderDto.setPatrolPeriodNum(null);
            regionLeaderDto.setPatrolPeriodUnit(null);
            regionLeaderDto.setPatrolPeriodBeginTime(null);
        }
        BeanUtils.copyProperties(regionLeaderDto, regionLeader);
        regionLeader.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(),regionLeaderDto.getTenantCode() ==null ? SysUtil.getTenantCode():regionLeaderDto.getTenantCode());
        return super.insert(regionLeader);
    }


    /**
     * 修改区域负责人
     *
     * @param regionLeaderDto
     * @return int
     */
    public int update(RegionLeaderDto regionLeaderDto) {
        RegionLeader regionLeader = new RegionLeader();
        // 查询该区域下是否添加负责人
        RegionLeader leader = regionLeaderMapper.findByRegionCode(regionLeaderDto.getRegionCode());
        if (leader!=null){
            return 0;
        }
        if (regionLeaderDto.getIsPatrol() == 1) {
            String periodUnit = regionLeaderDto.getPatrolPeriodUnit();
            Integer periodNum = regionLeaderDto.getPatrolPeriodNum();
            Integer second = null;
            Calendar calendar = Calendar.getInstance();
            Date beginTime = regionLeaderDto.getPatrolPeriodBeginTime();
            calendar.setTime(beginTime);
            switch (periodUnit){
                case DAY :
                     second = periodNum * 24 * 60 * 60;
                     break;
                case MONTH :
                     calendar.add(Calendar.MONTH,periodNum);
                     second = (int) ((calendar.getTimeInMillis() - beginTime.getTime())/1000);
                     break;
                case YEAR :
                     calendar.add(Calendar.YEAR,periodNum);
                    second = (int) ((calendar.getTimeInMillis() - beginTime.getTime())/1000);
                     break;
            }
            regionLeaderDto.setPatrolPeriod(second);
        }else {
            regionLeaderDto.setPatrolKeyId(null);
            regionLeaderDto.setPatrolPeriodNum(null);
            regionLeaderDto.setPatrolPeriodUnit(null);
            regionLeaderDto.setPatrolPeriodBeginTime(null);
        }
        BeanUtils.copyProperties(regionLeaderDto, regionLeader);
        regionLeader.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return super.update(regionLeader);
    }

    /**
     * 删除区域负责人
     *
     * @param regionLeader
     * @return int
     */
    @Transactional
    @Override
    public int delete(RegionLeader regionLeader) {
        return super.delete(regionLeader);
    }

    /**userList
     * 区域Id获取开启巡更区域负责人
     * @param regionId
     * @return
     */
    public RegionLeader findByRegionId(String regionId) {
        return regionLeaderMapper.findByRegionId(regionId);
    }

    public RegionLeader findByRegionCode(String regionCode){
        return regionLeaderMapper.findByRegionCode(regionCode);
    }


    /**
     * 根据用户id批量查询区域负责人信息
     *
     * @param userList 
     * @return List
     */
    public List<RegionLeader> getRegionLeaderByUserIds(List<User> userList) { 
        return this.dao.getRegionLeaderByUserIds(userList.stream().map(User::getId).distinct().toArray(String[]::new));
    }

    /**
     * 根据用户id更新区域负责人信息
     *
     * @param regionLeaderDto regionLeaderDto
     * @return int
     */
    public int  updateByUserId(RegionLeaderDto regionLeaderDto){
        if (regionLeaderDto.getIsPatrol() == 1) {
            String periodUnit = regionLeaderDto.getPatrolPeriodUnit();
            Integer periodNum = regionLeaderDto.getPatrolPeriodNum();
            Integer second = null;
            Calendar calendar = Calendar.getInstance();
            Date beginTime = regionLeaderDto.getPatrolPeriodBeginTime();
            calendar.setTime(beginTime);
            switch (periodUnit){
                case DAY :
                    second = periodNum * 24 * 60 * 60;
                    break;
                case MONTH :
                    calendar.add(Calendar.MONTH,periodNum);
                    second = (int) ((calendar.getTimeInMillis() - beginTime.getTime())/1000);
                    break;
                case YEAR :
                    calendar.add(Calendar.YEAR,periodNum);
                    second = (int) ((calendar.getTimeInMillis() - beginTime.getTime())/1000);
                    break;
            }
            regionLeaderDto.setPatrolPeriod(second);
        }
        return regionLeaderMapper.updateByUserId(regionLeaderDto);
    }

    /**
     * 根据用户id删除区域负责人信息
     *
     * @param userIds userIds
     * @return int
     */
    public int  deleteByUserId(String[] userIds){
        return regionLeaderMapper.deleteByUserId(userIds);
    }

    /**
     * 根据用户ID查询区域负责人
     * @param userId
     * @return int
     */
    public RegionLeader findRegionLeaderByUserId(String userId){
        return regionLeaderMapper.findRegionLeaderByUserId(userId);
    }

    /**
     *  根据区域编码查询区域负责人是否存在
     * @param regionCode
     * @return RegionLeader
     */
    public Boolean getIsExistLeader(String regionCode){
        RegionLeader regionLeader = regionLeaderMapper.findByRegionCode(regionCode);
        if (regionLeader== null){
            return true;
        }
        return false;
    }
    
}