package com.zyy.wechat.mq.spider.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyy.wechat.mq.spider.dao.ArticleRepository;
import com.zyy.wechat.mq.spider.dao.SpiderQueueRepository;
import com.zyy.wechat.mq.spider.dao.WechatMqRepository;
import com.zyy.wechat.mq.spider.entity.Article;
import com.zyy.wechat.mq.spider.entity.SpiderConfig;
import com.zyy.wechat.mq.spider.entity.SpiderQueue;
import com.zyy.wechat.mq.spider.entity.WechatMq;
import com.zyy.wechat.mq.spider.task.ImageDownloadTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akinoneko on 2017/3/25.
 */
@Service
public class ArticleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SpiderQueueRepository spiderQueueRepository;
    @Autowired
    private WechatMqRepository wechatMqRepository;

    @Autowired
    private SpiderConfig spiderConfig;

    @Transactional
    public void parseWechatMqHistory(String str, String url) throws UnsupportedEncodingException {
        String biz = null;
        for (String param : url.substring(url.indexOf("?") + 1).split("&")) {
            String key = param.split("=")[0];
            if ("__biz".equals(key)) {
                biz = param.substring(param.indexOf("=") + 1);
                break;
            }
        }
        //检查公众号是否被录入
        if (wechatMqRepository.findByBiz(biz) == null) {
            WechatMq wechatMq = new WechatMq();
            wechatMq.setBiz(biz);
            wechatMq.setCollect(System.currentTimeMillis());
            wechatMqRepository.save(wechatMq);
        }
        //解析公众号历史消息记录
        JSONObject json = null;
        JSONArray list = null;
        try {
            json = JSONObject.parseObject(str);
            list = json.getJSONArray("list");
        } catch (Exception e) {
            LOGGER.error("JSON数据解析失败" + e.getMessage());
//            LOGGER.error("json->" + str);
            return;
        }
        for (Object object : list) {
            JSONObject item = (JSONObject) object;
            JSONObject commMsgInfo = item.getJSONObject("comm_msg_info");
            JSONObject appMsgExtInfo = item.getJSONObject("app_msg_ext_info");
            Article article = null;
            int type = commMsgInfo.getIntValue("type");
            if (49 == type) {  //图文消息
                String contentUrl = HtmlUtils.htmlUnescape(appMsgExtInfo.getString("content_url"))
                        .replace("\\", "");
                int isMulti = appMsgExtInfo.getIntValue("is_multi");
                long datetime = commMsgInfo.getLongValue("datetime");
                if (articleRepository.findOneByContentUrl(contentUrl) == null) {
                    article = new Article();
                    article.setBiz(biz);
                    article.setIsMulti(isMulti);
                    article.setDatetime(datetime);
                    article.setContentUrl(contentUrl);
                    article.setFieldId(appMsgExtInfo.getLong("fileid"));
                    article.setTitle(appMsgExtInfo.getString("title"));
                    article.setTitle_encode(URLEncoder.encode(appMsgExtInfo.getString("title")
                            .replace("&nbsp;", ""), "UTF-8"));
                    article.setDigest(appMsgExtInfo.getString("digest"));
                    article.setSourceUrl(HtmlUtils.htmlUnescape(appMsgExtInfo.getString("source_url"))
                            .replace("\\", ""));
                    article.setCover(HtmlUtils.htmlUnescape(appMsgExtInfo.getString("cover"))
                            .replace("\\", ""));
                    article.setIsTop(1);
                    article.setCreateTime(System.currentTimeMillis());
                    article.setUpdateTime(System.currentTimeMillis());
                    try {
                        articleRepository.save(article);
                        SpiderQueue spiderQueue = new SpiderQueue();
                        spiderQueue.setContentUrl(contentUrl);
                        spiderQueue.setDatetime(System.currentTimeMillis());
                        spiderQueueRepository.save(spiderQueue);
                        LOGGER.info("头条标题:" + article.getTitle());
                    } catch (Exception e) {
                        LOGGER.error("文章保存失败" + e.getMessage());
                    }
                }
                if (isMulti > 0) {
                    JSONArray multiAppMsgItemList = appMsgExtInfo.getJSONArray("multi_app_msg_item_list");
                    for (Object oneMultiItem : multiAppMsgItemList) {
                        JSONObject multiItem = (JSONObject) oneMultiItem;
                        contentUrl = HtmlUtils.htmlUnescape(multiItem.getString("content_url"))
                                .replace("\\", "");
                        if (articleRepository.findOneByContentUrl(contentUrl) == null) {
                            article = new Article();
                            article.setBiz(biz);
                            article.setIsMulti(isMulti);
                            article.setDatetime(datetime);
                            article.setContentUrl(contentUrl);
                            article.setFieldId(multiItem.getLong("fileid"));
                            article.setTitle(multiItem.getString("title"));
                            article.setTitle_encode(URLEncoder.encode(multiItem.getString("title")
                                    .replace("&nbsp;", ""), "UTF-8"));
                            article.setDigest(multiItem.getString("digest"));
                            article.setSourceUrl(HtmlUtils.htmlUnescape(multiItem.getString("source_url"))
                                    .replace("\\", ""));
                            article.setCover(HtmlUtils.htmlUnescape(multiItem.getString("cover"))
                                    .replace("\\", ""));
                            article.setCreateTime(System.currentTimeMillis());
                            article.setUpdateTime(System.currentTimeMillis());
                            try {
                                articleRepository.save(article);
                                SpiderQueue spiderQueue = new SpiderQueue();
                                spiderQueue.setContentUrl(contentUrl);
                                spiderQueue.setDatetime(System.currentTimeMillis());
                                spiderQueueRepository.save(spiderQueue);
                                LOGGER.info("标题:" + article.getTitle());
                            } catch (Exception e) {
                                LOGGER.error("文章保存失败" + e.getMessage());
                            }
                        }
                    }
                }

            }
        }
    }

    @Transactional
    public void updateArticleReadNumAndLikeNum(String str, String url) {
        String biz = null;
        String sn = null;
        for (String param : url.substring(url.indexOf("?") + 1).split("&")) {
            String key = param.split("=")[0];
            if ("__biz".equals(key)) {
                biz = param.substring(param.indexOf("=") + 1);
            } else if ("sn".equals(key)) {
                sn = param.substring(param.indexOf("=") + 1);
            }

        }
        try {
            //解析文章数据
            JSONObject json = JSONObject.parseObject(str);
            JSONObject appMsgStat = json.getJSONObject("appmsgstat");
            int readNum = appMsgStat.getIntValue("read_num");
            int likeNum = appMsgStat.getIntValue("like_num");
            Article article = articleRepository.findBySnAndBiz(sn, biz);
            if (article != null) {
                //更新文章的阅读数和点赞数
                article.setReadNum(readNum);
                article.setLikeNum(likeNum);
                article.setUpdateTime(System.currentTimeMillis());
                articleRepository.save(article);
                //删除采集队列中的记录
                spiderQueueRepository.deleteBySn(sn);
            }
        } catch (Exception e) {
            LOGGER.error("SN=" + sn + "\nBIZ=" + biz + "\nSTR=" + str + "\nURL=" + url, e);
        }
    }

    @Transactional
    public void saveArticlePage(String str, String url) {
        boolean imgDown = false;
        String biz = null;
        String sn = null;
        for (String param : url.substring(url.indexOf("?") + 1).split("&")) {
            String key = param.split("=")[0];
            if ("__biz".equals(key)) {
                biz = param.substring(param.indexOf("=") + 1);
            } else if ("sn".equals(key)) {
                sn = param.substring(param.indexOf("=") + 1);
            }

        }
        List<String[]> urls = new ArrayList<>();
//        Map<String, String> urls = new HashMap<>();
        Document page = Jsoup.parse(str);
        Element postUserElement = page.getElementById("post-user");
        String postUser = null;
        if (postUserElement != null) {    //防止取不到文章的作者
            postUser = postUserElement.text().trim();
        }
        String pageContentHtml = null;
        Element pageContentElement = page.getElementById("js_content");
        if (pageContentElement != null) {   //防止文章内容没有的情况
            pageContentHtml = page.getElementById("js_content").toString();
            //获取图片下载地址
            for (Element element : page.getElementById("js_content").getElementsByTag("img")) {
                String originSrc = element.attr("data-src");
                String newSrc = "origin=" + originSrc;

//                pageContentHtml = pageContentHtml.replace(originSrc,
//                        spiderConfig.getImgUrlDomain() + "/" + newSrc);
//                urls.put(originSrc, newSrc);
                urls.add(new String[]{originSrc, newSrc});
            }
            //替换视频与图片地址
            if (pageContentHtml == null) pageContentHtml = "";
            pageContentHtml = pageContentHtml.replace("data-src=\"",
                    "src=\"http://" + spiderConfig.getImgUrlDomain() + "/origin=");
            pageContentHtml = pageContentHtml.replace("preview.html", "player.html");
            Document pageContent = Jsoup.parse(pageContentHtml.replace("data-src", "src"));
            String content = pageContent.getElementById("js_content").html().trim();
            Article article = articleRepository.findBySnAndBiz(sn, biz);
            if (null != article) {
                imgDown = article.getContent() == null;
                article.setAuthor(postUser);
                article.setContent(content);
                article.setUpdateTime(System.currentTimeMillis());
                articleRepository.save(article);
            }
            //获取公众号的昵称和头像
            String icon = null;
            String name = null;
            Pattern pattern = Pattern.compile("var ori_head_img_url = \"(.*?)\";"); //匹配头像地址
            Matcher matcher = pattern.matcher(page.toString());
            while (matcher.find()) {
                icon = matcher.group();
                icon = icon.substring(icon.indexOf("http"), icon.length() - 2);
            }
            pattern = Pattern.compile("var nickname = \"(.*?)\";");
            matcher = pattern.matcher(page.toString());
            while (matcher.find()) {
                name = matcher.group();
                name = name.substring(16, name.length() - 2);
            }
            WechatMq wechatMq = wechatMqRepository.findByBiz(biz);
            if (null != name && icon != null) {
                String newSrc = "origin=" + icon;
//                urls.put(icon, newSrc); //下载头像
                urls.add(new String[]{icon, newSrc});
                if (!name.equals(wechatMq.getName()) || !icon.equals(wechatMq.getIcon())) {
                    wechatMq.setName(name);
                    wechatMq.setIcon(spiderConfig.getImgUrlDomain() + "/" + icon);
                    wechatMqRepository.save(wechatMq);
                }
            }
            //添加图片到下载任务队列中
            if (imgDown) {
                ImageDownloadTask.addImageUrlToQueue(urls);
//                AsyncDownloadImage.asyncDownloadImage(urls);
            }
        }
    }

}
