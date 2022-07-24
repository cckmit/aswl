package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.AlarmOrderHandleDto;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import com.aswl.as.ibrs.api.vo.AlarmOrderHandleVo;
import org.apache.ibatis.annotations.Mapper;

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
}
