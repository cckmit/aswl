package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.CommunicationOperator;

/**
*
* 通讯运营商Mapper
* @author df
* @date 2021/12/03 11:23
*/

@Mapper
public interface CommunicationOperatorMapper extends CrudMapper<CommunicationOperator> {

}
