package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*
* 设备二维码表VO
* @author hfx
* @date 2020-02-26 15:44
*/
@Data
public class DeviceQrcodeVo implements Serializable {

    /**
    * 序号
    */

    @ApiModelProperty(value = "序号",name="qrCodeId")
    private Integer qrCodeId;
    /**
    * 二维码
    */

    @ApiModelProperty(value = "二维码",name="qrCode")
    private String qrCode;
    /**
    * 出厂编码
    */

    @ApiModelProperty(value = "出厂编码",name="factoryCode")
    private String factoryCode;
    /**
    * 是否在用
    */

    @ApiModelProperty(value = "是否在用",name="isUsed")
    private Integer isUsed;
    /**
    * 访问次数
    */

    @ApiModelProperty(value = "访问次数",name="visitTime")
    private Integer visitTime;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "备注",name="deviceId")
    private String deviceId;

}
