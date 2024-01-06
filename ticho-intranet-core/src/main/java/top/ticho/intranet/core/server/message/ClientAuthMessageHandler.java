package top.ticho.intranet.core.server.message;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.entity.PortInfo;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户端权限消息处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientAuthMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message message) {
        // @formatter:off
        Channel clientChannel = ctx.channel();
        String accessKey = message.getUri();
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(accessKey);
        if (Objects.isNull(clientInfo)) {
            String errorMsg = StrUtil.format("秘钥={}的客户端不可用", accessKey);
            notifyError(clientChannel, errorMsg, message.getSerial());
            return;
        }
        Map<Integer, PortInfo> portMap = clientInfo.getPortMap();
        if (MapUtil.isEmpty(portMap)) {
            String errorMsg = StrUtil.format("秘钥={}的客户端未绑定主机端口,客户端通道{}", accessKey, clientChannel);
            notifyError(clientChannel, errorMsg, message.getSerial());
            return;
        }
        // 旧的客户端被新的替换掉
        Channel channel = clientInfo.getChannel();
        if (null != channel) {
            String errorMsg = StrUtil.format("秘钥={}的客户端已经被其他客户端{}使用", accessKey, channel);
            notifyError(clientChannel, errorMsg, message.getSerial());
            serverHandler.closeClentAndRequestChannel(clientInfo);
        }
        String portStrs = portMap.keySet()
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining(","));
        log.warn("[2]秘钥={}客户端成功连接，绑定端口{},客户端通道{}", accessKey, portStrs, clientChannel);
        clientChannel.attr(CommConst.REQUEST_ID_ATTR_MAP).set(new LinkedHashMap<>());
        clientInfo.setConnectTime(LocalDateTime.now());
        clientInfo.setChannel(clientChannel);
        // @formatter:on
    }

    private void notifyError(Channel channel, String errorMsg, long serial) {
        log.info(errorMsg);
        notify(channel, Message.DISABLED_ACCESS_KEY, serial, errorMsg.getBytes(StandardCharsets.UTF_8));
    }

}
