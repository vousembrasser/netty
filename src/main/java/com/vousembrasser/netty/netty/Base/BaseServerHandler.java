package com.vousembrasser.netty.netty.Base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/13 0013 17:44
 */
public class BaseServerHandler extends ChannelInboundHandlerAdapter {


    private int counter;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;
        System.out.println("server receive order : " + body + ";the counter is: " + ++counter);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
