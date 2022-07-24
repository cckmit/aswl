package com.aswl.as.ibrs.api.vo;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEventHisWrongVo {
    /**
     * 日期
     */
    private String storeTime;

    /**
     * 设备数量
     */
    private Integer total;
}
