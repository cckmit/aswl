package com.aswl.as.ibrs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmTypeDeviceFavoriteDto;
import com.aswl.as.ibrs.api.dto.AlarmTypeUserFavoriteDto;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.ibrs.api.module.AlarmTypeUserFavorite;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.api.vo.UserWatchVo;
import com.aswl.as.ibrs.service.AlarmTypeDeviceFavoriteService;
import com.aswl.as.ibrs.service.AlarmTypeService;
import com.aswl.as.ibrs.service.AlarmTypeUserFavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户关注Controller
 *
 * @author jk
 * @version 1.0.0
 * @create 2019/10/31 15:19
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/userWatch", tags = "用户关注")
@RestController
@RequestMapping("/v1/userWatch")
public class UserWatchController {
    private final AlarmTypeUserFavoriteService userFavoriteService;
    private final AlarmTypeDeviceFavoriteService deviceFavoriteService;
    private final AlarmTypeService alarmTypeService;

    @GetMapping(value = "findUserWatchAlarmType")
    @ApiOperation(value = "获取用户的关注报警类型")
    public ResponseBean<List<Map>> findUserWatchAlarmType(@RequestParam(value = "userId") String userId) {

        List<UserWatchVo> userWatchAlarmType = userFavoriteService.findUserWatchAlarmType(userId);
        List<Map> listVo = new ArrayList<>();
        //抽取组名相同的元素
        Map<String, List<UserWatchVo>> map = new LinkedHashMap<>();
        for (UserWatchVo UserWatchVo : userWatchAlarmType) {
            List<UserWatchVo> list = new ArrayList<>();
            if (UserWatchVo.getGroupName() != null) {
                if (map.containsKey(UserWatchVo.getGroupName())) {
                    map.get(UserWatchVo.getGroupName()).add(UserWatchVo);
                } else {
                    list.add(UserWatchVo);
                    map.put(UserWatchVo.getGroupName(), list);
                }
            }
        }
        listVo.add(map);
        return new ResponseBean<>(listVo);
    }

    /**
     * 新增用户关注报警
     *
     * @param
     * @return
     */
    @PostMapping(value = "user")
    @ApiOperation(value = "新增用户关注报警", notes = "新增用户关注报警")
    @Log(value = "新增用户关注报警",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertUserWatch(@RequestBody @Valid String users) {
       /* AlarmTypeUserFavorite alarmTypeUser = new AlarmTypeUserFavorite();
        BeanUtils.copyProperties(userFavorite, alarmTypeUser);
        alarmTypeUser.setId(IdGen.snowflakeId());
        return new ResponseBean<>(userFavoriteService.insert(alarmTypeUser) > 0);*/
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AlarmTypeUserFavoriteDto.class);
        List<AlarmTypeUserFavoriteDto> alarmTypeDeviceFavorite = null;
        try {
            alarmTypeDeviceFavorite = mapper.readValue(users, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AlarmTypeUserFavorite> typeDeviceFavorites = new ArrayList<>();
        for (AlarmTypeUserFavoriteDto typeDeviceFavorite : alarmTypeDeviceFavorite) {
            AlarmTypeUserFavorite alarmTypeDevice = new AlarmTypeUserFavorite();
            BeanUtils.copyProperties(typeDeviceFavorite, alarmTypeDevice);
            alarmTypeDevice.setId(IdGen.snowflakeId());
            typeDeviceFavorites.add(alarmTypeDevice);
        }
        return new ResponseBean<>(userFavoriteService.batchInsert(typeDeviceFavorites) > 0);
    }

    /**
     * 修改用户关注报警
     *
     * @param
     * @return
     */
    @PutMapping(value = "user")
    @ApiOperation(value = "修改用户关注报警", notes = "修改用户关注报警")
    @Log(value = "修改用户关注报警",businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateUserWatch(@RequestBody @Valid String users) {
        /*AlarmTypeUserFavorite userFavorite = new AlarmTypeUserFavorite();
        BeanUtils.copyProperties(alarmTypeUserFavoriteDto, userFavorite);
        return new ResponseBean<>(userFavoriteService.update(userFavorite) > 0);*/
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AlarmTypeUserFavoriteDto.class);
        List<AlarmTypeUserFavoriteDto> alarmTypeDeviceFavorite = null;
        try {
            alarmTypeDeviceFavorite = mapper.readValue(users, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AlarmTypeUserFavorite> typeDeviceFavorites = new ArrayList<>();
        for (AlarmTypeUserFavoriteDto typeDeviceFavorite : alarmTypeDeviceFavorite) {
            AlarmTypeUserFavorite alarmTypeDevice = new AlarmTypeUserFavorite();
            BeanUtils.copyProperties(typeDeviceFavorite, alarmTypeDevice);
            alarmTypeDevice.setId(IdGen.snowflakeId());
            typeDeviceFavorites.add(alarmTypeDevice);
        }
        return new ResponseBean<>(userFavoriteService.batchUpdate(typeDeviceFavorites) > 0);
    }

    /**
     * 查询设备的报警类型
     *
     * @param alarmLevel
     * @return
     */
    @GetMapping(value = "findDeviceAlarmType")
    @ApiOperation(value = "查询设备关联报警类型")
    public ResponseBean<List<Map>> findAlarmType(@RequestParam(value = "alarmLevel", required = false) String alarmLevel,
                             @RequestParam(value = "id", required = false) String id,
                             @RequestParam(value = "kind", required = false) String kind) {
        String[] alarmLevels = null;
        if (alarmLevel != null && !"".equals(alarmLevel)) {
            alarmLevels = alarmLevel.split(",");
        }
        List<AlarmTypeVo> alarmTypeList = alarmTypeService.findDeviceAlarmType(alarmLevels, id, kind);
        List<Map> listVo = new ArrayList<>();
        //抽取组名相同的元素
        Map<String, List<AlarmTypeVo>> map = new LinkedHashMap<>();
        for (AlarmTypeVo alarmTypeVo : alarmTypeList) {
            List<AlarmTypeVo> list = new ArrayList<>();
            if (map.containsKey(alarmTypeVo.getGroupName())) {
                map.get(alarmTypeVo.getGroupName()).add(alarmTypeVo);

            } else {
                list.add(alarmTypeVo);
                map.put(alarmTypeVo.getGroupName(), list);
            }
        }
        listVo.add(map);
        return new ResponseBean<>(listVo);
    }

    @GetMapping(value = "findUserWatchDeviceAlarmType")
    @ApiOperation(value = "获取用户的关注设备报警类型")
    public ResponseBean<List<Map>> findUserWatchDeviceAlarmType(@RequestParam(value = "userId", required = false) String userId,
                                            @RequestParam(value = "deviceId", required = false) String deviceId) {
        List<UserWatchVo> userWatchAlarmType = deviceFavoriteService.findUserWatchDeviceAlarmType(userId, deviceId);
        List<Map> listVo = new ArrayList<>();
        //抽取组名相同的元素
        Map<String, List<UserWatchVo>> map = new LinkedHashMap<>();
        if (userWatchAlarmType == null) {
            return new ResponseBean<>(new ArrayList<>());
        }
        for (UserWatchVo UserWatchVo : userWatchAlarmType) {
            if (UserWatchVo.getGroupName() != null) {
                if (map.containsKey(UserWatchVo.getGroupName())) {
                    map.get(UserWatchVo.getGroupName()).add(UserWatchVo);
                } else {
                    List<UserWatchVo> list = new ArrayList<>();
                    list.add(UserWatchVo);
                    map.put(UserWatchVo.getGroupName(), list);
                }
            }
        }
        listVo.add(map);
        return new ResponseBean<>(listVo);
    }

    /**
     * 获取用户关注的设备集合
     * @param userId
     * @return
     */
    @GetMapping(value = "findUserWatchDevices")
    @ApiOperation(value = "获取用户关注的设备")
    public ResponseBean<List<Map>> findUserWatchDevices(@RequestParam(value = "userId", required = false) String userId) {
       return new ResponseBean<>(deviceFavoriteService.findUserWatchDevices(userId));
    }

    @PostMapping(value = "insert")
    @ApiOperation(value = "新增用户关注报警设备", notes = "新增用户关注报警设备")
    @Log(value = "新增用户关注报警设备",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertUserWatchDevice(@RequestBody @Valid AlarmTypeDeviceFavoriteDto alarmTypeDeviceFavorite) {
        AlarmTypeDeviceFavorite alarmTypeDevice = new AlarmTypeDeviceFavorite();
        BeanUtils.copyProperties(alarmTypeDeviceFavorite, alarmTypeDevice);
        alarmTypeDevice.setId(IdGen.snowflakeId());
        return new ResponseBean<>(deviceFavoriteService.insert(alarmTypeDevice) > 0);
    }

    @PutMapping(value = "update")
    @ApiOperation(value = "修改用户关注报警设备", notes = "修改用户关注报警设备")
    @Log(value = "修改用户关注报警设备",businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateUserWatchDevice(@RequestBody @Valid AlarmTypeDeviceFavoriteDto alarmTypeDeviceFavoriteDto) {
        AlarmTypeDeviceFavorite alarmTypeDeviceFavorite = new AlarmTypeDeviceFavorite();
        BeanUtils.copyProperties(alarmTypeDeviceFavoriteDto, alarmTypeDeviceFavorite);
        return new ResponseBean<>(deviceFavoriteService.update(alarmTypeDeviceFavorite) > 0);
    }

    /**
     * 批量新增用户关注的设备报警
     *
     * @param devices
     * @return
     */
    @PostMapping(value = "batchInsert")
    @ApiOperation(value = "批量新增用户关注报警设备", notes = "批量新增用户关注报警设备")
    @Log(value = "批量新增用户关注报警设备",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> batchInsertUserWatchDevice(@RequestBody @Valid String devices) {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AlarmTypeDeviceFavoriteDto.class);
        List<AlarmTypeDeviceFavoriteDto> alarmTypeDeviceFavorite = null;
        try {
            alarmTypeDeviceFavorite = mapper.readValue(devices, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AlarmTypeDeviceFavorite> typeDeviceFavorites = new ArrayList<>();
        for (AlarmTypeDeviceFavoriteDto typeDeviceFavorite : alarmTypeDeviceFavorite) {
            AlarmTypeDeviceFavorite alarmTypeDevice = new AlarmTypeDeviceFavorite();
            BeanUtils.copyProperties(typeDeviceFavorite, alarmTypeDevice);
            alarmTypeDevice.setId(IdGen.snowflakeId());
            typeDeviceFavorites.add(alarmTypeDevice);
        }
        return new ResponseBean<>(deviceFavoriteService.batchInsert(typeDeviceFavorites) > 0);
    }

    @PutMapping(value = "batchUpdate")
    @ApiOperation(value = "批量修改用户关注报警设备", notes = "批量修改用户关注报警设备")
    @Log(value = "批量修改用户关注报警设备",businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> batchUpdateUserWatchDevice(@RequestBody @Valid String devices) {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, AlarmTypeDeviceFavoriteDto.class);
        List<AlarmTypeDeviceFavoriteDto> alarmTypeDeviceFavorite = null;
        try {
            alarmTypeDeviceFavorite = mapper.readValue(devices, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<AlarmTypeDeviceFavorite> typeDeviceFavorites = new ArrayList<>();
        for (AlarmTypeDeviceFavoriteDto typeDeviceFavorite : alarmTypeDeviceFavorite) {
            AlarmTypeDeviceFavorite alarmTypeDevice = new AlarmTypeDeviceFavorite();
            BeanUtils.copyProperties(typeDeviceFavorite, alarmTypeDevice);
            typeDeviceFavorites.add(alarmTypeDevice);
        }
        return new ResponseBean<>(deviceFavoriteService.batchUpdate(typeDeviceFavorites) > 0);
    }

    /**
     * 根据用户Id查询关注的告警类型列表
     * @param userId  用户Id
     * @return
     */
    @GetMapping(value = "/getUserAlarmType/list/{userId}")
    @ApiOperation(value = "根据设备id与用户Id查询关注的告警类型")
    public  List<AlarmTypeDeviceFavorite> getAlarmTypeDeviceFavoriteByUserId(@PathVariable("userId") String userId) {
    	AlarmTypeDeviceFavorite entity = new AlarmTypeDeviceFavorite();
    	entity.setUserId(userId);
        return deviceFavoriteService.findList(entity);
    }
    
    /**
     * 根据设备id与用户Id查询关注的告警类型
     * @param userId  用户Id
     * @param deviceId  设备Id
     * @return
     */
    @GetMapping(value = "/getUserAlarmType/{deviceId}/{userId}")
    @ApiOperation(value = "根据设备id与用户Id查询关注的告警类型")
    public  UserWatchVo getAlarmTypeDeviceFavoriteBydeviceId(@PathVariable("deviceId") String deviceId,@PathVariable("userId") String userId) {
       return deviceFavoriteService.getAlarmTypeDeviceFavoriteBydeviceId(userId, deviceId);
    }

    /**
     *  根据用户Id和设备Id查询用户关注
     * @param userId
     * @param deviceId
     * @return AlarmTypeDeviceFavorite
     */
    @GetMapping(value = "/getFavorite/{userId}/{deviceId}")
    @ApiOperation(value = "根据用户Id和设备Id查询用户关注")
    public AlarmTypeDeviceFavorite getFavorite(@PathVariable("userId") String userId,@PathVariable("deviceId") String deviceId){
        return deviceFavoriteService.getFavorite(userId,deviceId);
    }

    /**
     * 用户告警类型订阅设置
     * @param alarmTypeUserFavorite
     * @return ResponseBean
     */
    @PostMapping(value = "/updateAlarmTypeUserFavorite")
    @ApiOperation(value = "用户告警类型订阅设置")
    public ResponseBean<Boolean> updateAlarmTypeUserFavorite(@RequestBody AlarmTypeUserFavorite alarmTypeUserFavorite){
        return new ResponseBean<>(userFavoriteService.insertAlarmTypeUserFavorite(alarmTypeUserFavorite)> 0 );
    }

    /**
     * 查看用户订阅的告警类型
     * @param userId
     * @return ResponseBean
     */
    @GetMapping(value = "/findByUserId")
    @ApiOperation(value = "查看用户订阅的告警类型")
    public ResponseBean<AlarmTypeUserFavorite> findByUserId(@RequestParam("userId") String userId){
        return new ResponseBean<>(userFavoriteService.findByUserId(userId) );
    }
    
}
