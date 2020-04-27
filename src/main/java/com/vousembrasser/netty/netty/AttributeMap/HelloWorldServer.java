package com.vousembrasser.netty.netty.AttributeMap;


import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 17:16
 */
public class HelloWorldServer {

    private int port;

    public HelloWorldServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new HelloWorldServerHandler());
                        }
                    })
                    /*在有些网络通信的场景下，要求低延迟，这样就需要我们设置一些TCP的链接属性：
                     * 在服务器端是在worker的Channel端设置属性，
                     * boot.childOption(ChannelOption.TCP_NODELAY, true);
                     * 设置这样做好的好处就是禁用nagle算法
                     *
                     * Nagle算法试图减少TCP包的数量和结构性开销, 将多个较小的包组合成较大的包进行发送.但这不是重点,
                     * 关键是这个算法受TCP延迟确认影响, 会导致相继两次向连接发送请求包,读数据时会有一个最多达500毫秒的延时.
                     * */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = sbs.bind(port).sync();

            System.out.println("Server start listen at " + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new HelloWorldServer(port).start();
    }

}
