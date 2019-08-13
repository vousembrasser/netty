package com.vousembrasser.netty.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/13 0013 14:33
 *
 */

/**
 * SimpleChannelInboundHandler的channelRead0还有一个好处就是你不用关心释放资源，
 * 因为源码中已经帮你释放了，所以如果你保存获取的信息的引用，是无效的~
 */
//public class HelloWorldServerHandler extends SimpleChannelInboundHandler<String> {}
public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server channelRead..");
        System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
        ctx.write("server write"+msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
