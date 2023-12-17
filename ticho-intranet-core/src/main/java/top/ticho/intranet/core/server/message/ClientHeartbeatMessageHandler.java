package top.ticho.intranet.core.server.message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.TichoMsg;
import top.ticho.intranet.core.util.TichoUtil;

/**
 * 服务端心跳处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientHeartbeatMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        Channel channel = ctx.channel();
        TichoUtil.notify(channel, TichoMsg.HEARTBEAT, msg.getSerial());
        // log.debug("心跳响应，响应信息{}", channel);
    }

}
