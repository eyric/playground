package org.example.dal.dao.impl;

import org.example.common.util.JSON;
import org.example.common.util.StrUtil;
import org.example.dal.dao.base.DemoBaseDao;
import org.example.dal.dao.intf.DemoDao;
import org.example.dal.entity.DemoEntity;
import org.springframework.stereotype.Repository;

/**
 * DemoDaoImpl: 数据操作接口实现
 *
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
@Repository
public class DemoDaoImpl extends DemoBaseDao implements DemoDao {
    public void test(){
        DemoEntity demo = new DemoEntity().setName(StrUtil.uuid());
        save(demo);
        System.out.println(JSON.stringify(demo));
        DemoEntity demo1 = selectById(demo.getId());
        System.out.println(JSON.stringify(demo1));
        demo1.setName(StrUtil.uuid());
        updateById(demo1);
        System.out.println(JSON.stringify(demo1));
        deleteById(demo1.getId());

    }
}
