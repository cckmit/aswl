package com.aswl.as.common.core.utils;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 *  数字操作工具类
 * </p>
 *
 * @author dingfei
 * @date 2019/09/20 16:04
 */
public class NumberUtil {

    private static String ARR_STR="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 获取字符串最后一位+1
    public static String addOne(String testStr){
        //根据不是数字的字符拆分字符串
        String[] strs = testStr.split("[^0-9]");
        //取出最后一组数字
        String numStr = strs[strs.length-1];
        //如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
        if(numStr != null && numStr.length()>0){
            //取出字符串的长度
            int n = numStr.length();
            //将该数字加1
            int num = Integer.parseInt(numStr)+1;
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length()-n)+added;
        }else{
            throw new NumberFormatException();
        }
    }

    /**
     * 根据编码添加1，跟addOne的功能一样，但是限定位数，比如到了A99的时候，就变成B01，不会变成 A100
     * // 获取字符串最后一个数值+1,不单A99，还可以W09,不然到了A100的时候，当A10匹配权限的时候，很可能like '%A10'，就会查到A100的数据
     * 如果设定maxNumString=99的话，就每一层有2599个，一般够用了，可以抽出来
     * 第一层的话，可以无限个，比如Z99的话会变成AA01，如果是DF99的话变成DG01，ZZ99的话变成AAA01，实际上除了第一层无限加也行，不过应该没必要，也为了兼容原来的规则
     * @param testStr
     * @return
     */
    public static String addOneNum(String testStr){

        //设置数值多少位（暂时是写死两位，最大是99）
        String maxNumString="99";

        //根据不是数字的字符拆分字符串
        String[] strs = testStr.split("[^0-9]");
        //取出最后一组数字
        String numStr = strs[strs.length-1];
        //如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常

        if(Integer.parseInt(numStr)>Integer.parseInt(maxNumString))
        {
            throw new NumberFormatException("编号不能比设置的最大值大");
        }

        if(numStr.equals(maxNumString))
        {
            int lastCharIndex=testStr.length()-(numStr.length()+1);
            char lastChar=testStr.charAt(lastCharIndex);

            boolean isFirst=true;
            for(int i=0;i<strs.length-1;i++)
            {
                if(!"".equals(strs[i]))
                {
                    isFirst=false;
                }
            }

            if(isFirst)
            {
                //如果是第一个，可以无限添加
                return getNextString(testStr.substring(0,lastCharIndex+1)) +"01";
            }
            else
            {
                if(lastChar=='Z')
                {
                    throw new NumberFormatException("编号已达到最大值");
                }
                return testStr.substring(0,lastCharIndex) + getNextChar(lastChar) +"01";
            }
        }
        else
        {
            if(numStr != null && numStr.length()>0){
                //取出字符串的长度
                int n = numStr.length();
                //将该数字加1
                int num = Integer.parseInt(numStr)+1;
                String added = String.valueOf(num);
                n = Math.min(n, added.length());
                //拼接字符串
                return testStr.subSequence(0, testStr.length()-n)+added;
            }else{
                throw new NumberFormatException();
            }
        }
    }

    //获取下一个字符
    private static char getNextChar(char c)
    {
        if(c=='Z')
        {
            return 'A';
        }
        return ARR_STR.charAt(ARR_STR.indexOf(c)+1);
    }

    //获取下一个字符串
    private static String getNextString(String str)
    {
        if(StringUtils.isBlank(str))
        {
            return "A";
        }

        char[] arr=str.toCharArray();
        int lastIndex=arr.length-1;
        if(arr[lastIndex]=='Z')
        {
            return getNextString(str.substring(0,str.length()-1))+'A';
        }
        else
        {
            arr[lastIndex]=getNextChar(arr[lastIndex]);
            return String.valueOf(arr);
        }
    }

}
