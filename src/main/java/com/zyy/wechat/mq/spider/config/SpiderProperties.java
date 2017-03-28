package com.zyy.wechat.mq.spider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by akinoneko on 2017/3/27.
 */
@ConfigurationProperties(prefix = "wechat.mq.spider")
public class SpiderProperties {

    /**
     * 图片存储域名
     */
    private String imgUrlDomain;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    public String getImgUrlDomain() {
        return imgUrlDomain;
    }

    public void setImgUrlDomain(String imgUrlDomain) {
        this.imgUrlDomain = imgUrlDomain;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
