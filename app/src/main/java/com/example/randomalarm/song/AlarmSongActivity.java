package com.example.randomalarm.song;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.example.randomalarm.R;
import com.example.randomalarm.adapter.SongInfoAdapter;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.edit.AlarmEditActivity;
import com.example.randomalarm.edit.EditItemInfo;
import com.example.randomalarm.setting.AlarmSettingActivity;
import com.example.randomalarm.setting.AlarmSettingInfo;
import com.example.randomalarm.setting.SongPlayedMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 闹钟铃声设置
 */
public class AlarmSongActivity extends AppCompatActivity {

    public static final int ADD_SONG_CODE = 5;
    public static final String ADD_SONG = "ADD_SONG";
    @BindView(R.id.rv_alarm_song)
    RecyclerView rvAlarmSong;
    @BindView(R.id.sv_alarm_song)
    SearchView svAlarmSong;
    @BindView(R.id.fab_song_scroll_first)
    FloatingActionButton fabSongScrollFirst;
    @BindString(R.string.ring_name)
    String ringName;
    ArrayList <SongInfo> songInfos;
    SongInfoAdapter adapter;
    AlarmSettingInfo alarmSettingInfo;
    HashMap <SongPlayedMode, Integer> songPlayedModeAndMenuIds;
    LinearLayoutManager layoutManager;
    private Menu currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_song);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        ViewerHelper.displayHomeAsUp(actionBar);

        Intent intent = getIntent();
        alarmSettingInfo = intent.getParcelableExtra(ringName);
        songInfos = alarmSettingInfo.getSongInfos();
        initPlayModeAndId();
        initAlarmSong();
        showAndHideSongScrollFirst();
    }

    private void showAndHideSongScrollFirst() {
        rvAlarmSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        currentMenu = menu;
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_select, menu);
        getMenuInflater().inflate(R.menu.menu_play_mode, menu);
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        setPlayModeSubMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void setPlayModeSubMenu() {
        SongPlayedMode songPlayedMode = alarmSettingInfo.getSongPlayedMode();
        Integer id = songPlayedModeAndMenuIds.get(songPlayedMode);
        if (id != null) {
            currentMenu.findItem(id).setChecked(true);
        }
    }

    private void initPlayModeAndId() {
        songPlayedModeAndMenuIds = new HashMap <>();
        songPlayedModeAndMenuIds.put(SongPlayedMode.random, R.id.mode_random);
        songPlayedModeAndMenuIds.put(SongPlayedMode.single, R.id.mode_single);
        songPlayedModeAndMenuIds.put(SongPlayedMode.order, R.id.mode_order);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_select:
                adapter.selectAll(rvAlarmSong);
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
            case R.id.action_edit:
                showEditActivity();
                break;
            case R.id.mode_order:
            case R.id.mode_random:
            case R.id.mode_single:
                item.setChecked(!item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAlarmSong() {
        adapter = new SongInfoAdapter(songInfos);
        // 开启拖拽
        adapter.enableDrag(rvAlarmSong, R.id.tv_song_name);
        // 开启滑动删除
        adapter.enableSwipeItem();
        //支持搜索
        adapter.setQueryTextListener(svAlarmSong);
        layoutManager = new LinearLayoutManager(this);
        rvAlarmSong.setLayoutManager(layoutManager);
        rvAlarmSong.setAdapter(adapter);
    }

    public void showEditActivity() {
        ArrayList <EditItemInfo> editData = new ArrayList <>();
        for (int i = 0; i < songInfos.size(); i++) {
            String songName = songInfos.get(i).getName();
            editData.add(new EditItemInfo(i, songName));
        }
        Intent intent = getNewIntent(AlarmEditActivity.class);
        intent.putParcelableArrayListExtra(AlarmEditActivity.EDIT_DATA, editData);
        startActivityForResult(intent, AlarmEditActivity.EDIT_SAVE);
    }

    /**
     * 保存
     */
    public void save() {
        Intent intent = getIntent();
        setPlayMode();
        alarmSettingInfo.setSongInfos(songInfos);
        intent.putExtra(ringName, alarmSettingInfo);
        setResult(AlarmSettingActivity.RING_SET, intent);
    }

    private void setPlayMode() {
        for (Map.Entry <SongPlayedMode, Integer> songPlayedModeAndMenuId : songPlayedModeAndMenuIds.entrySet()) {
            MenuItem item = currentMenu.findItem(songPlayedModeAndMenuId.getValue());
            if (item != null && item.isChecked()) {
                alarmSettingInfo.setSongPlayedMode(songPlayedModeAndMenuId.getKey());
            }
        }
    }

    private Intent getNewIntent(Class <?> cls) {
        return new Intent(this, cls);
    }

    @OnClick(R.id.fab_song_add)
    public void addSong_onClick(View view) {
        Intent intent = getNewIntent(SongPickerActivity.class);
        startActivityForResult(intent, ADD_SONG_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_SONG_CODE) {
            ArrayList <SongInfo> newSongPaths = data.getParcelableArrayListExtra(ADD_SONG);
            songInfos.addAll(newSongPaths);
            adapter.setNewData(songInfos);
        } else if (resultCode == AlarmEditActivity.EDIT_SAVE) {
            saveEdit(data);
        }
    }

    private void saveEdit(Intent data) {
        ArrayList <Integer> deleteNumbers = data.getIntegerArrayListExtra(AlarmEditActivity.DELETE_NUM);
        if (deleteNumbers == null) {
            return;
        }
        deleteNumbers.sort(Comparator.reverseOrder());
        //从大索引处开始删除
        for (int i = 0; i < deleteNumbers.size(); i++) {
            int num = deleteNumbers.get(i);
            songInfos.remove(num);
        }
        adapter.setNewData(songInfos);

    }

    @OnClick(R.id.fab_song_scroll_first)
    public void scrollToFirstSong_onClick(View view) {
        if (adapter.getItemCount() > 0) {
            rvAlarmSong.scrollToPosition(0);
        }
    }
}
