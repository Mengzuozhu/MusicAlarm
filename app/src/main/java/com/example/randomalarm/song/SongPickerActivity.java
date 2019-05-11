package com.example.randomalarm.song;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.randomalarm.R;
import com.example.randomalarm.common.TextQueryHandler;
import com.example.randomalarm.common.ViewerHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongPickerActivity extends AppCompatActivity {

    @BindView(R.id.rv_song_file)
    RecyclerView rvSongFile;
    @BindView(R.id.sv_song_file)
    SearchView svSongFile;
    BaseQuickAdapter adapter;
    ArrayList <SongInfo> songFiles;
    private int rbtnSongSelectId;
    HashMap <String, Spannable> nameAndQuerySpans = new HashMap <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_picker);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        rbtnSongSelectId = R.id.rbtn_song_select;
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                initAlarmSong();
            } else {
                ViewerHelper.showToast(SongPickerActivity.this, "无权限访问");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_select, menu);
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_select:
                selectAll();
                break;
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.sort_ascend:
                songFiles.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_descend:
                songFiles.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
                adapter.notifyDataSetChanged();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void initAlarmSong() {
        songFiles = FileManager.getInstance(SongPickerActivity.this).getSongInfos();
        adapter = new BaseQuickAdapter <SongInfo, BaseViewHolder>(R.layout.item_song, songFiles) {
            @Override
            protected void convert(BaseViewHolder helper, SongInfo item) {
                String name = item.getName();
                if (nameAndQuerySpans.containsKey(name)) {
                    helper.setText(R.id.tv_song_name, nameAndQuerySpans.get(name));
                } else {
                    helper.setText(R.id.tv_song_name, name);
                }
            }
        };
        ViewerHelper.setCheckBoxClick(adapter, rbtnSongSelectId);
        new TextQueryHandler(svSongFile, adapter, songFiles, nameAndQuerySpans).setQueryTextListener();
        rvSongFile.setLayoutManager(new LinearLayoutManager(this));
        rvSongFile.setAdapter(adapter);
    }

    /**
     * 保存
     */
    private void save() {
        ArrayList <SongInfo> newSongInfos = getCheckedSongInfos();
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(AlarmSongActivity.ADD_SONG, newSongInfos);
        setResult(AlarmSongActivity.ADD_SONG_CODE, intent);
    }

    /**
     * 全选
     */
    private void selectAll() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            CheckBox checkBox = (CheckBox) adapter.getViewByPosition(rvSongFile, i, rbtnSongSelectId);
            if (checkBox != null) {
                checkBox.setChecked(true);
            }
        }
    }

    public ArrayList <SongInfo> getCheckedSongInfos() {
        ArrayList <SongInfo> newSongPaths = new ArrayList <>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            CheckBox checkBox = (CheckBox) adapter.getViewByPosition(rvSongFile, i, rbtnSongSelectId);
            if (checkBox != null && checkBox.isChecked()) {
                newSongPaths.add(songFiles.get(i));
            }
        }
        return newSongPaths;
    }
}
