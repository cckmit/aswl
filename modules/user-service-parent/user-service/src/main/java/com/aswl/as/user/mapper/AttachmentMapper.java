package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Attachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件mapper
 *
 * @author aswl.com
 * @date 2018/10/30 20:55
 */
@Mapper
public interface AttachmentMapper extends CrudMapper<Attachment> {
}
