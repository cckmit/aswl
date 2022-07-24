package com.aswl.as.ibrs.api.vo;
import com.aswl.as.ibrs.api.module.DeviceModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author df
 * @date 2022/06/22 16:21
 */
@Data
public class DeviceModelJsonVo implements Serializable {
    
    private  DeviceModel deviceModel;
    private List<ExtendStatusJsonVo>  extendStatusJsonVos;
    private List<GroupExtendStatusJsonVo> groupExtendStatusJsonVos;
    
}
