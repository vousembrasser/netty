package com.vousembrasser.netty.netty.LengthFieldBasedFrameDecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/14 0014 11:29
 */
public class CustomClientHandler extends ChannelInboundHandlerAdapter {

    //type类型  系统编号 0xAB 表示A系统，0xBC 表示B系统

    //flag信息标志  0xAB 表示心跳包    0xBC 表示超时包  0xCD 业务信息包

    //length主题信息的长度

    //body主题信息

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CustomMsg customMsg = new CustomMsg((byte) 0xAB, (byte) 0xCD, "Hello,Netty".length(), "Hello,Netty");
        ctx.writeAndFlush(customMsg);
    }

}
