package com.aswl.as.asos.modules.asuser.entity;

import java.io.Serializable;

import com.aswl.as.common.core.persistence.BaseEntity;
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
 * 附件表
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysAttachment对象", description="附件表")
@TableName(value="sys_attachment")
public class AsUserSysAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "附件名称")
    private String attachName;

    @ApiModelProperty(value = "附件大小")
    private String attachSize;

    @ApiModelProperty(value = "组名称")
    private String groupName;

    @ApiModelProperty(value = "文件ID")
    private String fastFileId;

    @ApiModelProperty(value = "业务ID")
    private String busiId;

    @ApiModelProperty(value = "业务模块")
    private String busiModule;

    @ApiModelProperty(value = "业务类型 0-普通，1-头像")
    private String busiType;

    @ApiModelProperty(value = "预览地址")
    private String previewUrl;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private String modifyDate;

    @ApiModelProperty(value = "删除标记")
    private String delFlag;

    @ApiModelProperty(value = "系统编号")
    private String applicationCode;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;


}
