package com.aswl.as.user.service;
import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.vo.LogVo;
import com.aswl.as.user.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 日志service
 *
 * @author aswl.com
 * @date 2018/10/31 20:43
 */
@AllArgsConstructor
@Slf4j
@Service
public class LogService extends CrudService<LogMapper, Log> {
    private final LogMapper logMapper;
    public int clearAll() {
        return logMapper.clearAll();
    }

    /**
     * 分页查询所有信息
     *
     * @param page
     * @param log
     * @return PageInfo
     */
    public PageInfo<LogVo> findPageInfo(PageInfo<LogVo> page, Log log) {
        log.setTenantCode(SysUtil.getTenantCode());
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(logMapper.findLogInfo(log));
    }
    
}
