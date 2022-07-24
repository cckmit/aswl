package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.ReportHisInfoDto;
import com.aswl.as.ibrs.api.vo.ReportHisInfoVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ReportHisInfo;

import java.util.List;

/**
*
* 统计报表Mapper
* @author df
* @date 2021/07/20 17:28
*/

@Mapper
public interface ReportHisInfoMapper extends CrudMapper<ReportHisInfo> {

    /**
     * 分页查询统计报表记录
     * @param reportHisInfoDto
     * @return list
     */
    List<ReportHisInfoVo> findPageInfo(ReportHisInfoDto reportHisInfoDto);

}
