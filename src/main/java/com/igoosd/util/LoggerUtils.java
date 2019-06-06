package com.igoosd.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 2017/9/5.
 * 单独业务日志记录
 */
@Component
@Data
public class LoggerUtils {

    @Value("${log.path}")
    private String logPath;

    private static LoggerUtils loggerUtils;

    protected LoggerUtils() {
        loggerUtils = this;
    }

    public static final LoggerUtils GET_INSTANCE() {
        return loggerUtils;
    }



    public void log(String fileName,String marker,Object msg){
        Assert.isTrue(!Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(fileName), "业务日志不允许用 root 命名");
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);//root logger
        LoggerContext loggerContext = rootLogger.getLoggerContext();
        Logger destLogger = loggerContext.exists(fileName);
        if (null == destLogger) {
            synchronized (rootLogger) {
                destLogger = loggerContext.exists(fileName);
                if (null == destLogger) {
                    rootLogger.info("创建新的child Logger：{}", fileName);
                    destLogger = loggerContext.getLogger(fileName);//创建新的logger
                    //Encoder 内容排版
                    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
                    encoder.setContext(loggerContext);
                    encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss}|%msg%n");
                    encoder.start();
                    //添加Appender
                    FileAppender<ILoggingEvent> appender = new FileAppender<>();
                    String destFileName = fileName + ".log";
                    String filePath = logPath + destFileName;
                    appender.setFile(filePath);
                    appender.setContext(loggerContext);
                    appender.setEncoder(encoder);//输出appender组件添加encoder
                    appender.start();

                    destLogger.addAppender(appender);
                    destLogger.setLevel(Level.DEBUG);
                    destLogger.setAdditive(false);
                }
            }
        }
        //日志记录
        StringBuilder sb = new StringBuilder();
        marker = marker == null ? "":marker;
        sb.append(marker).append("|");
        sb.append(String.class.isAssignableFrom(msg.getClass()) ? msg : GsonUtil.toJson(msg));
        destLogger.info(sb.toString());
    }

    /**
     * 日志记录
     *
     * @param msg
     */
    public void log(String fileName, Object msg) {
        log(fileName,"",msg);
    }

}
