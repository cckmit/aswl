package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.AlarmCapture;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* ibrsMapper
* @author hzj
* @date 2020/12/08 10:41
*/

@Mapper
public interface AlarmCaptureMapper extends CrudMapper<AlarmCapture> {


    /**
     * 根据设备Id查询最近3个月的抓拍图
     * @param uEventId
     * @return
     */
    List<AlarmCapture> findByEventId(@Param("uEventId") String uEventId);
}
