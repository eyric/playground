package org.example;

import org.example.common.util.JSON;
import org.example.common.util.StrUtil;
import org.example.dal.dao.intf.DemoDao;
import org.example.dal.entity.DemoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaoTest {

    @Autowired
    private DemoDao demoDao;

    @Test
    public void test(){
        DemoEntity demo = new DemoEntity().setName(StrUtil.uuid());
        demoDao.save(demo);
        System.out.println(JSON.stringify(demo));
        DemoEntity demo1 = demoDao.selectById(demo.getId());
        System.out.println(JSON.stringify(demo1));
        demo1.setName(StrUtil.uuid());
        demoDao.updateById(demo1);
        System.out.println(JSON.stringify(demo1));
        demoDao.deleteById(demo1.getId());

    }
}
