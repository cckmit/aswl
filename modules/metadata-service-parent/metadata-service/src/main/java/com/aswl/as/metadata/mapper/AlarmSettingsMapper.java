package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
*
* ibrsMapper
* @author hzj
* @date 2020/12/08 10:43
*/

@Mapper
public interface AlarmSettingsMapper extends CrudMapper<AlarmSettings> {

    AlarmSettings getByTenantCode(@Param("projectId") String projectId, @Param("tenantCode") String tenantCode);
}
