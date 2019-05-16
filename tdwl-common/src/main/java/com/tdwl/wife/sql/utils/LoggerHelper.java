package com.tdwl.wife.sql.utils;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerHelper {

    public static String getInvokeName(MethodInvocation method){
        return method.getMethod().getDeclaringClass().getSimpleName() + "@" + method.getMethod().getName();
    }

    public static long beginInvoke(String invokeName, Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("Begin an invoke`" + invokeName + "`");// 方法前的操作
        return System.currentTimeMillis();
    }

    public static void endInvokeWithErrors(String invokeName, Class<?> clazz, long startTime) {
        Logger logger = LoggerFactory.getLogger(clazz);
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 1500) {
            logger.warn(String.format("End an invoke`%s`. Cost time`%.1f`s. With errors. With slowness.\n", invokeName, (endTime - startTime)/1000.0));
        } else {
            logger.info(String.format("End an invoke`%s`. Cost time`%.1f`s. With errors.\n", invokeName, (endTime - startTime)/1000.0));
        }
    }

    public static void endInvoke(String invokeName, Class<?> clazz, long startTime) {
        Logger logger = LoggerFactory.getLogger(clazz);
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 1500) {
            logger.warn(String.format("End an invoke`%s`. Cost time`%.1f`s. With slowness.\n", invokeName, (endTime - startTime)/1000.0));
        } else {
            logger.info(String.format("End an invoke`%s`. Cost time`%.1f`s.\n", invokeName, (endTime - startTime)/1000.0));
        }
    }
}
