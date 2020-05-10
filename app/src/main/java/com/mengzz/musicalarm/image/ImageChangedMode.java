package com.mengzz.musicalarm.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author : Mzz
 * date : 2019 2019/5/25 20:47
 * description :
 */
@AllArgsConstructor
public enum ImageChangedMode {
    RANDOM(0, "随机模式"),
    SINGLE(1, "单一模式");

    @Getter
    private final int id;
    @Getter
    private final String desc;//中文描述

    /**
     * Gets next mode.
     *
     * @param id the id
     * @return the next mode
     */
    public static ImageChangedMode getNextMode(int id) {
        id++;
        int length = values().length;
        if (id >= length) {
            id = id % length;
        }
        return values()[id];
    }

}
