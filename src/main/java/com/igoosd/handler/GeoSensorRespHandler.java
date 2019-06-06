package com.igoosd.handler;

import com.igoosd.model.Message;
import com.igoosd.model.response.AbsResponse;
import com.igoosd.service.BusinessService;
import com.igoosd.util.MsgTypeEnum;
import com.igoosd.util.SpringContextHolder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 2017/8/8.
 * 地磁数据接收响应handler
 */
@Slf4j
public class GeoSensorRespHandler extends ChannelInboundHandlerAdapter {

    /**
     * handler  netty 需要 重复 add  remove  这里用 springContextHolder 手动输入
     */
    private Collection<BusinessService> businessServiceList = SpringContextHolder.getBeans(BusinessService.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端：" + ctx.pipeline().channel().remoteAddress() + " 连接上了");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端：" + ctx.pipeline().channel().remoteAddress() + " 断开连接了");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        MsgTypeEnum msgTypeEnum = message.getMsgTypeEnum();
        log.info("处理客户端：{}，业务消息类型：{},消息体/消息长度:{}", ctx.pipeline().channel().remoteAddress(), msgTypeEnum.getName(), message.getObj());
        //存在业务处理服务 处理 ，基于不同的业务应答客户端 否则 不做应答
        for (BusinessService businessService : businessServiceList) {
            if (msgTypeEnum == businessService.getMsgTypeEnum()) {
                log.info("即将业务处理,业务处理接口[{}]", businessService.getClass().getName());
                //业务处理
                AbsResponse response = businessService.exec(message);
                if (null != response) {
                    log.info("业务处理成功...");
                    byte[] rspBytes = response.getResponseByte();
                    ChannelFuture channelFuture = ctx.writeAndFlush(ctx.alloc().buffer(rspBytes.length).writeBytes(rspBytes));
                    //应答结束后 关闭 channel
                    channelFuture.addListener((ChannelFutureListener) future -> {
                        if(future.isDone()){
                            if(future.isSuccess()){
                                businessService.respSuccess(message,response);
                            } else{
                                businessService.respException(message);
                            }
                        }
                    });
                } else {
                    log.error("业务处理异常，请检查....");
                }
            }
        }
    }


}
