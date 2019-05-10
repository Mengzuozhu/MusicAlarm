package com.example.randomalarm.edit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 编辑信息
 * author : Mzz
 * date : 2019 2019/5/7 18:31
 * description :
 */
public class EditItemInfo implements Parcelable {
    private int id;
    private String info;

    public EditItemInfo(int id, String info) {
        this.id = id;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.info);
    }

    protected EditItemInfo(Parcel in) {
        this.id = in.readInt();
        this.info = in.readString();
    }

    public static final Parcelable.Creator <EditItemInfo> CREATOR = new Parcelable.Creator <EditItemInfo>() {
        @Override
        public EditItemInfo createFromParcel(Parcel source) {
            return new EditItemInfo(source);
        }

        @Override
        public EditItemInfo[] newArray(int size) {
            return new EditItemInfo[size];
        }
    };
}
