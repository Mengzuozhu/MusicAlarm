package com.example.randomalarm.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Spannable;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.example.randomalarm.R;
import com.example.randomalarm.common.TextQueryHandler;
import com.example.randomalarm.song.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 铃声可拖拽适配器
 * author : Mzz
 * date : 2019 2019/5/11 16:26
 * description :
 */
public class SongDragAdapter extends BaseItemDraggableAdapter <SongInfo, BaseViewHolder> {

    private ArrayList <SongInfo> songInfos;

    private HashMap <String, Spannable> nameAndQuerySpans = new HashMap <>();

    public SongDragAdapter(ArrayList <SongInfo> songInfos) {
        super(R.layout.item_song, songInfos);
        this.songInfos = songInfos;
    }

    @Override
    protected void convert(BaseViewHolder helper, SongInfo item) {
        int rbtnSongSelect = R.id.rbtn_song_select;
        String name = item.getName();
        if (nameAndQuerySpans.containsKey(name)) {
            helper.setText(R.id.tv_song_name, nameAndQuerySpans.get(name));
        } else {
            helper.setText(R.id.tv_song_name, name);
        }
        helper.setChecked(rbtnSongSelect, item.isSelect());
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
}
