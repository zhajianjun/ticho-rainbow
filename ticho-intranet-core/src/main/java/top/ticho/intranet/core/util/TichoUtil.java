package top.ticho.intranet.core.util;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import top.ticho.intranet.core.entity.TichoMsg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.util.Objects;


/**
 * 通用工具
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
public class TichoUtil {
    private TichoUtil() {
    }

    public static void close(Channel channel) {
        if (null == channel) {
            return;
        }
        try {
            channel.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static boolean isActive(Channel channel) {
        if (Objects.isNull(channel)) {
            return false;
        }
        return channel.isActive() || channel.isOpen();
    }

    public static void close(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }
        try {
            close(ctx.channel());
            ctx.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void exec(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
             log.error("执行命令失败：{}, 错误信息：｛｝", cmd, e.getMessage(), e);
        }
    }


    /**
     * 通知
     *
     * @param channel   通道
     * @param msgType   msg类型
     * @param serialNum 串行num
     */
    public static void notify(Channel channel, byte msgType, Long serialNum) {
        if (!isActive(channel)) {
            return;
        }
        TichoMsg msg = new TichoMsg();
        if (null != serialNum) {
            msg.setSerial(serialNum);
        }
        msg.setType(msgType);
        channel.writeAndFlush(msg);
    }

    public static Integer getPortByChannel(Channel channel) {
        if (null == channel) {
            return null;
        }
        InetSocketAddress addr = (InetSocketAddress) channel.localAddress();
        return addr.getPort();
    }

    /**
     * 除
     *
     * @param a a
     * @param b b
     * @return long
     */
    public static long divide(long a, long b) {
        BigDecimal divide = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 0, RoundingMode.UP);
        return divide.longValue();
    }

    /**
     * 转换路径
     *
     * @param str str
     * @return {@link String}
     */
    public static String convertPath(String str) {
        if (Objects.isNull(str)) {
            return null;
        }
        str = str.replace('\\', '/');
        return str;
    }

}
