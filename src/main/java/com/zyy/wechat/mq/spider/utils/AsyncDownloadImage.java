package com.zyy.wechat.mq.spider.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import com.zyy.wechat.mq.spider.entity.SpiderConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by akinoneko on 2017/3/28.
 */
@Component
public class AsyncDownloadImage {

    @Autowired
    private SpiderConfig spiderConfigAutoWired;

    private static SpiderConfig spiderConfig;

    private final static Logger LOGGER = LoggerFactory.getLogger(AsyncDownloadImage.class);

    @PostConstruct
    private void init() {
        spiderConfig = spiderConfigAutoWired;
    }

    public static void asyncDownloadImage(Map<String, String> urls) {
        //异步下载图片
        new Thread(new DownloadImage(urls)).start();


    }

    private static class DownloadImage implements Runnable {
        private Map<String, String> urls;

        private DownloadImage(Map<String, String> urls) {
            this.urls = urls;
        }

        @Override
        public void run() {
            //图片抓取
            LOGGER.debug("抓取图片到云存储服务器");
            Zone zone = Zone.autoZone();
            Configuration configuration = new Configuration(zone);
            Auth auth = Auth.create(spiderConfig.getAccessKey(), spiderConfig.getSecretKey());
            BucketManager bucketManager = new BucketManager(auth, configuration);
            for (Map.Entry<String, String> entry : urls.entrySet()) {
                try {
                    bucketManager.fetch(entry.getKey(), spiderConfig.getBucketName(), entry.getValue());
                } catch (QiniuException e) {
                    Response response = e.response;
                    LOGGER.error(response.toString());
                }
            }
        }
    }
}
