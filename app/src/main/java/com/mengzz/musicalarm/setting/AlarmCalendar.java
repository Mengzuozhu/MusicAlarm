package com.mengzz.musicalarm.setting;

import android.os.Parcel;
import android.os.Parcelable;

import com.haibin.calendarview.Calendar;
import com.mzz.zandroidcommon.common.DateHelper;
import com.mzz.zandroidcommon.common.JsonConverter;
import com.mzz.zandroidcommon.common.StringHelper;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author : Mzz
 * date : 2019 2019/5/17 21:54
 * description :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmCalendar implements Parcelable, Comparable <AlarmCalendar> {

    public static final Parcelable.Creator <AlarmCalendar> CREATOR =
            new Parcelable.Creator <AlarmCalendar>() {
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

    private AlarmCalendar(Parcel in) {
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

    /**
     * Remove invalid date array list .
     *
     * @param alarmCalendars the alarm calendars
     * @return the array list
     */
    public static ArrayList <AlarmCalendar> removeInvalidDate(ArrayList <AlarmCalendar> alarmCalendars) {
        if (alarmCalendars == null) {
            return new ArrayList <>();
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

    boolean isOutOfDate() {
        return toCalendar().getTimeInMillis() < DateHelper.getNowInMillis();
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

    java.util.Calendar toCalendar() {
        java.util.Calendar calendar = DateHelper.getNowTime();
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
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
            return JsonConverter.convertToStringValue(entityProperty);
        }
    }
}
