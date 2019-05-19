package com.example.randomalarm.common;

import com.example.randomalarm.setting.AlarmSettingInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.annotations.Nullable;

/**
 * author : Mzz
 * date : 2019 2019/5/6 10:42
 * description :
 */
public class JsonConverter<T> {

    public static <T> ArrayList <T> jsonToList(String json, Class <T> classOfT) {
        Type type = new TypeToken <ArrayList <JsonObject>>() {
        }.getType();
        ArrayList <JsonObject> jsonObjs = new Gson().fromJson(json, type);

        ArrayList <T> listOfT = new ArrayList <>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(new Gson().fromJson(jsonObj, classOfT));
        }
        return listOfT;
    }

    public static <T> T jsonToClass(String databaseValue, Class <T> classOfT) {
        return new Gson().fromJson(databaseValue, (Type) classOfT);
    }

    public static <T> String convertToStringValue(@Nullable Object entityProperty) {
        if (entityProperty == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
