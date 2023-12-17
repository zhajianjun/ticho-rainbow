package top.ticho.intranet.core.server;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.prop.ServerProperty;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.entity.PortInfo;
import top.ticho.intranet.core.server.handler.AppHandler;
import top.ticho.intranet.core.server.handler.ServerHandler;
import top.ticho.intranet.core.util.TichoUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * netty协调处理器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class ServerCoordinateHandler {

    private final ServerHandler serverHandler;

    private final AppHandler appHandler;

    private final ServerProperty serverProperty;

    public ServerCoordinateHandler(ServerProperty serverProperty) {
        NioEventLoopGroup serverBoss = new NioEventLoopGroup();
        NioEventLoopGroup serverWorker = new NioEventLoopGroup();
        this.serverProperty = serverProperty;
        this.serverHandler = new ServerHandler(serverProperty, serverBoss, serverWorker);
        this.appHandler = new AppHandler(serverProperty, serverHandler, serverBoss, serverWorker);
    }

    public void saveClient(ClientInfo clientInfo) {
        if (null == clientInfo || StrUtil.isBlank(clientInfo.getAccessKey())) {
            return;
        }
        ClientInfo clientInfoGet = serverHandler.getClientByAccessKey(clientInfo.getAccessKey());
        if (null == clientInfoGet) {
            serverHandler.addClient(clientInfo.getAccessKey(), clientInfo);
            return;
        }
        clientInfo.setChannel(clientInfoGet.getChannel());
    }

    public void saveClientBatch(List<ClientInfo> clientInfos) {
        if (CollUtil.isEmpty(clientInfos)) {
            return;
        }
        clientInfos.forEach(this::saveClient);
    }

    public void deleteClient(ClientInfo clientInfo) {
        if (null == clientInfo) {
            return;
        }
        String accessKey = clientInfo.getAccessKey();
        ClientInfo clientInfoGet = serverHandler.getClientByAccessKey(accessKey);
        if (Objects.isNull(clientInfoGet)) {
            return;
        }
        Map<Integer, PortInfo> portMap = clientInfoGet.getPortMap();
        if (MapUtil.isNotEmpty(portMap)) {
            for (PortInfo port : portMap.values()) {
                appHandler.deleteApp(port.getPort());
            }
            portMap.clear();
        }
        serverHandler.closeClentAndRequestChannel(clientInfoGet);
        TichoUtil.close(clientInfoGet.getChannel());
        serverHandler.deleteClient(accessKey);
    }


    public void savePort(PortInfo portInfo) {
        if (null == portInfo) {
            return;
        }
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(portInfo.getAccessKey());
        if (null == clientInfo) {
            return;
        }
        Map<Integer, PortInfo> portMap = clientInfo.getPortMap();
        if (null == portMap) {
            portMap = new HashMap<>();
            clientInfo.setPortMap(portMap);
        }
        portMap.put(portInfo.getPort(), portInfo);
        appHandler.createApp(portInfo.getPort());
    }

    public void deletePort(PortInfo port) {
        if (null == port) {
            return;
        }
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(port.getAccessKey());
        if (null == clientInfo || MapUtil.isEmpty(clientInfo.getPortMap())) {
            return;
        }
        Integer portNum = port.getPort();
        if (clientInfo.getPortMap().containsKey(portNum)) {
            port = clientInfo.getPortMap().get(portNum);
            clientInfo.getPortMap().remove(portNum);
            appHandler.deleteApp(port.getPort());
        }
    }

    public void initAllApp() {
        // @formatter:off
        Collection<ClientInfo> clientInfos = serverHandler.getClientMap().values();
        if (CollUtil.isEmpty(clientInfos)) {
            return;
        }
        Long maxBindPorts = serverProperty.getMaxBindPorts();
        AtomicLong finalTotal = new AtomicLong(maxBindPorts);
        clientInfos
            .stream()
            .map(ClientInfo::getPortMap)
            .map(Map::values)
            .flatMap(Collection::stream)
            .map(PortInfo::getPort)
            .sorted()
            .forEach(port-> {
                if (finalTotal.get() <= 0L) {
                    return;
                }
                finalTotal.decrementAndGet();
                appHandler.createApp(port);
                log.info("主机端口:{}绑定成功", port);
            });
        // @formatter:on
    }

}
