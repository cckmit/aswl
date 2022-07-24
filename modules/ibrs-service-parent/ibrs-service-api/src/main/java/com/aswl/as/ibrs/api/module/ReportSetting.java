package com.aswl.as.ibrs.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报送人、报送人配置表Entity
 *
 * @author df
 * @date 2021/07/08 18:12
 */

@ApiModel(value = "ReportSetting", description = "报送人、报送人配置表Entity")
@Data
public class ReportSetting extends BaseEntity<ReportSetting> {

    /**
     * 主键
     */

    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    /**
     * 报送人
     */

    @ApiModelProperty(value = "报送人", name = "reportUser")
    private String reportUser;

    /**
     * 报送人邮箱
     */
    @ApiModelProperty(value = "报送人邮箱", name = "reportUserEmail")
    private String reportUserEmail;
    /**
     * 抄送人
     */

    @ApiModelProperty(value = "抄送人", name = "sendUser")
    private String sendUser;

    /**
     * 抄送人邮箱
     */
    @ApiModelProperty(value = "抄送人邮箱", name = "sendUserEmail")
    private String sendUserEmail;
    /**
     * 区域编码
     */

    @ApiModelProperty(value = "区域编码", name = "regionCode")
    private String regionCode;

    /**
     * 打印排版类型
     */
    @ApiModelProperty(value = "打印排版类型", name = "composeType")
    private String composeType;
}
