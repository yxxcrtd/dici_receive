package com.igoosd.verification;

import com.igoosd.util.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 2017/8/22.
 * 消息数据校验
 */
@Slf4j
public abstract class MsgVerification<T> {


    /**
     * 验证的消息类型
     *
     * @return
     */
    public abstract MsgTypeEnum getMsgTypeEnum();

    /**
     * 具有数据校验
     *
     * @param data
     * @return
     */
    protected abstract boolean verifyData(byte[] data);


    /**
     * 获取解析后的结果数据结构实例
     *
     * @Param msgKey 数据包唯一标志  便于后期数据排查定位
     * @return
     */
    public abstract T getResultObj(byte[] data,String msgKey);


    /**
     * 消息验证
     *
     * @return
     */
    public boolean verifyMsg(byte[] bytes) {

        if (null == bytes || 0 == getMsgTypeEnum().getLength()) {
            return false;
        }
        //校验 防止子类更改data数据 造成数据不准确
        return verifyData(Arrays.copyOf(bytes, bytes.length));
    }


    /**
     * 网关 号  高低字节 校验
     * 校验网管号是否合法 是否已经在DB中维护
     *
     * @param highByte
     * @param lowByte
     * @return
     */
    protected boolean verifyGatewayNum(byte highByte, byte lowByte) {
        //TODO
        return true;
    }

}
