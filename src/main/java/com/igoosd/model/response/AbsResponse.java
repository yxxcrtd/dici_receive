package com.igoosd.model.response;

import com.igoosd.util.ByteUtils;
import lombok.Data;

/**
 * 2017/8/9.
 * socket server 接收到数据后响应信息
 */
@Data
public abstract class AbsResponse {


    public AbsResponse(int gatewayNum){
        setGatewayHighByte(ByteUtils.getHightByte(gatewayNum));
        setGatewayLowByte(ByteUtils.getLowByte(gatewayNum));
    }

    protected static final Integer bytes_length = 9;

    /**
     * 网关高字节 int
     */
    private byte gatewayHighByte;

    /**
     * 网关 低字节 int
     */
    private byte gatewayLowByte;

    public abstract int getMsgType();

    /**
     * 返回response Byte 数组
     *
     * @return
     */
    public abstract byte[] getResponseByte();


    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("响应消息数组:\n");
        byte[] bytes = getResponseByte();
        for (byte b : bytes) {
            stringBuilder.append(b).append(" ");
        }
        stringBuilder.append("\n");
        stringBuilder.append("消息类型：" + getMsgType() + "\n");
        stringBuilder.append("网关高字节:" + getGatewayHighByte() + "\n");
        stringBuilder.append("网关低字节:" + getGatewayLowByte() + "\n");
        int gatewayNum = (getGatewayHighByte() << 8) | getGatewayLowByte();
        stringBuilder.append("网关号:" + gatewayNum);
        return stringBuilder.toString();
    }

    protected abstract int getByteSum();
}
