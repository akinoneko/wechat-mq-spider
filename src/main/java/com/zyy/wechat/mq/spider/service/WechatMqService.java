package com.zyy.wechat.mq.spider.service;

import com.zyy.wechat.mq.spider.dao.WechatMqRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by akinoneko on 2017/3/25.
 */
public class WechatMqService {

    private final static Logger LOGGER = LoggerFactory.getLogger(WechatMqService.class);
    @Autowired
    private WechatMqRepository wechatMqRepository;
}
