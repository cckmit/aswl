package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 未巡更历史Entity
 */
@ApiModel(value = "PatrolHisNoRecord", description = "未巡更历史Entity")
@Data
public class PatrolHisNoRecord extends BaseEntity<PatrolHisNoRecord> {

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;

    /**
     * ID密文
     */
    @ApiModelProperty(value = "ID密文",name="cipherId")
    private String cipherId;

    /**
     * 周期开始日期
     */
    @ApiModelProperty(value = "周期开始日期",name="periodBeginDate")
    private Date periodBeginDate;

    /**
     * 周期结束日期
     */
    @ApiModelProperty(value = "周期结束日期",name="periodEndDate")
    private Date periodEndDate;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;

}
