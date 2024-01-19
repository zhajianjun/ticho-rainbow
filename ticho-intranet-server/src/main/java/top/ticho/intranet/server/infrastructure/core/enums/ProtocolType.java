package top.ticho.intranet.server.infrastructure.core.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 协议类型
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public enum ProtocolType {

    /**  */
    HTTP(1, "http"),
    HTTPS(2, "https"),
    SSH(3, "ssh"),
    TELNET(4, "telnet"),
    DATABASE(5, "data base"),
    RDESKTOP(6, "remote desktop"),
    TCP(7, "tcp");

    private final int code;
    private final String type;

    ProtocolType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static boolean isValid(String value) {
        ProtocolType[] var1 = values();

        for (ProtocolType type : var1) {
            if (type.type().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public int code() {
        return this.code;
    }

    public String type() {
        return this.type;
    }


    public static ProtocolType getByCode(int code) {
        Optional<ProtocolType> optional = Arrays.stream(values()).filter(x -> code == x.code()).findFirst();
        return optional.orElse(null);
    }


}
