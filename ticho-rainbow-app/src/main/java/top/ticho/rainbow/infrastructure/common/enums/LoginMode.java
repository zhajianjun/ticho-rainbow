package top.ticho.rainbow.infrastructure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-06-21 12:23
 */
@Getter
@AllArgsConstructor
public enum LoginMode {
    NONE("1", "无"),
    IMAGE_CODE("2", "验证码");

    private final String code;
    private final String message;

}
