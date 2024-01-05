package top.ticho.intranet.core.client.message;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.util.TichoUtil;

import java.nio.charset.StandardCharsets;

/**
 * 客户端关闭处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerMessageCloseHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message msg) {
        Channel clientChannel = ctx.channel();
        log.warn("客户端{}={}关闭连接, 消息：{}", CommConst.ACCESS_KEY, clientProperty.getAccessKey(), StrUtil.str(msg.getData(), StandardCharsets.UTF_8));
        TichoUtil.close(clientChannel);
        clientHander.stop(msg.getType());
    }

}
