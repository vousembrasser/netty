package com.vousembrasser.netty.netty.AttributeMap;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

import static com.vousembrasser.netty.netty.AttributeMap.NettyChannel.NETTY_CHANNEL_KEY;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 17:19
 */
public class HelloWorldClientHandler1 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Attribute<NettyChannel> attr = ctx.channel().attr(NETTY_CHANNEL_KEY);
        NettyChannel nChannel = attr.get();
        if (nChannel == null) {
            NettyChannel newNChannel = new NettyChannel("HelloWorld0Client", new Date());
            nChannel = attr.setIfAbsent(newNChannel);
        } else {
            System.out.println("channelActive attributeMap 中是有值的");
            System.out.println(nChannel.getName() + "=======" + nChannel.getCreateDate());
        }
        System.out.println("HelloWorldC0ientHandler Active");
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Attribute<NettyChannel> attr = ctx.attr(NETTY_CHANNEL_KEY);
        NettyChannel nChannel = attr.get();
        if (nChannel == null) {
            NettyChannel newNChannel = new NettyChannel("HelloWorld0Client", new Date());
            nChannel = attr.setIfAbsent(newNChannel);
        } else {
            System.out.println("channelRead attributeMap 中是有值的");
            System.out.println(nChannel.getName() + "=======" + nChannel.getCreateDate());
        }
        System.out.println("HelloWorldClientHandler read Message:" + msg);

        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
