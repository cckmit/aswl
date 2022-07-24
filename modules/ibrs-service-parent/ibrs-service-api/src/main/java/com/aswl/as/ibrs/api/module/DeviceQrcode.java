package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 地址库表Entity
* @author hfx
* @date 2020-02-26 15:44
*/

@ApiModel(value = "DeviceQrcode",description = "地址库表Entity")
@Data
public class DeviceQrcode extends BaseEntity<DeviceQrcode> {

    /**
    * 序号
    */
    @ApiModelProperty(value = "序号",name="id")
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
     * 所属批次ID
     */
    @ApiModelProperty(value = "所属批次ID",name="visitTime")
    private String qrCodeBathId;

    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

}
