package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentNotice;

/**
*
* 系统消息表Mapper
* @author hfx
* @date 2020-03-11 13:28
*/

@Mapper
public interface ContentNoticeMapper extends CrudMapper<ContentNotice> {

}
