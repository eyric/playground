package org.example.dal.config;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.example.common.util.DateUtil;
import org.example.common.util.StrUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

/***
 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间*
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class}
        ),
        @Signature(
                type = StatementHandler.class,
                method = "update",
                args = {Statement.class}
        ),
        @Signature(
                type = StatementHandler.class,
                method = "batch",
                args = {Statement.class}
        )
})
@Component
@Slf4j
public class PerformanceInterceptor implements Interceptor {

    /**
     * 截取代理对象的属性值并返回
     *
     * @param invocation 当前方法的调用
     * @return 方法的返回值
     * @throws Throwable 抛出任何异常
     */
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        try {
            statement.getClass().getDeclaredField("stmt");
            statement = (Statement) SystemMetaObject.forObject(statement).getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }
        String originalSql = statement.toString();
        originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);

        int index = originalSql.indexOf(':');
        String sql = originalSql;
        if (index > 0) {
            sql = originalSql.substring(index + 1);
        }
        Object result;
        String url = statement.getConnection().getMetaData().getURL();
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        long start = System.currentTimeMillis();
        long timing = 0;
        String resultCnt = StrUtil.EMPTY;
        try {

            result = invocation.proceed();

            long end = System.currentTimeMillis();
            timing = end - start;

            if (result instanceof Collection<?> resultList) {
                resultCnt = String.valueOf(resultList.size());
            } else if (result instanceof Boolean) {
                resultCnt = result.toString();
            } else if (result instanceof Number) {
                resultCnt = result.toString();
            } else {
                resultCnt = "1";
            }

        } catch (Exception e) {
            log.warn(
                    "sqlExecutingError [startTime:{}] [url:{}] [cost:{}] - ID:{} Execute SQL:{} [errorMsg:{}]",
                    DateUtil.format(start),
                    url,
                    System.currentTimeMillis() - start,
                    ms.getId(),
                    sql,
                    e.getMessage());
            throw e;
        } finally {
            log.info(
                    "[startTime:{}] [url:{}] [cost:{}] - ID:{} Execute SQL:{} [resultCnt:{}]",
                    DateUtil.format(start),
                    url,
                    timing,
                    ms.getId(),
                    sql,
                    resultCnt);
        }
        return result;
    }

    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setProperties(Properties prop) {

    }
}
