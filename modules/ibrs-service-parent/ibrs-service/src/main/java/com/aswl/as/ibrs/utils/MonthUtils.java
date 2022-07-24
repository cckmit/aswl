package com.aswl.as.ibrs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 月份工具类
 */
public class MonthUtils {

    /**
     * 返回日期之间所有的月份
     * @param minDate
     * @param maxDate
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }
    public static List<String> getMonthBetween1(String minDate, String maxDate) {
        if (minDate == null) {
            return null;
        }
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        Collections.reverse(result);
        List<String> newList = new ArrayList<>();
        for (String s : result) {
            newList.add(dateformt(s));
        }
        newList.add("统计均值");
        return newList;
    }


    public static String dateformt(String s) {
        String[] split = s.split("-");
        String str = split[split.length - 1] + "/" + split[0];
        return str;
    }
    public static void main(String[] args) {
        String minDate = "2020-01-01 00:00:00";
        String maxDate = "2020-12-31 00:00:00";
        String str = "2020-12";
        System.out.println(dateformt(str));
        List<String> list = getMonthBetween1(minDate, maxDate);
        // List<String> list = getMonthBetween(minDate, maxDate);
        System.out.println(list);
    }
}
