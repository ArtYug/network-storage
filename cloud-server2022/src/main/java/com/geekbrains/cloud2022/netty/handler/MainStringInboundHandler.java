package com.geekbrains.cloud2022.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MainStringInboundHandler extends SimpleChannelInboundHandler<String> {
    private final SimpleDateFormat format;

    public MainStringInboundHandler() {
        format = new SimpleDateFormat();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        log.debug("received: {}", s);
        String dateString = "[" + format.format(new Date()) + "] ";
        s = dateString + s;
        log.debug("processed message:  {}", s);
        ctx.writeAndFlush(s);
    }
}
