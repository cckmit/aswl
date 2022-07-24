package com.aswl.as.metadata.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CityPlatformUtil {

    private  Boolean enable;
    private  Boolean isCityPlatform;
    private  String cityName;
    private  String cityCode;
    private  String projectCode;
    private  String projectName;

    @Autowired
    private Environment environment;

    public  Boolean getEnable() {
        return environment.getProperty("cityPlatform.enable",Boolean.class);
    }

    public  Boolean getIsCityPlatform() {
        return environment.getProperty("cityPlatform.platform.isCityPlatform",Boolean.class);
    }

    public  String getCityName() {
        return environment.getProperty("cityPlatform.platform.cityName");
    }

    public  String getCityCode() {
        return environment.getProperty("cityPlatform.platform.cityCode");
    }

    public  String getProjectCode() {
        return environment.getProperty("cityPlatform.platform.projectCode");
    }

    public  String getProjectName() {
        return environment.getProperty("cityPlatform.platform.projectName");
    }

    public Boolean isCityPlatform(){
        if(getEnable() == null || getIsCityPlatform() == null){
            return false;
        }
        return getEnable() && !getIsCityPlatform();
    }
}
