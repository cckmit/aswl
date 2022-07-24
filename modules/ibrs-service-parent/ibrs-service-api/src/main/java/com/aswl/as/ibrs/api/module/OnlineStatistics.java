package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* 设备在线统计Entity
* @author dingfei
* @date 2019-12-16 16:01
*/

@ApiModel(value = "OnlineStatistics",description = "设备在线统计Entity")
@Data
public class OnlineStatistics extends BaseEntity<OnlineStatistics> {
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 在线数量
    */

    @ApiModelProperty(value = "在线数量",name="onlineNum")
    private Integer onlineNum;
    /**
    * 设备数量
    */

    @ApiModelProperty(value = "设备数量",name="deviceNum")
    private Integer deviceNum;

    /**
     * 设备种类
     */
    @ApiModelProperty(value = "设备种类",name = "deviceKind")
    private String deviceKind;

    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号",name = "deviceModel")
    private String deviceModelId;

    /**
     * 完好数
     */
    @ApiModelProperty(value = "完好数",name = "intactNum")
    private Integer intactNum;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String  projectId;
}
