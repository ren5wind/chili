package com.topunion.chili.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author chenzipeng
 */
public class DateHelper {
    public final static String FORMAT_DATE_FULL_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_MINUTE = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_MONTH_TIME = "MM-dd HH:mm";
    public final static String FORMAT_MONTH_DAY = "MM月dd日 ";


    /**
     * 日期格式化 成 字符串
     *
     * @param format
     * @param date
     * @return
     */
    public static String formatDate(String format, Date date) {
        if (null != date) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.format(date);
        }
        return "";

    }

    /**
     * 解析 日期字符串为Date
     *
     * @param format
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String format, String dateString) throws ParseException {
        if (!TextUtils.isEmpty(dateString)) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            return formater.parse(dateString);
        }
        return new Date();

    }

    /**
     * 获取星期几
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        if (null != date) {
            Calendar ca = getCalendar(date);
            return ca.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return 0;

    }

    public static Calendar getCalendar(Date date) {
        Calendar ca = Calendar.getInstance();
        if (null != date)
            ca.setTime(date);
        return ca;
    }

    public static int getYear(Date date) {
        if (date != null) {
            Calendar ca = getCalendar(date);
            return ca.get(Calendar.YEAR);
        }
        return 1;

    }

    /**
     * 获取月份  0~11
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        if (null != date) {
            Calendar ca = getCalendar(date);
            return ca.get(Calendar.MONTH);
        }
        return 0;
    }

    /**
     * 获取日期
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        if (null != date) {
            Calendar ca = getCalendar(date);
            return ca.get(Calendar.DAY_OF_MONTH);
        }
        return 0;
    }

    /**
     * 根据参数 生成date
     *
     * @param year
     * @param monthOfYear 0~11
     * @return
     */
    public Date getDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR, year);
        ca.set(Calendar.MONTH, monthOfYear);
        ca.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return ca.getTime();
    }

}



