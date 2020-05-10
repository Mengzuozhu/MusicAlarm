package com.mengzz.musicalarm.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * author : Mzz
 * date : 2019 2019/5/11 16:47
 * description :
 */
@AllArgsConstructor
public class MultipleItem implements MultiItemEntity {
    public static final int RIGHT_BUTTON = 1;
    public static final int SWITCH = 2;
    private int itemType;
    @Getter
    private String content;
    @Getter
    private boolean isSwitch = false;

    public MultipleItem(int itemType, String content) {
        this.itemType = itemType;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

}
