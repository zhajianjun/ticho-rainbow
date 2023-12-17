package top.ticho.intranet.core.common;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.TichoMsg;


/**
 * 心跳检测处理
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class IdleChecker extends IdleStateHandler {

    public IdleChecker(int readerIdleTime, int writerIdleTime, int allIdleTime) {
        super(readerIdleTime, writerIdleTime, allIdleTime);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        // 检测到通道空闲事件时执行以下代码
        if (IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT == evt) {
            // log.debug("通道写超时:{}.", ctx.channel());
            // 创建一个心跳消息对象
            TichoMsg msg = new TichoMsg();
            msg.setType(TichoMsg.HEARTBEAT);
            // 向通道写入心跳消息并刷新
            ctx.channel().writeAndFlush(msg);
        } else if (IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT == evt) {
            // log.warn("通道读超时:{}.", ctx.channel());
            // 如果是读取超时，则关闭通道
            ctx.channel().close();
        }
        // 调用父类方法处理通道空闲事件
        super.channelIdle(ctx, evt);
    }
}
