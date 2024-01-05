package top.ticho.intranet.core.client.message;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.Message;

import java.nio.charset.StandardCharsets;

/**
 * 未知信息处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerMessageUnknownHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        log.debug("接收到未知类型{}的消息,{}", msg.getType(), StrUtil.str(msg.getData(), StandardCharsets.UTF_8));
    }

}
