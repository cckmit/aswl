package com.aswl.as.asos.modules.ibrs.service;

import com.alibaba.fastjson.JSON;
import com.aswl.as.asos.modules.ibrs.entity.AsIbrsAsDevice;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-25
 */
public interface IAsIbrsAsDeviceService extends IService<AsIbrsAsDevice> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsIbrsAsDevice entity);

    public boolean updateEntityById(AsIbrsAsDevice entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public ResponseBean<Boolean> osDevice2(DeviceDto deviceDto);

    ResponseBean<Boolean> test(DeviceDto deviceDto);

    ResponseBean<DeviceVo> osDevice3(String id);

    ResponseBean<Boolean> osDevice4(DeviceDto deviceDto);

    ResponseBean<Boolean> osDevice5(Device device);

    public ResponseBean<PageInfo<DeviceVo>> osDevice6(String pageNum,String pageSize,String sort,String order, String regionId, DeviceDto deviceDto);

    public ResponseBean<List<Map<String, Object>>> osDevice7(List<DeviceDto> deviceDtos);

    public ResponseBean<List<Device>> osDevice8(DeviceDto deviceDto);

    public ResponseBean<DeviceVo> osDevice9(String parentId);

    public ResponseBean<List<DeviceVo>> osDevice10(String id);

    public ResponseBean<Boolean> osDevice11(String id);

}
