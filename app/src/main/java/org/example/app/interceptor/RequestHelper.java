package org.example.app.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.common.util.CollUtil;
import org.example.common.util.JSON;
import org.example.common.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class RequestHelper {

    public ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public String getIpAddress() {
        HttpServletRequest request = getServletRequestAttributes().getRequest();

        String ip = request.getHeader("x-forwarded-for");
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("CLIENTIP6");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (StrUtil.isEmpty(ip)) {
            ip = StrUtil.split(ip, ',').get(0).trim();
        }

        return ip;
    }

    public String getCookieValue(String name) {
        HttpServletRequest request = getServletRequestAttributes().getRequest();
        if (ObjectUtil.isEmpty(request) || StrUtil.isEmpty(name)) {
            return StrUtil.EMPTY;
        }

        Cookie[] cookies = request.getCookies();
        if (ArrayUtil.isEmpty(cookies)) {
            return StrUtil.EMPTY;
        }

        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(item -> name.equals(item.getName())).findFirst();
        if (cookie.isPresent()) {
            return cookie.get().getValue();
        }

        return StrUtil.EMPTY;
    }

    /**
     * 获取请求中的所有参数，包括URL、表单
     */
    public String[] getParamValue(String name) {
        HttpServletRequest request = getServletRequestAttributes().getRequest();
        if (ObjectUtil.isEmpty(request) || StrUtil.isEmpty(name)) {
            return new String[]{""};
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollUtil.isEmpty(parameterMap)) {
            return new String[]{""};
        }

        if (parameterMap.containsKey(name)) {
            return parameterMap.get(name);
        }

        return new String[]{""};
    }

    public String getBodyString() throws IOException {
        HttpServletRequest request = getServletRequestAttributes().getRequest();
        StringBuilder sb = new StringBuilder();
        try (ServletInputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    public Map<String, Object> getBodyMap() {
        Map<String, Object> params = new HashMap<>();
        String bodyString;
        try {
            bodyString = getBodyString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!StrUtil.isEmpty(bodyString)) {
            params = JSON.parseObject(bodyString);
        }
        return params;
    }

    public String getBodyParamValue(String key) {
        return String.valueOf(getBodyMap().getOrDefault(key, ""));
    }

    public String getHeader(String key) {
        return getServletRequestAttributes().getRequest().getHeader(key);
    }
}
