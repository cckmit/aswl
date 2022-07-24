package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/** 设备二维码信息
 * @author df
 * @date 2020/12/10 14:39
 */
@Data
public class QrCodeDeviceVo implements Serializable {
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 设备型号名称
     */
    private String deviceModelName;

    /**
     * 出产日期
     */
    private String produceDate;
    

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
     * 二维码
     */
   private String qrCode;

    /**
     * 出厂编码
     */
    private String factoryCode;

    /**
     * 是否在用
     */
    private Integer isUsed;

}
