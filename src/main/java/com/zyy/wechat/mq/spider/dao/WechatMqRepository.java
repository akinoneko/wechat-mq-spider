package com.zyy.wechat.mq.spider.dao;

import com.zyy.wechat.mq.spider.entity.WechatMq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by akinoneko on 2017/3/25.
 */
public interface WechatMqRepository extends CrudRepository<WechatMq, Integer> {

    @Query("select w from WechatMq w where biz=:biz")
    public WechatMq findByBiz(@Param("biz") String biz);

    @Query("select w from WechatMq w")
    public Page<WechatMq> findByPage(Pageable pageable);
}
