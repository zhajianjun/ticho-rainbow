package top.ticho.intranet.core.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.prop.ClientProperty;

import java.util.Optional;


/**
 * app连接后处理
 * 1.激活连接
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
@AllArgsConstructor
public class AppConnectAfterHander implements ChannelFutureListener {

    private final ClientHander clientHander;

    private final AppHandler appHandler;

    private final ClientProperty clientProperty;

    private final Channel serverChannel;

    private final String requestId;

    @Override
    public void operationComplete(ChannelFuture channelFuture) {
        if (!channelFuture.isSuccess()) {
            Message msg = new Message();
            msg.setType(Message.DISCONNECT);
            msg.setUri(requestId);
            this.serverChannel.writeAndFlush(msg);
            return;
        }
        // 访问的客户端通道
        Channel requestChannel = channelFuture.channel();
        requestChannel.config().setOption(ChannelOption.AUTO_READ, false);
        Channel readyServerChannel = clientHander.getReadyServerChannel();
        if (readyServerChannel == null) {
            String host = clientProperty.getServerHost();
            int port = Optional.ofNullable(clientProperty.getServerPort()).orElse(CommConst.SERVER_PORT_DEFAULT);
            ServerConnectAfterHander listener = new ServerConnectAfterHander(appHandler, clientProperty, serverChannel, requestChannel, requestId);
            clientHander.connect(host, port, listener);
            return;
        }

        readyServerChannel.attr(CommConst.CHANNEL).set(requestChannel);
        requestChannel.attr(CommConst.CHANNEL).set(readyServerChannel);
        Message msg = new Message();
        msg.setType(Message.CONNECT);
        // requestId @ accessKey
        msg.setUri(requestId + "@" + clientProperty.getAccessKey());
        readyServerChannel.writeAndFlush(msg);
        requestChannel.config().setOption(ChannelOption.AUTO_READ, true);
        appHandler.saveRequestChannel(requestId, requestChannel);
        requestChannel.attr(CommConst.URI).set(requestId);
        // log.warn("[5][客户端]连接信息回传服务端，回传通道{}，携带通道{}，消息{}", readyServerChannel, requestChannel, msg);
    }

}
