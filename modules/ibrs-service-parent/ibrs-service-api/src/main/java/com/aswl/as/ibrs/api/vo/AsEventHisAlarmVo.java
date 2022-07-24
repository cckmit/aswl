package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 设备历史事件-报警
 * @ibrs liuliepan
 * @date 2019-10-22 15:29
 */
@Data
public class AsEventHisAlarmVo {
    //主键
    private String tableName;//表名
    private String endtime;//结束时间
    private String id;
    private String ueventid;//统一事件ID
    private String deviceid;//设备ID
    private String regionno;//区域编码
    private int rectime;//接收时间(从1970-01-01 08-00-00到现在的秒)
    private Date recdate;//记录日期
    private String passway1;//通道有效位1
    private String passway2;//通道有效位2
    private String passway3;//预留通道3
    private String fld01;//箱门
    private String fld02;//授权
    private String fld03;//风扇1
    private String fld04;//风扇2
    private String fld05;//加热
    private String fld06;//水浸
    private String fld07;//烟雾
    private String fld08;//供电
    private String fld09;//短路
    private String fld10;//漏电
    private String fld11;//电源模块
    private String fld12;//雷击保护
    private String fld13;//震动
    private String fld14;//门磁开关
    private String fld15;//预留1
    private String fld16;//预留2
    private String fld17;//预留3
    private String fld18;//预留4
    private String fld19;//预留5
    private String fld20;//预留6
    private String fld21;//预留7
    private String fld22;//预留8
    private String fld23;//预留9
    private String fld24;//预留10
    private Date storetime;//存储时间
    private String applicationcode;//系统编号
    private String tenantcode;//租户编码

}
