package com.igoosd.handler.codec;

import com.igoosd.model.Message;
import com.igoosd.util.SpringContextHolder;
import com.igoosd.verification.MsgVerification;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * 2017/8/21.
 */
@Slf4j
public class MsgRecDecoder extends ByteToMessageDecoder {

    private Collection<MsgVerification> msgVerificationList = SpringContextHolder.getBeans(MsgVerification.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int len = in.readableBytes();
        byte[] bytes;
        if (len > 0) {
            String key = UUID.randomUUID().toString().replaceAll("-","");
            bytes = new byte[len];
            in.readBytes(bytes);
            log.info("key:{},接收到客户端:{}数据，长度：{},初始数据：{}",key, ctx.pipeline().channel().remoteAddress(), len, bytes);

            //循环 遍历 length 要唯一 否则要进一步校验
            for (MsgVerification msgVerification : msgVerificationList) {
                if (msgVerification.getMsgTypeEnum().getLength() == len) {
                    //消息验证
                    boolean flag = msgVerification.verifyMsg(bytes);
                    if (flag) {
                        Object resultObj = msgVerification.getResultObj(bytes,key);
                        out.add(new Message(key,msgVerification.getMsgTypeEnum(), resultObj));
                        return;
                    }
                }
            }
        }else{
            log.info("客户端断开连接，channel关闭操作...");
            ctx.close();
        }
        return;
    }

}
