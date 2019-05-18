package com.example.randomalarm.setting;

import com.example.randomalarm.common.DateHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * 闹钟重复模式
 * author : Mzz
 * date : 2019 2019/4/28 21:42
 * description :
 */
public enum AlarmRepeatMode {

    MONDAY(0, "周一"),
    TUESDAY(1, "周二"),
    WEDNESDAY(2, "周三"),
    THURSDAY(3, "周四"),
    FRIDAY(4, "周五"),
    SATURDAY(5, "周六"),
    SUNDAY(6, "周日");

    /**
     * 星期的天数
     */
    public final static int RECYCLE_DAY = 7;
    private final int id;
    private final String desc;//中文描述

    AlarmRepeatMode(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * 获取所有枚举值
     *
     * @return
     */
    public static String[] toDescArray() {
        String[] strings = new String[values().length];
        AlarmRepeatMode[] values = values();
        for (int i = 0; i < values.length; i++) {
            strings[i] = (values[i].getDesc());
        }

        return strings;
    }

    public static ArrayList <AlarmRepeatMode> toArrayList() {
        ArrayList <AlarmRepeatMode> repeatModes = new ArrayList <>();
        Collections.addAll(repeatModes, values());
        return repeatModes;
    }

    /**
     * 当前星期不在重复模式中
     *
     * @param alarmRepeatModes
     * @return
     */
    public static boolean isNowNotInRepeatModeDay(ArrayList <AlarmRepeatMode> alarmRepeatModes) {
        Calendar nowTime = DateHelper.getNowTime();
        //修改星期为从0开始
        int weekDay = nowTime.get(Calendar.DAY_OF_WEEK) - 1;
        for (AlarmRepeatMode repeatMode : alarmRepeatModes) {
            if (repeatMode.getId() == weekDay) {
                return false;
            }
        }
        return true;
    }

    public static int getWeekDay(Calendar calendar) {
        //因为枚举id从0（周一）开始算，所以减2
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        //若是负数，则为周日，那么id为6
        if (weekDay < 0) {
            weekDay = 6;
        }
        return weekDay;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
