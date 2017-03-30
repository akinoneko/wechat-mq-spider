package com.zyy;

import com.zyy.wechat.mq.spider.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URLDecoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMqSpiderApplicationTests {

    @Autowired
    private ArticleService articleService;

    @Test
    public void contextLoads() throws IOException {
    }

}
