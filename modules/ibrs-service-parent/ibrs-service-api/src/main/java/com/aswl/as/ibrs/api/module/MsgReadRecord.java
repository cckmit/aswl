package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息阅读表Entity
 *
 * @author df
 * @date 2021/12/08 10:08
 */

@ApiModel(value = "MsgReadRecord", description = "消息阅读表Entity")
@Data
public class MsgReadRecord extends BaseEntity<MsgReadRecord> {
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
}
