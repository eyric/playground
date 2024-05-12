package org.example.common.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LogUtil {
    public static void error(Logger logger, String msg, Throwable t) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String className = stackTrace.getClassName();
        String methodName = stackTrace.getMethodName();
        logger.error(String.join(StrUtil.UNDERLINE, getClassName(className), methodName, msg), t);
    }

    public static void error(String msg, Throwable t) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String className = stackTrace.getClassName();
        String methodName = stackTrace.getMethodName();
        try {
            Logger logger = LoggerFactory.getLogger(Class.forName(className));
            logger.error(String.join(StrUtil.UNDERLINE, getClassName(className), methodName, msg), t);
        } catch (ClassNotFoundException e) {
            log.error("get logger class not found ", e);
        }
    }

    private static String getClassName(String completeClassName) {
        return StrUtil.splitAndGetLast(completeClassName, "\\.");
    }
}
