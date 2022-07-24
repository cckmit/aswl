package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 派单处理工单设置Entity
* @author hfx
* @date 2020-03-23 11:43
*/

@ApiModel(value = "AlarmOrderHandle",description = "派单处理工单设置Entity")
@Data
public class AlarmOrderHandle extends BaseEntity<AlarmOrderHandle> {

    /**
    * 自动处理已修复工单 0-自动 1-手动
    */

    @ApiModelProperty(value = "自动处理已修复工单 0-自动 1-手动",name="orderHandleType")
    private Integer orderHandleType;
    /**
    * 处理的管理人员，使用用户id
    */

    @ApiModelProperty(value = "处理的管理人员，使用用户id",name="handleUserId")
    private String handleUserId;

    /**
     * 派单类型（1：自动派单，2：手动派单，3：不派单）
     */
    @ApiModelProperty(value = "派单类型（1：自动派单，2：手动派单，3：不派单）",name="sendOrderType")
    private Integer sendOrderType;

    /**
     * APP通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "APP通知（0：禁用，1：启用）",name="appNotice")
    private Integer appNotice;

    /**
     * 短信通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "短信通知（0：禁用，1：启用）",name="smsNotice")
    private Integer smsNotice;

    /**
     * 邮件通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "邮件通知（0：禁用，1：启用）",name="emailNotice")
    private Integer emailNotice;
}
