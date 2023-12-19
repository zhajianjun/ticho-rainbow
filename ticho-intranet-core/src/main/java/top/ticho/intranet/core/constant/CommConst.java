package top.ticho.intranet.core.constant;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Map;


/**
 * 常量
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public class CommConst {
    private CommConst() {
    }

    public static final byte HEADER_SIZE = 4;

    public static final int TYPE_SIZE = 1;

    public static final int SERIAL_NUM_SIZE = 8;

    public static final int URI_LEN_SIZE = 1;

    /* Max packet size is 2M */
    public static final int MAX_FRAME_LEN = 2 * 1024 * 1024;

    public static final int FIELD_OFFSET = 0;

    public static final int FIELD_LEN = 4;

    public static final int INIT_BYTES_TO_STRIP = 0;

    public static final int ADJUSTMENT = 0;

    public static final int READ_IDLE_TIME = 60;

    public static final int WRITE_IDLE_TIME = 40;

    public static final int SERVER_PORT_DEFAULT = 5121;

    public static final long ONE_SECOND = 1000L;

    public static final long ONE_MINUTE = 60 * ONE_SECOND;

    public static final String TLS = "TLS";

    public static final String JKS = "JKS";

    public static final String ACCESS_KEY = "ACCESS_KEY";

    public static final String SERVER_HOST = "SERVER_HOST";

    public static final String SERVER_PORT = "SERVER_PORT";

    public static final String MAX_POOL_SIZE = "MAX_POOL_SIZE";

    public static final String SSL_ENABLE = "SSL_ENABLE";

    public static final String SSL_PATH = "SSL_PATH";

    public static final String SSL_PASSWORD = "SSL_PASSWORD";

    /** 请求id key */
    public static final String REQUEST_ID_MAP_KEY = "REQUEST_ID";
    /** 请求id attr key */
    public static final AttributeKey<Map<String, Channel>> REQUEST_ID_ATTR_MAP = AttributeKey.newInstance(REQUEST_ID_MAP_KEY);

    public static final AttributeKey<Channel> CHANNEL = AttributeKey.newInstance("CHANNEL");

    public static final AttributeKey<String> URI = AttributeKey.newInstance("URI");

    public static final AttributeKey<String> KEY = AttributeKey.newInstance("KEY");

    /** 本地地址 */
    public static final String LOCALHOST = "0.0.0.0";

    /** 系统类型 */
    public static final String OS_TYPE = System.getProperty("os.name").toLowerCase();
    /** ssl */
    public static final String SSL = "ssl";

    public static final int MAX_PORT = 65535;

    public static final long ONE_KB = 1024L;

}
