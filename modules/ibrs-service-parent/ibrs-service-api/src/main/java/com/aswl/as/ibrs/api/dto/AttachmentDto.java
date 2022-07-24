package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 附件表Dto
* @author com.aswl
* @date 2020-04-17 13:56
*/

@ApiModel(value = "AttachmentDto",description = "附件表Dto")
@Data
public class AttachmentDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 文件ID
    */
    @ApiModelProperty(value = "文件ID",name="fileId")
    private String fileId;
    /**
    * 文件名
    */
    @ApiModelProperty(value = "文件名",name="fileName")
    private String fileName;
    /**
    * 扩展名
    */
    @ApiModelProperty(value = "扩展名",name="extend")
    private String extend;
    /**
    * 业务KEY
    */
    @ApiModelProperty(value = "业务KEY",name="businessKey")
    private String businessKey;
    /**
    * 下载地址
    */
    @ApiModelProperty(value = "下载地址",name="downloadUrl")
    private String downloadUrl;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creator")
    private String creator;
    /**
    * 创建日期
    */
    @ApiModelProperty(value = "创建日期",name="createDate")
    private Date createDate;
    /**
    * 项目id
    */
    @ApiModelProperty(value = "项目id",name="projectId")
    private String projectId;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编号
    */
    @ApiModelProperty(value = "租户编号",name="tenantCode")
    private String tenantCode;
}
