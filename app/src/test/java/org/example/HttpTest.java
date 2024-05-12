package org.example;

import org.example.common.util.JSON;
import org.example.sal.sug.SugRecClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpTest {

    @Autowired
    private SugRecClient sugRecClient;

    @Test
    public void test() {
        System.out.println(JSON.stringify(sugRecClient.sugRec("教师资格证")));
    }
}
