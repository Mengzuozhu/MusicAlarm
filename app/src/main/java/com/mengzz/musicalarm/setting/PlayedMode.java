package com.mengzz.musicalarm.setting;

import org.greenrobot.greendao.converter.PropertyConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author : Mzz
 * date : 2019 2019/5/6 10:52
 * description :
 */
@AllArgsConstructor
public enum PlayedMode {
    RANDOM(0, "随机播放"),
    SINGLE(1, "单曲循环"),
    ORDER(2, "顺序播放");

    @Getter
    private final int id;
    @Getter
    private final String desc;//中文描述

    public static class SongPlayedModeConverter implements PropertyConverter <PlayedMode,
            Integer> {

        @Override
        public PlayedMode convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return PlayedMode.RANDOM;
            }
            return PlayedMode.values()[databaseValue];
        }

        @Override
        public Integer convertToDatabaseValue(PlayedMode entityProperty) {
            return entityProperty == null ? 0 : entityProperty.getId();
        }
    }
}
