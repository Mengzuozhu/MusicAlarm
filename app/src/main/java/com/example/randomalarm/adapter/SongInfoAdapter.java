package com.example.randomalarm.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.example.randomalarm.R;
import com.example.randomalarm.common.TextQueryHandler;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.song.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 铃声可拖拽适配器
 * author : Mzz
 * date : 2019 2019/5/11 16:26
 * description :
 */
public class SongInfoAdapter extends BaseItemDraggableAdapter <SongInfo, BaseViewHolder> {

    private ArrayList <SongInfo> songInfos;
    private int chbSongSelectId;

    private HashMap <String, Spannable> nameAndQuerySpans = new HashMap <>();

    public SongInfoAdapter(ArrayList <SongInfo> songInfos) {
        super(R.layout.item_song, songInfos);
        this.songInfos = songInfos;
        chbSongSelectId = R.id.chb_song_select;
        setOnItemChildCheckAlarmListener();
        ViewerHelper.setOnItemClickWithCheckBox(this, R.id.chb_song_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, SongInfo item) {
        String name = item.getName();
        if (nameAndQuerySpans.containsKey(name)) {
            helper.setText(R.id.tv_song_name, nameAndQuerySpans.get(name));
        } else {
            helper.setText(R.id.tv_song_name, name);
        }
        helper.setChecked(chbSongSelectId, item.isSelect()).addOnClickListener(chbSongSelectId);
        ;
    }

    /**
     * 开启拖拽
     *
     * @param recyclerView
     * @param toggleViewId
     */
    public void enableDrag(RecyclerView recyclerView, int toggleViewId) {
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // 开启拖拽
        this.enableDragItem(itemTouchHelper, toggleViewId, true);
    }

    public void setQueryTextListener(SearchView svAlarmSong) {
        new TextQueryHandler(svAlarmSong, this, songInfos, nameAndQuerySpans).setQueryTextListener();
    }

    private void setOnItemChildCheckAlarmListener() {
        this.setOnItemChildClickListener((adapter, view, position) -> {
            CheckBox checkBox = (CheckBox) view;
            SongInfo songInfo = (SongInfo) adapter.getItem(position);
            if (songInfo != null) {
                songInfo.setSelect(checkBox.isChecked());
            }
        });
    }

    /**
     * 全选
     */
    public void selectAll(RecyclerView recyclerView) {
        for (int i = 0; i < this.getItemCount(); i++) {
            CheckBox checkBox = (CheckBox) this.getViewByPosition(recyclerView, i, chbSongSelectId);
            if (checkBox != null) {
                checkBox.setChecked(true);
            }
            SongInfo songInfo = this.getItem(i);
            if (songInfo != null) {
                songInfo.setSelect(true);
            }
        }
    }

    public void sort(boolean isAscend) {
        if (isAscend) {
            getData().sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        } else {
            getData().sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        }
        notifyDataSetChanged();
    }
}
