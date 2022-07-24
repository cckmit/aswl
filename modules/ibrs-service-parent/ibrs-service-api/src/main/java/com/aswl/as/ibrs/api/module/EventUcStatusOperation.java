package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件状态操作Entity
* @author dingfei
* @date 2019-10-28 14:27
*/

@ApiModel(value = "EventUcStatusOperation",description = "事件状态操作Entity")
@Data
public class EventUcStatusOperation extends BaseEntity<EventUcStatusOperation> {
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
