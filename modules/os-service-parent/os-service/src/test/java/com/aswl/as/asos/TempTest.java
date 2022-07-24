package com.aswl.as.asos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.asos.common.utils.IbrsResponseBean;
import com.aswl.as.asos.common.utils.OsHttpUtil;

import java.util.*;

public class TempTest {

    public static void main(String[] args) {

//        System.out.println(Pinyin4jUtil.getFirstSpellPinYin("傲视物联",false));
//        System.out.println(Pinyin4jUtil.getFirstSpell("傲视物联"));

        /*
        for(int i=0;i<100;i++)
        {
            System.out.println(DateUtils.format(new Date(),DateUtils.DATE_TIME_MILLISECOND_PATTERN));
        }
        */

//        java.util.function.Function;
//        java.util.stream.BaseStream;

        List<String> l= Arrays.asList("a", "b", "c", "d");

        String t="c4ca4238a0b923820dcc509a6f75849b,1";


        Integer i1=1;

        Integer i2=2;

        System.out.println("i1.compareTo(i2)======"+i1.compareTo(i2));;


        List<String> tl=Arrays.asList(t.split(","));

        System.out.println(tl);

        try
        {
            /*
            String url="http://localhost:8000";
            OkHttpUtil.getInstance().get(url,null);
             */

            // 13
            //32

            /*
            String str="c4ca4238a0b923820dcc509a6f75849b";

            Long time=1234567890123L;

            String timeString=String.valueOf(time);

            String result= SecureUtil.md5(timeString.substring(6)+str);

            String uuid="12345678901234567890123456789012";

            String t=timeString+uuid.substring(0,16)+result+(uuid.substring(16));

            System.out.println(t);

            //下面是解析的逻辑

            String tempT=t.substring(0,13);
            System.out.println("time==="+tempT);

            String result2=SecureUtil.md5(tempT.substring(6)+str);

            if(result2.equals(t.substring(29,61)))
            {
                System.out.println("成功");
            }
            */

//            System.out.println(md5.digest(String.valueOf(l)));

            String KEY_STRING="c4ca4238a0b923820dcc509a6f75849b";

            /*
            String timeString=String.valueOf(System.currentTimeMillis());
            String uuid= UUID.randomUUID().toString().replace("-","");
            timeString+uuid.substring(0,13)+SecureUtil.md5(timeString.substring(6)+KEY_STRING)+(uuid.substring(13));
            */


            String timeString=String.valueOf(System.currentTimeMillis()); //13
            String uuid= UUID.randomUUID().toString().replace("-","");
            //SecureUtil.md5(timeString.substring(6)+"c4ca4238a0b923820dcc509a6f75849b").length(); //32
            String randomStr= timeString+uuid.substring(0,1)+SecureUtil.md5(timeString.substring(6)+"c4ca4238a0b923820dcc509a6f75849b").substring(16)+(uuid.substring(1,3)); //77

            System.out.println("randomStr.length()==="+randomStr.length());

            // 解析
            String s=randomStr;
            int RANDOM_STRING_LENGTH=32;
            int TIME_OUT_IN_MILLISECONDS =60000;
            if(s!=null && s.length()==RANDOM_STRING_LENGTH && System.currentTimeMillis()-Long.valueOf(s.substring(0,13))<TIME_OUT_IN_MILLISECONDS )
            {
                if(SecureUtil.md5(s.substring(6,13)+KEY_STRING).substring(16).equals(s.substring(14,30)))
                {
                    System.out.println("通过");
                }
            }



            String url="http://localhost:8096/v1/index/os/queryMap?randomStr="+randomStr;
            Map<String, Object> header=new HashMap<String,Object>();
//            header.put("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpblR5cGUiOiJQV0QiLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwidGVuYW50Q29kZSI6ImFzd2wiLCJleHAiOjE1NzU2MTQ2NDIsImF1dGhvcml0aWVzIjpbInN5czpyb2xlOmRlbCIsInN5czpkZXB0OmVkaXQiLCJzeXM6bWVudTppbXBvcnQiLCJzeXM6bWVudTpkZWwiLCJzeXM6bWVudTphZGQiLCJzeXM6dXNlcjphZGQiLCJzeXM6dXNlcjpleHBvcnQiLCJzeXM6cm9sZTplZGl0Iiwic3lzOmRlcHQ6ZGVsIiwic3lzOnVzZXI6ZWRpdCIsInN5czp1c2VyOmltcG9ydCIsInN5czpkZXB0OmFkZCIsInN5czpyb2xlOmF1dGgiLCJzeXM6bWVudTplZGl0Iiwic3lzOnJvbGU6YWRkIiwic3lzOm1lbnU6ZXhwb3J0Iiwic3lzOnVzZXI6ZGVsIl0sImp0aSI6IjJkYjdkODI0LWZmYjItNGY3OS04NDAxLTc5MTI1YzI1YzRmZiIsImNsaWVudF9pZCI6IndlYl9hcHAifQ.N3HG88qfTR3MW9I9sqbfWUJtHoOqDa783I9oSFq2rvPO7NVTAP5_2z9b54L2KmgELBjEAZgssfzN7UZ6cgYgzSCeu0Q0eJ5mtO8U5FJPWSbLU0f0nU7VLhQZCrOyC-MVkA0z2XrULusnBWuMZjnEadeJq0Puvbxc5elpTA1wGLEJK0On0BZs5G3XR5hhfFxr3_FiaZ4I_QCBLe9goeiByVy5U9EydL-ubR-hHn1siBHmTb_H7LDKy5xPyjrban3DTS-6Tj2Qh0RZ0jfVKW6IAbhfCfEsJhxiTdPxs6-CDtC6117vFMKheraC7_Y_DzgpI_UsJdLUFciGLbg1I_jjBw");
            header.put("Tenant-Code","aswl");
            header.put("Project-Id",null);
            String tempS= OsHttpUtil.getInstance().get(url,header);
//            OsOkHttpUtil.getInstance().postForm("",null,null);

            System.out.println("返回的数据为"+tempS);
//            JSONObject json=JSON.parseObject(s);

            JSONObject json=JSON.parseObject(tempS);
            IbrsResponseBean ibrsResponseBean=new IbrsResponseBean(
                    json.getIntValue("code"),json.getIntValue("status"),
                    json.getString("msg"),json.getJSONObject("data"));


            System.out.println("最终传给前端的数据为"+JSON.toJSONString(json));

            System.out.println(UUID.randomUUID().toString().replace("-",""));

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }




}
