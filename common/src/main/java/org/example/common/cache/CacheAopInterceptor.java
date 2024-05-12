package org.example.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.common.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class CacheAopInterceptor {

    private static final String CACHE_KEY_PRE = "playground:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${playground.cacheable.switch:true}")
    Boolean cacheableSwitch;

    @Around("@annotation(org.example.common.annotation.Cacheable)")
    public Object handle(ProceedingJoinPoint jp) {
        Object result = null;
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Cacheable annotation = method.getAnnotation(Cacheable.class);
        if (cacheableSwitch && annotation != null) {
            String cacheKey = getCacheKey(jp, method);
            log.info("get from redis cache key is {}", cacheKey);
            long beginTime = System.currentTimeMillis();
            try {
                result = redisTemplate.opsForValue().get(cacheKey);
            } catch (Throwable throwable) {
                log.error("get_value_fail , cache key is {}", cacheKey, throwable);
            }
            long endTime = System.currentTimeMillis();
            if (result != null) {
                log.info("hit cache , cache key is {}, total: {}", cacheKey, (endTime - beginTime));
            } else {
                log.info("miss cache ,cache key is {}, total: {}", cacheKey, (endTime - beginTime));
                try {
                    result = jp.proceed();
                } catch (Throwable throwable) {
                    log.warn("{} invoke 1st fail,arguments is {}",
                            jp.getTarget().getClass().getSimpleName() + "." +
                                    method.getName(), Arrays.toString(jp.getArgs()), throwable);
                }
                if (result != null) {
                    int expire = annotation.expire();
                    beginTime = System.currentTimeMillis();
                    try {
                        redisTemplate.opsForValue().set(cacheKey, result, expire, TimeUnit.SECONDS);
                    } catch (Throwable throwable) {
                        log.error("set_value_fail , cache key is {}", cacheKey, throwable);
                    }
                    endTime = System.currentTimeMillis();
                    log.info("set data to redis {}, total: {}", result, (endTime - beginTime));
                }
            }
        } else {
            try {
                result = jp.proceed();
            } catch (Throwable throwable) {
                log.error("cache_invoke_2nd_fail, key {}arguments is {}",
                        jp.getTarget().getClass().getSimpleName() + "." +
                                method.getName(), Arrays.toString(jp.getArgs()));
            }
        }
        return result;
    }


    String getCacheKey(ProceedingJoinPoint jp, Method method) {
        return CACHE_KEY_PRE + jp.getTarget().getClass().getSimpleName() + ":"
                + method.getName() + ":" + StringUtils.arrayToDelimitedString(jp.getArgs(), ":");
    }

}