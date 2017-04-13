package com.zyy.wechat.mq.spider.aspectj;

import com.alibaba.fastjson.JSON;
import com.zyy.wechat.mq.spider.annotation.ServiceLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by akinoneko on 2017/4/13.
 */
@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogAspect.class);

    //service调用日志记录点
    @Pointcut("@annotation(com.zyy.wechat.mq.spider.annotation.ServiceLog)")
    public void serviceMethodLog() {
    }

    @Before("serviceMethodLog()")
    public void doServiceBefore(JoinPoint joinPoint) {

    }

    @Around("serviceMethodLog()")
    public void doServiceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = targetName + "." + joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        long executeTime = System.currentTimeMillis() - start;
        LOGGER.info("方法<{}>执行时间{}ms", methodName, executeTime);
    }

    @After("serviceMethodLog()")
    public void doServiceAfter(JoinPoint joinPoint) {

    }

    @AfterThrowing(pointcut = "serviceMethodLog()", throwing = "e")
    public void doServiceThrowing(JoinPoint joinPoint, Throwable e) throws Exception {
        String params = JSON.toJSONString(joinPoint.getArgs());
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = targetName + "." + joinPoint.getSignature().getName();
        String message = "\r\n=====SERVICE异常记录开始=====" +
                "\r\n异常方法:" + methodName +
                "\r\n异常信息:" + e.getMessage() +
                "\r\n异常参数:" + params +
                "\r\n方法描述:" + getServiceMethodDescription(joinPoint) +
                "\r\n=====SERVICE异常记录结束=====";
        LOGGER.error("发生异常,异常信息{} {}", e.getMessage(), message);
    }

    /**
     * 解析注解中对于方法的描述信息
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getServiceMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == args.length) {
                    description = method.getAnnotation(ServiceLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}
