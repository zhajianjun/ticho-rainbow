package top.ticho.rainbow.application.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ticho.tool.intranet.server.entity.AppDataCollector;
import top.ticho.tool.intranet.server.filter.AppListenFilter;

import java.net.InetSocketAddress;

/**
 * 内网穿透代理应用监听过滤器
 *
 * @author zhajianjun
 * @date 2024-05-15 08:49
 */
@Slf4j
@Component
public class IntranetAppListenFilter implements AppListenFilter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().localAddress();
        AppDataCollector collector = AppDataCollector.getCollector(addr.getPort());
        collector.getChannels().incrementAndGet();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, ByteBuf msgByteBuf) {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().localAddress();
        AppDataCollector collector = AppDataCollector.getCollector(addr.getPort());
        collector.incrementReadBytes(msgByteBuf.readableBytes());
        collector.incrementReadMsgs(1L);
    }

    @Override
    public void write(ChannelHandlerContext ctx, ByteBuf msgByteBuf, ChannelPromise promise) {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().localAddress();
        AppDataCollector collector = AppDataCollector.getCollector(addr.getPort());
        collector.incrementWriteBytes(msgByteBuf.readableBytes());
        collector.incrementWriteMsgs(1L);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress addr = (InetSocketAddress) ctx.channel().localAddress();
        AppDataCollector collector = AppDataCollector.getCollector(addr.getPort());
        collector.getChannels().decrementAndGet();
    }

}
