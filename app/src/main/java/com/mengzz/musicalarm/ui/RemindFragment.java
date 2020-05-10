package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.alarm.TimeTickReceiver;
import com.mzz.zandroidcommon.common.DateHelper;
import com.mzz.zandroidcommon.common.EventBusHelper;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tyrantgit.explosionfield.ExplosionField;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class RemindFragment extends Fragment {

    public static final String RING_TITLE = "RING_TITLE";
    public static final String REMIND_IMAGE_PATH = "REMIND_IMAGE_PATH";
    public static final String REMARK_KEY = "REMARK_KEY";
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_real_time)
    TextView tvRealTime;
    @BindView(R.id.tv_alarm_song)
    TextView tvAlarmSong;
    @BindView(R.id.tv_alarm_remark)
    TextView tvRemark;
    @BindView(R.id.iv_moon_remind)
    ImageView ivMoonRemind;
    @BindView(R.id.iv_sun_close)
    ImageView ivSunClose;
    @BindView(R.id.layout_remind1)
    ConstraintLayout layoutRemind;
    private TimeTickReceiver timeTickReceiver;
    private FragmentActivity activity;
    private AlarmHandler alarmHandler;
    private ExplosionField explosionField;

    public static RemindFragment newInstance(String ringTitle, String remindImagePath,
                                             String remark) {
        RemindFragment fragment = new RemindFragment();
        Bundle args = new Bundle();
        args.putString(RING_TITLE, ringTitle);
        args.putString(REMIND_IMAGE_PATH, remindImagePath);
        args.putString(REMARK_KEY, remark);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.bind(this, view);
        EventBusHelper.register(this);
        activity = getActivity();
        timeTickReceiver = new TimeTickReceiver();
        activity.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        explosionField = ExplosionField.attach2Window(activity);
        Bundle bundle = getArguments();
        String remindImagePath = "";
        if (bundle != null) {
            String ringTitle = bundle.getString(RING_TITLE);
            tvAlarmSong.setText(ringTitle);
            String remark = bundle.getString(REMARK_KEY);
            tvRemark.setText(remark);
            remindImagePath = bundle.getString(REMIND_IMAGE_PATH);
        }
        showRealTime(Calendar.getInstance());
        setBackgroundImage(remindImagePath);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBusHelper.unregister(this);
        activity.unregisterReceiver(timeTickReceiver);
        super.onDestroy();
    }

    public void setAlarmHandler(AlarmHandler alarmHandler) {
        this.alarmHandler = alarmHandler;
    }

    @Subscribe
    public void getRealTimeEvent(Calendar calendar) {
        showRealTime(calendar);
    }

    public void showRealTime(Calendar calendar) {
        tvRealTime.setText(DateHelper.formatHHmm(calendar.getTime()));
    }

    @OnClick({R.id.iv_sun_close, R.id.tv_close})
    public void closeOnClick() {
        explode(ivSunClose);
        explode(tvClose);
        if (alarmHandler != null) {
            alarmHandler.setFirstAlarm();
        }
    }

    @OnClick({R.id.iv_moon_remind, R.id.tv_remind})
    public void remindOnClick() {
        explode(ivMoonRemind);
        explode(tvRemind);
        if (alarmHandler != null) {
            alarmHandler.setNextIntervalAlarm();
        }
    }

    public void explode(View view) {
        explosionField.explode(view);
        view.setOnClickListener(null);
    }

    private void setBackgroundImage(String remindImagePath) {
        File file = new File(remindImagePath);
        if (file.exists()) {
            Drawable drawable = Drawable.createFromPath(remindImagePath);
            layoutRemind.setBackground(drawable);
        } else {
            layoutRemind.setBackground(null);
            int color = activity.getColor(R.color.colorSkyBlue);
            layoutRemind.setBackgroundColor(color);
        }
    }

    public interface AlarmHandler {
        void setNextIntervalAlarm();

        void setFirstAlarm();
    }
}
