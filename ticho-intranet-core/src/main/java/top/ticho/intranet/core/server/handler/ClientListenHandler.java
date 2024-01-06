package top.ticho.intranet.core.server.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.message.AbstractClientMessageHandler;
import top.ticho.intranet.core.server.message.ClientAuthMessageHandler;
import top.ticho.intranet.core.server.message.ClientConnectMessageHandler;
import top.ticho.intranet.core.server.message.ClientDisconnectMessageHandler;
import top.ticho.intranet.core.server.message.ClientHeartbeatMessageHandler;
import top.ticho.intranet.core.server.message.ClientMessageUnknownHandler;
import top.ticho.intranet.core.server.message.ClientTransferMessageHandler;
import top.ticho.intranet.core.util.TichoUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端监听处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientListenHandler extends SimpleChannelInboundHandler<Message> {

    private final ServerHandler serverHandler;

    public final Map<Byte, AbstractClientMessageHandler> MAP = new HashMap<>();

    public final AbstractClientMessageHandler UNKNOWN = new ClientMessageUnknownHandler();

    public ClientListenHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
        ClientAuthMessageHandler serverAuthHandle = new ClientAuthMessageHandler();
        ClientConnectMessageHandler serverConnectHandle = new ClientConnectMessageHandler();
        ClientDisconnectMessageHandler serverDisconnectHandle = new ClientDisconnectMessageHandler();
        ClientHeartbeatMessageHandler serverHeartbeatHandle = new ClientHeartbeatMessageHandler();
        ClientTransferMessageHandler serverTransferHandle = new ClientTransferMessageHandler();
        // MAP.put(MsgType.AUTH, null);
        MAP.put(Message.AUTH, serverAuthHandle);
        MAP.put(Message.CONNECT, serverConnectHandle);
        MAP.put(Message.DISCONNECT, serverDisconnectHandle);
        MAP.put(Message.TRANSFER, serverTransferHandle);
        MAP.put(Message.HEARTBEAT, serverHeartbeatHandle);
        MAP.values().forEach(item -> item.setServerHandler(serverHandler));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常 {} {}", ctx.channel(), cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        AbstractClientMessageHandler serverHandle = MAP.getOrDefault(msg.getType(), UNKNOWN);
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
