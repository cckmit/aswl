package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息阅读表Dto
 *
 * @author df
 * @date 2021/12/08 10:08
 */

@ApiModel(value = "MsgReadRecordDto", description = "消息阅读表Dto")
@Data
public class MsgReadRecordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 业务代码
     */
    @ApiModelProperty(value = "业务代码", name = "businessKey")
    private String businessKey;
    /**
     * 关联ID
     */
    @ApiModelProperty(value = "关联ID", name = "relatedId")
    private String relatedId;
    /**
     * 阅读用户ID集（逗号分隔）
     */
    @ApiModelProperty(value = "阅读用户ID集（逗号分隔）", name = "readUserIds")
    private String readUserIds;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId")
    private String userId;
}
