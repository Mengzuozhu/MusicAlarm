package com.mengzz.musicalarm.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.adapter.SongInfoAdapter;
import com.mengzz.musicalarm.song.FileManager;
import com.mengzz.musicalarm.song.SongInfo;
import com.mzz.zandroidcommon.view.BaseActivity;
import com.mzz.zandroidcommon.view.ViewerHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongPickerActivity extends BaseActivity {

    @BindView(R.id.rv_song_file)
    RecyclerView rvSongFile;
    @BindView(R.id.sv_song_file)
    SearchView svSongFile;
    @BindView(R.id.fab_song_file_scroll_first)
    FloatingActionButton fabSongScrollFirst;
    SongInfoAdapter adapter;
    List <SongInfo> songFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_picker);
        ButterKnife.bind(this);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                initAlarmSong();
            } else {
                showToast("无权限访问");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_select_all, menu);
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_select_all:
                adapter.selectAll(true);
                break;
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.action_sort_ascend:
                adapter.sort(true);
                break;
            case R.id.action_sort_descend:
                adapter.sort(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initAlarmSong() {
        songFiles = FileManager.getInstance(SongPickerActivity.this).getSongInfos();
        adapter = new SongInfoAdapter(songFiles, rvSongFile, this);
        adapter.setQueryTextListener(svSongFile);
        ViewerHelper.setScrollFirstShowInNeed(rvSongFile, adapter.getLayoutManager(),
                fabSongScrollFirst);
    }

    /**
     * 保存
     */
    private void save() {
        ArrayList <SongInfo> newSongInfos = getCheckedSongInfos();
        Intent intent = getIntent().putParcelableArrayListExtra(AlarmSongActivity.ADD_SONG,
                newSongInfos);
        setResult(AlarmSongActivity.ADD_SONG_CODE, intent);
    }

    private ArrayList <SongInfo> getCheckedSongInfos() {
        ArrayList <SongInfo> newSongPaths = new ArrayList <>();
        for (SongInfo songFile : songFiles) {
            if (songFile.isChecked()) {
                newSongPaths.add(songFile);
            }
        }
        return newSongPaths;
    }

    @OnClick(R.id.fab_song_file_scroll_first)
    public void scrollToFirstSong_onClick(View view) {
        if (adapter.getItemCount() > 0) {
            rvSongFile.scrollToPosition(0);
        }
    }
}
