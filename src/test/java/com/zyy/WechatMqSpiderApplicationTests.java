package com.zyy;

import com.zyy.wechat.mq.spider.dao.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMqSpiderApplicationTests {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void contextLoads() throws IOException {
    }

}
