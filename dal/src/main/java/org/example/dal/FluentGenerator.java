package org.example.dal;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;

public class FluentGenerator {
    public static final String url = "jdbc:mysql://localhost:3306/playground?useUnicode=true&characterEncoding=utf8";

    public static void main(String[] args) {
        FileGenerator.build(Empty.class);
    }

    @Tables(
            // 设置数据库连接信息
            url = url, username = "root", password = "12345678",
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "dal/src/main/java",
            // 设置entity类的package值
            basePack = "org.example.dal",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "dal/src/main/java",
            // 设置哪些表要生成Entity文件
            tables = {@Table(value = {"demo"})}
    )
    static class Empty {

    }
}