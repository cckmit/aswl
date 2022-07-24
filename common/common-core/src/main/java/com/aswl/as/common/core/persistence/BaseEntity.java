package com.aswl.as.common.core.persistence;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.project.CloudCommon;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity基类
 *
 * @author aswl.com
 * @date 2018-08-24 18:58
 */
@Data
@NoArgsConstructor
@ApiModel(value = "BaseEntity", description = "Entity基类")
public class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */

	@ApiModelProperty(value = "主键", name = "id")
	protected String id;

	/**
	 * 创建者
	 */

	@ApiModelProperty(value = "创建者", name = "creator")
	protected String creator;

	/**
	 * 创建日期
	 */

	@ApiModelProperty(value = "创建日期", name = "createDate")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	protected Date createDate;

	/**
	 * 更新者
	 */

	@ApiModelProperty(value = "更新者", name = "modifier")
	protected String modifier;

	/**
	 * 更新日期
	 */

	@ApiModelProperty(value = "更新日期", name = "modifyDate")
	protected Date modifyDate;

	/**
	 * 删除标记 0:正常，1-删除
	 */

	@ApiModelProperty(value = "删除标记 0:正常，1-删除", name = "delFlag")
	protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;

	/**
	 * 系统编号
	 */

	@ApiModelProperty(value = "系统编号", name = "applicationCode", example = "AS")
	protected String applicationCode;

	/**
	 * 租户编号
	 */
	@ApiModelProperty(value = "租户编号", name = "tenantCode", example = "aswl")
	protected String tenantCode;

	/**
	 * 项目ID
	 */
	@ApiModelProperty(value = "项目ID", name = "projectId", example = "1")
	protected String projectId;

	/**
	 * 项目名称
	 */
	@ApiModelProperty(value = "项目名称", name = "projectName")
	protected String projectName;

	/**
	 * 是否为新记录
	 */

	@ApiModelProperty(value = "是否为新记录", name = "isNewRecord")
	protected boolean isNewRecord;

	/**
	 * ID数组
	 */

	@ApiModelProperty(value = "ID数组", name = "ids")
	protected String[] ids;

	/**
	 * ID字符串，多个用逗号隔开
	 */

	@ApiModelProperty(value = "ID字符串，多个用逗号隔开", name = "idString")
	protected String idString;

	public BaseEntity(String id) {
		this();
		this.id = id;
	}

	/**
	 * 是否为新记录
	 *
	 * @return boolean
	 */
	public boolean isNewRecord() {
		return this.isNewRecord || StringUtils.isBlank(this.getId());
	}

	/**
	 * 设置基本属性
	 *
	 * @param userCode        用户编码
	 * @param applicationCode 系统编号
	 */
	public void setCommonValue(String userCode, String applicationCode) {
		setCommonValue(userCode, applicationCode, SysUtil.getTenantCode(), SysUtil.getProjectId());
	}

	//注意，如果是云平台，如果表里有project_id的话，就不能使用这个方法，否则如果含有多个项目的话，project_id会存了多个项目id，
	// 比如 1,2,3 应该使用setCommonValue(String userCode, String applicationCode, String tenantCode, String projectId)，前端直接传projectId过来
	public void setCommonValue(String userCode, String applicationCode, String tenantCode) {
		setCommonValue(userCode, applicationCode, tenantCode, SysUtil.getProjectId());
	}

	/**
	 * 设置基本属性
	 *
	 * @param userCode        用户编码
	 * @param applicationCode 系统编号
	 * @param tenantCode      租户编号
	 */
	public void setCommonValue(String userCode, String applicationCode, String tenantCode, String projectId) {
		Date currentDate = DateUtils.asDate(LocalDateTime.now());
		if (this.isNewRecord()) {
			this.setId(IdGen.snowflakeId());
			this.setNewRecord(true);
			this.creator = userCode;
			this.createDate = currentDate;
			if (!CloudCommon.isCloud()) {
				this.projectId = projectId;
			}

		}
		this.modifier = userCode;
		this.modifyDate = currentDate;
		this.delFlag = 0;
		this.applicationCode = applicationCode;
		this.tenantCode = tenantCode;
	}

	/**
	 * 设置基本属性
	 *
	 * @param userCode        用户编码
	 * @param applicationCode 系统编号
	 * @param tenantCode      租户编号
	 */
	public void setCommonValue_meta(String userCode, String applicationCode, String tenantCode, String projectId) {
		Date currentDate = DateUtils.asDate(LocalDateTime.now());
		if (this.isNewRecord()) {
			this.setId(IdGen.snowflakeId());
			this.setNewRecord(true);
			this.creator = userCode;
			this.createDate = currentDate;
		}
		this.modifier = userCode;
		this.modifyDate = currentDate;
		this.delFlag = 0;
		this.applicationCode = applicationCode;
		this.tenantCode = tenantCode;
	}
}
