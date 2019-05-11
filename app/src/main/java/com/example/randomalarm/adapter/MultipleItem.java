package com.example.randomalarm.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * author : Mzz
 * date : 2019 2019/5/11 16:47
 * description :
 */
public class MultipleItem implements MultiItemEntity {
    public static final int RIGHT_BUTTON = 1;
    public static final int SWITCH = 2;
    private int itemType;
    private String content;
    private boolean isSwitch = false;

    public MultipleItem(int itemType, String content, boolean isSwitch) {
        this.itemType = itemType;
        this.content = content;
        this.isSwitch = isSwitch;
    }

    public MultipleItem(int itemType, String content) {
        this.itemType = itemType;
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public String getContent() {
        return content;
    }

}
