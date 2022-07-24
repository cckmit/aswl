package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @Description
 * @date 2019-11-01 19:56
 * @Version V1
 */
@Data
public class FlowRunStatisticsVo implements  java.io.Serializable{


    /**
     * 未派单
     */
    @ApiModelProperty(value = "未派单",name = "noDispatchCount")
    private Integer noDispatchCount;

    /**
     * 已派单
     */
    @ApiModelProperty(value = "已派单",name = "dispatchCount")
    private Integer dispatchCount;
    
    /**
     * 已完成数
     */
    @ApiModelProperty(value = "已完成",name = "finished")
    private Integer finishedCount;

    /**
     * 修复中数
     */
    @ApiModelProperty(value = "修复中数",name = "repairCount")
    private Integer repairCount;

    /**
     * 修复时长(秒)
     */
    @ApiModelProperty(value = "修复时长(秒)",name = "avgFinished")
    private Integer avgFinished;

    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人",name = "userName")
    private String leaderUserName;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称",name = "regionName")
    private String regionName;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称",name = "projectName")
    private String projectName;
    

}
