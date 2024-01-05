package top.ticho.intranet.core.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.client.message.AbstractServerMessageHandler;
import top.ticho.intranet.core.client.register.ServerMessageHandlerRegister;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.prop.ClientProperty;
import top.ticho.intranet.core.util.TichoUtil;


/**
 * 客户端处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
@AllArgsConstructor
public class ServerListenHandler extends SimpleChannelInboundHandler<Message> {

    private final ClientHander clientHander;

    private final AppHandler appHandler;

    private final ClientProperty clientProperty;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        byte type = msg.getType();
        AbstractServerMessageHandler clientHandle = ServerMessageHandlerRegister.getClientHandle(type);
        clientHandle.setClientHander(clientHander);
        clientHandle.setAppHandler(appHandler);
        clientHandle.setClientProperty(clientProperty);
        clientHandle.channelRead0(ctx, msg);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel clientChannel = ctx.channel();
        Channel requestCHannel = clientChannel.attr(CommConst.CHANNEL).get();
        if (null != requestCHannel) {
            requestCHannel.config().setOption(ChannelOption.AUTO_READ, clientChannel.isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel clientChannel = ctx.channel();
        if (clientHander.getAuthServerChannel() == clientChannel) {
            clientHander.setAuthServerChannel(null);
            appHandler.clearRequestChannels();
            clientHander.restart();
        } else {
            Channel requestCHannel = clientChannel.attr(CommConst.CHANNEL).get();
            TichoUtil.close(requestCHannel);
        }
        clientHander.removeReadyServerChannel(clientChannel);
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常 {} {}", ctx.channel(), cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

}
