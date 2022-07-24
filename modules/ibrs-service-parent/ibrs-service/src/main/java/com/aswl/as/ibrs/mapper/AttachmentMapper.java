package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.Attachment;

/**
*
* 附件表Mapper
* @author com.aswl
* @date 2020-04-17 13:56
*/

@Mapper
public interface AttachmentMapper extends CrudMapper<Attachment> {

}
