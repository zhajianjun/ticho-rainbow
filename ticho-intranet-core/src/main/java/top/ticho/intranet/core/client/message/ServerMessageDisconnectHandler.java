package top.ticho.intranet.core.client.message;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;

/**
 * 客户端断开通道连接处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class ServerMessageDisconnectHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Channel clientChannel = ctx.channel();
        Channel requestCHannel = clientChannel.attr(CommConst.CHANNEL).get();
        if (null == requestCHannel) {
            return;
        }
        clientChannel.attr(CommConst.CHANNEL).set(null);
        clientHander.saveReadyServerChannel(clientChannel);
        requestCHannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

}
