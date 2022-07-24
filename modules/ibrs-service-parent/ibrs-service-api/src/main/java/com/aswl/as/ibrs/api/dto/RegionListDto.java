package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author dingfei
 * @date 2019-12-05 17:15
 * @Version V1
 */
@Data
public class RegionListDto implements  java.io.Serializable {

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
     * 父区域名称
     */
    @ApiModelProperty(value = "父区域名称",name="parentName")
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
     * 智能箱数量
     */

    @ApiModelProperty(value = "智能箱数量",name="boxNum")
    private Integer boxNum;

    /**
     * 摄像机数量
     */

    @ApiModelProperty(value = "摄像机数量",name="cameraNum")
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
    /**
     *租户编号
     */
    @ApiModelProperty(value = "租户编号",name="tenantCode")
    private String tenantCode;

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
     * 项目id
     */
    @ApiModelProperty(value = "项目id",name="projectId")
    private String projectId;

}
