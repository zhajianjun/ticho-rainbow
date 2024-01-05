package top.ticho.intranet.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;


/**
 * 消息编码器
 * <p>
 * 将TichoMsg对象转换为符合特定协议的字节流，方便在网络中传输和解码。
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class MsgEncoder extends MessageToByteEncoder<Message> {

    /**
     * 首先计算消息体的长度，包括消息类型、序列号和URI的长度。
     * 如果URI字段不为null，则将URI转换为字节数组，并计算URI的长度。
     * 如果数据字段不为null，则计算数据的长度。
     * 接下来，在ByteBuf中写入消息体的总长度（不包含长度字段的长度）。
     * 然后，写入消息类型、序列号和URI的长度。
     * 如果URI字段不为null，则写入URI的长度和字节数组。
     * 如果URI字段为null，则写入0x00表示URI长度为0。
     * 最后，如果数据字段不为null，则写入数据。
     *
     * @param ctx     ctx
     * @param message 消息
     * @param byteBuf byteBuf
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf byteBuf) {
        // 计算消息体的长度
        int bodyLength = CommConst.TYPE_SIZE + CommConst.SERIAL_NUM_SIZE + CommConst.URI_LEN_SIZE;
        byte[] uriBytes = null;
        if (null != message.getUri()) {
            // 如果URI不为null，则将URI转换为字节数组，并计算URI的长度
            uriBytes = message.getUri().getBytes();
            bodyLength += uriBytes.length;
        }
        if (null != message.getData()) {
            // 如果数据不为null，则计算数据的长度
            bodyLength += message.getData().length;
        }
        // 写入消息体的总长度（不包含长度字段的长度）
        byteBuf.writeInt(bodyLength);
        // 写入消息类型
        byteBuf.writeByte(message.getType());
        // 写入消息序列号
        byteBuf.writeLong(message.getSerial());
        if (null != uriBytes) {
            // 如果URI不为null，则写入URI的长度和字节数组
            byteBuf.writeByte((byte) uriBytes.length);
            byteBuf.writeBytes(uriBytes);
        } else {
            // 如果URI为null，则写入0x00表示长度为0
            byteBuf.writeByte((byte) 0x00);
        }
        if (null != message.getData()) {
            // 如果数据不为null，则写入数据
            byteBuf.writeBytes(message.getData());
        }
    }

}
