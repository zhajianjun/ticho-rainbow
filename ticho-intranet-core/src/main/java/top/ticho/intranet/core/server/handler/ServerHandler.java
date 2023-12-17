package top.ticho.intranet.core.server.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.common.IdleChecker;
import top.ticho.intranet.core.common.MsgDecoder;
import top.ticho.intranet.core.common.MsgEncoder;
import top.ticho.intranet.core.common.SslHandler;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.prop.ServerProperty;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.util.TichoUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 服务处理
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Getter
@Slf4j
public class ServerHandler {

    /** 客户端与服务端的通道 */
    private final Map<String, ClientInfo> clientMap = new ConcurrentHashMap<>();

    public ServerHandler(ServerProperty serverProperty, NioEventLoopGroup serverBoss, NioEventLoopGroup serverWorker) {
        try {
            int servPort = serverProperty.getPort();
            String host = CommConst.LOCALHOST;
            // 创建netty服务端
            ServerBootstrap server = this.newServer(false, serverProperty, serverWorker, serverBoss);
            server.bind(host, servPort).get();
            log.info("netty服务端启动，地址：[{}:{}]", host, servPort);
            if (Boolean.TRUE.equals(serverProperty.getSslEnable())) {
                // 创建ssl服务端
                ServerBootstrap sslServer = this.newServer(true, serverProperty, serverWorker, serverBoss);
                Integer sslServerPort = serverProperty.getSslPort();
                ChannelFuture cf = sslServer.bind(host, sslServerPort);
                cf.sync();
                log.info("netty ssl服务端启动，地址：[{}:{}]", host, sslServerPort);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("intranet服务启动异常，{}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }

    }

    public void addClient(String accessKey, ClientInfo clientInfo) {
        if (StrUtil.isBlank(accessKey) || Objects.isNull(clientInfo)) {
            return;
        }
        clientMap.put(accessKey, clientInfo);
    }

    public void deleteClient(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return;
        }
        clientMap.remove(accessKey);
    }

    public ClientInfo getClientByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return null;
        }
        return clientMap.get(accessKey);
    }

    public ClientInfo getClientByPort(Integer port) {
        // @formatter:off
        return clientMap.values()
            .stream()
            .filter(Objects::nonNull)
            .filter(x-> x.getPortMap().containsKey(port))
            .findFirst()
            .orElse(null);
        // @formatter:off
    }

    public Channel getClientChannelByPort(Integer port) {
        // @formatter:off
        return Optional
            .ofNullable(getClientByPort(port))
            .map(ClientInfo::getChannel)
            .orElse(null);
        // @formatter:on
    }

    public Channel getRequestChannel(Channel channel, String requestId) {
        if (null == channel || StrUtil.isBlank(requestId)) {
            return null;
        }
        Map<String, Channel> requestChannelMap = channel.attr(CommConst.REQUEST_ID_ATTR_MAP).get();
        if (MapUtil.isEmpty(requestChannelMap) && !requestChannelMap.containsKey(requestId)) {
            return null;
        }
        return requestChannelMap.get(requestId);
    }

    public Channel removeRequestChannel(String key, String requestId) {
        ClientInfo hc = clientMap.get(key);
        return removeRequestChannel(hc.getChannel(), requestId);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public Channel removeRequestChannel(Channel channel, String requestId) {
        if (null == channel) {
            return null;
        }
        Map<String, Channel> requestChannelMap = channel.attr(CommConst.REQUEST_ID_ATTR_MAP).get();
        if (MapUtil.isNotEmpty(requestChannelMap) && requestChannelMap.containsKey(requestId)) {
            synchronized (channel) {
                return requestChannelMap.remove(requestId);
            }
        }
        return null;
    }

    public void closeClentAndRequestChannel(ClientInfo clientInfo) {
        if (Objects.isNull(clientInfo)) {
            return;
        }
        Channel channel = clientInfo.getChannel();
        if (Objects.isNull(channel)) {
            return;
        }
        Map<String, Channel> requestChannelMap = channel.attr(CommConst.REQUEST_ID_ATTR_MAP).get();
        if (MapUtil.isNotEmpty(requestChannelMap)) {
            requestChannelMap.values().forEach(TichoUtil::close);
            requestChannelMap.clear();
        }
        TichoUtil.close(channel);
        clientInfo.setChannel(null);
    }

    private ServerBootstrap newServer(boolean sslEnabled, ServerProperty serverProperty, NioEventLoopGroup serverBoss, NioEventLoopGroup serverWorker) {
        ServerBootstrap strap = new ServerBootstrap();
        ServerBootstrap group = strap.group(serverBoss, serverWorker);
        ServerBootstrap channel = group.channel(NioServerSocketChannel.class);
        ServerListenHandlerInit childHandler = new ServerListenHandlerInit(sslEnabled, this, serverProperty);
        channel.childHandler(childHandler);
        return strap;
    }

    public static class ServerListenHandlerInit extends ChannelInitializer<SocketChannel> {

        private final boolean sslEnabled;

        private final ServerHandler serverHandler;

        private final ServerProperty serverProperty;

        public ServerListenHandlerInit(boolean sslEnabled, ServerHandler serverHandler, ServerProperty serverProperty) {
            this.sslEnabled = sslEnabled;
            this.serverHandler = serverHandler;
            this.serverProperty = serverProperty;
        }

        protected void initChannel(SocketChannel sc) {
            if (Boolean.TRUE.equals(this.sslEnabled)) {
                SslHandler sslHandler = new SslHandler(serverProperty.getSslPath(), serverProperty.getSslPassword());
                SSLContext sslContext = sslHandler.getSslContext();
                SSLEngine engine = sslContext.createSSLEngine();
                engine.setUseClientMode(false);
                engine.setNeedClientAuth(true);
                sc.pipeline().addLast(CommConst.SSL, new io.netty.handler.ssl.SslHandler(engine));
            }
            sc.pipeline().addLast(new MsgDecoder(CommConst.MAX_FRAME_LEN, CommConst.FIELD_OFFSET, CommConst.FIELD_LEN, CommConst.ADJUSTMENT, CommConst.INIT_BYTES_TO_STRIP));
            sc.pipeline().addLast(new MsgEncoder());
            sc.pipeline().addLast(new IdleChecker(CommConst.READ_IDLE_TIME, CommConst.WRITE_IDLE_TIME, 0));
            sc.pipeline().addLast(new ClientListenHandler(serverHandler));
        }

    }

}
