package com.aswl.as.asos.common.utils;

import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.vo.OsVo;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用HTTP获取ibrs的数据，本工具类一般不用，但是以免跟租户端不在同一个局域网内，写好这个直接使用http的工具获取数据
 */
public class IbrsData {

    public static String BASE_URL="http://localhost:8096";

    public static final String CODE_STRING="code";

    public static final String STATUS_STRING="status";

    public static final String MEG_STRING="msg";

    public static final String DATA_STRING="data";

    public static String buildGetUrl (String shortUrl,Map<String, Object> data)
    {
        StringBuilder sb=new StringBuilder();
        sb.append(BASE_URL);
        sb.append(shortUrl);
        if(data!=null&& !data.keySet().isEmpty())
        {
            boolean isFirst=true;
            for(String key:data.keySet())
            {
                Object o=data.get(key);
                if(o!=null)
                {
                    if(isFirst)
                    {
                        sb.append("?");
                        isFirst=false;
                    }
                    else
                    {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(o.toString());
                }
            }
        }
        System.out.println("最终请求的url="+sb.toString());
        return sb.toString();
    }

    public static String buildGetUrl(String shortUrl,String key,String value)
    {
        StringBuilder sb=new StringBuilder();
        sb.append(BASE_URL);
        sb.append(shortUrl);
        sb.append("?");
        sb.append(key);
        sb.append("=");
        sb.append(value);
        return sb.toString();
    }

    public static String buildPostUrl(String shortUrl)
    {
        return BASE_URL+shortUrl;
    }

    public static IbrsResponseBean getBean(String s) {
        /*
        JSONObject json= JSON.parseObject(s);

        IbrsResponseBean ibrsResponseBean=new IbrsResponseBean(
                json.getInteger(CODE_STRING),json.getInteger(STATUS_STRING),
                json.getString(MEG_STRING),json.get(DATA_STRING)
        );
        return ibrsResponseBean;
         */
        return JSON.parseObject(s,IbrsResponseBean.class);
    }

    public static Map<String,Object> getHeaderMap()
    {
        Map<String, Object> header=new HashMap<String,Object>();
//        header.put("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpblR5cGUiOiJQV0QiLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwidGVuYW50Q29kZSI6ImFzd2wiLCJleHAiOjE1NzU2MTQ2NDIsImF1dGhvcml0aWVzIjpbInN5czpyb2xlOmRlbCIsInN5czpkZXB0OmVkaXQiLCJzeXM6bWVudTppbXBvcnQiLCJzeXM6bWVudTpkZWwiLCJzeXM6bWVudTphZGQiLCJzeXM6dXNlcjphZGQiLCJzeXM6dXNlcjpleHBvcnQiLCJzeXM6cm9sZTplZGl0Iiwic3lzOmRlcHQ6ZGVsIiwic3lzOnVzZXI6ZWRpdCIsInN5czp1c2VyOmltcG9ydCIsInN5czpkZXB0OmFkZCIsInN5czpyb2xlOmF1dGgiLCJzeXM6bWVudTplZGl0Iiwic3lzOnJvbGU6YWRkIiwic3lzOm1lbnU6ZXhwb3J0Iiwic3lzOnVzZXI6ZGVsIl0sImp0aSI6IjJkYjdkODI0LWZmYjItNGY3OS04NDAxLTc5MTI1YzI1YzRmZiIsImNsaWVudF9pZCI6IndlYl9hcHAifQ.N3HG88qfTR3MW9I9sqbfWUJtHoOqDa783I9oSFq2rvPO7NVTAP5_2z9b54L2KmgELBjEAZgssfzN7UZ6cgYgzSCeu0Q0eJ5mtO8U5FJPWSbLU0f0nU7VLhQZCrOyC-MVkA0z2XrULusnBWuMZjnEadeJq0Puvbxc5elpTA1wGLEJK0On0BZs5G3XR5hhfFxr3_FiaZ4I_QCBLe9goeiByVy5U9EydL-ubR-hHn1siBHmTb_H7LDKy5xPyjrban3DTS-6Tj2Qh0RZ0jfVKW6IAbhfCfEsJhxiTdPxs6-CDtC6117vFMKheraC7_Y_DzgpI_UsJdLUFciGLbg1I_jjBw");
        header.put("Authorization","ffb7b211497d4996975447913de75fd8");
        header.put("Tenant-Code","aswl");
        header.put("Project-Id",null);
        return header;
    }

    public static Map<String,Object> getBodyMap()
    {
        return new HashMap<String,Object>();
    }

    public static Map<String,Object> getBodyMapWithRandomStrMap()
    {
        Map<String,Object> m= new HashMap<String,Object>();
        m.put(OsVo.CHECK_STRING, OsVo.getRandomStr());
        return m;
    }

    public static void setBodyMapRandomStr(Map<String,Object> m)
    {
        m.put(OsVo.CHECK_STRING, OsVo.getRandomStr());
    }

}
