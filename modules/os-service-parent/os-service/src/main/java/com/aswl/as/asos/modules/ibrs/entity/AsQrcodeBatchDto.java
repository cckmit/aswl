package com.aswl.as.asos.modules.ibrs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 二维码批次表
 * </p>
 *
 * @author df
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsQrcodeBatch对象", description="二维码批次表")
public class AsQrcodeBatchDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "设备型号ID")
    private String deviceModelId;

    @ApiModelProperty(value = "设备型号名")
    private String deviceModelName;

    @ApiModelProperty(value = "区域名称",name="areaName")
    private String areaName;

    @ApiModelProperty(value = "生产厂家",name="manufacturer")
    private String manufacturer;

    @ApiModelProperty(value = "售后服务电话",name="afterSalesServiceTel")
    private String afterSalesServiceTel;

    @ApiModelProperty(value = "售后技术支持电话",name="afterSalesSupportTel")
    private String afterSalesSupportTel;

    @ApiModelProperty(value = "原厂客服电话",name="manufacturerTel")
    private String manufacturerTel;

    @ApiModelProperty(value = "附件",name="docFile")
    private String docFile;

    @ApiModelProperty(value = "起始序列号")
    private String qrcodeStartSn;

    @ApiModelProperty(value = "结束序列号")
    private String qrcodeEndSn;

    @ApiModelProperty(value = "二维码数量")
    private Integer qrcodeNum;

    @ApiModelProperty(value = "压缩包路径")
    private String zipUrl;

    @ApiModelProperty(value = "出产日期")
    private  String produceDate;

    @ApiModelProperty(value = "二维码名称串")
    private  String qrcodeSns;

    @ApiModelProperty(value = "logo图片")
    private  String logo;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd ",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "ID字符串")
    private String[] ids;

    @ApiModelProperty(value = "压缩包名称")
    private String zipname;

    @ApiModelProperty(value = "压缩包名称")
    private Integer qrCodeStartNum;

}
