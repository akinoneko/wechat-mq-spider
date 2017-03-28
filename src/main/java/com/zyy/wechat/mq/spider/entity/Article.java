package com.zyy.wechat.mq.spider.entity;

import javax.persistence.*;

/**
 * Created by akinoneko on 2017/3/25.
 * 公众号文章
 */
@Entity
@Table(name = "wechat_article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //文章对应的公众号biz
    @Column(nullable = false)
    private String biz;

    //微信定义的一个id,每条文章唯一
    @Column(nullable = false)
    private Long fieldId;

    //文章标题
    private String title = "";

    //文章编码,防止文章出现emoji
    private String title_encode;

    //文章摘要
    private String digest = "";

    //文章地址
    private String contentUrl;

    //阅读原文地址
    private String sourceUrl;

    //封面图片
    private String cover;

    //是否多图文
    private Integer isMulti;

    //是否头条
    private Integer isTop;

    //文章时间戳
    private Long datetime;

    //文章阅读量
    private Integer readNum = 1;

    //文章点赞量
    private Integer likeNum = 0;

    private String content;

    private String author;

    private Long createTime;

    private Long updateTime;

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

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_encode() {
        return title_encode;
    }

    public void setTitle_encode(String title_encode) {
        this.title_encode = title_encode;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getIsMulti() {
        return isMulti;
    }

    public void setIsMulti(Integer isMulti) {
        this.isMulti = isMulti;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public Integer getReadNum() {
        return readNum;
    }

    public void setReadNum(Integer readNum) {
        this.readNum = readNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
