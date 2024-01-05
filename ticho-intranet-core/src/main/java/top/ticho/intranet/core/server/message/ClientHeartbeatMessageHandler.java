package top.ticho.intranet.core.server.message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.Message;

import java.nio.charset.StandardCharsets;

/**
 * 服务端心跳处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientHeartbeatMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Channel channel = ctx.channel();
        notify(channel, Message.HEARTBEAT, msg.getSerial(), "心跳检测".getBytes(StandardCharsets.UTF_8));
    }

}
