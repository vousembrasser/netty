package com.vousembrasser.netty.netty.Heart;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 11:00
 */

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

/**
 *
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 *
 * 1）继承了ChannelInboundHandlerAdapter，说明它也是Handler，也对，作为一个检测对象，肯定会放在链路中，否则怎么检测
 *
 * 2）实现了2个接口，TimeTask，IChannelHandlerHolder
 *
 *    ①TimeTask，我们就要写run方法，这应该是一个定时任务，这个定时任务做的事情应该是重连的工作
 *
 *    ②IChannelHandlerHolder的接口，这个接口我们刚才说过是维护的所有的Handlers，因为在重连的时候需要获取Handlers
 *
 * 3）bootstrap对象，重连的时候依旧需要这个对象
 *
 * 4）当链路断开的时候会触发channelInactive这个方法，也就说触发重连的导火索是从这边开始的
 *
 *
 * 也稍微说明一下：
 * 1）创建了ConnectionWatchdog对象，自然要实现handlers方法
 *
 * 2）初始化好bootstrap对象
 *
 * 3）4秒内没有写操作，进行心跳触发，也就是IdleStateHandler这个方法
 */
@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask,IChannelHandlerHolder{

    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;

    private final String host;

    private volatile boolean reconnect = true;
    private int attempts;


    public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port,String host, boolean reconnect) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("当前链路已经激活了，重连尝试次数重新置为0");

        attempts = 0;
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链接关闭");
        if(reconnect){
            System.out.println("链接关闭，将进行重连");
            if (attempts < 12) {
                attempts++;
                //重连的间隔时间会越来越长
                int timeout = 2 << attempts;
                timer.newTimeout(this, timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelInactive();
    }

    @Override
    public void run(Timeout timeout) throws Exception {

        ChannelFuture future;
        //bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized (bootstrap) {
            bootstrap.handler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {

                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host,port);
        }
        //future对象
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                boolean succeed = f.isSuccess();

                //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                if (!succeed) {
                    System.out.println("重连失败");
                    f.channel().pipeline().fireChannelInactive();
                }else{
                    System.out.println("重连成功");
                }
            }
        });

    }

}
