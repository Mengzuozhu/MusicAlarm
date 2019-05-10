package com.example.randomalarm.common;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.SearchView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.randomalarm.song.SongInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/8 9:15
 * description :
 */
public class TextQueryHandler {

    private SearchView svAlarmSong;
    private BaseQuickAdapter adapter;
    private ArrayList <SongInfo> songInfos;
    private HashMap <String, Spannable> nameAndQuerySpans;

    public TextQueryHandler(SearchView svAlarmSong, BaseQuickAdapter adapter,
                            ArrayList <SongInfo> songInfos, HashMap <String, Spannable> nameAndQuerySpans) {
        this.svAlarmSong = svAlarmSong;
        this.adapter = adapter;
        this.songInfos = songInfos;
        this.nameAndQuerySpans = nameAndQuerySpans;
    }

    public void setQueryTextListener() {
        svAlarmSong.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    nameAndQuerySpans.clear();
                    adapter.setNewData(songInfos);
                }
                return false;
            }
        });
    }

    private void queryText(String query) {
        nameAndQuerySpans.clear();
        if (TextUtils.isEmpty(query)) {
            adapter.setNewData(songInfos);
        } else {
            List <SongInfo> querySongs = new ArrayList <>();
            for (SongInfo songInfo : songInfos) {
                String name = songInfo.getName();
                int index = name.indexOf(query);
                if (index >= 0) {
                    Spannable span = new SpannableString(name);
                    span.setSpan(new ForegroundColorSpan(Color.BLUE), index, index + query.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    nameAndQuerySpans.put(name, span);
                    querySongs.add(songInfo);
                }
            }
            adapter.setNewData(querySongs);
        }
    }

}
