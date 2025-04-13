package top.ticho.rainbow.infrastructure.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用状态
 * <p>通用状态;1-启用,0-停用</p>
 *
 * @author zhajianjun
 * @date 2024-05-13 18:30
 */
public enum CommonStatus {

    /** 启用 */
    ENABLE(1, "启用"),
    /** 停用 */
    DISABLE(0, "停用"),
    ;

    private final int code;
    private final String msg;

    CommonStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    private static final Map<Integer, String> map;

    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(CommonStatus::code, CommonStatus::msg));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

}
