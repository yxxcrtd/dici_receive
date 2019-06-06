package com.igoosd.util;

import com.google.gson.Gson;

/**
 * 2017/8/29.
 */
public class GsonUtil {

    public static <T> String toJson(T t){
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    public static <T> T parseJsonToObject(String json,Class<T> clazz){
        Gson gson = new Gson();
        return gson.fromJson(json,clazz);
    }
}
