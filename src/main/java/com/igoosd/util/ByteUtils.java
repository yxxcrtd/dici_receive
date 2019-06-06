package com.igoosd.util;

/**
 * 2017/8/22.
 */
public class ByteUtils {


    /**
     *
     * 获取 高位字节
     * @return
     */
    public static byte getHightByte(int originVal){
        return (byte) (originVal >> 8);
    }

    /**
     * 获取低位字节
     * @param originVal
     * @return
     */
    public static byte getLowByte(int originVal){
        return (byte) originVal;
    }

    public static int getOriginVal(byte highByte,byte lowByte){
        return (highByte & 0xff) << 8 | (lowByte & 0xff);
    }
}
