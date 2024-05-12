package org.example.common.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/***
 * JSON操作辅助类
 */
@SuppressWarnings({"unchecked", "JavaDoc", "UnnecessaryLocalVariable"})
public class JSON {

    private static volatile ObjectMapper objectMapper;

    /**
     * 初始化ObjectMapper
     */
    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            synchronized (ObjectMapper.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
            }
        }

        return objectMapper;
    }

    /**
     * 将Java对象转换为Json String
     */
    public static String stringify(Object object) {
        return toJSONString(object);
    }

    /**
     * 转换对象为JSON字符串
     */
    public static String toJSONString(Object model) {
        if (ObjectUtil.isEmpty(model)) {
            return "";
        }

        try {
            String json = getObjectMapper().writeValueAsString(model);
            return json;
        } catch (Exception e) {
            LogUtil.error("Java转Json异常", e);
            return null;
        }
    }

    /***
     * 将JSON字符串转换为java对象
     */
    public static <T> T toJavaObject(String jsonStr, Class<T> clazz) {
        if (StrUtil.isBlank(jsonStr)) {
            return null;
        }

        try {
            T model = getObjectMapper().readValue(jsonStr, clazz);
            return model;
        } catch (Exception e) {
            LogUtil.error("Json转Java异常,str:" + jsonStr, e);
            return null;
        }
    }

    /***
     * 将JSON字符串转换为Map<String, Object></>对象
     */
    public static Map<String, Object> parseObject(String jsonStr) {
        try {
            JavaType javaType = getObjectMapper().getTypeFactory()
                    .constructParametricType(Map.class, String.class, Object.class);
            return getObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception e) {
            LogUtil.error("Json转Java异常,str:" + jsonStr, e);
            return null;
        }
    }

    /***
     * 将JSON字符串转换为java对象
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (StrUtil.isBlank(jsonStr)) {
            return null;
        }
        return toJavaObject(jsonStr, clazz);
    }

    /***
     * 将JSON字符串转换为复杂类型的Java对象
     */
    public static <T> T parseObject(String jsonStr, TypeReference<T> typeReference) {
        try {
            T model = getObjectMapper().readValue(jsonStr, typeReference);
            return model;
        } catch (Exception e) {
            LogUtil.error("Json转Java异常,str:" + jsonStr, e);
            return null;
        }
    }

    /***
     * 将JSON字符串转换为list对象
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        if (StrUtil.isBlank(jsonStr)) {
            return null;
        }
        try {
            JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(List.class, clazz);
            return getObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception e) {
            LogUtil.error("Json转Java异常", e);
            return null;
        }
    }

    /***
     * 将JSON字符串转换为java对象
     */
    public static <K, T> Map<K, T> toMap(String jsonStr) {
        return (Map<K, T>) toJavaObject(jsonStr, Map.class);
    }


    public static Map<String, String> toMapIgnoreNull(String jsonStr) {
        Map<String, Object> map = toMap(jsonStr);
        if (CollUtil.isEmpty(map)) {
            return Map.of();
        }

        return map.entrySet().stream().filter(item -> ObjectUtil.isNotEmpty(item.getKey())
                        && ObjectUtil.isNotEmpty(item.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, item -> JSON.stringify(item.getValue())));
    }

    /***
     * 将JSON字符串转换为Map对象
     */
    public static <K, T> LinkedHashMap<K, T> toLinkedHashMap(String jsonStr) {
        if (StrUtil.isEmpty(jsonStr)) {
            return null;
        }
        return (LinkedHashMap<K, T>) toJavaObject(jsonStr, LinkedHashMap.class);
    }
}