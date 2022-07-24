package com.aswl.as.user.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统值班员/项目值班员 DTO
 * @author df
 * @date 2021/07/01 16:18
 */
@Data
public class DutyUserDto implements Serializable {
    private String userIds;
    private List<UserDto> userDtoList=new ArrayList<>();
}
