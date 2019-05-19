package com.example.randomalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.randomalarm.adapter.MultipleItem;
import com.example.randomalarm.adapter.MultipleItemQuickAdapter;
import com.example.randomalarm.common.ViewerHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppSettingActivity extends AppCompatActivity {

    @BindView(R.id.rv_app_setting)
    RecyclerView rvAppSetting;
    MultipleItemQuickAdapter multipleItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        initSetting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSetting() {
        List <MultipleItem> settings = getSettings();
        multipleItemAdapter = new MultipleItemQuickAdapter(settings);
        multipleItemAdapter.setOnItemClickListener((adapter1, view, position) -> {
            TextView textView = view.findViewById(R.id.tv_setting_name);
            showSetting(position);
        });
        rvAppSetting.setLayoutManager(new LinearLayoutManager(this));
        rvAppSetting.setAdapter(multipleItemAdapter);
    }

    private Intent getNewIntent(Class <?> cls) {
        return new Intent(AppSettingActivity.this, cls);
    }

    private List <MultipleItem> getSettings() {
        List <MultipleItem> settings = new ArrayList <>();
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, "设置背景图片"));
        return settings;
    }

    private void showSetting(int position) {
        if (position == 0) {
            Intent newIntent = getNewIntent(RemindImagePickerActivity.class);
            startActivity(newIntent);
        }
    }

}
