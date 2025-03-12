package top.ticho.rainbow.infrastructure.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用状态
 * <p>通用状态;1-正常,0-停用</p>
 *
 * @author zhajianjun
 * @date 2024-05-13 18:30
 */
public enum CommonStatus {

    /** 正常 */
    NORMAL(1, "正常"),

    /** 停用 */
    NOT_ACTIVE(0, "停用"),

    ;

    private final int code;    private final String msg;
    CommonStatus(int code, String msg) {
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
        map = Arrays.stream(values()).collect(Collectors.toMap(CommonStatus::code, CommonStatus::message));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

}
