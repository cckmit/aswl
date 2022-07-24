package com.aswl.as.asos.modules.test.service;

import com.aswl.as.asos.modules.test.entity.Test;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 测试表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
public interface ITestService extends IService<Test> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(Test entity);

    public boolean updateEntityById(Test entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

}
