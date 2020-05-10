package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.adapter.SongInfoAdapter;
import com.mengzz.musicalarm.edit.EditItemInfo;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.setting.PlayedMode;
import com.mengzz.musicalarm.song.SongInfo;
import com.mzz.zandroidcommon.view.BaseActivity;
import com.mzz.zandroidcommon.view.ViewerHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 闹钟铃声设置
 */
public class AlarmSongActivity extends BaseActivity {

    public static final int ADD_SONG_CODE = 5;
    public static final int ALARM_SONG_SET_CODE = 4;
    public static final String ADD_SONG = "ADD_SONG";
    public static final String ALARM_SONG_INFO = "ALARM_SONG_INFO";
    @BindView(R.id.rv_alarm_song)
    RecyclerView rvAlarmSong;
    @BindView(R.id.sv_alarm_song)
    SearchView svAlarmSong;
    @BindView(R.id.fab_song_scroll_first)
    FloatingActionButton fabSongScrollFirst;
    ArrayList <SongInfo> songInfos;
    SongInfoAdapter adapter;
    AlarmSettingInfo alarmSettingInfo;
    EnumMap <PlayedMode, Integer> songPlayedModeAndMenuIds;
    private Menu currentMenu;

    /**
     * Start for result.
     *
     * @param activity the activity
     * @param value    the value
     */
    public static void startForResult(FragmentActivity activity, Parcelable value) {
        Intent intent = new Intent(activity, AlarmSongActivity.class).putExtra(ALARM_SONG_INFO,
                value);
        activity.startActivityForResult(intent, ALARM_SONG_SET_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_song);
        ButterKnife.bind(this);

        alarmSettingInfo = getParcelableExtra(ALARM_SONG_INFO);
        songInfos = alarmSettingInfo.getSongInfos();
        initPlayModeAndId();
        initAlarmSong();
        fabSongScrollFirst.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        currentMenu = menu;
        getMenuInflater().inflate(R.menu.menu_save, menu);
        getMenuInflater().inflate(R.menu.menu_select_all, menu);
        getMenuInflater().inflate(R.menu.menu_play_mode, menu);
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        setPlayModeSubMenu();
        return super.onCreateOptionsMenu(menu);
    }

    private void setPlayModeSubMenu() {
        PlayedMode playedMode = alarmSettingInfo.getPlayedMode();
        Integer id = songPlayedModeAndMenuIds.get(playedMode);
        if (id != null) {
            currentMenu.findItem(id).setChecked(true);
        }
    }

    private void initPlayModeAndId() {
        songPlayedModeAndMenuIds = new EnumMap <>(PlayedMode.class);
        songPlayedModeAndMenuIds.put(PlayedMode.RANDOM, R.id.mode_random);
        songPlayedModeAndMenuIds.put(PlayedMode.SINGLE, R.id.mode_single);
        songPlayedModeAndMenuIds.put(PlayedMode.ORDER, R.id.mode_order);
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
            case R.id.action_edit:
                showEditActivity();
                break;
            case R.id.mode_order:
                setPlayedMode(item, PlayedMode.RANDOM);
                break;
            case R.id.mode_random:
                setPlayedMode(item, PlayedMode.RANDOM);
                break;
            case R.id.mode_single:
                setPlayedMode(item, PlayedMode.SINGLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPlayedMode(MenuItem item, PlayedMode random) {
        item.setChecked(true);
        alarmSettingInfo.setPlayedMode(random);
    }

    private void initAlarmSong() {
        adapter = new SongInfoAdapter(songInfos, rvAlarmSong, this);
        // 开启拖拽
        adapter.enableDrag(R.id.tv_song_name);
        // 开启滑动删除
        adapter.enableSwipeItem();
        //支持搜索
        adapter.setQueryTextListener(svAlarmSong);
        ViewerHelper.setScrollFirstShowInNeed(rvAlarmSong, adapter.getLayoutManager(),
                fabSongScrollFirst);
    }

    private void showEditActivity() {
        ArrayList <EditItemInfo> editData = new ArrayList <>();
        for (int i = 0; i < songInfos.size(); i++) {
            String songName = songInfos.get(i).getName();
            editData.add(new EditItemInfo(i, songName));
        }
        LinearEditActivity.startForResult(this, editData);
    }

    /**
     * 保存
     */
    public void save() {
        setPlayedMode();
        alarmSettingInfo.setSongInfos(songInfos);
        Intent intent = putExtra(ALARM_SONG_INFO, alarmSettingInfo);
        setResult(ALARM_SONG_SET_CODE, intent);
    }

    private void setPlayedMode() {
        for (Map.Entry <PlayedMode, Integer> songPlayedModeAndMenuId :
                songPlayedModeAndMenuIds.entrySet()) {
            MenuItem item = currentMenu.findItem(songPlayedModeAndMenuId.getValue());
            if (item != null && item.isChecked()) {
                alarmSettingInfo.setPlayedMode(songPlayedModeAndMenuId.getKey());
                break;
            }
        }
    }

    @OnClick(R.id.fab_song_add)
    public void addSongOnClick(View view) {
        openActivityForResult(SongPickerActivity.class, ADD_SONG_CODE);
    }

    @OnClick(R.id.fab_song_scroll_first)
    public void scrollToFirstSong_onClick(View view) {
        if (adapter.getItemCount() > 0) {
            rvAlarmSong.scrollToPosition(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == ADD_SONG_CODE) {
            ArrayList <SongInfo> newSongPaths = data.getParcelableArrayListExtra(ADD_SONG);
            songInfos.addAll(newSongPaths);
            adapter.setNewData(songInfos);
        } else if (resultCode == LinearEditActivity.EDIT_SAVE) {
            saveEdit(data);
        }
    }

    private void saveEdit(Intent data) {
        ArrayList <Integer> deleteNumbers =
                data.getIntegerArrayListExtra(LinearEditActivity.DELETE_NUM);
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
