package com.cs.common.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
    public static String toJsonString(Object o){
        return JSON.toJSONString(o);
    }
    public static <T> T parseObject(String jsonString,Class<T> aClass){
        return JSON.parseObject(jsonString,aClass);
    }

    public static JSONObject parseObject(String jsonString){
        return JSON.parseObject(jsonString);
    }
}
