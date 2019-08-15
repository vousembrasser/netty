package com.vousembrasser.netty.netty.Heart;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Timer;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 13:40
 */
public class HeratClientConnectionWatchdog extends ConnectionWatchdog{

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    public HeratClientConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect) {
        super(bootstrap, timer, port, host, reconnect);
    }

    @Override
    public ChannelHandler[] handlers() {
        System.out.println("Class====="+ this.getClass());
        return new ChannelHandler[] {
                this,
                new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                idleStateTrigger,
                new StringDecoder(),
                new StringEncoder(),
                new HeartBeatClientHandler()
        };
    }
}
