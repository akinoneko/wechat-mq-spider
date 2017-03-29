package com.zyy;

import com.zyy.wechat.mq.spider.dao.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMqSpiderApplicationTests {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void contextLoads() throws IOException {
//        articleRepository.findBySnAndBiz("11973d5edd9e4637551b11c43cb9e917","MjM5NTAyODc2MA==");
    }

}
