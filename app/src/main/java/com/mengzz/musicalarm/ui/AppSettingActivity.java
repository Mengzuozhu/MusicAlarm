package com.mengzz.musicalarm.ui;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.adapter.MultipleItem;
import com.mengzz.musicalarm.adapter.MultipleItemQuickAdapter;
import com.mzz.zandroidcommon.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppSettingActivity extends BaseActivity {

    @BindView(R.id.rv_app_setting)
    RecyclerView rvAppSetting;
    MultipleItemQuickAdapter multipleItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        ButterKnife.bind(this);
        initSetting();
    }

    private void initSetting() {
        List <MultipleItem> settings = getSettings();
        multipleItemAdapter = new MultipleItemQuickAdapter(settings);
        multipleItemAdapter.setOnItemClickListener((adapter1, view, position) -> showSetting(position));
        rvAppSetting.setLayoutManager(new LinearLayoutManager(this));
        rvAppSetting.setAdapter(multipleItemAdapter);
    }

    private List <MultipleItem> getSettings() {
        List <MultipleItem> settings = new ArrayList <>();
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, "设置背景图片"));
        return settings;
    }

    private void showSetting(int position) {
        if (position == 0) {
            openActivity(RemindImageActivity.class);
        }
    }

}
