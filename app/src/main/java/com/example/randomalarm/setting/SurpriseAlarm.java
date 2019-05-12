package com.example.randomalarm.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;

import com.example.randomalarm.R;

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
    private MediaPlayer mediaPlayer;

    public SurpriseAlarm(Context context) {
        this.context = context;
    }

    public void showSurprise(int hour, int minute) {
        if (hour != HOUR || minute != MINUTE) {
            return;
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.dawn);
        //播放完成后，自动关闭
        mediaPlayer.setOnCompletionListener(mp -> {
            close();
        });
        mediaPlayer.start();
        alertDialog = new AlertDialog.Builder(context).setTitle("Surprise").setMessage("彩蛋！！")
                .setNegativeButton("关闭", (dialog, which) -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    dialog.dismiss();
                }).create();
        alertDialog.show();
    }

    public void close() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
