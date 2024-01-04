package top.ticho.intranet.core.server.handler;

import cn.hutool.core.util.StrUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.prop.ServerProperty;
import top.ticho.intranet.core.server.collect.DataCollectHandler;
import top.ticho.intranet.core.server.entity.PortInfo;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class AppHandler {

    private final ServerBootstrap serverBootstrap;

    /** 请求id */
    private final AtomicLong requestId = new AtomicLong(0L);

    /** 与绑定端口的通道 */
    @Getter
    private final Map<Integer, Channel> bindPortChannelMap = new ConcurrentHashMap<>();

    public AppHandler(ServerProperty serverProperty, ServerHandler serverHandler, NioEventLoopGroup serverBoss, NioEventLoopGroup serverWorker) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ServerBootstrap group = serverBootstrap.group(serverBoss, serverWorker);
        ServerBootstrap channel = group.channel(NioServerSocketChannel.class);
        AppListenHandlerInit childHandler = new AppListenHandlerInit(serverProperty, serverHandler, this);
        channel.childHandler(childHandler);
        this.serverBootstrap = serverBootstrap;
    }

    public void createApp(PortInfo portInfo) {
        Integer port;
        if (Objects.isNull(portInfo) || Objects.isNull(port = portInfo.getPort())) {
            return;
        }
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port);
            channelFuture.get();
            bindPortChannelMap.put(port, channelFuture.channel());
        } catch (InterruptedException | ExecutionException e) {
            portInfo.setStatus(0);
            portInfo.setStatusMessage(StrUtil.format("创建失败，{}", e.getMessage()));
            log.warn("创建应用失败，端口：{}，错误信息：{}", port, e.getMessage(), e);
        }
        portInfo.setStatus(1);
        portInfo.setStatusMessage("创建成功");
    }

    public void deleteApp(Integer port) {
        if (null == port) {
            return;
        }
        Channel channel = bindPortChannelMap.get(port);
        if (channel == null) {
            return;
        }
        channel.close();
        bindPortChannelMap.remove(port);
    }

    public String getRequestId() {
        return String.valueOf(requestId.incrementAndGet());
    }

    @AllArgsConstructor
    public static class AppListenHandlerInit extends ChannelInitializer<SocketChannel> {

        private final ServerProperty serverProperty;

        private final ServerHandler serverHandler;

        private final AppHandler appHandler;

        protected void initChannel(SocketChannel socketChannel) {
            socketChannel.pipeline().addFirst(new DataCollectHandler());
            socketChannel.pipeline().addLast(new AppListenHandler(serverProperty, serverHandler, appHandler));
        }

    }

}
