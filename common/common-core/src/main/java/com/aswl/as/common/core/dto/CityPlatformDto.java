package com.aswl.as.common.core.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@ApiModel(value = "CityPlatformDto",description = "下级平台与市级平台数据传输对象")
public class CityPlatformDto<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 下级平台所属的行政区域名称
     */
    private String cityName;
    /**
     * 下级平台所属的行政区域code
     */
    private String cityCode;

    /**
     * 下级平台项目code
     */
    private String projectCode;
    /**
     * 下级平台项目名
     */
    private String projectName;

    /**
     * 下级平台projectId
     */
    private String projectId;

    /**
     * 标识
     */
    private String flag;
    private T data;

    public CityPlatformDto() {
    }

    public CityPlatformDto(String cityName, String cityCode, String projectCode, String projectName, String projectId, String flag, T data) {
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.projectId = projectId;
        this.flag = flag;
        this.data = data;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
