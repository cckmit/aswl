package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 报表下载记录表Entity
* @author df
* @date 2021/07/20 17:31
*/

@ApiModel(value = "ReportDownloadRecord",description = "报表下载记录表Entity")
@Data
public class ReportDownloadRecord extends BaseEntity<ReportDownloadRecord> {
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
