package org.example.dal.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FluentMybatisConfig {

    @Bean
    public String mybatisInterceptor(SqlSessionFactory sqlSessionFactory) {
        PerformanceInterceptor sqlInterceptor = new PerformanceInterceptor();
        sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        return "interceptor";
    }

    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}
