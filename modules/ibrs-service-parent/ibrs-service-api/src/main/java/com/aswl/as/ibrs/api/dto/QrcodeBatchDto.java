package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考勤全部记录Dto
* @author df
* @date 2020/12/17 13:58
*/

@ApiModel(value = "QrcodeBatchDto",description = "考勤全部记录Dto")
@Data
public class QrcodeBatchDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
    /**
    * 项目名称
    */
    @ApiModelProperty(value = "项目名称",name="projectName")
    private String projectName;
    /**
    * 设备型号ID
    */
    @ApiModelProperty(value = "设备型号ID",name="deviceModelId")
    private String deviceModelId;
    /**
    * 设备型号名
    */
    @ApiModelProperty(value = "设备型号名",name="deviceModelName")
    private String deviceModelName;



    /**
     * 区域名称
     */

    @ApiModelProperty(value = "区域名称",name="areaName")
    private String areaName;

    /**
     * 生产厂家
     */

    @ApiModelProperty(value = "生产厂家",name="manufacturer")
    private String manufacturer;

    /**
     * 售后服务电话
     */

    @ApiModelProperty(value = "售后服务电话",name="afterSalesServiceTel")
    private String afterSalesServiceTel;


    /**
     * 售后技术支持电话
     */

    @ApiModelProperty(value = "售后技术支持电话",name="afterSalesSupportTel")
    private String afterSalesSupportTel;

    /**
     * 原厂客服电话
     */

    @ApiModelProperty(value = "原厂客服电话",name="manufacturerTel")
    private String manufacturerTel;

    /**
     * 附件
     */

    @ApiModelProperty(value = "附件",name="docFile")
    private String docFile;
    
    /**
    * 起始序列号
    */
    @ApiModelProperty(value = "起始序列号",name="qrcodeStartSn")
    private String qrcodeStartSn;
    /**
    * 结束序列号
    */
    @ApiModelProperty(value = "结束序列号",name="qrcodeEndSn")
    private String qrcodeEndSn;
    /**
    * 二维码数量
    */
    @ApiModelProperty(value = "二维码数量",name="qrcodeNum")
    private Integer qrcodeNum;
    /**
    * logo图片
    */
    @ApiModelProperty(value = "logo图片",name="logo")
    private String logo;
    /**
    * 压缩包路径
    */
    @ApiModelProperty(value = "压缩包路径",name="zipUrl")
    private String zipUrl;
    /**
    * 出产日期
    */
    @ApiModelProperty(value = "出产日期",name="produceDate")
    private String produceDate;
    /**
    * 创建日期
    */
    @ApiModelProperty(value = "创建日期",name="createDate")
    private Date createDate;
}
