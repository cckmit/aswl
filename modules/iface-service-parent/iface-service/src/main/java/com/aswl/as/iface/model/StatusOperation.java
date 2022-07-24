package com.aswl.as.iface.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/31 13:34
 */
@Data
@ApiModel(value = "状态操作",description = "状态操作")
public class StatusOperation implements Serializable {
    /**
     * 标题
     */

    @ApiModelProperty(value = "标题",name = "title")
    private String title;

    /**
     * 操作名称
     */

    @ApiModelProperty(value = "操作名称",name = "operName")
    private String operName;

    /**
     * 操作编码
     */

    @ApiModelProperty(value = "操作编码",name = "operCode")
    private String operCode;

    /**
     * 排序
     */

    @ApiModelProperty(value = "排序",name = "operSort")
    private String operSort;

    /**
     * 端口序号
     */
    @ApiModelProperty(value = "端口序号",name = "portSerial")
    private Integer portSerial;
}
