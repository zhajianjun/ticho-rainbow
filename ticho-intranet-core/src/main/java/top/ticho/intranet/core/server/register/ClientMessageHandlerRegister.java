package top.ticho.intranet.core.server.register;

import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.server.message.AbstractClientMessageHandler;
import top.ticho.intranet.core.server.message.ClientAuthMessageHandler;
import top.ticho.intranet.core.server.message.ClientConnectMessageHandler;
import top.ticho.intranet.core.server.message.ClientDisconnectMessageHandler;
import top.ticho.intranet.core.server.message.ClientHeartbeatMessageHandler;
import top.ticho.intranet.core.server.message.ClientTransferMessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端处理注册器
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class ClientMessageHandlerRegister {

    public static final Map<Byte, AbstractClientMessageHandler> MAP = new HashMap<>();

    static {
        ClientAuthMessageHandler serverAuthHandle = new ClientAuthMessageHandler();
        ClientConnectMessageHandler serverConnectHandle = new ClientConnectMessageHandler();
        ClientDisconnectMessageHandler serverDisconnectHandle = new ClientDisconnectMessageHandler();
        ClientHeartbeatMessageHandler serverHeartbeatHandle = new ClientHeartbeatMessageHandler();
        ClientTransferMessageHandler serverTransferHandle = new ClientTransferMessageHandler();
        // MAP.put(MsgType.AUTH, null);
        MAP.put(Message.AUTH, serverAuthHandle);
        MAP.put(Message.CONNECT, serverConnectHandle);
        MAP.put(Message.DISCONNECT, serverDisconnectHandle);
        MAP.put(Message.TRANSFER, serverTransferHandle);
        MAP.put(Message.HEARTBEAT, serverHeartbeatHandle);
    }

    public static AbstractClientMessageHandler getMessageHandler(byte msgType) {
        return MAP.get(msgType);
    }


}
