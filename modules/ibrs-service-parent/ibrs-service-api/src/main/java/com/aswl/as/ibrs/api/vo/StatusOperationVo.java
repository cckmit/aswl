package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-28 13:18
 * @Version V1
 */
@Data
public class StatusOperationVo implements Serializable {

    /**
     * ID
     */
    @ApiModelProperty(value = "主键",name = "id")
    private String id;
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
