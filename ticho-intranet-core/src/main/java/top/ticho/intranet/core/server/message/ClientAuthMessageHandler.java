package top.ticho.intranet.core.server.message;

import cn.hutool.core.map.MapUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.TichoMsg;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.entity.PortInfo;
import top.ticho.intranet.core.util.TichoUtil;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 服务端验证客户端权限 处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ClientAuthMessageHandler extends AbstractClientMessageHandler {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TichoMsg msg) {
        // @formatter:off
        Channel clientChannel = ctx.channel();
        String accessKey = msg.getUri();
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(accessKey);
        if (Objects.isNull(clientInfo)) {
            log.info("秘钥={}的客户端，服务端不存在", accessKey);
            TichoUtil.notify(clientChannel, (byte) 16, msg.getSerial());
            return;
        }
        if (!Objects.equals(clientInfo.getEnabled(), 1)) {
            log.info("秘钥={}的客户端为关闭状态", accessKey);
            TichoUtil.notify(clientChannel, TichoMsg.DISABLED_ACCESS_KEY, msg.getSerial());
            return;
        }
        Map<Integer, PortInfo> portMap = clientInfo.getPortMap();
        if (MapUtil.isEmpty(portMap)) {
            log.info("秘钥={}的客户端未绑定主机端口,客户端通道{}", accessKey, clientChannel);
            TichoUtil.notify(clientChannel, TichoMsg.NO_AVAILABLE_PORT, msg.getSerial());
            return;
        }
        // 旧的客户端被新的替换掉
        Channel channel = clientInfo.getChannel();
        if (null != channel) {
            log.info("秘钥={}的客户端已经被其他客户端{}使用", accessKey, channel);
            TichoUtil.notify(channel, TichoMsg.IS_INUSE_KEY, msg.getSerial());
            serverHandler.closeClentAndRequestChannel(clientInfo);
        }
        String portStrs = portMap.keySet()
            .stream()
            .map(Objects::toString)
            .collect(Collectors.joining(","));
        log.warn("[2]秘钥={}客户端成功连接，绑定端口{},客户端通道{}", accessKey, portStrs, clientChannel);
        clientChannel.attr(CommConst.REQUEST_ID_ATTR_MAP).set(new LinkedHashMap<>());
        clientInfo.setChannel(clientChannel);
        clientInfo.setConnectTime(LocalDateTime.now());
    }

}
