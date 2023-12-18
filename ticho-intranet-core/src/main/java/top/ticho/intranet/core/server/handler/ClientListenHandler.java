package top.ticho.intranet.core.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.TichoMsg;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.message.AbstractClientMessageHandler;
import top.ticho.intranet.core.server.register.ClientMessageHandlerRegister;
import top.ticho.intranet.core.util.TichoUtil;

import java.util.Objects;

/**
 * 服务端处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientListenHandler extends SimpleChannelInboundHandler<TichoMsg> {

    private final ServerHandler serverHandler;

    public ClientListenHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常 {} {}", ctx.channel(), cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        AbstractClientMessageHandler serverHandle = ClientMessageHandlerRegister.getMessageHandler(msg.getType());
        if (Objects.isNull(serverHandle)) {
            return;
        }
        serverHandle.setServerHandler(serverHandler);
        serverHandle.channelRead0(ctx, msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Channel extraChannel = channel.attr(CommConst.CHANNEL).get();
        if (null != extraChannel) {
            extraChannel.config().setOption(ChannelOption.AUTO_READ, channel.isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Channel extraChannel = channel.attr(CommConst.CHANNEL).get();
        String accessKey = channel.attr(CommConst.KEY).get();
        if (TichoUtil.isActive(extraChannel)) {
            String requestId = channel.attr(CommConst.URI).get();
            // 移除requestId的map信息
            serverHandler.removeRequestChannel(accessKey, requestId);
            extraChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            extraChannel.close();
        } else {
            // 关闭客户端通道、请求通道
            ClientInfo clientInfo = serverHandler.getClientByAccessKey(accessKey);
            serverHandler.closeClentAndRequestChannel(clientInfo);
            TichoUtil.close(channel);
        }
        super.channelInactive(ctx);
    }

}