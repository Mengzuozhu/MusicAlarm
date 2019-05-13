package com.example.randomalarm.song;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.randomalarm.R;
import com.example.randomalarm.adapter.SongInfoAdapter;
import com.example.randomalarm.common.ViewerHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongPickerActivity extends AppCompatActivity {

    @BindView(R.id.rv_song_file)
    RecyclerView rvSongFile;
    @BindView(R.id.sv_song_file)
    SearchView svSongFile;
    SongInfoAdapter adapter;
    ArrayList <SongInfo> songFiles;
    LinearLayoutManager layoutManager;
    @BindView(R.id.fab_song_scroll_first)
    FloatingActionButton fabSongScrollFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_picker);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                initAlarmSong();
            } else {
                ViewerHelper.showToast(SongPickerActivity.this, "无权限访问");
            }
        });
        showAndHideSongScrollFirst();
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
                adapter.selectAll(rvSongFile);
                break;
            case R.id.action_save:
                save();
                this.finish();
                return true;
            case R.id.sort_ascend:
                adapter.sort(true);
                break;
            case R.id.sort_descend:
                adapter.sort(false);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showAndHideSongScrollFirst() {
        rvSongFile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null) {
                    int position = layoutManager.findFirstVisibleItemPosition();
                    if (position > 0) {
                        fabSongScrollFirst.show();
                    } else {
                        fabSongScrollFirst.hide();
                    }
                }
            }
        });
    }

    public void initAlarmSong() {
        songFiles = FileManager.getInstance(SongPickerActivity.this).getSongInfos();
        adapter = new SongInfoAdapter(songFiles);
        adapter.setQueryTextListener(svSongFile);
        layoutManager = new LinearLayoutManager(this);
        rvSongFile.setLayoutManager(layoutManager);
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

    public ArrayList <SongInfo> getCheckedSongInfos() {
        ArrayList <SongInfo> newSongPaths = new ArrayList <>();
//        for (int i = 0; i < adapter.getItemCount(); i++) {
//            CheckBox checkBox = (CheckBox) adapter.getViewByPosition(rvSongFile, i, chbSongSelectId);
//            if (checkBox != null && checkBox.isChecked()) {
//                newSongPaths.add(songFiles.get(i));
//            }
//        }
        for (SongInfo songFile : songFiles) {
            if (songFile.isSelect()) {
                newSongPaths.add(songFile);
            }
        }
        return newSongPaths;
    }

    public void scrollToFirstSong_onClick(View view) {
        if (adapter.getItemCount() > 0) {
            rvSongFile.scrollToPosition(0);
        }
    }
}
