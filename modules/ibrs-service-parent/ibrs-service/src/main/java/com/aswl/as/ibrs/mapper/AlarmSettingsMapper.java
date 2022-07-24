package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.vo.AlarmSettingsVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.AlarmSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 告警设置Mapper
* @author hzj
* @date 2020/12/08 10:43
*/

@Mapper
public interface AlarmSettingsMapper extends CrudMapper<AlarmSettings> {

    /**
     * 根据项目ID 和租户获取告警设置
     * @param projectId
     * @param tenantCode
     * @return AlarmSettings
     */
    AlarmSettings getByTenantCode(@Param("projectId") String projectId, @Param("tenantCode") String tenantCode);


    /**
     * 获取项目告警设置信息列表
     * @param alarmSettings
     * @return list
     */
    List<AlarmSettingsVo> getProjectAlarmSettings(AlarmSettings alarmSettings);
}
