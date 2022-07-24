package com.aswl.as.ibrs.api.vo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 维护统计总表Vo
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceStatisticsVo implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 项目名称
     */
    private String projectName;

    /**
     *使用单位
     */
    private String useCompany;

    /**
     * 使用单位联系人
     */
    private String useCompanyLinkMan;

    /**
     * 使用单位电话
     */
    private String useCompanyPhoneNo;

    /**
     * 使用单位邮箱
     */
    private String useCompanyEmail;

    /**
     *维护单位
     */
    private String MaintenanceCompany;

    /**
     * 维护单位联系人
     */
    private String MaintenanceCompanyLinkMan;

    /**
     * 维护单位电话
     */
    private String MaintenanceCompanyPhoneNo;

    /**
     * 维护单位邮箱
     */
    private String MaintenanceCompanyEmail;

    /**
     * 统计时间
     */
    private String statisticsTime;

    /**
     * 当前设备箱总数量
     */
    private Long deviceBoxCount;

    /**
     * 当前摄像机总数量
     */
    private Long camCount;

    /**
     * 设备箱在线率
     */
    private Double deviceBoxOnlineRate;

    /**
     * 摄像机在线率
     */
    private Double camCountOnlineRate;

    /**
     * 设备箱故障率
     */
    private Double deviceBoxWrongRate;

    /**
     * 摄像机故障率
     */
    private Double camCountWrongRate;

    /**
     * 设备箱故障平均修复时长
     */
    private String deviceBoxRepairDuration;

    /**
     * 摄像机故障平均修复时长
     */
    private String camRepairDuration;

    /**
     * 设备箱修复率
     */
    private Double boxRepairRate;

    /**
     * 摄像机修复率
     */
    private Double camRepairRate;

    /**
     * 设备箱累计总数量
     */
    private Long boxTotalNum;

    /**
     * 摄像机累计总数量
     */
    private Long camTotalNum;

    /**
     * 设备箱累计完好数
     */
    private Long boxIntactNum;

    /**
     * 摄像机累计完好数
     */
    private Long camIntactNum;

    /**
     * 设备箱累计在线数
     */
    private Long boxOnlineNum;

    /**
     * 摄像机累计在线数
     */
    private Long camOnlineNum;

    /**
     * 设备箱累计工单数
     */
    private Long boxRunNum;

    /**
     * 摄像机累计工单数
     */
    private Long camRunNum;

    /**
     * 设备箱累计工单修复数
     */
    private Long boxRepairNum;

    /**
     * 摄像机累计工单修复数
     */
    private Long camRepairNum;

    /**
     * 电量统计
     */
    private Double  electricNum;

    /**
     * 电量统计
     */
    private String  electricFee;

    /**
     * 故障告警数
     */
    private Long alarmCount;

    /**
     * 告警类型
     */
    private List<AlarmTypeStatisticsVo> alarmType;

}
