package com.example.randomalarm.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
    //分钟转为毫秒
    public  final static  int MINUTE_TO_MILLIS = 60000;

    public static String format(Date date) {
        return df.format(date);
    }

    public static Calendar getNowTime() {
        return Calendar.getInstance();
    }

    public static int getNowHour() {
        return getNowTime().get(Calendar.HOUR_OF_DAY);
    }

    public static int getNowMinute() {
        return getNowTime().get(Calendar.MINUTE);
    }

    /**
     * 获取时间差距
     * @param time
     * @return
     */
    public static String getDiffTime(long time) {
        int millisToHour = MINUTE_TO_MILLIS * 60;
        int millisToDay = millisToHour * 24;
        long days = time / millisToDay;
        long remainHour = time - days * millisToDay;
        long hours = remainHour / millisToHour;
        long remainMinute = remainHour - hours * millisToHour;
        long minutes = remainMinute / MINUTE_TO_MILLIS;
        String diffTime = "";
        if (days > 0) {
            diffTime += StringHelper.getLocalFormat("%d天", days);
        }
        if (hours > 0) {
            diffTime += StringHelper.getLocalFormat("%d小时", hours);
        }
        if (minutes >= 0) {
            diffTime += StringHelper.getLocalFormat("%d分钟", minutes);
        } else {
            diffTime = "";
        }
        return diffTime;
    }

    /**
     * 获取小时和分钟连接字符串（HH:mm）
     *
     * @param calendar
     * @return
     */
    public static String getHourAndMinuteString(Calendar calendar) {
        return StringHelper.getLocalFormat("%d:%d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static Calendar getCalendarByHourAndMinute(int hourOfDay, int minute) {
        Calendar calendar = getNowTime();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
