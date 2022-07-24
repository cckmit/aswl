package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.RatingStandardDto;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.RatingStandard;

import java.util.List;

/**
*
* 告警评级设置Mapper
* @author hzj
* @date 2021/01/11 16:03
*/

@Mapper
public interface RatingStandardMapper extends CrudMapper<RatingStandard> {


    List<RatingStandard> findAll();
}
