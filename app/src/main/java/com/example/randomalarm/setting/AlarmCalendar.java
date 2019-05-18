package com.example.randomalarm.setting;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.JsonConverter;
import com.example.randomalarm.common.StringHelper;
import com.haibin.calendarview.Calendar;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/5/17 21:54
 * description :
 */
public class AlarmCalendar implements Parcelable, Comparable <AlarmCalendar> {

    public static final Parcelable.Creator <AlarmCalendar> CREATOR = new Parcelable.Creator <AlarmCalendar>() {
        @Override
        public AlarmCalendar createFromParcel(Parcel source) {
            return new AlarmCalendar(source);
        }

        @Override
        public AlarmCalendar[] newArray(int size) {
            return new AlarmCalendar[size];
        }
    };

    /**
     * 年
     */
    private int year;
    /**
     * 月1-12，若转换为系统月份，则需要减1
     */
    private int month;
    /**
     * 日1-31
     */
    private int day;

    public AlarmCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public AlarmCalendar() {
    }

    protected AlarmCalendar(Parcel in) {
        this.year = in.readInt();
        this.month = in.readInt();
        this.day = in.readInt();
    }

    public static AlarmCalendar calendarToThis(Calendar calendar) {
        if (calendar == null) {
            return new AlarmCalendar();
        }
        return new AlarmCalendar(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }

    public static ArrayList <AlarmCalendar> removeInvalidDate(ArrayList <AlarmCalendar> alarmCalendars) {
        if (alarmCalendars == null) {
            return null;
        }
        for (int i = alarmCalendars.size() - 1; i >= 0; i--) {
            if (alarmCalendars.get(i).isOutOfDate()) {
                alarmCalendars.remove(i);
            }
        }
        return alarmCalendars;
    }

    public boolean isCurYear() {
        return year == DateHelper.getCurYear();
    }

    public boolean isOutOfDate() {
        return toMultCalendar().getTimeInMillis() < DateHelper.getNowInMillis();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return StringHelper.getLocalFormat("%d/%d/%d", year, month, day);
    }

    public String toMonthAndDayString() {
        return StringHelper.getLocalFormat("%d/%d", month, day);
    }

    public Calendar toMultCalendar() {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        return calendar;
    }

    public java.util.Calendar toCalendar() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.year);
        dest.writeInt(this.month);
        dest.writeInt(this.day);
    }

    @Override
    public int compareTo(AlarmCalendar o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (this.year > o.year) {
            return 1;
        } else if (this.year < o.year) {
            return -1;
        }

        if (this.month > o.month) {
            return 1;
        } else if (this.month < o.month) {
            return -1;
        }

        if (this.day > o.day) {
            return 1;
        } else if (this.day < o.day) {
            return -1;
        }
        return 0;
    }

    public static class Converter implements PropertyConverter <ArrayList <AlarmCalendar>, String> {
        @Override
        public ArrayList <AlarmCalendar> convertToEntityProperty(String databaseValue) {
            return JsonConverter.jsonToList(databaseValue, AlarmCalendar.class);
        }

        @Override
        public String convertToDatabaseValue(ArrayList <AlarmCalendar> entityProperty) {
            return JsonConverter.convertToDatabaseValue(entityProperty);
        }
    }
}
