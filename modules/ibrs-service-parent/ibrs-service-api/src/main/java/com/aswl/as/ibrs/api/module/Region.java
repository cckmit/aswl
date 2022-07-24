package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;

/**
*
* 区域Entity
* @author dingfei
* @date 2019-11-08 18:08
*/

@ApiModel(value = "Region",description = "区域Entity")
@Data
public class Region extends BaseEntity<Region> {

    /**
    * 区域名称
    */

    @ApiModelProperty(value = "区域名称",name="regionName")
    private String regionName;
    /**
    * 父区域ID
    */

    @ApiModelProperty(value = "父区域ID",name="parentId")
    private String parentId;

    /**
     * 父区域名称
     */

    @ApiModelProperty(value = "父区域名称",name = "parentName")
    private String parentName;
    /**
    * 区域全称
    */

    @ApiModelProperty(value = "区域全称",name="fullName")
    private String fullName;
    /**
    * 描述
    */

    @ApiModelProperty(value = "描述",name="description")
    private String description;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    /**
    * 维度
    */

    @ApiModelProperty(value = "维度",name="latitude")
    private Double latitude;
    /**
    * 经度
    */

    @ApiModelProperty(value = "经度",name="longitude")
    private Double longitude;

    /**
     * IP段开始
     */

    @ApiModelProperty(value = "IP段开始",name="ipPartBegin")
    private String ipPartBegin;

    /**
     * IP段结束
     */

    @ApiModelProperty(value = "IP段结束",name="ipPartEnd")
    private String ipPartEnd;

    /**
     * IP段2开始
     */

    @ApiModelProperty(value = "IP段2开始",name="ipPartBegin2")
    private String ipPartBegin2;

    /**
     * IP段2结束
     */

    @ApiModelProperty(value = "IP段2结束",name="ipPartEnd2")
    private String ipPartEnd2;


    /**
     * 智能箱数量
     */

    @ApiModelProperty(value = "智能箱数量",name="boxNum")
    private Integer boxNum = 0;

    /**
     * 摄像机数量
     */

    @ApiModelProperty(value = "摄像机数量",name="cameraNum")
    private Integer cameraNum = 0;
    
    /**
    * 创建人名称
    */

    @ApiModelProperty(value = "创建人名称",name="createName")
    private String createName;
    /**
    * 更新人名称
    */

    @ApiModelProperty(value = "更新人名称",name="updateName")
    private String updateName;
    /**
    * 所属机构
    */

    @ApiModelProperty(value = "所属机构",name="sysOrgCode")
    private String sysOrgCode;
    /**
    * 所属区域
    */

    @ApiModelProperty(value = "所属区域",name="sysRegionCode")
    private String sysRegionCode;
    /**
    * 排序
    */

    @ApiModelProperty(value = "排序",name="sort")
    private Integer sort;

    /**
     * 项目编码
     */

    @ApiModelProperty(value = "项目编码",name="projectCode")
    private String projectCode;

    /**
     * 设备IP模式(0,手动,1,自动)
     */
    @ApiModelProperty(value = "设备IP模式(0,手动,1,自动)",name="deviceIpMode")
    private Integer deviceIpMode;


    /**
     * 设备型号ID (非数据库字段)
     */
    @ApiModelProperty(value = "设备型号ID",name="deviceModelId")
    private String deviceModelId;

    /**
     * 通讯运营商ID
     */
    @ApiModelProperty(value = "通讯运营商ID",name="communicationOperatorId")
    private String communicationOperatorId;

    /**
     * 通讯运营商名称
     */
    @ApiModelProperty(value = "通讯运营商名称",name="communicationOperatorName")
    private String communicationOperatorName;


   private double lonMin;
    private  double lonMax;
    private  double latMin;
    private  double latMax;
}
