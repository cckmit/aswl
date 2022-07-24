package com.aswl.as.metadata.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "EventHisPatrol",description = "设备历史事件-设备巡更Entity")
@Data
public class EventHisPatrol extends BaseEntity<EventHisPatrol> {

    private String deviceId; //设备ID
    private String regionNo; //区域编码
    private long recTime; //接收时间(从1970-01-01 08-00-00到现在的秒)',
    private String keyMac; //钥匙MAC',
    private String idCipher; //ID密文',
    private Date authTime; //授权时间',
    private Date storeTime; //存储时间',

}
