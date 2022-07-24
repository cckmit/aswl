package com.aswl.as.asos.modules.asuser.service.impl;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.SysConfig;
import com.aswl.as.asos.modules.asuser.mapper.SysConfigMapper;
import com.aswl.as.asos.modules.asuser.service.ISysConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author df
 * @date 2021/11/29 14:59
 */
@Service
@Slf4j
@AllArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    
    
    @DataSource("slave2")
    public boolean saveEntity(SysConfig sysConfig) {
        return this.save(sysConfig);
    }
}
