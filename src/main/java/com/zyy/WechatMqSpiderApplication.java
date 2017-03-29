package com.zyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@EnableScheduling   //开启定时任务
public class WechatMqSpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMqSpiderApplication.class, args);
    }
}
