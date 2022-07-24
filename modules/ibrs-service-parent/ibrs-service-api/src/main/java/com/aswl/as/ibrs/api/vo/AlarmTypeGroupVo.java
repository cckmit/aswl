package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dingfei
 * @version 1.0.0
 * @create 2019/10/21 16:29
 */
@Data
public class AlarmTypeGroupVo implements Serializable {

    @ApiModelProperty(value = "统计时间",name = "statisticTime")
    private String statisticTime;

    @ApiModelProperty(value = "告警类型列表",name = "items")
    private List<AlarmTypeStatisticsVo> items;

    public List<AlarmTypeStatisticsVo> getItems() {
        if (this.items==null){
           items= new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<AlarmTypeStatisticsVo> items) {
        this.items = items;
    }
}
