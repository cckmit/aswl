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
* @date 2020-01-04 15:35
*/

@ApiModel(value = "AddressBase",description = "地址库表Entity")
@Data
public class AddressBase extends BaseEntity<AddressBase> {

    /**
    * 点位名称
    */

    @ApiModelProperty(value = "点位名称",name="name")
    private String name;
    /**
    * 详细地址
    */

    @ApiModelProperty(value = "详细地址",name="address")
    private String address;
    /**
    * 经度
    */

    @ApiModelProperty(value = "经度",name="latitude")
    private Double latitude;
    /**
    * 纬度
    */

    @ApiModelProperty(value = "纬度",name="longitude")
    private Double longitude;
    /**
    * 绑定的设备IP
    */

    @ApiModelProperty(value = "绑定的设备IP",name="ip")
    private String ip;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;

    /**
     * 模糊查询参数
     */
    @ApiModelProperty(value = "模糊查询参数",name="query")
    private String query;

    /**
    * 是否已用
    */

    @ApiModelProperty(value = "是否已用",name="isUsed")
    private Integer isUsed;
    /**
    * 绑定的设备ID
    */

    @ApiModelProperty(value = "绑定的设备ID",name="bindDeviceId")
    private String bindDeviceId;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    //---- 下面字段只作传递数据使用 ，不保存到数据库

    @ApiModelProperty(value = "设备报警级别",name = "alarmLevel")
    private String alarmLevel;

    @ApiModelProperty(value = "报警类型名称",name = "alarmTypeName")
    private String alarmTypeName;

    @ApiModelProperty(value = "网络状态",name = "isOnline")
    private Integer isOnline;

    @ApiModelProperty(value = "摄像头数量",name = "cameraCount")
    private Integer cameraCount;

    @ApiModelProperty(value = "设备名称",name = "deviceName")
    private String deviceName;

    @ApiModelProperty(value = "型号图片",name = "picUrl")
    private String picUrl;

}
