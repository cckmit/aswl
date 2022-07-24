package com.aswl.as.ibrs.api.dto;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.ibrs.api.module.Region;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
*
* 区域Dto
* @author dingfei
* @date 2019-11-08 18:08
*/

@ApiModel(value = "RegionDto",description = "区域Dto")
@Data
public class RegionDto extends TreeEntity<RegionDto> {

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
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

    @ApiModelProperty(value = "智能箱数量",name="ipPartEnd2")
    private Integer boxNum;

    /**
     * 摄像机数量
     */

    @ApiModelProperty(value = "摄像机数量",name="ipPartEnd2")
    private Integer cameraNum;

    /**
    * 创建人账号
    */
    @ApiModelProperty(value = "创建人账号",name="creator")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty(value = "创建人名称",name="createName")
    private String createName;
    /**
    * 创建日期
    */
    @ApiModelProperty(value = "创建日期",name="createDate")
    private Date createDate;
    /**
    * 更新人账号
    */
    @ApiModelProperty(value = "更新人账号",name="modifier")
    private String modifier;
    /**
    * 更新人名称
    */
    @ApiModelProperty(value = "更新人名称",name="updateName")
    private String updateName;
    /**
    * 更新日期
    */
    @ApiModelProperty(value = "更新日期",name="modifyDate")
    private Date modifyDate;
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
    * 删除标记 0:正常;1:删除
    */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除",name="delFlag")
    private Integer delFlag;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;

    @ApiModelProperty(value = "项目Id",name="projectId")
    private String projectId;
    /**
     * 设备IP模式(0,手动,1,自动)
     */
    @ApiModelProperty(value = "设备IP模式(0,手动,1,自动)",name="deviceIpMode")
    private Integer deviceIpMode;
    
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

    /**
    * 
    */
    @ApiModelProperty(value = "",name="tenantCode")
    private String tenantCode;
    public RegionDto(Region region) {
        this.id = region.getId();
        this.regionName = region.getRegionName();
        this.parentId = region.getParentId();
        this.fullName = region.getFullName();
        this.description = region.getDescription();
        this.longitude = region.getLongitude();
        this.latitude = region.getLatitude();
        this.ipPartBegin=region.getIpPartBegin();
        this.ipPartEnd=region.getIpPartEnd();
        this.regionCode = region.getRegionCode();
        this.createName = region.getCreateName();
        this.updateName = region.getUpdateName();
        this.sysOrgCode = region.getSysOrgCode();
        this.sort =region.getSort() == null ? 0:region.getSort();
        this.sysRegionCode = region.getSysRegionCode();
        this.projectId=region.getProjectId();
        this.deviceIpMode =region.getDeviceIpMode();
        this.boxNum = region.getBoxNum();
        this.cameraNum = region.getCameraNum();
        this.communicationOperatorId=region.getCommunicationOperatorId();
        this.communicationOperatorName=region.getCommunicationOperatorName();
    }
}
