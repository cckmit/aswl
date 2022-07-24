package com.aswl.as.metadata.vo;

import java.util.List;

import lombok.Data;

@Data
public class ChangeDeviceVo {
    private String deviceCode;

    private String deviceName;

    private List<ChangeStatusVo> changeStatuses;
}
