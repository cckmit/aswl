package com.aswl.as.user.mapper;
import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.vo.LogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 日志
 *
 * @author aswl.com
 * @date 2018/10/31 20:38
 */
@Mapper
public interface LogMapper extends CrudMapper<Log> {
    int clearAll();
    List<LogVo> findLogInfo(Log log);
}
