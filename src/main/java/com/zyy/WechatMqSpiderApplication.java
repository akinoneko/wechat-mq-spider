package com.zyy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class WechatMqSpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatMqSpiderApplication.class, args);
    }
}
