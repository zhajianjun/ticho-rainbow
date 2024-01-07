package top.ticho.intranet.core.client.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import top.ticho.intranet.core.client.handler.AppHandler;
import top.ticho.intranet.core.client.handler.ClientHander;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.prop.ClientProperty;


/**
 * 服务端消息处理器抽象类
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Setter
public abstract class AbstractServerMessageHandler {

    protected ClientHander clientHander;

    protected AppHandler appHandler;

    protected ClientProperty clientProperty;

    /**
     * 读取服务端信息进行不同的处理
     *
     * @param ctx 通道处理上线文
     * @param msg 服务端传输的信息
     */
    public abstract void channelRead0(ChannelHandlerContext ctx, Message msg);

}
