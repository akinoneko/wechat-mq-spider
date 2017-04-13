package com.zyy;

import com.alibaba.fastjson.JSONArray;
import com.zyy.wechat.mq.spider.dao.ArticleRepository;
import com.zyy.wechat.mq.spider.entity.Article;
import com.zyy.wechat.mq.spider.service.ArticleService;
import com.zyy.wechat.mq.spider.utils.QiniuUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatMqSpiderApplicationTests {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void contextLoads() {
        Article article = new Article();
        article.setContent("123123");
        article.setBiz("4321");
        article.setFieldId(12321412L);
    }

}
