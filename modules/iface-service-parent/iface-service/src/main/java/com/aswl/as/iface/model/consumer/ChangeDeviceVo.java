package com.aswl.as.iface.model.consumer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/13 16:23
 */
@Data
public class ChangeDeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String deviceCode;

    private String deviceName;

    private List<ChangeStatusVo> changeStatuses;
}
