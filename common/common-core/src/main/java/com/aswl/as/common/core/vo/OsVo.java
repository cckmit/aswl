package com.aswl.as.common.core.vo;

import cn.hutool.crypto.SecureUtil;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


//本类为了简单限制普通访问
public class OsVo {

    public static final String CHECK_STRING="randomStr";

    // 这个可以从数据库读一个字段或者一开始请求的时候就传递设置
    private static String KEY_STRING="c4ca4238a0b923820dcc509a6f75849b";

    //1分钟(接收到的时间) 虽然请求默认超时时间为1分钟，但是设置两分钟
    private static int TIME_OUT_IN_MILLISECONDS =60000;

    // 需要缩减随机码的长度，截成32位
    //随机码的长度
    private static int RANDOM_STRING_LENGTH=32;

    /**
     * 校验错误时，默认返回的错误
     */
    public static ResponseBean ERR_RESPONSE_BEAN=new ResponseBean(new CommonException("获取数据接口错误"));

    public static boolean isWrongRandomStr(String s)
    {
        if(s!=null && s.length()==RANDOM_STRING_LENGTH && System.currentTimeMillis()-Long.valueOf(s.substring(0,13))<TIME_OUT_IN_MILLISECONDS )
        {
            if(SecureUtil.md5(s.substring(6,13)+KEY_STRING).substring(16).equals(s.substring(14,30)))
            {
                return false;
            }
        }
        return true;
    }

    public static String getRandomStr()
    {
        String timeString=String.valueOf(System.currentTimeMillis());
        String uuid= UUID.randomUUID().toString().replace("-","");
        return timeString+uuid.substring(0,1)+SecureUtil.md5(timeString.substring(6)+KEY_STRING).substring(16)+(uuid.substring(1,3));
    }

    /* */
    public static boolean checkRandomStr(String s)
    {
        return !isWrongRandomStr(s);
    }


    //随机4位数字

}
