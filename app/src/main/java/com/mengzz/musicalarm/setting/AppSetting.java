package com.mengzz.musicalarm.setting;

import android.content.Context;
import android.content.SharedPreferences;

import com.mengzz.musicalarm.common.util.FileUtil;
import com.mengzz.musicalarm.image.ImageChangedMode;
import com.mengzz.musicalarm.image.RemindImage;
import com.mzz.zandroidcommon.common.JsonConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

/**
 * author : Mzz
 * date : 2019 2019/5/19 17:54
 * description :
 */
public class AppSetting {

    private static final String APP_SETTING = "AppSetting";
    @Setter
    private List<RemindImage> remindImagePaths;
    @Getter
    @Setter
    private ImageChangedMode imageChangedMode = ImageChangedMode.RANDOM;
    private List<String> checkedImagePaths = null;

    private AppSetting(ArrayList<RemindImage> remindImagePaths) {
        this.remindImagePaths = remindImagePaths;
    }

    /**
     * Read setting app setting.
     *
     * @param context the context
     * @return the app setting
     */
    public static AppSetting readSetting(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String jsonSetting = sharedPreferences.getString(APP_SETTING, "");
        AppSetting appSetting = JsonConverter.jsonToClass(jsonSetting, AppSetting.class);
        if (appSetting == null) {
            appSetting = new AppSetting(new ArrayList<>());
        }
        return appSetting;
    }

    /**
     * Apply setting.
     *
     * @param context the context
     */
    public void applySetting(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String stringValue = JsonConverter.convertToStringValue(this);
        editor.putString(APP_SETTING, stringValue);
        editor.apply();
    }

    /**
     * Gets showed image path.
     *
     * @return the showed image path
     */
    public String getShowedImagePath() {
        if (checkedImagePaths == null) {
            initCheckedImagePaths();
        }
        if (checkedImagePaths.isEmpty()) {
            return "";
        }
        int index = 0;
        if (imageChangedMode == ImageChangedMode.RANDOM) {
            Random random = new Random();
            index = random.nextInt(checkedImagePaths.size());
        }
        return checkedImagePaths.get(index);
    }

    public List<RemindImage> getRemindImagePaths() {
        if (remindImagePaths == null) {
            remindImagePaths = new ArrayList<>();
        }
        return remindImagePaths;
    }

    /**
     * Sets next image changed mode.
     *
     * @return the next image changed mode
     */
    public ImageChangedMode setNextImageChangedMode() {
        imageChangedMode = ImageChangedMode.getNextMode(imageChangedMode.getId());
        return imageChangedMode;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTING, Context.MODE_PRIVATE);
    }

    private void initCheckedImagePaths() {
        checkedImagePaths = new ArrayList<>();
        for (RemindImage remindImage : getRemindImagePaths()) {
            if (remindImage.isChecked() && FileUtil.isFileExists(remindImage.getFilePath())) {
                checkedImagePaths.add(remindImage.getFilePath());
            }
        }
    }
}
