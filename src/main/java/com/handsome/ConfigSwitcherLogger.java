package com.handsome;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类
 */
public final class ConfigSwitcherLogger {
    private  static final Logger LOGGER = LoggerFactory.getLogger(ConfigSwitcherLogger.class);
    private static final  String LOGGER_TITLE="[[title=ConfigSwitcherLogger]] ";
    /**
     * 打印Info日志
     * @param msg 日志信息
     * @param params  日志信息格式化参数列表
     */
    public static void infoLog(String msg,Object ... params){
        LOGGER.info(LOGGER_TITLE+msg,params);
    }

    /**
     * 打印Warn级别日志
     * @param msg
     * @param params
     */
    public static void warnLog(String msg,Object ... params){
        LOGGER.warn(LOGGER_TITLE+msg,params);
    }
    /**
     * 打印Error级别日志
     * @param msg
     * @param ex
     */
    public static void errorLog(String msg,Throwable ex){
        LOGGER.error(LOGGER_TITLE+msg,ex);
    }
}
