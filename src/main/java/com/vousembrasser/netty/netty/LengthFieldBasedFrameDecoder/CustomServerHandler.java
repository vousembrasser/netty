package com.vousembrasser.netty.netty.LengthFieldBasedFrameDecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/14 0014 11:27
 */
public class CustomServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CustomMsg) {
            CustomMsg customMsg = (CustomMsg) msg;
            System.out.println("Client->Server:" + ctx.channel().remoteAddress() + " send " + customMsg.getBody());
        }

    }

}
