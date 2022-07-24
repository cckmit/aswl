package com.aswl.as.ibrs.api.module;
import java.sql.Time;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核时段设置Entity
* @author hfx
* @date 2020-03-19 10:20
*/

@ApiModel(value = "ExamineTimePartConfig",description = "考核时段设置Entity")
@Data
public class ExamineTimePartConfig extends BaseEntity<ExamineTimePartConfig> {

	/**
	 * 考核项ID
	 */

	@ApiModelProperty(value = "考核项ID", name = "examineItemId")
	private String examineItemId;
	/**
	 * 考核开始时段
	 */

	@ApiModelProperty(value = "考核开始时段", name = "beginTime")
	@JsonFormat(pattern = "HH:mm",timezone="GMT+8")
	private Date beginTime;

	/**
	 * 考核开始时段
	 */

	@ApiModelProperty(value = "考核结束时段", name = "endTime")
	@JsonFormat(pattern = "HH:mm",timezone="GMT+8")
	private Date endTime;

	/**
	 * 响应时长数量
	 */
	@ApiModelProperty(value = "响应时长数量", name = "respondNum")
	private Integer respondNum;

	/**
	 * 响应时长单位
	 */
	@ApiModelProperty(value = "响应时长单位", name = "respondUnit")
	private String respondUnit;

	/**
	 * 响应时长（时间戳，单位为秒）
	 */
	@ApiModelProperty(value = "响应时长（时间戳，单位为秒）", name = "respondTime")
	private Integer respondTime;

	/**
	 * 存储时间
	 */
	@ApiModelProperty(value = "存储时间", name = "storeTime")
	private Date storeTime;

}