package top.ticho.intranet.core.client.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.TichoMsg;

/**
 * 未知信息处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerMessageUnknownHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        // log.debug("客户端{}={}接收到未知类型的消息,{}", ClientConst.ACCESS_KEY, config.strValue(ClientConst.ACCESS_KEY), msg);
    }

}
