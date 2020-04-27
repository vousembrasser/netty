package com.vousembrasser.netty.netty.AttributeMap;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 17:18
 */
public class HelloWorldClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        initChannel();
    }

    public static void initChannel() throws InterruptedException {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    /*在有些网络通信的场景下，要求低延迟，这样就需要我们设置一些TCP的链接属性：
                     * 在客户端我们需要这样设置：
                     * bootstap.option(ChannelOption.TCP_NODELAY, true);
                     * 设置这样做好的好处就是禁用nagle算法
                     *
                     * Nagle算法试图减少TCP包的数量和结构性开销, 将多个较小的包组合成较大的包进行发送.但这不是重点,
                     * 关键是这个算法受TCP延迟确认影响, 会导致相继两次向连接发送请求包,读数据时会有一个最多达500毫秒的延时.
                     * */
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new HelloWorldClientHandler1());
                            p.addLast(new HelloWorldClientHandler2());
                        }
                    });

            ChannelFuture future = b.connect(HOST, PORT).sync();
            future.channel().writeAndFlush("hello Netty,Test attributeMap");
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

}
