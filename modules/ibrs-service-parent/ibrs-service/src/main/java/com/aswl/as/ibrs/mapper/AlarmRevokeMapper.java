package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmRevoke;

/**
 * 报警撤销记录表Mapper
 *
 * @author dingfei
 * @date 2020-01-08 10:42
 */

@Mapper
public interface AlarmRevokeMapper extends CrudMapper<AlarmRevoke> {

}
