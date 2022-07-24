package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备型号Vo
 *
 * @author dingfei
 * @date 2019-09-26 15:22
 */
@Data
public class DeviceModelVo extends BaseEntity<DeviceModelVo> {
    /**
     * 设备型号名
     */
    @ApiModelProperty(value ="设备型号名" ,name = "deviceModelName")
    private String deviceModelName;

    /**
     * 设备类型编码
     */

    @ApiModelProperty(value ="设备类型编码" ,name = "deviceType")
    private String deviceType;

    /**
     * 设备类型名称
     */

    @ApiModelProperty(value = "设备类型名称",name = "deviceTypeName")
    private String deviceTypeName;

    /**
     * 设备种类ID
     */

    @ApiModelProperty(value ="设备种类ID" ,name = "deviceKindId")
    private String deviceKindId;

    /**
     * 设备种类
     */

    @ApiModelProperty(value ="设备种类" ,name = "kind")
    private String kind;

    /**
     * 设备种类名称
     */

    @ApiModelProperty(value ="设备种类名称" ,name = "deviceKindName")
    private String deviceKindName;

    /**
     * 交流电源口数量
     */

    @ApiModelProperty(value ="交流电源口数量" ,name = "acpowerNumber")
    private Integer acpowerNumber;
    /**
     * 直流电源口数量
     */

    @ApiModelProperty(value ="直流电源口数量" ,name = "dcpowerNumber")
    private Integer dcpowerNumber;
    /**
     * 网络口数量
     */

    @ApiModelProperty(value ="网络口数量" ,name = "rj45Number")
    private Integer rj45Number;
    /**
     * 光纤口数量
     */

    @ApiModelProperty(value ="光纤口数量" ,name = "fibreOpticalNumber")
    private Integer fibreOpticalNumber;
    /**
     * 温度上限
     */

    @ApiModelProperty(value ="温度上限" ,name = "maxTemperature")
    private Double maxTemperature;
    /**
     * 电压1上限
     */

    @ApiModelProperty(value ="电压1上限" ,name = "maxVoltage1")
    private Double maxVoltage1;
    /**
     * 电压2上限
     */

    @ApiModelProperty(value ="电压2上限" ,name = "maxVoltage2")
    private Double maxVoltage2;
    /**
     * 电压2上限
     */

    @ApiModelProperty(value = "电压2上限",name = "maxVoltage3")
    private Double maxVoltage3;
    /**
     * 电压4上限
     */

    @ApiModelProperty(value ="maxVoltage4" ,name = "maxVoltage4")
    private Double maxVoltage4;
    /**
     * 电压5上限
     */

    @ApiModelProperty(value = "电压5上限",name = "maxVoltage5")
    private Double maxVoltage5;
    /**
     * 温度下限
     */

    @ApiModelProperty(value ="温度下限" ,name = "minTemperature")
    private Double minTemperature;
    /**
     * 电压1下限
     */

    @ApiModelProperty(value ="电压1下限" ,name = "minVoltage1")
    private Double minVoltage1;
    /**
     * 电压2下限
     */

    @ApiModelProperty(value ="电压2下限" ,name = "minVoltage2")
    private Double minVoltage2;
    /**
     * 电压3下限
     */

    @ApiModelProperty(value ="电压3下限" ,name = "minVoltage3")
    private Double minVoltage3;
    /**
     * 电压4下限
     */

    @ApiModelProperty(value ="电压4下限" ,name = "minVoltage4")
    private Double minVoltage4;
    /**
     * 电压5下限
     */

    @ApiModelProperty(value ="电压5下限" ,name = "minVoltage5")
    private Double minVoltage5;
    /**
     * 创建终端
     */

    @ApiModelProperty(value ="创建终端" ,name = "createTerminal")
    private String createTerminal;
    /**
     * 修改终端
     */

    @ApiModelProperty(value ="修改终端" ,name = "modifyTerminal")
    private String modifyTerminal;
    /**
     * 备注
     */

    @ApiModelProperty(value ="备注" ,name = "remark")
    private String remark;

    /**
     * 图片路径
     */

    @ApiModelProperty(value = "图片路径",name = "picUrl")
    private String picUrl;

    /**
     * 型号描述
     */

    @ApiModelProperty(value = "型号描述",name = "description")
    private String description;

    /**
     * 厂家负责人
     */
    @ApiModelProperty(value = "厂家负责人",name = "personInCharge")
    private String personInCharge;

    /**
     * 客服联系人
     */
    @ApiModelProperty(value = "客服联系人",name = "linkman")
    private String linkman;

    /**
     * 客服联系电话
     */
    @ApiModelProperty(value = "客服联系电话",name = "telNum")
    private String telNum;


    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家",name = "manufacturer")
    private String manufacturer;

    /**
     * 是否可编辑（0：否，1：是）
     */
    @ApiModelProperty(value = "是否可编辑（0：否，1：是）",name = "isEdit")
    private Integer isEdit;
}
