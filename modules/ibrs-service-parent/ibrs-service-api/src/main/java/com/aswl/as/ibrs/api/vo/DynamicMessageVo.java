package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/2 17:05
 */
@Data
@ApiModel(value = "设备动态Vo",description = "设备动态Vo")
public class DynamicMessageVo implements Serializable {

    private String deviceName;
    private String regionName;
    private String alarmType;
    private String alarmTime;
    private String intervalTime;
}
