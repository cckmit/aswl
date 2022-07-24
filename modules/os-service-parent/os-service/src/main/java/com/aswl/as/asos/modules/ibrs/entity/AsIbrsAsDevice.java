package com.aswl.as.asos.modules.ibrs.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.aswl.as.asos.common.utils.AsOsBaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author hfx
 * @since 2019-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsDevice对象", description="设备表")
@TableName(value = "as_device")
public class AsIbrsAsDevice extends AsOsBaseEntity<AsIbrsAsDevice> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "编码")
    private String deviceCode;

    @ApiModelProperty(value = "名称")
    private String deviceName;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "网络口")
    private Integer rj45No;

    @ApiModelProperty(value = "光纤口")
    private Integer fibreOpticalNo;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "端口")
    private Integer port;

    @ApiModelProperty(value = "经度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "经度A")
    private BigDecimal latitudeA;

    @ApiModelProperty(value = "维度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "维度A")
    private BigDecimal longitudeA;

    @ApiModelProperty(value = "上级220V电源口")
    private Integer parentAcpowerNo;

    @ApiModelProperty(value = "上级直流电源口")
    private Integer parentDcpowerNo;

    @ApiModelProperty(value = "上级光纤口")
    private Integer parentFibreOpticalNo;

    @ApiModelProperty(value = "上级网络口")
    private Integer parentRj45No;

    @ApiModelProperty(value = "上级槽位")
    private Integer parentSlotO;

    @ApiModelProperty(value = "设备型号ID")
    private String deviceModelId;

    @ApiModelProperty(value = "上级设备ID")
    private String parentDeviceId;

    @ApiModelProperty(value = "地区ID")
    private String regionId;

    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "购买日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date purchaseDate;

    @ApiModelProperty(value = "保修期")
    private Integer guaranteeTime;

    @ApiModelProperty(value = "修改终端")
    private String modifyTerminal;

    @ApiModelProperty(value = "创建终端")
    private String createTerminal;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

}
