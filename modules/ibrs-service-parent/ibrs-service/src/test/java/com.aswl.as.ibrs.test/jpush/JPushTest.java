package com.aswl.as.ibrs.test.jpush;

import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.JPushUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JPushTest {

    @Test
    public void pushMsg(){
        Map<String,String> extras=new HashMap<>();
        extras.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.WORK_AUDIT.getCode());
        extras.put("workOrderId", "541651335135");
        JPushUtils.JPushResult jPushResult = JPushUtils.sendAlias("测试", "工单处理 待审核测试", extras, 300, new String[]{"888818338564804608"} );
        log.info("jPushResult: " + JSON.toJSONString(jPushResult));
    }
}
