package com.igoosd.service;

import com.igoosd.model.Message;
import com.igoosd.model.response.AbsResponse;
import com.igoosd.util.MsgTypeEnum;

/**
 * 2017/8/22.
 */
public interface BusinessService<T> {

    /**
     * 获取消息类型枚举
     */
    MsgTypeEnum getMsgTypeEnum();

    /**
     * 业务逻辑处理
     * 业务执行
     *
     * @param message
     */
    AbsResponse exec(Message<T> message);


    /**
     * 业务执行异常....
     * @param message
     */
    void respSuccess(Message<T> message, AbsResponse responses);

    /**
     * 异常响应--业务处理
     */
    void respException(Message<T> message);


}
