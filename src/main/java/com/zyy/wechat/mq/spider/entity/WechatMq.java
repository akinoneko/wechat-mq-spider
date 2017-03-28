package com.zyy.wechat.mq.spider.entity;

import javax.persistence.*;

/**
 * Created by akinoneko on 2017/3/25.
 * 微信公众号列表
 */
@Entity
@Table(name = "wechat_mq")
public class WechatMq {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //公众号唯一标识biz
    @Column
    private String biz = "";

    //记录采集时间的时间戳
    @Column(length = 11)

    //公众号昵称
    private String name;

    //公众号头像
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    private Long collect = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public Long getCollect() {
        return collect;
    }

    public void setCollect(Long collect) {
        this.collect = collect;
    }
}
