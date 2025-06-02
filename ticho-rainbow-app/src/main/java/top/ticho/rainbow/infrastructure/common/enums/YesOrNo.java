package top.ticho.rainbow.infrastructure.common.enums;

import lombok.AllArgsConstructor;

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
    private final String message;

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static YesOrNo get(boolean flag) {
        if (flag) {
            return YES;
        }
        return NO;
    }

    public static int getCode(boolean flag) {
        if (flag) {
            return YES.code();
        }
        return NO.code();
    }

}
