package com.example.randomalarm.setting;

import com.example.randomalarm.common.JsonConverter;
import com.example.randomalarm.song.SongInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmRepeatConverter implements PropertyConverter <ArrayList <AlarmRepeatMode>, String> {

    @Override
    public ArrayList <AlarmRepeatMode> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return new ArrayList <>();
        }
        Gson gson = new Gson();
        Type type = new TypeToken <ArrayList <AlarmRepeatMode>>() {
        }.getType();
        return gson.fromJson(databaseValue, type);
    }

    @Override
    public String convertToDatabaseValue(ArrayList <AlarmRepeatMode> entityProperty) {
        return JsonConverter.convertToDatabaseValue(entityProperty);
    }
}
