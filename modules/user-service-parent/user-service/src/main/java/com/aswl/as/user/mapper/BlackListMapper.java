package com.aswl.as.user.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.BlackList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
*
* 系统IP黑名单Mapper
* @author dingfei
* @date 2019-11-08 15:32
*/

@Mapper
public interface BlackListMapper extends CrudMapper<BlackList> {

    BlackList findIpById(@Param("id") String id);
}
