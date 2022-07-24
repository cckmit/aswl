package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ReportDownloadRecord;
import com.aswl.as.ibrs.mapper.ReportDownloadRecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ReportDownloadRecordService extends CrudService<ReportDownloadRecordMapper, ReportDownloadRecord> {
    private final ReportDownloadRecordMapper reportDownloadRecordMapper;

    /**
     * 新增报表下载记录表
     *
     * @param reportDownloadRecord
     * @return int
     */
    @Transactional
    @Override
    public int insert(ReportDownloadRecord reportDownloadRecord) {
        return reportDownloadRecordMapper.insert(reportDownloadRecord);
    }

    /**
     * 删除报表下载记录表
     *
     * @param reportDownloadRecord
     * @return int
     */
    @Transactional
    @Override
    public int delete(ReportDownloadRecord reportDownloadRecord) {
        return reportDownloadRecordMapper.delete(reportDownloadRecord);
    }
}