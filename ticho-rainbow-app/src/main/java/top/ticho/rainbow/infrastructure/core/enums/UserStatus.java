package top.ticho.rainbow.infrastructure.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户状态
 * <p>用户状态;1-正常,2-未激活,3-已锁定,4-已注销</p>
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public enum UserStatus {

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
    private final String msg;

    UserStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String message() {
        return msg;
    }

    private static final Map<Integer, String> map;

    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(UserStatus::code, UserStatus::message));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

}
