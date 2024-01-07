package top.ticho.intranet.core.server.message;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.util.TichoUtil;

/**
 * 客户端断开连接消息处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class ClientDisconnectMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Channel channel = ctx.channel();
        String requestId = msg.getUri();
        String accessKey = channel.attr(CommConst.KEY).get();
        Channel requestChannel;
        if (StrUtil.isEmpty(accessKey)) {
            requestChannel = serverHandler.removeRequestChannel(channel, requestId);
            if (TichoUtil.isActive(requestChannel)) {
                requestChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
            return;
        }
        requestChannel = serverHandler.removeRequestChannel(accessKey, requestId);
        if (!TichoUtil.isActive(requestChannel)) {
            return;
        }
        requestChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        TichoUtil.close(channel.attr(CommConst.CHANNEL).get());
        channel.attr(CommConst.URI).set(null);
        channel.attr(CommConst.KEY).set(null);
        channel.attr(CommConst.CHANNEL).set(null);
    }

}
