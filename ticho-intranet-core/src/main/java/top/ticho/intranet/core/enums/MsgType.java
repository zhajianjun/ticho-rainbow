package top.ticho.intranet.core.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息类型
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public enum MsgType {

    /** 验证消息以检查accessKey是否正确 */
    AUTH((byte) 1, "验证消息以检查accessKey是否正确"),
    /** 没有可用的端口 */
    NO_AVAILABLE_PORT((byte) 2, "没有可用的端口"),
    /** 客户端链接 */
    CONNECT((byte) 3, "客户端通道连接"),
    /** 客户端断开链接 */
    DISCONNECT((byte) 4, "客户端断开通道连接"),
    /** 数据传输 */
    TRANSFER((byte) 5, "数据传输"),
    /** accessKey已被其它客户端使用 */
    IS_INUSE_KEY((byte) 6, "accessKey已被其它客户端使用"),
    /** 客户端心跳 */
    HEARTBEAT((byte) 7, "客户端心跳"),
    /** 禁用访问密钥 */
    DISABLED_ACCESS_KEY((byte) 8, "禁用访问密钥"),
    /** 已禁用试用客户端 */
    DISABLED_TRIAL_CLIENT((byte) 9, "已禁用试用客户端"),
    /** 无效的访问密钥 */
    INVALID_KEY((byte) 10, "无效的访问密钥"),
    ;

    private final byte code;
    private final String msg;


    MsgType(byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public byte code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    private static final Map<Byte, MsgType> map;


    static {
        // @formatter:off
        map = Arrays.stream(values()).collect(Collectors.toMap(MsgType::code, Function.identity()));
        // @formatter:on
    }

    public static MsgType getMsgType(Number number) {
        Byte i = number.byteValue();
        return map.get(i);
    }

    public static void main(String[] args) {
        for (MsgType value : values()) {
            System.out.println(StrUtil.format("/** {} */\npublic static final byte {} = MsgType.{}.getCode();", value.msg(), value.name(), value.name()));
        }
    }

}
