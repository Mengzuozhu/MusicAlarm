package com.example.randomalarm.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.randomalarm.common.JsonConverter;

/**
 * author : Mzz
 * date : 2019 2019/5/19 17:54
 * description :
 */
public class AppSetting {

    private final static String APP_SETTING = "AppSetting";
    private String remindImagePath;

    public AppSetting(String remindImagePath) {
        this.remindImagePath = remindImagePath;
    }

    public static AppSetting getSetting(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String jsonSetting = sharedPreferences.getString(APP_SETTING, "");
        AppSetting appSetting = JsonConverter.jsonToClass(jsonSetting, AppSetting.class);
        if (appSetting == null) {
            return new AppSetting("");
        }
        return appSetting;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTING, Context.MODE_PRIVATE);
    }

    public void writeSetting(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String stringValue = JsonConverter.convertToStringValue(this);
        editor.putString(APP_SETTING, stringValue);
        editor.apply();
    }

    public String getRemindImagePath() {
        return remindImagePath;
    }

    public void setRemindImagePath(String remindImagePath) {
        this.remindImagePath = remindImagePath;
    }
}
