package org.example.common.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreadContextHolder {

    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> REQUEST_IP = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, Object>> CONTEXT_PARAM = new ThreadLocal<>();

    private static final ThreadLocal<LinkedHashMap<String, Long>> COST_TIME_PARAM = new ThreadLocal<>();

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static String getRequestIp() {
        return REQUEST_IP.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static void setRequestIp(String requestIp) {
        REQUEST_IP.set(requestIp);
    }


    public static void put(String key, Object value) {
        if (CONTEXT_PARAM.get() == null) {
            CONTEXT_PARAM.set(new HashMap<>());
        }
        CONTEXT_PARAM.get().put(key, value);
    }

    public static Object get(String key) {
        if (CONTEXT_PARAM.get() == null) {
            return null;
        } else {
            return CONTEXT_PARAM.get().get(key);
        }
    }

    public static void clean() {
        CONTEXT_PARAM.remove();
        REQUEST_ID.remove();
        COST_TIME_PARAM.remove();
        REQUEST_IP.remove();
    }

}
