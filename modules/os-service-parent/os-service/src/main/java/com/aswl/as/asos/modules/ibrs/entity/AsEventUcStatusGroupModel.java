package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事件状态组-设备型号
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsEventUcStatusGroupModel对象", description="事件状态组-设备型号")
@TableName(value = "as_event_uc_status_group_model")
public class AsEventUcStatusGroupModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "扩展状态组ID")
    private String eventStatusGroupId;

    @ApiModelProperty(value = "设备型号ID")
    private String deviceModelId;

    @ApiModelProperty(value = "端口数量")
    private Integer portNum;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "创建人账号")
    private String createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "更新人账号")
    private String updateBy;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;


}
