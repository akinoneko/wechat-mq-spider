package com.zyy.wechat.mq.spider.task;

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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by akinoneko on 2017/3/29.
 * 定时检查队列,下载图片
 */
@Component
public class ImageDownloadTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageDownloadTask.class);

    private static Queue<String[]> imageDownloadQueue = new LinkedList<>();

    @Autowired
    private SpiderConfig spiderConfig;

    //五秒钟下载一次队列中的图片
    @Scheduled(fixedDelay = 5000L)
    public void downloadImage() {
        //图片抓取
        LOGGER.info("抓取图片到云存储服务器,队列长度" + imageDownloadQueue.size());
        Zone zone = Zone.autoZone();
        Configuration configuration = new Configuration(zone);
        Auth auth = Auth.create(spiderConfig.getAccessKey(), spiderConfig.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, configuration);
        String[] urlKV = null;
        while ((urlKV = imageDownloadQueue.poll()) != null && urlKV.length == 2) {
            try {
                bucketManager.fetch(urlKV[0], spiderConfig.getBucketName(), urlKV[1]);
            } catch (QiniuException e) {
                Response response = e.response;
                LOGGER.error(response.toString());
            }
        }
    }

    public static void addImageUrlToQueue(String[] url) {
        imageDownloadQueue.add(url);
    }


    public static void addImageUrlToQueue(List<String[]> urls) {
        imageDownloadQueue.addAll(urls);
    }
}
