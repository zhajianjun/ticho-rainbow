package top.ticho.intranet.core.server.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.util.TichoUtil;

/**
 * 客户端信息传输消息处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientTransferMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Channel channel = ctx.channel();
        Channel requestChannel = channel.attr(CommConst.CHANNEL).get();
        if (!TichoUtil.isActive(requestChannel)) {
            return;
        }
        ByteBuf data = ctx.alloc().buffer(msg.getData().length);
        data.writeBytes(msg.getData());
        requestChannel.writeAndFlush(data);
        log.warn("[10][服务端]响应信息接收，接收通道：{}；写入通道：{}, 消息{}", channel, requestChannel, msg);
    }

}
