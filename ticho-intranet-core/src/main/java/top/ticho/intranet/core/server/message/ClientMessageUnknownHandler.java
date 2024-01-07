package top.ticho.intranet.core.server.message;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.Message;

import java.nio.charset.StandardCharsets;

/**
 * 客户端未知消息处理器
 *
 * @author zhajianjun
 * @date 2024-01-06 14:56
 */
@Slf4j
public class ClientMessageUnknownHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        log.debug("接收到未知类型{}的消息,{}", msg.getType(), StrUtil.str(msg.getData(), StandardCharsets.UTF_8));
    }

}
