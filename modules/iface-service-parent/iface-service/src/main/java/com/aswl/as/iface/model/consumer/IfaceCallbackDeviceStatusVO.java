package com.aswl.as.iface.model.consumer;

import com.aswl.as.iface.model.consumer.ChangeDeviceVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/10 16:51
 */
@Data
public class IfaceCallbackDeviceStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String callbackUrl;

    private ChangeDeviceVo changeDevice;


}
