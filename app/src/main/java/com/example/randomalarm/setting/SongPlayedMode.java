package com.example.randomalarm.setting;

import com.example.randomalarm.R;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/5/6 10:52
 * description :
 */
public enum SongPlayedMode {
    random(0, "随机播放"),
    single(1, "单曲循环"),
    order(2, "顺序播放");

    private final int id;
    private final String desc;//中文描述

    SongPlayedMode(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static  class SongPlayedModeConverter implements PropertyConverter <SongPlayedMode, Integer > {

        @Override
        public SongPlayedMode convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return SongPlayedMode.random;
            }
            return SongPlayedMode.values()[databaseValue];
        }

        @Override
        public Integer convertToDatabaseValue(SongPlayedMode entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }
}
