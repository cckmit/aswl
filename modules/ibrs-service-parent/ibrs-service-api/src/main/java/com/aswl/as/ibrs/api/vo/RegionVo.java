package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.TreeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aswl.com
 * @version 1.0.0
 * @create 2019/10/11 9:41
 */
@Getter@Setter
public class RegionVo extends TreeEntity<RegionVo> {
    /**
     * 区域名称
     */

    @ApiModelProperty(value = "区域名称",name = "regionName")
    private String regionName;
    /**
     * 父区域ID
     */

    @ApiModelProperty(value = "父区域ID",name = "parentId")
    private String parentId;
    /**
     * 区域全称
     */

    @ApiModelProperty(value = "区域全称",name = "fullName")
    private String fullName;
    /**
     * 描述
     */

    @ApiModelProperty(value = "描述",name = "description")
    private String description;
    /**
     * 区域编码
     */

    @ApiModelProperty(value = "区域编码",name = "regionCode")
    private String regionCode;
    /**
     * 创建人名称
     */

    @ApiModelProperty(value = "创建人名称",name = "createName")
    private String createName;
    /**
     * 更新人名称
     */

    @ApiModelProperty(value = "更新人名称",name = "updateName")
    private String updateName;
    /**
     * 所属机构
     */

    @ApiModelProperty(value = "所属机构",name = "sysOrgCode")
    private String sysOrgCode;

    /**
     * 所属区域
     */

    @ApiModelProperty(value = "所属区域",name = "sysRegionCode")
    private String sysRegionCode;

    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String projectId;

    /**
     * 经度
     */

    @ApiModelProperty(value = "经度",name="longitude")
    private Double longitude;

    /**
     * 维度
     */

    @ApiModelProperty(value = "维度",name="latitude")
    private Double latitude;

    @ApiModelProperty(value = "告警级别",name="alarmLevel")
    private Integer alarmLevel;
}
