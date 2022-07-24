package com.aswl.as.common.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author aswl.com
 * @date 2019/4/28 16:03
 */
public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter FORMATTER_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddhhmmssSSS");

    // 指定模式的时间格式
    private static SimpleDateFormat getSDFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String localDateToString(LocalDateTime date) {
        return date != null ? date.format(FORMATTER) : "";
    }

    /**
     * date转时分秒
     */
    public static String dateToString(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param format 日期
     * @param format 日期格式
     * @return 字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 日期转string
     *
     * @param date date
     * @return String
     */
    public static String localDateMillisToString(LocalDateTime date) {
        return date != null ? date.format(FORMATTER_MILLIS) : "";
    }

    /**
     * LocalDate转Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转Date
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date转LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * 获取指定日期、月份之前或者之后的 日期字符串集合(包括当前日、月、周)
     *
     * @param date 当前日期
     * @param num  天数
     * @param type 类型
     * @return
     */
    public static List<String> getBeforeOrAfterDate(Date date, int num, String type) {
        String format = "";
        int t = Calendar.DATE;
        if ("week".equals(type)) {
            format = "yyyy-MM-dd";
        }
        if ("month".equals(type)) {
            format = "yyyy-MM-dd";
        }
        if ("year".equals(type)) {
            format = "yyyy-MM";
            t = Calendar.MONTH;
        }
        if (format.length() == 0) {
            return null;
        }
        List<String> dateStr = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();//获取日历
        calendar.setTime(date);//当date的值是当前时间，则可以不用写这段代码。
        dateStr.add(new SimpleDateFormat(format).format(date));
        for (int i = 1; i <= num - 1; i++) {//正序
            calendar.add(t, -1);
            Date d = calendar.getTime();//把日历转换为Date
            dateStr.add(new SimpleDateFormat(format).format(d));
        }
        return dateStr;
    }

    /**
     * 获取某个时间段内所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return list
     */
    public static List<String> getDayBetweenDates(String begin, String end) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sd.parse(begin);
            dEnd = sd.parse(end);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dBegin);
            // calendar.add(Calendar.DAY_OF_MONTH, 1);//加一天
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

    /**
     *  获取某个时间段内所有月份
     * @param minDate 开始日期
     * @param maxDate 结束日期
     * @return
     * @throws ParseException
     */
    public  static List<String> getMonthBetween(String minDate, String maxDate) {
            ArrayList<String> result = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            while (min.before(max)) {
                result.add(sdf.format(min.getTime()));
                min.add(Calendar.MONTH, 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 时长转换
     */
    public static String formatDateTime(String time) {
        if (time == null) {
            return "";
        }
        long ss = Long.valueOf(time);
        String intervalTime;
        long days = ss / (60 * 60 * 24);
        long hours = (ss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (ss % (60 * 60)) / 60;
        long seconds = ss % 60;
        if (days > 0) {
            intervalTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            intervalTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            intervalTime = minutes + "分钟";
        } else {
            intervalTime = "1分钟内";
        }
        return intervalTime;
    }

    /**
     * 指定日期按指定格式显示
     *
     * @param date
     *            指定的日期
     * @param pattern
     *            指定的格式
     * @return 指定日期按指定格式显示
     */
    public static String formatDate(Date date, String pattern) {
        return getSDFormat(pattern).format(date);
    }

    /**
     * 根据指定的格式将字符串转换成Date 如输入：2003-11-19 11:20:20将按照这个转成时间
     *
     * @param dataStr
     *            将要转换的原始字符窜
     * @param pattern
     *            转换的匹配格式
     * @return 如果转换成功则返回转换后的日期
     * @throws ParseException
     * @throws AIDateFormatException
     */
    public static Date parseDate(String dataStr, String pattern)
            throws ParseException {
        return getSDFormat(pattern).parse(dataStr);

    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
