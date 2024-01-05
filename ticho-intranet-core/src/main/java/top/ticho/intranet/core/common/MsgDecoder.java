package top.ticho.intranet.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import top.ticho.intranet.core.constant.CommConst;
import top.ticho.intranet.core.entity.Message;


/**
 * 消息解码器
 * <p>
 * 将接收到的字节数据按照特定的协议进行解码，生成对应的消息对象
 * 该类是Netty提供的一个解码器，用于处理基于长度字段的消息帧。通过指定长度字段的偏移量、长度、调整值和需要跳过的字节数，可以将接收到的字节数据按照指定的长度字段进行解码。
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class MsgDecoder extends LengthFieldBasedFrameDecoder {

    public MsgDecoder(int maxFrameLen, int lenFieldOffset, int lenFieldLen, int lenAdjustment, int initBytesToStrip) {
        super(maxFrameLen, lenFieldOffset, lenFieldLen, lenAdjustment, initBytesToStrip);
    }

    /**
     * 解码方法
     *
     * @param ctx ChannelHandlerContext对象
     * @param bin ByteBuf对象，待解码的数据
     * @return 解码后的TichoMsg对象
     * @throws Exception 解码过程中可能抛出的异常
     */
    @Override
    protected Message decode(ChannelHandlerContext ctx, ByteBuf bin) throws Exception {
        // 调用父类的解码方法，获取解码后的ByteBuf对象
        ByteBuf in = (ByteBuf) super.decode(ctx, bin);
        if (null == in) {
            return null;
        }
        // 如果可读字节数小于消息头部固定长度，则返回null
        if (in.readableBytes() < CommConst.HEADER_SIZE) {
            return null;
        }
        // 读取消息体的长度
        int frameLen = in.readInt();
        // 如果可读字节数小于消息体长度，则返回null
        if (in.readableBytes() < frameLen) {
            return null;
        }
        // 创建Message对象
        Message message = new Message();
        // 读取消息类型
        message.setType(in.readByte());
        // 读取消息序列号
        message.setSerial(in.readLong());
        // 读取URI长度
        byte uriLen = in.readByte();
        // 读取URI字节数组
        byte[] uriBytes = new byte[uriLen];
        in.readBytes(uriBytes);
        // 将URI字节数组转换为字符串
        message.setUri(new String(uriBytes));
        // 计算数据部分的长度
        int dataLen = frameLen - CommConst.TYPE_SIZE - CommConst.SERIAL_NUM_SIZE - CommConst.URI_LEN_SIZE - uriLen;
        // 读取数据部分字节数组
        byte[] data = new byte[dataLen];
        in.readBytes(data);
        // 设置数据部分
        message.setData(data);
        // 释放ByteBuf对象
        in.release();
        // 返回解码后的TichoMsg对象
        return message;
    }

}
