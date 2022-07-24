package com.aswl.as.iface.model.consumer;

import com.aswl.as.iface.model.consumer.ChangeDeviceVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/13 10:41
 */
@Data
public class IfaceCallbackDeviceStatusDto implements Serializable {

    private ChangeDeviceVo changeDevice;

}
