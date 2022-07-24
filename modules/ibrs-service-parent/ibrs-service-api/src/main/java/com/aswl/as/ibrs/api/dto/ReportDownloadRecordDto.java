package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 报表下载记录表Dto
* @author df
* @date 2021/07/20 17:31
*/

@ApiModel(value = "ReportDownloadRecordDto",description = "报表下载记录表Dto")
@Data
public class ReportDownloadRecordDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 报表记录ID
    */
    @ApiModelProperty(value = "报表记录ID",name="reportHisInfoId")
    private String reportHisInfoId;
    /**
    * 用户ID
    */
    @ApiModelProperty(value = "用户ID",name="userId")
    private String userId;
    /**
    * 用户名称
    */
    @ApiModelProperty(value = "用户名称",name="userName")
    private String userName;
    /**
    * 账号
    */
    @ApiModelProperty(value = "账号",name="userIdentifier")
    private String userIdentifier;
    /**
    * 下载时间
    */
    @ApiModelProperty(value = "下载时间",name="downloadDate")
    private Date downloadDate;
}
