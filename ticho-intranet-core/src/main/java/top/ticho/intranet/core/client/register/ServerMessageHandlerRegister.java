package top.ticho.intranet.core.client.register;

import top.ticho.intranet.core.client.message.AbstractServerMessageHandler;
import top.ticho.intranet.core.client.message.ServerMessageCloseHandler;
import top.ticho.intranet.core.client.message.ServerMessageConnectHandler;
import top.ticho.intranet.core.client.message.ServerMessageDisconnectHandler;
import top.ticho.intranet.core.client.message.ServerMessageTransferHandler;
import top.ticho.intranet.core.client.message.ServerMessageUnknownHandler;
import top.ticho.intranet.core.entity.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 客户端处理器工厂
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class ServerMessageHandlerRegister {

    public static final Map<Byte, AbstractServerMessageHandler> MAP = new HashMap<>();
    public static final AbstractServerMessageHandler UNKNOWN = new ServerMessageUnknownHandler();

    static {
        ServerMessageConnectHandler clientConnectHandle = new ServerMessageConnectHandler();
        ServerMessageDisconnectHandler clientDisconnectHandle = new ServerMessageDisconnectHandler();
        ServerMessageTransferHandler clientTransferHandle = new ServerMessageTransferHandler();
        ServerMessageCloseHandler clientCloseHandle = new ServerMessageCloseHandler();
        // MAP.put(Message.AUTH, null);
        MAP.put(Message.DISABLED_ACCESS_KEY, clientCloseHandle);
        MAP.put(Message.CONNECT, clientConnectHandle);
        MAP.put(Message.DISCONNECT, clientDisconnectHandle);
        MAP.put(Message.TRANSFER, clientTransferHandle);
        // MAP.put(Message.HEARTBEAT, null);

    }

    public static AbstractServerMessageHandler getClientHandle(byte msgType) {
        return Optional.ofNullable(MAP.get(msgType)).orElse(UNKNOWN);
    }


}
