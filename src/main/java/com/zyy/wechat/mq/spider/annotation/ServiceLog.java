package com.zyy.wechat.mq.spider.annotation;

import java.lang.annotation.*;

/**
 * Created by akinoneko on 2017/4/13.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceLog {

    String description() default "";
}
