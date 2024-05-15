package top.ticho.rainbow.domain.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ticho.tool.intranet.server.filter.AppListenFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
        // InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        // InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        // log.info("{} active {}, ", remoteAddress, localAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        // InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        handle(byteBuf, remoteAddress.getHostString());
        // log.info("{} read from {}, size={}", localAddress, remoteAddress, byteBuf.readableBytes());
    }

    @Override
    public void write(ChannelHandlerContext ctx, ByteBuf byteBuf, ChannelPromise channelPromise) {
        // InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        // InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        // log.info("{} write to {}, size={}b", localAddress, remoteAddress, byteBuf.readableBytes());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        // InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        // log.info("{} inactive {}", remoteAddress, localAddress);
    }

    public void handle(ByteBuf byteBuf, String realIp) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ByteBufUtil.getBytes(byteBuf));
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(reader);
        String line;
        try {
            // 读取第一行
            String firstLine = br.readLine();
            boolean contains = firstLine.contains("HTTP");
            if (contains) {
                byteBuf.clear();
                byteBuf.writeBytes((firstLine + "\n").getBytes());
                byteBuf.writeBytes(String.format("Real-Ip: %s\n", realIp).getBytes());
            } else {
                return;
            }
            while ((line = br.readLine()) != null) {
                byteBuf.writeBytes((line + "\n").getBytes());
            }
        } catch (IOException ignore) {
        }
    }

}
