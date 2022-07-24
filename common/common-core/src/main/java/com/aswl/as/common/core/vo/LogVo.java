package com.aswl.as.common.core.vo;

import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * logVo
 *
 * @author aswl.com
 * @date 2019-01-05 17:07
 */
@Data
public class LogVo extends BaseEntity<LogVo> {

    private Log log;

    private String username;
}
