package com.mengzz.musicalarm.song;

import android.os.Parcel;
import android.os.Parcelable;

import com.mzz.zandroidcommon.adapter.ICheckable;
import com.mzz.zandroidcommon.common.JsonConverter;
import com.mzz.zandroidcommon.view.QueryInfo;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * 歌曲信息
 * author : Mzz
 * date : 2019 2019/5/7 19:22
 * description :
 */
@Data
@AllArgsConstructor
public class SongInfo implements Parcelable, ICheckable, QueryInfo {
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

    private String name;
    private String path;
    private boolean isChecked;

    private SongInfo(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    @Override
    public String getInfo() {
        return name;
    }

    @Override
    public void setIsChecked(boolean b) {
        isChecked = b;
    }

    public static class SongInfoListConverter implements PropertyConverter <ArrayList <SongInfo>,
            String> {

        @Override
        public ArrayList <SongInfo> convertToEntityProperty(String databaseValue) {
            return JsonConverter.jsonToList(databaseValue, SongInfo.class);
        }

        @Override
        public String convertToDatabaseValue(ArrayList <SongInfo> entityProperty) {
            return JsonConverter.convertToStringValue(entityProperty);
        }
    }
}
