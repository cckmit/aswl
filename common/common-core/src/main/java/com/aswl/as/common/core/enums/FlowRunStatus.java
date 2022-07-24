package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowRunStatus {
    //流程实例状态 0 未确认"审批中"  1 已完成（无需审核） 2 已确认  3 维修中 4-未修复(未完成) 5-已撤销 6-已处理（待审核） 7-已处理（未通过审核） 8-已处理（通过审核）

    // 未完成(0,2,3,4,6,7)
    // 已确认(2)
    // 未修复(4)
    // 待审核(6)
    // 已完成(1,8)

    INIT(0), //0是一开始新创建的时候的状态  未确认"审批中"

    MAINTENANCE_COMPLETED(1), //1是区域负责人或者系统自动处理了的情况 "已完成"
    CONFIRMED(2),//"已确认" //区域负责人在手机中点击确认
    MAINTENANCE(3),//"维修中" //区域负责人在手机点击已确认
    MAINTENANCE_UNDONE(4), //4是区域负责人 暂时未修复的情况 "未完成"
    REVOKE(5), //5是该流程已撤销，应该是系统自动处理的情况 "已撤销"

    //--- 上面是不用审核的情况

    //这两个审不审核都有

    //--- 下面是需要审核的情况

    REVIEW_PENDING(6),//已处理（待审核）
    REVIEW_NOT_PASS(7),//已处理（未通过审核）
    REVIEW_PASSED(8);//"已处理（通过审核）"

    private Integer value;


}
