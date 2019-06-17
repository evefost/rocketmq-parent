/**
 * DateUtils.java Created by william in 2013年9月30日.
 * Copyright(c) 2013-2013 PingAnFu,Inc.All Rights Reserved.
 */
package com.xie.message.client.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 日期转换工具类
 * @Version 1.0
 */

public class DateUtils {

    public static final String           FUALL_TIMESTAMP  = "yyyyMMddHHmmssSSS";
    
    public static final String           TIME_PATTERN  = "yyyy-MM-dd HH:mm:ss";

    public static final String           DATE_PATTERN = "yyyy-MM-dd";

    //private static final String           DATE_PATTERN2 = "yyyyMMdd";

    //private static final String           DATE_PATTERN3 = "yyyy/MM/dd";


    /*
     * 获取当前时间
     */
    public static String getTime() {
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(new Date());
    }

    /*
     * 获取当前时间,制定格式
     */
    public static String getTime(String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /*
     * 获取当前日期
     */
    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(new Date());
    }

    /*
     * 获取当前日期
     */
    public static String getDate(String pattern) {
        if (pattern == null || "".equals(pattern)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 格式化日期
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(date);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = null;
        if (pattern == null || "".equals(pattern)) {
            df = new SimpleDateFormat(TIME_PATTERN);
        } else {
            df = new SimpleDateFormat(pattern);
        }
        return df.format(date);
    }

    /**
     * 将字符型转换成日期型.
     * @param strDate 字符型
     * @return 日期型
     */
    private final static String[] datePatternStrs = 
            new String[] { "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd"};
    
    public static Date parse2Date(String dateStr) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        Date date = null;
        for (String datePattern : datePatternStrs) {
            try {
                date = new SimpleDateFormat(datePattern).parse(dateStr);
            } catch (ParseException e) {
                date = null;
            }
            if (date != null) {
                return date;
            }
        }
        return null;
    }
    
    private final static String[] timePatternStrs = 
            new String[] { "yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss", "yyyy/MM/dd HH/mm/ss", "yyyyMMdd HHmmss" };
    
    public static Date parse2Time(String timeStr) {
        if (timeStr == null || "".equals(timeStr)) {
            return null;
        }
        Date date = null;
        for (String timePattern : timePatternStrs) {
            try {
                date = new SimpleDateFormat(timePattern).parse(timeStr);
            } catch (ParseException e) {
                date = null;
            }
            if (date != null) {
                return date;
            }
        }
        return null;
    }
    
    public static Date parse2Date(String dateStr, String pattern) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        try {
            SimpleDateFormat df = null;
            if (pattern == null || "".equals(pattern)) {
                df = new SimpleDateFormat(TIME_PATTERN);
            } else {
                df = new SimpleDateFormat(pattern);
            }
            return df.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    
    
    /*
     * 时间偏移运算
     */
    public static String getTime(int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(cal.getTime());
    }

    /*
     * 某一时间的偏移运算
     */
    public static String getTime(String timeStr, int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(parse2Time(timeStr));
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(cal.getTime());
    }

    /*
     * 日期偏移运算(增、减几日）
     */
    public static String getDate(int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(cal.getTime());
    }
    
    /**
     * 日期偏移运算(增、减几日) 指定格式
     * @param skipDay 偏移量
     * @param pattern 日期格式
     * @return
     */
    public static String getDate(int skipDay, String pattern) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        SimpleDateFormat df = null;
        if (pattern == null || "".equals(pattern)) {
            df = new SimpleDateFormat(DATE_PATTERN);
        } else {
            df = new SimpleDateFormat(pattern);
        }
        return df.format(cal.getTime());
    }

    /**
     * 指定 日期偏移运算(增、减几日）
     * @param dateStr 指定日期
     * @param skipDay 偏移量
     * @return
     */
    public static String getDate(String dateStr, int skipDay) {
        if (null == dateStr) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(parse2Date(dateStr));
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(cal.getTime());
    }

    /*
     * 时间偏移运算(增、减几日、几小时、几分）
     */
    public static String getTime(int skipDay, int skipHour, int skipMinute) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
        cal.add(GregorianCalendar.MINUTE, skipMinute);
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(cal.getTime());
    }

    /*
     * 某一时间的偏移运算(增、减几日、几小时、几分）
     */
    public static String getTime(String timeStr, int skipDay, int skipHour, int skipMinute) {
        if (null == timeStr) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(parse2Time(timeStr));
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
        cal.add(GregorianCalendar.MINUTE, skipMinute);
        cal.get(GregorianCalendar.DAY_OF_WEEK_IN_MONTH);
        SimpleDateFormat df = new SimpleDateFormat(TIME_PATTERN);
        return df.format(cal.getTime());
    }

    /*
     * 某一时间的偏移运算(增、减几日、几小时、几分）
     */
    public static Date getTime(Date time, int skipDay, int skipHour, int skipMinute) {
        if (null == time) {
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(time);
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
        cal.add(GregorianCalendar.MINUTE, skipMinute);
        cal.get(GregorianCalendar.DAY_OF_WEEK_IN_MONTH);
        return cal.getTime();
    }

    // 获取年
    public static int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }
    
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    // 获取月
    public static int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);
    }
    
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }

    // 获取日
    public static int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }
    
    /*
     * 日期偏移运算(增、减几日）
     */
    public static Date getSkipDate(int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        return cal.getTime();
    }
    
    /*
     * 日期偏移运算(增、减几日）
     */
    public static Date getSkipDate(Date date, int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
        return cal.getTime();
    }
    
    /*
     * 某一时间的偏移运算(增、减几日、几小时、几分）
     */
    public static Date getSkipHour(Date date, int skipHour) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
        return cal.getTime();
    }

    public static boolean checkDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateFormat.parse(date);
        } catch (Exception e) {
            return false;
        }
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-9]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        return m.matches();
    }

    public static Date getForeverDate() {
        return parse2Time("9999-1-1 00:00:00");
    }
    
    /*
     * 非周末日期偏移运算(增、减几日) 
     */
    public static Date getSkipWorkday(int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.THURSDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 2);
                break;
            case Calendar.FRIDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 2);
                break;
            case Calendar.SATURDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 1);
                break;
            default:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
                break;
        }
        return cal.getTime();
    }

    /*
     * 非周末日期偏移运算(增、减几日) 
     */
    public static Date getSkipWorkday(Date date, int skipDay) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.THURSDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 2);
                break;
            case Calendar.FRIDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 2);
                break;
            case Calendar.SATURDAY:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay + 1);
                break;
            default:
                cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
                break;
        }
        return cal.getTime();
    }
    
    // 判断是否闰年
    public static boolean isLeapYear(int year) {
        if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 4) == 0) && ((year % 400) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    public static Date parseLong2Date(long newScheduleSendTimeInMillis) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(newScheduleSendTimeInMillis);
        return cal.getTime();
    }
}
