package com.igoosd.util;

/**
 * 2017/8/22.
 * 常量类
 */
public interface Constants {

    /**
     * 数据包 开始 字节
     */
    int PKG_HEAD = Integer.parseInt("aa", 16);

    /**
     * 数据包结束字节
     */
    int PKG_TAIL = Integer.parseInt("bb", 16);

    /**
     * 0xff Integer 类型
     */
    int HEX_0xFF_INT = Integer.parseInt("ff",16);

    /**
     * 流量地磁 传感器节点数
     */
    int GATEWAY_FLOW_SENSOR_NODE_NUM= 2;



    /**
     * 业务  logger 单独记录
     */
    String LOGGER_NAME_FLOW_INFO_DATA="flowInfoDataLogger";

    /**
     * 网关异常redis 记录异常时候 记录该文件
     */
    String LOGGER_NAME_GATEWAY_RESP_PRE_FILENAME= "gatewayResponseExceptionFile#";

    //////////////////////// redis  key ///////////////////////////
    /**
     * 业务服务和接入服务网络异常 响应 数据记录
     */
    String REDIS_KEY_CONFIRM_DATA = "acceptor:confirm_data";

    /**
     * 接入服务和网关 网络异常 响应 数据记录
     */
    String  REDIS_KEY_GATEWAY_RESPONSE_EXCEPTION_KEY = "gateway:response_exception";


}
