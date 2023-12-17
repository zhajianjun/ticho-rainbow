package top.ticho.intranet.core.client.message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.TichoMsg;
import top.ticho.intranet.core.enums.MsgType;
import top.ticho.intranet.core.util.TichoUtil;

/**
 * 客户端关闭处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerMessageCloseHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        Channel clientChannel = ctx.channel();
        MsgType msgType = MsgType.getMsgType(msg.getType());
        log.warn("客户端{}={}关闭连接, 消息：{}", CommConst.ACCESS_KEY, clientProperty.getAccessKey(), msgType.msg());
        TichoUtil.close(clientChannel);
        clientHander.stop(msg.getType());
    }
}
