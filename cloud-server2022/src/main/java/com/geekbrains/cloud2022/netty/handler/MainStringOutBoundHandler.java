package com.geekbrains.cloud2022.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MainStringOutBoundHandler extends ChannelOutboundHandlerAdapter {
@Override
    public void write (ChannelHandlerContext ctx, Object msg,ChannelPromise promise){
    String str = (String)msg;
    log.debug("Send: {}",msg);
    ctx.write(str);
}
}
