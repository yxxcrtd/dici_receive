package com.igoosd.model.response;

import com.igoosd.util.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * 2017/8/22.
 * 流量地磁响应
 */
@Slf4j
public class CommonResponse extends AbsResponse {

    public CommonResponse(int gatewayNum) {
        super(gatewayNum);
    }

    @Override
    public int getMsgType() {
        return 2;
    }

    @Override
    public byte[] getResponseByte() {
        byte[] bytes = new byte[9];
        bytes[0] = (byte) Constants.PKG_HEAD;
        bytes[1] = bytes_length.byteValue();
        bytes[2] = (byte) getMsgType();
        bytes[3] = getGatewayHighByte();
        bytes[4] = getGatewayLowByte();
        bytes[5] = 0;//备用
        bytes[6] = 0;//备用
        bytes[7] = (byte) getByteSum();
        bytes[8] = (byte) Constants.PKG_TAIL;

        return bytes;
    }

    @Override
    protected int getByteSum() {
        return Constants.PKG_HEAD + bytes_length + getMsgType()
                + (getGatewayHighByte() & 0xff) + (getGatewayLowByte() & 0xff);
    }

    /*public static void main(String[] args) {
        int num = Constants.HEX_0xFF_INT;
        byte highByte = ByteUtils.getHightByte(num);
        byte lowByte = ByteUtils.getLowByte(num);

        System.out.println(num);
        System.out.println(highByte);
        System.out.println(lowByte);
        System.out.println(ByteUtils.getOriginVal(highByte,lowByte));
    }*/


    public static void main(String[] args) {
        CommonResponse commonResponse = new CommonResponse(261);
        System.out.println(commonResponse.getByteSum());

    }
}
