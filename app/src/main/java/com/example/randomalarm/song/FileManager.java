package com.example.randomalarm.song;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

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
    public ArrayList <Song> getMusics() {
        ArrayList <Song> songs = new ArrayList <>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (c == null) {
                return songs;
            }
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径

                if (!new File(path).exists()) {
                    continue;
                }

                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
//                int time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id

                int indexOf = name.indexOf(".");
                if (indexOf > 0) {
                    name = name.substring(0, indexOf);
                }
                Song song = new Song(name, path, album, artist, size, duration);
                songs.add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return songs;
    }

    /**
     * 获取本机音乐列表
     *
     * @return
     */
    public ArrayList <SongInfo> getSongInfos() {
        ArrayList <SongInfo> songs = new ArrayList <>();
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            if (c == null) {
                return songs;
            }
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径

                if (!new File(path).exists()) {
                    continue;
                }

                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名

                int indexOf = name.indexOf(".");
                if (indexOf > 0) {
                    name = name.substring(0, indexOf);
                }
                SongInfo song = new SongInfo(name, path, true);
                songs.add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return songs;
    }
}
