package com.zyy.wechat.mq.spider.config;

import com.zyy.wechat.mq.spider.entity.SpiderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by akinoneko on 2017/3/28.
 */
@Configuration
@EnableConfigurationProperties(SpiderProperties.class)
public class SpiderConfiguration {

    @Autowired
    private SpiderProperties spiderProperties;

    @Bean
    public SpiderConfig spiderConfig() {
        SpiderConfig spiderConfig = new SpiderConfig();
        spiderConfig.setImgUrlDomain(spiderProperties.getImgUrlDomain());
        spiderConfig.setAccessKey(spiderProperties.getAccessKey());
        spiderConfig.setSecretKey(spiderProperties.getSecretKey());
        spiderConfig.setBucketName(spiderProperties.getBucketName());
        return spiderConfig;
    }
}
