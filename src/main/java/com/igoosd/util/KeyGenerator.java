package com.igoosd.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 2017/8/24.
 * 消息唯一标志创建者
 */
public class KeyGenerator {


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String KEY_SEPARATOR = ":";

    /***
     * @return ${yyyyMMddHHmmss}:${gatewayId}
     */
    public static String genPreKey(){
        return sdf.format(new Date());
    }
}
