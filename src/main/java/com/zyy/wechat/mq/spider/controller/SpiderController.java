package com.zyy.wechat.mq.spider.controller;

import com.zyy.wechat.mq.spider.service.ArticleService;
import com.zyy.wechat.mq.spider.service.SpiderQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by akinoneko on 2017/3/24.
 */
@RestController
@RequestMapping("spider")
public class SpiderController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpiderController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private SpiderQueueService spiderQueueService;

    @RequestMapping(value = "getWechatHistory")
    public Object getWechatHistory() throws IOException {
        LOGGER.debug("获取微信公众号历史消息");
        LOGGER.debug("输出代理的内容");
        String url = spiderQueueService.getHistoryPageNextUrl();
        //注入js脚本到微信的网页中
        return "<script>var lastHeight = document.querySelector(\"body\").scrollTop;var clientHeight = document.documentElement.clientHeight;var c = 0;setInterval(function() {if ((lastHeight+clientHeight) == document.documentElement.scrollHeight) {c++;if (c == 3) {window.location.href = '" + url + "';}}else{document.getElementsByTagName('BODY')[0].scrollTop = document.getElementsByTagName('BODY')[0].scrollHeight;c = 0;lastHeight = document.querySelector('body').scrollTop;}},2000);</script>";
//        return "<script>setTimeout(function(){window.location.href='" + url + "';},2000);</script>";
    }

    @RequestMapping(value = "getWechatMsgJson")
    public Object getWechatMsgJson(String str, String url) throws IOException {
        LOGGER.debug("获取微信公众号JSON消息");
        str = HtmlUtils.htmlUnescape(URLDecoder.decode(str, "UTF-8"));
        url = URLDecoder.decode(url, "UTF-8");
        articleService.parseWechatMqHistory(str, url);
        return "getWechatMsgJson request success";
    }

    @RequestMapping(value = "getWechatMsgExt")
    public Object getWechatMsgExt(String str, String url) throws IOException {
        LOGGER.debug("获取微信公众号阅读量数据");
        str = HtmlUtils.htmlUnescape(URLDecoder.decode(str, "UTF-8"));
        url = URLDecoder.decode(url, "UTF-8");
        articleService.updateArticleReadNumAndLikeNum(str, url);
        return "getWechatMsgExt request success";
    }

    @RequestMapping(value = "getWechatPost")
    public Object getWechatPost() throws IOException {
        LOGGER.debug("获取微信公众号下一跳地址");
        String url = spiderQueueService.getArticlePageNextUrl();
        //注入js脚本到微信的网页中
        if (url == null) {
            return "<script>setTimeout(function(){alert('程序已经终止');},2000);</script>";
        } else {
            return "<script>setInterval(function(){window.location.href='" + url + "';},2000);</script>";
        }
    }

    @RequestMapping(value = "saveWechatArticle")
    public Object saveWechatArticle(String str, String url) throws UnsupportedEncodingException {
        LOGGER.debug("抓取文章内容与图片");
        str = HtmlUtils.htmlUnescape(URLDecoder.decode(str, "UTF-8"));
        url = URLDecoder.decode(url, "UTF-8");
        articleService.saveArticlePage(str, url);
        return "saveWechatArticle request success";
    }

}
