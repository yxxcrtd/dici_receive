package com.igoosd.model;

import com.igoosd.util.MsgTypeEnum;
import lombok.Data;

/**
 * 2017/8/22.
 * 统一的业务数据接口
 */
@Data
public class Message<T>  extends AbsModel{

    public Message(String key, MsgTypeEnum msgTypeEnum, T obj) {
        super(key);
        this.msgTypeEnum = msgTypeEnum;
        this.obj = obj;
    }


    /**
     * 指定的消息类型
     */
    private MsgTypeEnum msgTypeEnum;

    /**
     * 业务需要的 数据结构
     */
    private T obj;

    /**
     * 消息唯一标识
     */
}
