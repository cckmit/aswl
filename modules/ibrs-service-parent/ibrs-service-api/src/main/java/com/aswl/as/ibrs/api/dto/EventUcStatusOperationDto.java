package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件状态操作Dto
* @author dingfei
* @date 2019-11-12 10:24
*/

@ApiModel(value = "EventUcStatusOperationDto",description = "事件状态操作Dto")
@Data
public class EventUcStatusOperationDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 标题
    */
    @ApiModelProperty(value = "标题",name="title")
    private String title;
    /**
    * 操作名称
    */
    @ApiModelProperty(value = "操作名称",name="operName")
    private String operName;
    /**
    * 操作编码
    */
    @ApiModelProperty(value = "操作编码",name="operCode")
    private String operCode;
    /**
    * 排序
    */
    @ApiModelProperty(value = "排序",name="operSort")
    private Integer operSort;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
    /**
    * 创建人账号
    */
    @ApiModelProperty(value = "创建人账号",name="createBy")
    private String createBy;
    /**
    * 更新时间
    */
    @ApiModelProperty(value = "更新时间",name="updateDate")
    private Date updateDate;
    /**
    * 更新人账号
    */
    @ApiModelProperty(value = "更新人账号",name="updateBy")
    private String updateBy;
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
}
