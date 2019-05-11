package com.example.randomalarm.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.randomalarm.R;
import com.example.randomalarm.common.ViewerHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmEditActivity extends AppCompatActivity {

    public static final String EDIT_DATA = "EDIT_DATA";
    public static final String DELETE_NUM = "DELETE_NUM";
    public static final int EDIT_SAVE = 3;
    @BindView(R.id.rv_edit)
    RecyclerView rvEdit;
    ArrayList <Integer> deleteData;
    ArrayList <EditItemInfo> editData;
    BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        Intent intent = getIntent();
        editData = intent.getParcelableArrayListExtra(EDIT_DATA);
        deleteData = new ArrayList <>();
        initAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.action_delete_all:
                deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAll() {
        for (EditItemInfo editDatum : editData) {
            deleteData.add(editDatum.getId());
        }
        adapter.setNewData(new ArrayList());
    }

    public void save() {
        Intent intent = getIntent();
        intent.putIntegerArrayListExtra(DELETE_NUM, deleteData);
        setResult(EDIT_SAVE, intent);
    }

    public void initAlarm() {
        adapter = new BaseQuickAdapter <EditItemInfo, BaseViewHolder>(R.layout.item_edit, editData) {
            @Override
            protected void convert(BaseViewHolder helper, EditItemInfo item) {
                helper.setText(R.id.tv_edit_time, item.getInfo()).addOnClickListener(R.id.iv_edit_del);
            }
        };
        setDeleteAlarmListener(adapter);
        rvEdit.setLayoutManager(new LinearLayoutManager(this));
        rvEdit.setAdapter(adapter);
    }

    public void setDeleteAlarmListener(BaseQuickAdapter adapter) {
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            EditItemInfo item = (EditItemInfo) adapter1.getItem(position);
            if (item == null) {
                return;
            }
            deleteData.add(item.getId());
            adapter1.remove(position);
        });
    }
}
