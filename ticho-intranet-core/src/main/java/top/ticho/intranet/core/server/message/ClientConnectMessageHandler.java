package top.ticho.intranet.core.server.message;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.TichoMsg;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.util.TichoUtil;

import java.util.Objects;

/**
 * 服务端连接处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientConnectMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        Channel channel = ctx.channel();
        log.warn("[6][服务端]接收客户端连接信息{}, 消息{}", channel, msg);
        String uri = msg.getUri();
        if (StrUtil.isBlank(uri)) {
            log.warn("链接地址为空");
            channel.close();
            return;
        }
        String[] tokens = uri.split("@");
        if (tokens.length != 2) {
            log.warn("链接地址不合法");
            channel.close();
            return;
        }
        String requestId = tokens[0];
        String accessKey = tokens[1];
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(accessKey);
        Channel clientChannel;
        if (Objects.isNull(clientInfo) || Objects.isNull((clientChannel = clientInfo.getChannel()))) {
            log.warn("该秘钥没有可用通道{}", accessKey);
            channel.close();
            return;
        }
        Channel requestChannel = serverHandler.getRequestChannel(clientChannel, requestId);
        if (!TichoUtil.isActive(requestChannel)) {
            return;
        }
        channel.attr(CommConst.URI).set(requestId);
        channel.attr(CommConst.KEY).set(accessKey);
        channel.attr(CommConst.CHANNEL).set(requestChannel);
        requestChannel.attr(CommConst.CHANNEL).set(channel);
        requestChannel.config().setOption(ChannelOption.AUTO_READ, true);
    }
}
