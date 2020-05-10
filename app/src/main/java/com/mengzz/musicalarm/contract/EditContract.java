package com.mengzz.musicalarm.contract;

import android.app.Activity;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/5/26 16:06
 * description :
 */
public interface EditContract {

    interface View {
        Activity getActivity();

    }

    interface Presenter {
        void deleteAll();

        void setDeleteAlarmListener();

        ArrayList <Integer> getDeleteIds();
    }
}
