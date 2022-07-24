package com.aswl.as.metadata.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class IfaceCallbackDeviceStatusVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String callbackUrl;

    private ChangeDeviceVo changeDevice;
    
}
