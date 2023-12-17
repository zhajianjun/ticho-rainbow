package top.ticho.intranet.core.server;

import ch.qos.logback.classic.util.ContextInitializer;
import top.ticho.intranet.core.prop.ServerProperty;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.entity.PortInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端启动测试
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class ServerStartTest {

    public static void main(String[] args) {
        // 测试
        System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "conf/logback.xml");
        ServerProperty serverProperty = new ServerProperty();
        serverProperty.setPort(9120);
        serverProperty.setSslEnable(false);
        ServerCoordinateHandler serverCoordinateHandler = new ServerCoordinateHandler(serverProperty);
        ClientInfo clientInfo = new ClientInfo();
        PortInfo portInfo = new PortInfo();
        Map<Integer, PortInfo> portMap = new HashMap<>();
        portMap.put(80, portInfo);
        clientInfo.setPortMap(portMap);
        clientInfo.setAccessKey("68bfe8f0af124ecfa093350ab8d4b44f");
        clientInfo.setEnabled(1);
        portInfo.setAccessKey("68bfe8f0af124ecfa093350ab8d4b44f");
        portInfo.setPort(80);
        portInfo.setEndpoint("192.168.243.138:80");
        serverCoordinateHandler.saveClient(clientInfo);
        serverCoordinateHandler.initAllApp();
    }

}
