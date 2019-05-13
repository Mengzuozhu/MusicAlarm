package com.example.randomalarm.setting;

import android.app.AlertDialog;
import android.content.Context;

import com.example.randomalarm.R;
import com.example.randomalarm.common.MediaPlayHelper;

/**
 * author : Mzz
 * date : 2019 2019/5/12 21:45
 * description :
 */
public class SurpriseAlarm {

    private final static int HOUR = 14;
    private final static int MINUTE = 25;
    private AlertDialog alertDialog;
    private Context context;
    private MediaPlayHelper mediaPlayHelper;

    public SurpriseAlarm(Context context) {
        this.context = context;
    }

    public void showSurprise(int hour, int minute) {
        if (hour != HOUR || minute != MINUTE) {
            return;
        }
        mediaPlayHelper = new MediaPlayHelper(context, R.raw.sea);
        //播放完成后，自动关闭
        mediaPlayHelper.setOnCompletionListener(mp -> {
            close();
        });
        mediaPlayHelper.start();
        alertDialog = new AlertDialog.Builder(context).setTitle("彩蛋！").setMessage("老人与海")
                .setNegativeButton("关闭", (dialog, which) -> {
                    close();
                }).create();
        alertDialog.show();
    }

    public void close() {
        if (mediaPlayHelper != null) {
            mediaPlayHelper.release();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
