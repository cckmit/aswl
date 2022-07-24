package com.aswl.as.metadata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author dingfei
 * @date 2019-11-07 10:50
 * @Version V1
 */
public class DateUtil {
    public static void main(String[] args) {
        List<String> dayBetweenDates = getDayBetweenDates("2019-11-01", "2019-11-08");
        for (String s:dayBetweenDates) {
           // System.out.println(dayBetweenDates.size());
            System.out.println(s);
        }
    }

    public  static List<String> getDayBetweenDates(String begin, String end) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sd.parse(begin);
            dEnd = sd.parse(end);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dBegin);
            calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
            List<String> lDate = new ArrayList<String>();
            lDate.add(sd.format(calendar.getTime()));
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.setTime(calendar.getTime());
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.setTime(dEnd);
            // 测试此日期是否在指定日期之后
            while (dEnd.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.DAY_OF_MONTH, 1);
                lDate.add(sd.format(calBegin.getTime()));
            }
            return lDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
