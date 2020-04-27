package com.vousembrasser.netty.netty.AttributeMap;

import java.util.Date;

import io.netty.util.AttributeKey;

/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/15 0015 17:15
 */
public class NettyChannel {

    public static final AttributeKey<NettyChannel> NETTY_CHANNEL_KEY = AttributeKey.valueOf("netty.channel");

    private String name;


    private Date createDate;


    public NettyChannel(String name, Date createDate) {
        this.name = name;
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
