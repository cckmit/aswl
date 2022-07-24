package com.aswl.as.asos.modules.ibrs.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 设备型号
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsDeviceModel对象", description="设备型号")
@TableName(value = "as_device_model")
public class AsDeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "设备型号名")
    private String deviceModelName;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "交流电源口数量")
    private Integer acpowerNumber;

    @ApiModelProperty(value = "直流电源口数量")
    private Integer dcpowerNumber;

    @ApiModelProperty(value = "网络口数量")
    private Integer rj45Number;

    @ApiModelProperty(value = "光纤口数量")
    private Integer fibreOpticalNumber;

    @ApiModelProperty(value = "温度上限")
    private Double maxTemperature;

    @ApiModelProperty(value = "电压1上限")
    private Double maxVoltage1;

    @ApiModelProperty(value = "电压2上限")
    private Double maxVoltage2;

    @ApiModelProperty(value = "电压2上限")
    private Double maxVoltage3;

    @ApiModelProperty(value = "电压4上限")
    private Double maxVoltage4;

    @ApiModelProperty(value = "电压5上限")
    private Double maxVoltage5;

    @ApiModelProperty(value = "温度下限")
    private Double minTemperature;

    @ApiModelProperty(value = "电压1下限")
    private Double minVoltage1;

    @ApiModelProperty(value = "电压2下限")
    private Double minVoltage2;

    @ApiModelProperty(value = "电压3下限")
    private Double minVoltage3;

    @ApiModelProperty(value = "电压4下限")
    private Double minVoltage4;

    @ApiModelProperty(value = "电压5下限")
    private Double minVoltage5;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "创建终端")
    private String createTerminal;

    @ApiModelProperty(value = "修改者")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value = "修改终端")
    private String modifyTerminal;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "图片路径")
    private String picUrl;

    @ApiModelProperty(value = "型号描述")
    private String description;

    @ApiModelProperty(value = "附件")
    private String docFile;

    @ApiModelProperty(value = "厂家负责人")
    private String personInCharge;

    @ApiModelProperty(value = "客服联系人")
    private String linkman;

    @ApiModelProperty(value = "客服电话")
    private String telNum;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "售后服务电话")
    private String afterSalesTel;

    @ApiModelProperty(value = "售后支持")
    private String afterSalesSupport;

    @ApiModelProperty(value = "备注")
    private String remark;

    //--- 下面的字段不保存到数据库，仅作为查询时使用 --------------------------
    @ApiModelProperty(value = "设备种类ID")
    @TableField(exist=false)
    private String deviceKindId;

    @ApiModelProperty(value = "设备种类名")
    @TableField(exist=false)
    private String deviceKindName;

    @ApiModelProperty(value = "设备类型名")
    @TableField(exist=false)
    private String deviceTypeName;

}
