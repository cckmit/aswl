package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AddressBase;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.DeviceAlarmVo;
import com.aswl.as.ibrs.mapper.AddressBaseMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AddressBaseService extends CrudService<AddressBaseMapper, AddressBase> {
    private final AddressBaseMapper addressBaseMapper;

    private final DeviceService deviceService;

    /**
     * 新增地址库表
     *
     * @param addressBase
     * @return int
     */
    @Transactional
    @Override
    public int insert(AddressBase addressBase) {
        return super.insert(addressBase);
    }

    /**
     * 删除地址库表
     *
     * @param addressBase
     * @return int
     */
    @Transactional
    @Override
    public int delete(AddressBase addressBase) {
        return super.delete(addressBase);
    }

    @Transactional
    public boolean bindAddressBaseDevice(AddressBase addressBase)
    {

        AddressBase a=null;
        Device d=null;
        boolean result = false;

        //校验
        if(StringUtils.isEmpty(addressBase.getBindDeviceId())||StringUtils.isEmpty(addressBase.getId()))
        {
            throw new CommonException("传递数据错误");
        }
        try
        {
            String tenantCode=SysUtil.getTenantCode();
            String projectId=SysUtil.getProjectId();

            a=this.get(addressBase);
            if(a==null)
            {
                throw new CommonException("没有该地址");
            }
            if(!tenantCode.equals(a.getTenantCode()))
            {
                throw new CommonException("地址 不是本租户的地址");
            }

            //TODO 暂时 地址绑定设备 不校验项目
//            String[]arr=projectId.split(",");
//            boolean inProject=false;
//            for(String temp:arr)
//            {
//                if(temp.equals(a.getProjectId()))
//                {
//                    inProject=true;
//                    break;
//                }
//            }
//            if(!inProject)
//            {
//                //报错
//                throw new CommonException("地址不在拥有的项目内");
//            }

            // TODO 先清除这个设备以前绑定过的点位
            Device tempD=new Device();
            tempD.setIds(new String[]{addressBase.getBindDeviceId()});
            List<Device> dList=deviceService.findListById(tempD);
            if(dList==null)
            {
                throw new CommonException("没有该设备");
            }
            d=dList.get(0);
            if(!tenantCode.equals(d.getTenantCode()))
            {
                throw new CommonException("绑定的 地址 跟 设备 的所在的租户不相同");
            }
//            if(a.getProjectId().equals(d.getProjectId()))
//            {
//                throw new CommonException("绑定的 地址 跟 设备 的所在的项目不相同");
//            }
            AddressBase temp = new AddressBase();
            temp.setBindDeviceId(addressBase.getBindDeviceId());
            List<AddressBase> list = addressBaseMapper.findList(temp);
            //设备没有与任何地址绑定
            if (list == null || list.size() == 0) {
                temp.setId(addressBase.getId());
                temp.setBindDeviceId(addressBase.getBindDeviceId());
                temp.setIsUsed(1);
                int row = addressBaseMapper.update(temp);
                if (row > 0) {
                    AddressBase ab = addressBaseMapper.get(temp);
                    d.setId(ab.getBindDeviceId());
                    d.setAddress(ab.getAddress());
                    d.setLatitude(ab.getLatitude());
                    d.setLongitude(ab.getLongitude());
                    int flag = deviceService.update(d);
                    result = flag > 0;
                    if (!result) {
                        throw new CommonException("传递数据错误");
                    }
                }
            } else {
                //设备已经绑定过地址库需要先解绑然后再绑定
                AddressBase addr = list.get(0);
                temp.setId(addr.getId());
                temp.setBindDeviceId("");
                temp.setIsUsed(0);
                int row = addressBaseMapper.update(temp);
                if (row > 0) {
                    //清除设备中绑定的地址
                    Device d1 = new Device();
                    d1.setId(addr.getBindDeviceId());

                    d.setId(addr.getBindDeviceId());
                    d.setAddress(addr.getAddress());
                    d.setLatitude(addr.getLatitude());
                    d.setLongitude(addr.getLongitude());
                    int flag = deviceService.update(d);
                    result =  flag > 0;
                    if (!result) {
                        throw new CommonException("传递数据错误");
                    }
                }
            }

//            a.setIp(d.getIp());
//             d.setAddress(a.getName());

            //真正更新

        }
        catch (CommonException e)
        {
            throw e;
        }
        catch (Exception e2)
        {
            throw new CommonException("传递数据错误");
        }

        // return update(a)>0;
        return result;
    }

    /**
     * 查询分页,专门给APP使用
     *
     * @param page   page
     * @param entity entity
     * @return PageInfo
     */
    public PageInfo<AddressBase> findPageForApp(PageInfo<AddressBase> page, AddressBase entity) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<AddressBase> list = dao.findListForApp(entity);
        List<AddressBase> result = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AddressBase addressBase : list) {
                if (addressBase.getBindDeviceId() == null || "".equals(addressBase.getBindDeviceId())) {
                    addressBase.setCameraCount(0);
                }else {
                    List<DeviceAlarmVo> devices = addressBaseMapper.findCamByDeviceParentId(addressBase.getBindDeviceId());
                    if (devices == null || devices.size() == 0) {
                        addressBase.setCameraCount(0);
                    }else{
                        //找出摄像头的数量
                        long count = devices.stream().filter(d -> d.getType() == 3).count();
                        addressBase.setCameraCount((int) count);
                    }
                }
                result.add(addressBase);
            }
        }
        return new PageInfo<AddressBase>(result);
    }
}