package top.ticho.intranet.core.server.message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import top.ticho.intranet.core.entity.Message;
import top.ticho.intranet.core.server.handler.ServerHandler;
import top.ticho.intranet.core.util.TichoUtil;


/**
 * 客户端消息处理器抽象类
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Setter
public abstract class AbstractClientMessageHandler {

    protected ServerHandler serverHandler;

    /**
     * 读取服务端信息进行不同的处理
     *
     * @param ctx 通道处理上线文
     * @param msg 服务端传输的信息
     */
    public abstract void channelRead0(ChannelHandlerContext ctx, Message msg);

    /**
     * 通知
     *
     * @param channel 通道
     * @param msgType msg类型
     * @param serial  序列号
     * @param data    传输数据
     */
    protected void notify(Channel channel, byte msgType, Long serial, byte[] data) {
        if (!TichoUtil.isActive(channel)) {
            return;
        }
        Message msg = new Message();
        if (null != serial) {
            msg.setSerial(serial);
        }
        msg.setType(msgType);
        msg.setData(data);
        channel.writeAndFlush(msg);
    }

}
