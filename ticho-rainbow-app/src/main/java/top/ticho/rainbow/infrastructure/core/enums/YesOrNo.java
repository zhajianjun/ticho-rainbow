package top.ticho.rainbow.infrastructure.core.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2025-03-02 19:54
 */
@AllArgsConstructor
public enum YesOrNo {
    /** 是 */
    YES(1, "是"),
    /** 否 */
    NO(0, "否"),
    ;

    private final int code;
    private final String msg;


    public int code() {
        return code;
    }

    public String message() {
        return msg;
    }

    private static final Map<Integer, String> map;

    static {
        map = Arrays.stream(values()).collect(Collectors.toMap(YesOrNo::code, YesOrNo::message));
    }

}
