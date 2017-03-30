package com.zyy.wechat.mq.spider.service;

import com.zyy.wechat.mq.spider.dao.SpiderQueueRepository;
import com.zyy.wechat.mq.spider.dao.WechatMqRepository;
import com.zyy.wechat.mq.spider.entity.SpiderQueue;
import com.zyy.wechat.mq.spider.entity.WechatMq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by akinoneko on 2017/3/25.
 */
@Service
public class SpiderQueueService {

    @Autowired
    private SpiderQueueRepository spiderQueueRepository;
    @Autowired
    private WechatMqRepository wechatMqRepository;

    @Transactional
    public String getHistoryPageNextUrl() {
        String url = null;
        //删除loading为1的记录
        spiderQueueRepository.deleteLoading();
        //取出队列中最早的一条记录
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.ASC, "datetime");
        Page<SpiderQueue> spiderQueuePage = spiderQueueRepository.findByPage(pageable);
        List<SpiderQueue> spiderQueueList = spiderQueuePage.getContent();
        if (spiderQueueList.size() > 0) {
            SpiderQueue spiderQueue = spiderQueueList.get(0);
            spiderQueue.setLoading(1);
            spiderQueueRepository.save(spiderQueue);    //更新loading为1
            url = spiderQueue.getContentUrl();
        } else {
            //队列为空
            pageable = new PageRequest(0, 1, Sort.Direction.ASC, "collect");
            Page<WechatMq> wechatMqPage = wechatMqRepository.findByPage(pageable);
            List<WechatMq> wechatMqList = wechatMqPage.getContent();
            if (wechatMqList.size() > 0) {
                WechatMq wechatMq = wechatMqList.get(0);
                url = "http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=" + wechatMq.getBiz()
                        + "#wechat_webview_type=1&wechat_redirect";
                wechatMq.setCollect(System.currentTimeMillis());
                wechatMqRepository.save(wechatMq);
            }
        }
        return url;
    }

    @Transactional
    public String getArticlePageNextUrl() {
        String url = null;
        spiderQueueRepository.deleteLoading();
        Pageable pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        Page<SpiderQueue> spiderQueuePage = spiderQueueRepository.findByPage(pageable);
        List<SpiderQueue> spiderQueueList = spiderQueuePage.getContent();
        if (spiderQueueList.size() > 1) {   //当队列还剩下一条的时候,从存储的公众号biz表中取出一个biz
            SpiderQueue spiderQueue = spiderQueueList.get(0);
            spiderQueue.setLoading(1);
            spiderQueueRepository.save(spiderQueue);    //更新loading为1
            url = spiderQueue.getContentUrl();
        } else {
            pageable = new PageRequest(0, 1, Sort.Direction.ASC, "collect");
            Page<WechatMq> wechatMqPage = wechatMqRepository.findByPage(pageable);
            List<WechatMq> wechatMqList = wechatMqPage.getContent();
            WechatMq wechatMq = wechatMqList.get(0);
            if (System.currentTimeMillis()-wechatMq.getCollect()<86400000L){
                return null;
            }
//            url = "http://mp.weixin.qq.com/mp/getmasssendmsg?__biz=" + wechatMq.getBiz()
//                    + "#wechat_webview_type=1&wechat_redirect";
            url = "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=" + wechatMq.getBiz()
                    + "&scene=124#wechat_redirect";//拼接公众号历史消息url地址（第二种页面形式）
            wechatMq.setCollect(System.currentTimeMillis());
            wechatMqRepository.save(wechatMq);
        }
        return url;
    }

}
