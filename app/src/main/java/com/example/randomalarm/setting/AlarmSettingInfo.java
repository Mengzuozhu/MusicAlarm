package com.example.randomalarm.setting;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.song.SongInfoListConverter;
import com.example.randomalarm.song.SongInfo;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * author : Mzz
 * date : 2019 2019/4/25 16:19
 * description :
 */
@Entity
public class AlarmSettingInfo implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    private int hour;
    private int minute;
    private Boolean isOpenStatus = true;
    //闹钟间隔，单位分钟，默认为5
    private int interval = 1;
    //重复响铃次数
    private int repeatFrequency = 3;
    //响铃时长，单位分钟，默认为1
    private int duration = 1;

    //重复模式
    @Convert(columnType = String.class, converter = AlarmRepeatConverter.class)
    private ArrayList <AlarmRepeatMode> alarmRepeatMode = new ArrayList <>();
    //铃声信息
    @Convert(columnType = String.class, converter = SongInfoListConverter.class)
    private ArrayList <SongInfo> songInfos = new ArrayList <>();
    //播放模式
    @Convert(columnType = Integer.class, converter = SongPlayedMode.SongPlayedModeConverter.class)
    private SongPlayedMode songPlayedMode = SongPlayedMode.random;

    @Transient
    private int currentRepeatNum;
    @Transient
    private ArrayList <String> checkedSongPaths = null;

    @Generated(hash = 1196837071)
    public AlarmSettingInfo(Long id, int hour, int minute, Boolean isOpenStatus, int interval, int repeatFrequency,
            int duration, ArrayList<AlarmRepeatMode> alarmRepeatMode, ArrayList<SongInfo> songInfos,
            SongPlayedMode songPlayedMode) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isOpenStatus = isOpenStatus;
        this.interval = interval;
        this.repeatFrequency = repeatFrequency;
        this.duration = duration;
        this.alarmRepeatMode = alarmRepeatMode;
        this.songInfos = songInfos;
        this.songPlayedMode = songPlayedMode;
    }

    @Generated(hash = 664695116)
    public AlarmSettingInfo() {
    }

//    public boolean isTrigger(String currentTime) {
//        currentRepeatNum = 0;
//        if (!isOpenStatus) return false;
//
//        Calendar calendar = Calendar.getInstance();
//        //修改为从0开始
//        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
//        if (AlarmRepeatMode.isNowNotInRepeatModeDay(alarmRepeatMode, weekDay)) return false;
//
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, minute);
//        Log.w("id:", String.valueOf(id));
//        //判断当前时间是否满足闹钟的触发时间，需考虑重复间隔和次数
//        for (int i = 0; i < repeatFrequency; i++) {
////            //若已经关闭闹钟，则不再提醒
////            if (!isRemindLater && i > 0) {
////                return false;
////            }
//            String triggerTime = DateHelper.getHourAndMinuteString(calendar);
//            if (triggerTime.equals(currentTime)) {
//                currentRepeatNum = i;
//                //闹钟第一次响，则可以再次提醒
//                if (i == 0) {
//                    updateRemindLater(true);
//                }
//                return true;
//            }
//            //每次添加一个时间间隔，因为每次循环都会累加
//            calendar.add(Calendar.MINUTE, interval);
//        }
//        return false;
//    }

    public Calendar getNextIntervalAlarm() {
        if (AlarmRepeatMode.isNowNotInRepeatModeDay(alarmRepeatMode)) return null;

        Calendar calendar = DateHelper.getCalendarByHourAndMinute(hour, minute);
        Calendar nowTime = DateHelper.getNowTime();
        //与下一分钟比较，避免重复触发
        nowTime.add(Calendar.MINUTE, 1);
        for (int i = 0; i < repeatFrequency; i++) {
            //下一个间隔闹钟时间大于当前时间
            if (calendar.compareTo(nowTime) > 0) {
                return calendar;
            }
            calendar.add(Calendar.MINUTE, interval);
        }
        return null;
    }

    public String getPlayedSongPath() {
        if (checkedSongPaths == null) {
            initSelectedSongPaths();
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

    public String getShowTime() {
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

    private void initSelectedSongPaths() {
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
        initSelectedSongPaths();
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
        dest.writeList(this.alarmRepeatMode);
        dest.writeTypedList(this.songInfos);
        dest.writeInt(this.songPlayedMode == null ? -1 : this.songPlayedMode.ordinal());
    }

    protected AlarmSettingInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.isOpenStatus = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.interval = in.readInt();
        this.repeatFrequency = in.readInt();
        this.duration = in.readInt();
        this.alarmRepeatMode = new ArrayList <AlarmRepeatMode>();
        in.readList(this.alarmRepeatMode, AlarmRepeatMode.class.getClassLoader());
        this.songInfos = in.createTypedArrayList(SongInfo.CREATOR);
        int tmpSongPlayedMode = in.readInt();
        this.songPlayedMode = tmpSongPlayedMode == -1 ? null : SongPlayedMode.values()[tmpSongPlayedMode];
    }

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
}
