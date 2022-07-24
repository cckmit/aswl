package com.aswl.as.asos.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统IP黑名单
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("as_os_sys_black_list")
@ApiModel(value="AsOsSysBlackList对象", description="系统IP黑名单")
public class SysBlackListEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "所属部门")
    private String orgCode;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "创建人登录名称")
    private String creator;

    @ApiModelProperty(value = "创建日期")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人登录名称")
    private String modifier;

    @ApiModelProperty(value = "更新日期")
    private LocalDateTime modifyDate;

    private Integer delFlag;

    @ApiModelProperty(value = "备注")
    private String remark;


}
