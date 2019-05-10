package com.example.randomalarm.song;

import com.example.randomalarm.common.JsonConverter;
import com.example.randomalarm.song.SongInfo;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/4/26 20:54
 * description :
 */
public class SongInfoListConverter implements PropertyConverter <ArrayList <SongInfo>, String> {

    @Override
    public ArrayList <SongInfo> convertToEntityProperty(String databaseValue) {
        return JsonConverter.jsonToList(databaseValue, SongInfo.class);
    }

    @Override
    public String convertToDatabaseValue(ArrayList <SongInfo> entityProperty) {
        return JsonConverter.convertToDatabaseValue(entityProperty);
    }
}
