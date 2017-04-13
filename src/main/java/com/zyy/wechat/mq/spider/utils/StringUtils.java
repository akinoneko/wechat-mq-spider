package com.zyy.wechat.mq.spider.utils;

/**
 * Created by akinoneko on 2017/4/13.
 */
public class StringUtils extends org.springframework.util.StringUtils{

    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;
        }
        //含有emoji表情,过滤
        StringBuffer sb = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (sb == null) {
                    sb = new StringBuffer(source.length());
                }
                sb.append(codePoint);
            }
        }

        if (sb == null) {
            return source;
        } else {
            if (sb.length() == len) {   //优化
                sb = null;
                return source;
            } else {
                return sb.toString();
            }
        }
    }

    private static boolean containsEmoji(String source) {
        if (isEmpty(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static void main(String[] args) {
        String testStr = "\uD83D\uDE42\uD83D\uDE2D这是有🐩很多的emoji表情,\uD83D\uDE08\uD83D\uDE01你好,我是一个✋👂.";
        String res = StringUtils.filterEmoji(testStr);
        System.out.println(testStr);
        System.out.println(res);
    }
}
