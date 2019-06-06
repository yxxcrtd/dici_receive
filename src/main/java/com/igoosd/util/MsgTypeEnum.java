package com.igoosd.util;

/**
 * 2017/8/21.
 * 接收的数据包 消息类型枚举
 */
public enum MsgTypeEnum {
    DATA_FLOW_MSG(30,1, "打包数据帧消息"), GATEWAY_REG(6, "网关注册帧消息"), HEARTBEAT(2, "心跳包"),ERROR_MSG("解析错误的消息");

    private int length;
    private String name;
    private int type;

    MsgTypeEnum( String name) {
        this.name = name;
    }
    MsgTypeEnum(int length, String name) {
        this.length = length;
        this.name = name;
    }
    MsgTypeEnum(int length, int type,String name) {
        this.length = length;
        this.type = type;
        this.name = name;
    }

  /*  public static MsgTypeEnum getMessageTypeEnum(int length) {
        for (MsgTypeEnum messageTypeEnum : MsgTypeEnum.values()) {
            if (length == messageTypeEnum.getLength()) {
                return messageTypeEnum;
            }
        }
        return null;
    }*/

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
