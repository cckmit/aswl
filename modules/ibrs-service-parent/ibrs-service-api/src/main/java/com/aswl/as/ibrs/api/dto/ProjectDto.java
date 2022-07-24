package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 项目表Dto
* @author com.aswl
* @date 2020-04-14 17:37
*/

@ApiModel(value = "ProjectDto",description = "项目表Dto")
@Data
public class ProjectDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    /**
     * 父项目ID
     */
    @ApiModelProperty(value = "父项目ID",name="父项目ID")
    private String parentId;
    /**
    * 项目编码 地区拼音首字母+年份月+序列
    */
    @ApiModelProperty(value = "项目编码 地区拼音首字母+年份月+序列",name="projectCode")
    private String projectCode;
    /**
    * 项目名称
    */
    @ApiModelProperty(value = "项目名称",name="projectName")
    private String projectName;
    /**
    * 项目业主单位-公安用户
    */
    @ApiModelProperty(value = "项目业主单位-公安用户",name="projectOwner")
    private String projectOwner;
    /**
    * 项目描述
    */
    @ApiModelProperty(value = "项目描述",name="projectDes")
    private String projectDes;
    /**
    * 联系人
    */
    @ApiModelProperty(value = "联系人",name="contacts")
    private String contacts;
    /**
    * 电话
    */
    @ApiModelProperty(value = "电话",name="telephone")
    private String telephone;
    /**
    * 邮箱
    */
    @ApiModelProperty(value = "邮箱",name="email")
    private String email;
    /**
    * 项目地址
    */
    @ApiModelProperty(value = "项目地址",name="projectAddr")
    private String projectAddr;
    /**
    * 项目规模
    */
    @ApiModelProperty(value = "项目规模",name="projectArea")
    private Integer projectArea;
    /**
    * 项目规模描述
    */
    @ApiModelProperty(value = "项目规模描述",name="projectAreaDes")
    private String projectAreaDes;
    /**
    * 设计单位名称
    */
    @ApiModelProperty(value = "设计单位名称",name="designDeptName")
    private String designDeptName;
    /**
    * 设计单位负责人
    */
    @ApiModelProperty(value = "设计单位负责人",name="designDeptLeader")
    private String designDeptLeader;
    /**
    * 设计单位联系电话
    */
    @ApiModelProperty(value = "设计单位联系电话",name="designDeptPhone")
    private String designDeptPhone;
    /**
    * 建设单位名称
    */
    @ApiModelProperty(value = "建设单位名称",name="buildDeptName")
    private String buildDeptName;
    /**
    * 建设单位负责人
    */
    @ApiModelProperty(value = "建设单位负责人",name="buildDeptLeader")
    private String buildDeptLeader;
    /**
    * 建设单位电话
    */
    @ApiModelProperty(value = "建设单位电话",name="buildDeptPhone")
    private String buildDeptPhone;
    /**
    * 维护单位名称
    */
    @ApiModelProperty(value = "维护单位名称",name="maintainDeptName")
    private String maintainDeptName;
    /**
    * 维护单位负责人
    */
    @ApiModelProperty(value = "维护单位负责人",name="maintainDeptLeader")
    private String maintainDeptLeader;
    /**
    * 维护单位电话
    */
    @ApiModelProperty(value = "维护单位电话",name="maintainDeptPhone")
    private String maintainDeptPhone;
    /**
    * 招标日期
    */
    @ApiModelProperty(value = "招标日期",name="callBidDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date callBidDate;
    /**
    * 投标日期
    */
    @ApiModelProperty(value = "投标日期",name="throwBidDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date throwBidDate;
    /**
    * 中标日期
    */
    @ApiModelProperty(value = "中标日期",name="winBidDate")
    private Date winBidDate;
    /**
    * 竣工日期
    */
    @ApiModelProperty(value = "竣工日期",name="finishBidDate")
    private Date finishBidDate;
    /**
    * 验收日期
    */
    @ApiModelProperty(value = "验收日期",name="checkBidDate")
    private Date checkBidDate;
    /**
    * 维护开始日期
    */
    @ApiModelProperty(value = "维护开始日期",name="maintainStartDate")
    private Date maintainStartDate;
    /**
    * 维护结束日期
    */
    @ApiModelProperty(value = "维护结束日期",name="maintainEndDate")
    private Date maintainEndDate;
    /**
    * 项目启动时间
    */
    @ApiModelProperty(value = "项目启动时间",name="startAt")
    private Date startAt;
    /**
    * 项目验收时间
    */
    @ApiModelProperty(value = "项目验收时间",name="endAt")
    private Date endAt;
    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creatorId")
    private String creatorId;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createAt")
    private Date createAt;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="updaterId")
    private String updaterId;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="updateAt")
    private Date updateAt;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 项目管理员账号
     */
    @ApiModelProperty(value = "项目管理员账号",name = "adminAccount")
    private String adminAccount;


    /**
     * 项目管理员账号密码 ( 不存在数据库)
     */
    @ApiModelProperty(value = "项目管理员账号密码 ( 不存在数据库)",name = "passWord")
    private String passWord;

    /**
     * 设备箱数量
     */
    @ApiModelProperty(value = "设备箱数量",name = "boxNum")
    private Integer boxNum;


    /**
     * 摄像机数量
     */
    @ApiModelProperty(value = "摄像机数量",name = "cameraNum")
    private Integer cameraNum;

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

    /**
     * 系统图标
     */
    @ApiModelProperty(value = "系统图标",name="sysLogo")
    private String sysLogo;

    /**
     * 系统名字
     */
    @ApiModelProperty(value = "系统名字",name="sysName")
    private String sysName;
    

    /**
     * 系统编号
     */

    @ApiModelProperty(value = "系统编号", name = "applicationCode", example = "AS")
    protected String applicationCode;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号", name = "tenantCode", example = "aswl")
    protected String tenantCode;
}
