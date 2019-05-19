package com.example.randomalarm.setting;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author : Mzz
 * date : 2019 2019/5/19 17:54
 * description :
 */
@Entity
public class DefaultSetting {

    @Id(autoincrement = true)
    private Long id;
    @Convert(columnType = String.class, converter = AlarmSettingInfo.Converter.class)
    private AlarmSettingInfo alarmSettingInfo;
    private String remindImagePath;

    @Generated(hash = 439450473)
    public DefaultSetting(Long id, AlarmSettingInfo alarmSettingInfo,
            String remindImagePath) {
        this.id = id;
        this.alarmSettingInfo = alarmSettingInfo;
        this.remindImagePath = remindImagePath;
    }

    @Generated(hash = 815651863)
    public DefaultSetting() {
    }

    public String getRemindImagePath() {
        return remindImagePath;
    }

    public void setRemindImagePath(String remindImagePath) {
        this.remindImagePath = remindImagePath;
    }

    public AlarmSettingInfo getAlarmSettingInfo() {
        return alarmSettingInfo;
    }

    public void setAlarmSettingInfo(AlarmSettingInfo alarmSettingInfo) {
        this.alarmSettingInfo = alarmSettingInfo;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
