package com.aswl.as.asos.modules.ibrs.entity;

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
 * 事件状态组
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsEventUcStatusGroup对象", description="事件状态组")
@TableName(value = "as_event_uc_status_group")
public class AsEventUcStatusGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "StatusGroupName")
    private String statusGroupName;

    @ApiModelProperty(value = "最大端口数量")
    private Integer maxPortNum;


}
