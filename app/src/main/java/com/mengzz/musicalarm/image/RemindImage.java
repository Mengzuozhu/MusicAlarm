package com.mengzz.musicalarm.image;

import com.mzz.zandroidcommon.adapter.ICheckable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * author : Mzz
 * date : 2019 2019/5/25 16:36
 * description :
 */
@Data
@AllArgsConstructor
public class RemindImage implements ICheckable {

    String filePath;
    boolean isChecked;

    @Override
    public void setIsChecked(boolean b) {
        isChecked = b;
    }
}
