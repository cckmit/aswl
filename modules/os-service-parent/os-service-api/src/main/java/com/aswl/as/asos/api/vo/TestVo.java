package com.aswl.as.asos.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-24 14:29
 * @Version V1
 */
@Data
public class TestVo {
    /**
     * 故障数量
     */
    @ApiModelProperty(value = "故障数量",name = "counts")
    private String counts;
    /**
     * 故障时间
     */
    @ApiModelProperty(value = "故障时间",name = "gatherTime")
    private String gatherTime;
}
