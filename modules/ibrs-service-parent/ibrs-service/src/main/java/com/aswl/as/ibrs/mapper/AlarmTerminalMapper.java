package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmTerminal;

/**
 * 报警终端设备Mapper
 *
 * @author dingfei
 * @date 2019-11-09 10:13
 */

@Mapper
public interface AlarmTerminalMapper extends CrudMapper<AlarmTerminal> {

}
