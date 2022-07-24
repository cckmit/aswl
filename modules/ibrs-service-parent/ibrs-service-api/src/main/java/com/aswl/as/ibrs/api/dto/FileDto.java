package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
*
* 上传文件表Dto
* @author com.aswl
* @date 2020-04-17 13:25
*/

@ApiModel(value = "FileDto",description = "上传文件表Dto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @ApiModelProperty(value = "id",name="id")
    private String id;
    /**
    * 文件名称
    */
    @ApiModelProperty(value = "文件名称",name="fileName")
    private String fileName;
    /**
    * 文件绝对路径
    */
    @ApiModelProperty(value = "文件绝对路径",name="path")
    private String path;

    @ApiModelProperty(value = "下载路径",name="url")
    private String url;

    @ApiModelProperty(value = "用户名",name="userName")
    private String userName;
    /**
    * 文件大小
    */
    @ApiModelProperty(value = "文件大小",name="fileSize")
    private String fileSize;
    /**
    * 文件指纹
    */
    @ApiModelProperty(value = "文件指纹",name="fileMd5")
    private String fileMd5;
    /**
    * 上传时间
    */
    @ApiModelProperty(value = "上传时间",name="uploadTime")
    private Date uploadTime;
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
