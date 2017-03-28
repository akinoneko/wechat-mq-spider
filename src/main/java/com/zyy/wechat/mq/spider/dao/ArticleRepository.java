package com.zyy.wechat.mq.spider.dao;

import com.zyy.wechat.mq.spider.entity.Article;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by akinoneko on 2017/3/25.
 */
public interface ArticleRepository extends CrudRepository<Article, Integer> {

    @Query("select a from Article a where contentUrl = :contentUrl")
    public Article findOneByContentUrl(@Param("contentUrl") String contentUrl);

    @Query("select a from Article a where biz=:biz and contentUrl like CONCAT('%',:sn,'%')")
    public Article findBySnAndBiz(@Param("sn")String sn,@Param("biz")String biz);
}
