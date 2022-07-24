package com.aswl.as.ibrs.api.vo;

import com.aswl.as.ibrs.api.module.ExamineStatisticsRecord;
import lombok.Data;
import org.apache.commons.collections4.map.LinkedMap;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class ExamineStatisticsVo implements Serializable {

//    List<LinkedHashMap<String,LinkedHashMap>> resMaps;

    LinkedList<Map> resMaps;
//    List<LinkedHashMap<String,List<LinkedHashMap>>> deductDetailsRes;

    List<Map<String,List<List<Map>>>> details;

    //考核标准id
    String standardId;
}
