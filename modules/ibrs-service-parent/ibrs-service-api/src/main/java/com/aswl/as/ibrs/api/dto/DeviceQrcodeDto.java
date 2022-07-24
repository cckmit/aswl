package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 二维码批次表Dto
* @author hfx
* @date 2020-02-26 15:44
*/

@ApiModel(value = "DeviceQrcodeDto",description = "地址库表Dto")
@Data
public class DeviceQrcodeDto implements Serializable {

private static final long serialVersionUID = 1L;

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
