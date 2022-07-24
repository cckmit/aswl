package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.common.core.persistence.AsOsTreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
*
* 区域Dto
* @author dingfei
* @date 2019-11-08 18:08
*/

@ApiModel(value = "RegionDto",description = "区域Dto")
@Data
public class OsProjectDto extends AsOsTreeEntity<OsProjectDto> {

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "项目编码 地区拼音首字母+年份月+序列")
    private String projectCode;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目业主单位-公安用户")
    private String projectOwner;

    @ApiModelProperty(value = "项目描述")
    private String projectDes;

    @ApiModelProperty(value = "项目启动时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startAt;

    @ApiModelProperty(value = "项目验收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endAt;

    @ApiModelProperty(value = "创建者")
    private String creatorId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createAt;

    private String updaterId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateAt;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "系统编号")
//    @NotBlank(message="系统编号不能为空")
    private String applicationCode;

    @ApiModelProperty(value = "租户编号")
    @NotBlank(message="租户编号不能为空")
    private String tenantCode;

    //------- 下面的字段只用来查询时使用，不会保存到数据库------------------------------------------------

    @TableField(exist=false)
    private String tenantName;

    @TableField(exist=false)
    @ApiModelProperty(value = "节点类型，1为租户，2为项目，3为区域")
    private String type;

    public OsProjectDto(AsProject p) {

        projectId=p.getProjectId();
        projectCode=p.getProjectCode();
        projectName=p.getProjectName();
        projectOwner=p.getProjectOwner();
        projectDes=p.getProjectDes();
        startAt=p.getStartAt();
        endAt=p.getEndAt();
        creatorId=p.getCreatorId();
        createAt=p.getCreateAt();
        updaterId=p.getUpdaterId();
        updateAt=p.getUpdateAt();
        remark=p.getRemark();
        applicationCode=p.getApplicationCode();
        tenantCode=p.getTenantCode();
        tenantName=p.getTenantName();

        code=p.getProjectId();

        //后面会设置
        parent=null;
        parentId=null;

        sort=0;

        this.type="2";

    }
}
