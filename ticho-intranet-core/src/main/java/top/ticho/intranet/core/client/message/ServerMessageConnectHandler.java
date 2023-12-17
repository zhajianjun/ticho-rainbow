package top.ticho.intranet.core.client.message;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.client.handler.AppConnectAfterHander;
import top.ticho.intranet.core.entity.TichoMsg;

import java.nio.charset.StandardCharsets;

/**
 * 客户端通道连接处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerMessageConnectHandler extends AbstractServerMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        Channel serverChannel = ctx.channel();
        String data = StrUtil.str(msg.getData(), StandardCharsets.UTF_8);
        String requestId = msg.getUri();
        String host = StrUtil.subBefore(data, ":", false);
        String port = StrUtil.subAfter(data, ":", false);
        if (StrUtil.isBlank(host)) {
            log.error("地址不能为空 {}", host);
            return;
        }
        if (StrUtil.isBlank(port) || !StrUtil.isNumeric(port)) {
            log.error("端口不能为空 {}", port);
            return;
        }
        int portNum = Integer.parseInt(port);
        AppConnectAfterHander listener = new AppConnectAfterHander(clientHander, appHandler, clientProperty, serverChannel, requestId);
        log.debug("[客户端]连接{}:{}", host, port);
        log.warn("[4][客户端]接收连接信息, 连接通道{}, 消息{}", serverChannel, msg);
        appHandler.connect(host, portNum, listener);
    }

}
