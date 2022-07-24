package com.aswl.as.asos.modules.asuser.service;
import com.aswl.as.asos.modules.asuser.entity.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author df
 * @date 2021/11/29 14:57
 */
public interface ISysConfigService extends IService<SysConfig> {
    
    public boolean saveEntity(SysConfig sysConfig);
}
