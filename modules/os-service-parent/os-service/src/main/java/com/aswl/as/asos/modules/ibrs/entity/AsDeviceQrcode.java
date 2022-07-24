package com.aswl.as.asos.modules.ibrs.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备二维码
 * </p>
 *
 * @author df
 * @since 2020-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsDeviceQrcode对象", description="设备二维码")
public class AsDeviceQrcode implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "出厂编码")
    private String factoryCode;

    @ApiModelProperty(value = "是否在用")
    private Integer isUsed;

    @ApiModelProperty(value = "访问次数")
    private Integer visitTime;

    @ApiModelProperty(value = "所属批次ID")
    private String qrcodeBathId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
