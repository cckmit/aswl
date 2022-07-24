package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-25 19:46
 * @Version V1
 */
@Data
public class DeviceStatusGroupVo implements java.io.Serializable {

    /**
     * 状态组显示名称
     */
  @ApiModelProperty(value = "状态组显示名称",name = "name")
    private String name;

  /**
   * 状态组显示英文
   */
  @ApiModelProperty(value = "状态组显示英文",name = "nameEn")
  private String nameEn;

    /**
     * 状态组名称
     */

    @ApiModelProperty(value = "状态组名称",name = "statusGroupName")
    private String statusGroupName;

    /**
     * 组内状态位list
     */

    @ApiModelProperty(value = "组内状态位list",name = "deviceStatusVoList")
    private List<DeviceStatusVo> deviceStatusVoList = new ArrayList();
}
