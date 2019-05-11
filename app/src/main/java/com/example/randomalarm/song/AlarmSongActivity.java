package com.example.randomalarm.song;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.randomalarm.adapter.SongDragAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;

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
    String ringName;
    String playModeName;
    ArrayList <SongInfo> songInfos;
    SongDragAdapter adapter;
    AlarmSettingInfo alarmSettingInfo;
    HashMap <SongPlayedMode, Integer> songPlayedModeAndMenuIds;
    int rbtnSongSelect;
    private Menu currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_song);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        ViewerHelper.displayHomeAsUp(actionBar);

        ringName = this.getString(R.string.ringing);
        playModeName = this.getString(R.string.play_mode);
        Intent intent = getIntent();
        alarmSettingInfo = intent.getParcelableExtra(ringName);
        songInfos = alarmSettingInfo.getSongInfos();
        initPlayModeAndId();
        initAlarmSong();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        currentMenu = menu;
        getMenuInflater().inflate(R.menu.menu_save, menu);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            case R.id.sort_ascend:
                songInfos.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_descend:
                songInfos.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
                adapter.notifyDataSetChanged();
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
        rbtnSongSelect = R.id.rbtn_song_select;
        adapter = new SongDragAdapter(songInfos);
        // 开启拖拽
        adapter.enableDrag(rvAlarmSong, R.id.tv_song_name);
        // 开启滑动删除
        adapter.enableSwipeItem();
        //支持搜索
        adapter.setQueryTextListener(svAlarmSong);
        ViewerHelper.setCheckBoxClick(adapter, rbtnSongSelect);
        rvAlarmSong.setLayoutManager(new LinearLayoutManager(this));
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
        for (int i = 0; i < adapter.getItemCount(); i++) {
            CheckBox checkBox = (CheckBox) adapter.getViewByPosition(rvAlarmSong, i, rbtnSongSelect);
            if (checkBox != null) {
                songInfos.get(i).setSelect(checkBox.isChecked());
            }
        }
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

    public Intent getNewIntent(Class <?> cls) {
        return new Intent(this, cls);
    }

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

}
