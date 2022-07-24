package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.mapper.AttachmentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AttachmentService extends CrudService<AttachmentMapper, Attachment> {
private final AttachmentMapper attachmentMapper;

/**
* 新增附件表
* @param  attachment
* @return  int
*/
@Transactional
@Override
public int insert(Attachment attachment) {
return super.insert(attachment);
}

/**
* 删除附件表
* @param attachment
* @return int
*/
@Transactional
@Override
public int delete(Attachment attachment) {
return super.delete(attachment);
}
}