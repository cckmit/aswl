package com.aswl.as.ibrs.api.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceStatisticsDto implements Serializable{

    private static final long serialVersionUID = 1L;

    private String startTime;

    private String endTime;
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
     * 设备箱总数量
     */
    private Long deviceBoxCount;

    /**
     * 摄像机总数量
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
     * 电量
     */
    private Double electricNum;

    /**
     * 电费
     */
    private Double electricFee;

    /**
     * 故障数
     */
    private Integer alarmCount;

    /**
     * 告警类型
     */
    private String  alarmType;
}
