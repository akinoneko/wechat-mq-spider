package com.zyy.wechat.mq.spider.entity;

import javax.persistence.*;

/**
 * Created by akinoneko on 2017/3/25.
 * 采集队列
 */
@Entity
@Table(name = "spider_queue")
public class SpiderQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //文章地址
    private String contentUrl;

    //读取中标志
    private Integer loading = 0;

    private Long datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public Integer getLoading() {
        return loading;
    }

    public void setLoading(Integer loading) {
        this.loading = loading;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }
}
