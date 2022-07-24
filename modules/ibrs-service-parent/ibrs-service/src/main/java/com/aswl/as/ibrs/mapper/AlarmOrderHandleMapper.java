package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.AlarmOrderHandleDto;
import com.aswl.as.ibrs.api.vo.AlarmOrderHandleVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 派单处理工单设置Mapper
* @author hfx
* @date 2020-03-23 11:43
*/

@Mapper
public interface AlarmOrderHandleMapper extends CrudMapper<AlarmOrderHandle> {

    /**
     * 分页查询项目派单设置列表
     * @param alarmOrderHandleDto
     * @return
     */
    List<AlarmOrderHandleVo> findProjectOrderList(AlarmOrderHandleDto alarmOrderHandleDto);

    /**
     * 根据租户编码删除工单设置
     * @param tenantCode
     * @return int 
     */
    int deleteByTenantCode(@Param("tenantCode") String tenantCode);
}
