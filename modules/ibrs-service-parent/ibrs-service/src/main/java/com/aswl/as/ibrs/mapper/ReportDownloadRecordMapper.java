package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ReportDownloadRecord;

/**
*
* 报表下载记录表Mapper
* @author df
* @date 2021/07/20 17:31
*/

@Mapper
public interface ReportDownloadRecordMapper extends CrudMapper<ReportDownloadRecord> {

}
