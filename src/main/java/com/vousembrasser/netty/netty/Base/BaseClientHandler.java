package com.vousembrasser.netty.netty.Base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Copyright (C),2019-2020 中盈优创
 *
 * @author: dingwd
 * @date: 2019/8/13 0013 17:35
 */
public class BaseClientHandler extends ChannelInboundHandlerAdapter {

//    private byte[] req;
//    private int counter;
//
//    public BaseClientHandler() {
//        //req = ("BazingaLyncc is learner" + System.getProperty("line.separator")).getBytes();
//
//        req = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
//                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
//                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
//                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
//                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
//                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
//                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
//                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
//                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
//                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
//                + "sdsa ddasd asdsadas dsadasdas").getBytes();
//    }
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        //？？并且在我们平时的普通开发中，用的也是Unpool类型的普通ByteBuf，但是在一些高并发的应用中，Pooled化的ByteBuf性能会更加优秀
//        ByteBuf message = null;
////        for (int i = 0; i < 100; i++) {
////            message = Unpooled.buffer(req.length);
////            message.writeBytes(req);
////            ctx.writeAndFlush(message);
////        }
//        message = Unpooled.buffer(req.length);
//        message.writeBytes(req);
//        ctx.writeAndFlush(message);
//        message = Unpooled.buffer(req.length);
//        message.writeBytes(req);
//        ctx.writeAndFlush(message);
//    }
//
//
//    @Override
//
//
//    public void channelRead(ChannelHandlerContext ctx, Object msg)
//            throws Exception {
//        String buf = (String) msg;
//        System.out.println("Now is : " + buf + " ; the counter is : " + ++counter);
//    }
//
//
//    @Override
//
//
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        ctx.close();
//    }


//    /**
//     *
//     * ------------------------------粘包 现象------------------------------
//     */
//    private byte[] req;
//
//    private int counter;
//
//    public BaseClientHandler() {
//        req = ("BazingaLyncc is learner").getBytes();
////        req = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
////                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
////                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
////                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
////                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
////                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
////                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
////                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
////                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
////                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
////                + "sdsa ddasd asdsadas dsadasdas").getBytes();
//    }
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf message = null;
//        for (int i = 0; i < 100; i++) {
//            message = Unpooled.buffer(req.length);
//            message.writeBytes(req);
//            ctx.writeAndFlush(message);
//        }
////        message = Unpooled.buffer(req.length);
////        message.writeBytes(req);
////        ctx.writeAndFlush(message);
////        message = Unpooled.buffer(req.length);
////        message.writeBytes(req);
////        ctx.writeAndFlush(message);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        ctx.close();
//    }

    //        req = ("BazingaLyncc is learner").getBytes();


    /**
     * 粘包解决方案
     *
     *
     * Netty提供了几个常用的解码器，帮助我们解决这些问题，其实上述的粘包和拆包的问题，归根结底的解决方案就是发送端给远程端一个标记，
     * 告诉远程端，每个信息的结束标志是什么，这样，远程端获取到数据后，根据跟发送端约束的标志，将接收的信息分切或者合并成我们需要的信息，
     * 这样我们就可以获取到正确的信息了
     *
     *
     *
     * 例如，我们刚才的例子中，我们可以在发送的信息中，加一个结束标志，例如两个远程端规定以行来切分数据，那么发送端，
     * 就需要在每个信息体的末尾加上行结束的标志，部分代码如下：
     *
     * 打完标记，其实我们这个示例中的server中还不知道是以行为结尾的，所以我们需要修改server的handler链，在inbound链中加一个额外的处理链，判断一下，获取的信息按照行来切分，我们很庆幸，这样枯燥的代码Netty已经帮我们完美地完成了，Netty提供了一个LineBasedFrameDecoder这个类，顾名思义，这个类名字中有decoder，说明是一个解码器，我们再看看它的详细声明：
     * ---------------------
     * 版权声明：本文为CSDN博主「BazingaLyncc」的原创文章，遵循CC 4.0 by-sa版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/linuu/article/details/51338538
     *
     */
    private byte[] req;

    private int counter;

    public BaseClientHandler() {
        //req = ("BazingaLyncc is learner").getBytes();
        req = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
                + "sdsa ddasd asdsadas dsadasdas" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
//        message = Unpooled.buffer(req.length);
//        message.writeBytes(req);
//        ctx.writeAndFlush(message);
//        message = Unpooled.buffer(req.length);
//        message.writeBytes(req);
//        ctx.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}