package com.igoosd.verification;

import com.igoosd.model.reciver.GatewayFlowInfo;
import com.igoosd.util.Constants;
import com.igoosd.util.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 2017/8/22.
 * 网关上报数据校验
 */
@Service
@Slf4j
public class GatewayFlowInfoVerification extends MsgVerification {
    @Override
    public MsgTypeEnum getMsgTypeEnum() {
        return MsgTypeEnum.DATA_FLOW_MSG;
    }

    @Override
    protected boolean verifyData(byte[] data) {
        //头校验
        if (data[0] != (byte) Constants.PKG_HEAD) {
            log.error("头部校验失败");
            return false;
        }
        //尾校验
        if (data[getMsgTypeEnum().getLength() - 1] != (byte) Constants.PKG_TAIL) {
            log.error("尾部校验失败");
            return false;
        }
        //长度  超类已校验
        //类型
        if ((byte)getMsgTypeEnum().getType() != data[2]) {
            log.error("消息类型校验失败");
            return false;
        }
        //网关电量校验  略
        //节点数校验
        if ((byte) Constants.GATEWAY_FLOW_SENSOR_NODE_NUM != data[6]) {
            return false;
        }
        //TODO  其他信息校验
        //字节sum校验 0-27 共28个字节int值 求和
        byte sum = 0;
        for (int i = 0; i <= 27; i++) {
            sum += data[i];
        }
        if (sum != data[28]) {
            log.error("字节求和异常,sum={},data={}", sum, data[28]);
            return false;
        }
        return true;
    }

    @Override
    public Object getResultObj(byte[] data,String msgKey) {
        return GatewayFlowInfo.parseGatewayInfo(data,msgKey);
    }

}
