package com.example.randomalarm.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public static <T> String convertToDatabaseValue(ArrayList <T> entityProperty) {
        if (entityProperty == null) {
            return "";
        } else {
            Gson gson = new Gson();
            return gson.toJson(entityProperty);
        }
    }
}
