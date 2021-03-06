package com.mengzz.musicalarm.song;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/25 18:57
 * description :
 */
public class FileManager {
    private static FileManager mInstance = new FileManager();
    private static ContentResolver mContentResolver;

    public static FileManager getInstance(Context context) {
        mContentResolver = context.getContentResolver();
        return mInstance;
    }

    /**
     * 获取本机音乐列表
     *
     * @return
     */
    public List <SongInfo> getSongInfos() {
        ArrayList <SongInfo> songs = new ArrayList <>();
        try (Cursor c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER)) {

            if (c == null) {
                return songs;
            }
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                // 路径

                if (!new File(path).exists()) {
                    continue;
                }

                String name =
                        c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名

                int indexOf = name.indexOf('.');
                if (indexOf > 0) {
                    name = name.substring(0, indexOf);
                }
                SongInfo song = new SongInfo(name, path, false);
                songs.add(song);
            }

        } catch (Exception e) {
            Log.d("FileManager", "e:" + e.getMessage());
        }
        return songs;
    }
}
