package com.example.randomalarm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.randomalarm.alarm.AlarmConstant;
import com.example.randomalarm.alarm.TimeTickReceiver;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.EventBusHelper;
import com.example.randomalarm.setting.AppSetting;

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
 * Use the {@link RemindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindFragment extends Fragment {

    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_real_time)
    TextView tvRealTime;
    @BindView(R.id.tv_alarm_song)
    TextView tvAlarmSong;
    @BindView(R.id.iv_moon_remind)
    ImageView ivMoonRemind;
    @BindView(R.id.iv_sun_close)
    ImageView ivSunClose;
    TimeTickReceiver timeTickReceiver;
    @BindView(R.id.layout_remind1)
    ConstraintLayout layoutRemind;
    Context context;
    FragmentActivity activity;
    AlarmHandler alarmOnLister;
    private ExplosionField explosionField;

    public static RemindFragment newInstance(String ringTitle) {
        RemindFragment fragment = new RemindFragment();
        Bundle args = new Bundle();
        args.putString(AlarmConstant.RING_TITLE, ringTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public void setAlarmOnLister(AlarmHandler alarmOnLister) {
        this.alarmOnLister = alarmOnLister;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.bind(this, view);
        EventBusHelper.register(this);
        context = getContext();
        activity = getActivity();
        timeTickReceiver = new TimeTickReceiver();
        activity.registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        explosionField = ExplosionField.attach2Window(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String ringTitle = bundle.getString(AlarmConstant.RING_TITLE);
            tvAlarmSong.setText(ringTitle);
        }
        showRealTime(Calendar.getInstance());
        setBackgroundImage();
        return view;
    }

    private void setBackgroundImage() {
        AppSetting setting = AppSetting.getSetting(context);
        String remindImagePath = setting.getRemindImagePath();
        File file = new File(remindImagePath);
        if (!file.exists()) {
            return;
        }
        Drawable drawable = Drawable.createFromPath(remindImagePath);
        layoutRemind.setBackground(drawable);
    }

    @Subscribe
    public void getRealTimeEvent(Calendar calendar) {
        showRealTime(calendar);
    }

    public void showRealTime(Calendar calendar) {
        tvRealTime.setText(DateHelper.formatHHmm(calendar.getTime()));
    }

    @Override
    public void onDestroy() {
        EventBusHelper.unregister(this);
        activity.unregisterReceiver(timeTickReceiver);
        super.onDestroy();
    }

    @OnClick({R.id.iv_sun_close, R.id.tv_close})
    public void close_onClick(View view) {
        explode(ivSunClose);
        explode(tvClose);
        if (alarmOnLister != null) {
            alarmOnLister.setFirstAlarm();
        }
    }

    @OnClick({R.id.iv_moon_remind, R.id.tv_remind})
    public void remind_onClick(View view) {
        explode(ivMoonRemind);
        explode(tvRemind);
        if (alarmOnLister != null) {
            alarmOnLister.setNextIntervalAlarm();
        }
    }

    public void explode(View view) {
        explosionField.explode(view);
        view.setOnClickListener(null);
    }

    public interface AlarmHandler {
        void setNextIntervalAlarm();

        void setFirstAlarm();
    }
}