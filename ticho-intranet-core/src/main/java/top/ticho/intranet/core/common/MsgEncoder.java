package top.ticho.intranet.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.TichoMsg;


/**
 * 消息编码器
 * <p>
 * 将TichoMsg对象转换为符合特定协议的字节流，方便在网络中传输和解码。
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class MsgEncoder extends MessageToByteEncoder<TichoMsg> {

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
     * @param ctx ctx
     * @param msg 味精
     * @param out 出
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, TichoMsg msg, ByteBuf out) {
        // 计算消息体的长度
        int bodyLen = CommConst.TYPE_SIZE + CommConst.SERIAL_NUM_SIZE + CommConst.URI_LEN_SIZE;
        byte[] uriBytes = null;
        if (null != msg.getUri()) {
            // 如果URI不为null，则将URI转换为字节数组，并计算URI的长度
            uriBytes = msg.getUri().getBytes();
            bodyLen += uriBytes.length;
        }
        if (null != msg.getData()) {
            // 如果数据不为null，则计算数据的长度
            bodyLen += msg.getData().length;
        }
        // 写入消息体的总长度（不包含长度字段的长度）
        out.writeInt(bodyLen);
        // 写入消息类型
        out.writeByte(msg.getType());
        // 写入消息序列号
        out.writeLong(msg.getSerial());
        if (null != uriBytes) {
            // 如果URI不为null，则写入URI的长度和字节数组
            out.writeByte((byte) uriBytes.length);
            out.writeBytes(uriBytes);
        } else {
            // 如果URI为null，则写入0x00表示长度为0
            out.writeByte((byte) 0x00);
        }
        if (null != msg.getData()) {
            // 如果数据不为null，则写入数据
            out.writeBytes(msg.getData());
        }
    }
}
