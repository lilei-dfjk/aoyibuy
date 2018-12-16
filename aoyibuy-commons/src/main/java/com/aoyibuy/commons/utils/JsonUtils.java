package com.aoyibuy.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 处理json序列化和反序列化的工具类
 * 
 * @author wh
 * @since 0.0.1
 */
public abstract class JsonUtils {

    /**
     * 序列化Java对象为json串
     * @param o
     * @return
     */
    public static String toJson(Object o) {
        return JSON.toJSONString(o);
    }
    
    /**
     * 反序列化json到简单类型
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }
    
    /**
     * 反序列化json到复杂类型
     * @param text
     * @param type
     * @return
     */
    public static <T> T fromJson(String text, TypeReference<T> type) {
        return JSON.parseObject(text, type);
    }

}
