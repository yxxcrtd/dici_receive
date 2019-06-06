package com.igoosd.verification;

import com.igoosd.util.ByteUtils;
import com.igoosd.util.Constants;
import com.igoosd.util.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2017/8/22.
 */
@Service
@Slf4j
public class GatewayRegVerification extends MsgVerification {

    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.GATEWAY_REG;
    }

    @Override
    protected boolean verifyData(byte[] data) {
        boolean flag = (byte) Constants.HEX_0xFF_INT == (data[0])
                && (byte) Constants.HEX_0xFF_INT == (data[1])
                && (byte) Constants.HEX_0xFF_INT == (data[2]);
        if (!flag) {
            log.error("报文数据异常....");
            return false;
        }
        flag = verifyGatewayNum(data[3], data[4]);
        if (!flag) {
            log.error("不合法的网关号....");
            return false;
        }
        return true;
    }

    @Override
    public Object getResultObj(byte[] data,String key) {
        return ByteUtils.getOriginVal(data[3],data[4]);
    }

}
