package top.ticho.rainbow.infrastructure.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 租户状态
 * <p>租户状态;1-正常,2-未激活,3-已锁定,4-已注销</p>
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public enum TenantStatus {

    /** 正常 */
    NORMAL(1, "正常"),

    /** 未激活 */
    NOT_ACTIVE(2, "未激活"),

    /** 锁定 */
    LOCKED(3, "已锁定"),

    /** 注销 */
    LOG_OUT(4, "已注销"),
    ;

    private final int code;
    private final String message;

    TenantStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    private static final Map<Integer, String> map;

    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(TenantStatus::code, TenantStatus::message));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

}
