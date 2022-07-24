package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 派单处理工单设置
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsAlarmOrderHandle对象", description="派单处理工单设置")
public class AsAlarmOrderHandle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "自动处理已修复工单 0-自动 1-手动")
    private Integer orderHandleType;

    @ApiModelProperty(value = "处理的管理人员，使用用户id")
    private String handleUserId;

    @ApiModelProperty(value = "修改者")
    private String modifier;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime modifyDate;

    @ApiModelProperty(value = "创建着")
    private String creator;

    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "SAAS租户编码")
    private String tenantCode;


}
