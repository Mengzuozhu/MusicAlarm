package com.example.randomalarm.song;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 歌曲信息
 * author : Mzz
 * date : 2019 2019/5/7 19:22
 * description :
 */
public class SongInfo implements Parcelable {
    private String name;
    private String path;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public SongInfo(String name, String path, boolean isSelect) {
        this.name = name;
        this.path = path;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected SongInfo(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator <SongInfo> CREATOR = new Creator <SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel source) {
            return new SongInfo(source);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };
}
