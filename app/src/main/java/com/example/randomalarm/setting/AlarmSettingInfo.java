package com.example.randomalarm.setting;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.JsonConverter;
import com.example.randomalarm.song.SongInfo;
import com.example.randomalarm.song.SongInfoListConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

/**
 * author : Mzz
 * date : 2019 2019/4/25 16:19
 * description :
 */
@Entity
public class AlarmSettingInfo implements Parcelable {

    public static final Creator <AlarmSettingInfo> CREATOR = new Creator <AlarmSettingInfo>() {
        @Override
        public AlarmSettingInfo createFromParcel(Parcel source) {
            return new AlarmSettingInfo(source);
        }

        @Override
        public AlarmSettingInfo[] newArray(int size) {
            return new AlarmSettingInfo[size];
        }
    };

    @Id(autoincrement = true)
    private Long id;
    private int hour = 8;
    private int minute = 0;
    private Boolean isOpenStatus = true;
    //闹钟间隔，单位分钟，默认为5
    private int interval = 5;
    //重复响铃次数
    private int repeatFrequency = 3;
    //响铃时长，单位分钟，默认为1
    private int duration = 1;
    //是否振动
    private boolean isVibrated = true;
    //重复模式
    @Convert(columnType = String.class, converter = AlarmRepeatConverter.class)
    private ArrayList <AlarmRepeatMode> alarmRepeatMode = new ArrayList <>();
    //铃声信息
    @Convert(columnType = String.class, converter = SongInfoListConverter.class)
    private ArrayList <SongInfo> songInfos = new ArrayList <>();
    //播放模式
    @Convert(columnType = Integer.class, converter = SongPlayedMode.SongPlayedModeConverter.class)
    private SongPlayedMode songPlayedMode = SongPlayedMode.random;
    //闹钟日期
    @Convert(columnType = String.class, converter = AlarmCalendar.Converter.class)
    private ArrayList <AlarmCalendar> alarmCalendars = new ArrayList <>();
    @Transient
    private int currentRepeatNum;
    @Transient
    private ArrayList <String> checkedSongPaths = null;

    @Generated(hash = 1549448405)
    public AlarmSettingInfo(Long id, int hour, int minute, Boolean isOpenStatus, int interval, int repeatFrequency, int duration,
                            boolean isVibrated, ArrayList <AlarmRepeatMode> alarmRepeatMode, ArrayList <SongInfo> songInfos,
                            SongPlayedMode songPlayedMode,
                            ArrayList <AlarmCalendar> alarmCalendars) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isOpenStatus = isOpenStatus;
        this.interval = interval;
        this.repeatFrequency = repeatFrequency;
        this.duration = duration;
        this.isVibrated = isVibrated;
        this.alarmRepeatMode = alarmRepeatMode;
        this.songInfos = songInfos;
        this.songPlayedMode = songPlayedMode;
        this.alarmCalendars = alarmCalendars;
    }

    @Generated(hash = 664695116)
    public AlarmSettingInfo() {
    }

    protected AlarmSettingInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.isOpenStatus = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.interval = in.readInt();
        this.repeatFrequency = in.readInt();
        this.duration = in.readInt();
        this.isVibrated = in.readByte() != 0;
        this.alarmRepeatMode = new ArrayList <AlarmRepeatMode>();
        in.readList(this.alarmRepeatMode, AlarmRepeatMode.class.getClassLoader());
        this.songInfos = in.createTypedArrayList(SongInfo.CREATOR);
        int tmpSongPlayedMode = in.readInt();
        this.songPlayedMode = tmpSongPlayedMode == -1 ? null : SongPlayedMode.values()[tmpSongPlayedMode];
        this.alarmCalendars = in.createTypedArrayList(AlarmCalendar.CREATOR);
    }

    /**
     * 获取下一个响铃日期
     *
     * @param nextCalendar
     * @return
     */
    public Calendar getNextAlarmDate(Calendar nextCalendar) {
        alarmCalendars = AlarmCalendar.removeInvalidDate(alarmCalendars);
        //首选闹钟日期
        if (alarmCalendars != null && alarmCalendars.size() > 0) {
            return getNextAlarmDateByCalendar(nextCalendar);
        }
        return getNextAlarmDateByRepeatMode(nextCalendar);
    }

    private Calendar getNextAlarmDateByRepeatMode(Calendar nextCalendar) {
        int weekDay = AlarmRepeatMode.getWeekDay(nextCalendar);
        int minDay = Integer.MAX_VALUE;
        //获取响铃星期中与当前星期的最小天数差
        for (AlarmRepeatMode repeatMode : alarmRepeatMode) {
            int diff = repeatMode.getId() - weekDay;
            //若天数差小于0，则添加一个周期7
            if (diff < 0) {
                diff += AlarmRepeatMode.RECYCLE_DAY;
            }
            if (diff < minDay) {
                minDay = diff;
            }
        }
        if (minDay != Integer.MAX_VALUE) {
            nextCalendar.add(Calendar.DATE, minDay);
        }
        return nextCalendar;
    }

    private Calendar getNextAlarmDateByCalendar(Calendar nextCalendar) {
        Collections.sort(alarmCalendars);
        for (int i = 0; i < alarmCalendars.size(); i++) {
            AlarmCalendar alarmCalendar = alarmCalendars.get(i);
            if (alarmCalendar.isOutOfDate()) {
                continue;
            }
            //获取下一个闹钟日期
            if (nextCalendar.compareTo(alarmCalendar.toCalendar()) <= 0) {
                nextCalendar.set(Calendar.YEAR, alarmCalendar.getYear());
                nextCalendar.set(Calendar.MONTH, alarmCalendar.getMonth() - 1);
                nextCalendar.set(Calendar.DAY_OF_MONTH, alarmCalendar.getDay());
                return nextCalendar;
            }
        }
        return null;
    }

    /**
     * 获取下一重复间隔的闹钟
     *
     * @return
     */
    public Calendar getNextIntervalAlarm() {
        currentRepeatNum = 0;
        if (AlarmRepeatMode.isNowNotInRepeatModeDay(alarmRepeatMode)) return null;

        Calendar calendar = DateHelper.getCalendarByHourAndMinute(hour, minute);
        Calendar nowTime = DateHelper.getNowTime();
        //与下一分钟比较，避免重复触发
        nowTime.add(Calendar.MINUTE, 1);
        for (int i = 0; i < repeatFrequency; i++) {
            //下一个间隔闹钟时间大于当前时间
            if (calendar.compareTo(nowTime) > 0) {
                currentRepeatNum = i;
                return calendar;
            }
            calendar.add(Calendar.MINUTE, interval);
        }
        return null;
    }

    /**
     * 获取播放音乐路径
     *
     * @return
     */
    public String getPlayedSongPath() {
        if (checkedSongPaths == null) {
            initCheckedSongPaths();
        }
        int size = checkedSongPaths.size();
        if (size == 0) {
            return "";
        }
        int index = 0;
        switch (songPlayedMode) {
            //随机播放歌曲
            case random:
                Random random = new Random();
                index = random.nextInt(size);
                break;
            //单曲循环第一首
            case single:
                break;
            case order:
                index = currentRepeatNum % size;
                break;
        }
        return checkedSongPaths.get(index);
    }

    /**
     * 获取显示时间
     *
     * @return 显示时间
     */
    public String getShowedTime() {
        String strMinute = String.valueOf(minute);
        String strHour = String.valueOf(hour);
        if (minute < 10) {
            strMinute = "0" + minute;
        }
        if (hour < 10) {
            strHour = "0" + hour;
        }

        return String.format("%s:%s", strHour, strMinute);
    }

    /**
     * 初始化选中的音乐路径
     */
    private void initCheckedSongPaths() {
        checkedSongPaths = new ArrayList <>();
        for (SongInfo songInfo : songInfos) {
            if (songInfo.isSelect()) {
                checkedSongPaths.add(songInfo.getPath());
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Boolean getIsOpenStatus() {
        return this.isOpenStatus;
    }

    public void setIsOpenStatus(Boolean isOpenStatus) {
        this.isOpenStatus = isOpenStatus;
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getRepeatFrequency() {
        return this.repeatFrequency;
    }

    public void setRepeatFrequency(int RepeatNumber) {
        this.repeatFrequency = RepeatNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList <SongInfo> getSongInfos() {
        return songInfos;
    }

    public void setSongInfos(ArrayList <SongInfo> songInfos) {
        this.songInfos = songInfos;
        initCheckedSongPaths();
    }

    public ArrayList <AlarmRepeatMode> getAlarmRepeatMode() {
        return alarmRepeatMode;
    }

    public void setAlarmRepeatMode(ArrayList <AlarmRepeatMode> alarmRepeatMode) {
        this.alarmRepeatMode = alarmRepeatMode;
    }

    public SongPlayedMode getSongPlayedMode() {
        return this.songPlayedMode;
    }

    public void setSongPlayedMode(SongPlayedMode songPlayedMode) {
        this.songPlayedMode = songPlayedMode;
    }

    public boolean getIsVibrated() {
        return this.isVibrated;
    }

    public void setIsVibrated(boolean isVibrated) {
        this.isVibrated = isVibrated;
    }

    public ArrayList <AlarmCalendar> getAlarmCalendars() {
        return this.alarmCalendars;
    }

    public void setAlarmCalendars(ArrayList <AlarmCalendar> alarmCalendars) {
        this.alarmCalendars = alarmCalendars;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.hour);
        dest.writeInt(this.minute);
        dest.writeValue(this.isOpenStatus);
        dest.writeInt(this.interval);
        dest.writeInt(this.repeatFrequency);
        dest.writeInt(this.duration);
        dest.writeByte(this.isVibrated ? (byte) 1 : (byte) 0);
        dest.writeList(this.alarmRepeatMode);
        dest.writeTypedList(this.songInfos);
        dest.writeInt(this.songPlayedMode == null ? -1 : this.songPlayedMode.ordinal());
        dest.writeTypedList(this.alarmCalendars);
    }

    public static class Converter implements PropertyConverter <AlarmSettingInfo, String> {

        @Override
        public AlarmSettingInfo convertToEntityProperty(String databaseValue) {
            return JsonConverter.jsonToClass(databaseValue, AlarmSettingInfo.class);
        }

        @Override
        public String convertToDatabaseValue(AlarmSettingInfo entityProperty) {
            return JsonConverter.convertToStringValue(entityProperty);
        }
    }
}
