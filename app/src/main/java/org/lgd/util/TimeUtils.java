package org.lgd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 时间工具类
 * Created by Bruce Wu on 2016/4/2.
 */
public class TimeUtils {

    /**
     * 获取当前日期
     *
     * @param l :
     * @return
     */
    public static String getDate(long l) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(l);
        Formatter ft = new Formatter(Locale.CHINA);
        String res = ft.format("%1$tm/%1$td", cal).toString();
        String[] ress = res.split("/");
        if (res.charAt(0) == '0') {
            ress[0] = ress[0].substring(1);
        }
        if (ress[1].charAt(0) == '0') {
            res = ress[0] + "/" + ress[1].substring(1);
        }
        return res;
    }

    /**
     * 获取当天是星期几
     *
     * @param l
     * @return
     */
    public static String getWeek(long l) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(l);
        Formatter ft = new Formatter(Locale.CHINA);
        String res = ft.format("%1$tA", cal).toString();
        return res;
    }

    /**
     * 获取当前时间
     *
     * @param l
     * @return
     */
    public static String getTime(long l) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(l);
        Formatter ft = new Formatter(Locale.CHINA);
        return ft.format("%1$tT:%1$tp", cal).toString();
    }

    /**
     * 将时间转化为秒
     *
     * @param l : 当前的系统时间
     * @return
     */
    public static long getTimeMillis(long l) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(l);
        String[] times = format.format(date).split(":");
        long lon = 0;
        for (int i = 0; i < times.length; i++) {
            if (i == 0) {
                lon += (StringUtils.str2Int(times[i]) * 3600);
            } else {
                lon += (StringUtils.str2Int(times[i]) * 60);
            }
        }
        return lon;
    }

    /**
     * 将一个String s的时间转化为Mills
     *
     * @param s : 时间字符串
     * @return
     */
    public static long getTimeMillis(String s) {
        String[] times = s.split(":");
        long lon = 0;
        for (int i = 0; i < times.length; i++) {
            if (i == 0) {
                lon += (StringUtils.str2Int(times[i]) * 3600);
            } else {
                lon += (StringUtils.str2Int(times[i]) * 60);
            }
        }
        return lon;
    }

}
