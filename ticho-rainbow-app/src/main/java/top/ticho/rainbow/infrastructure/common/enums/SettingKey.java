package top.ticho.rainbow.infrastructure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-06-22 10:07
 */
@Getter
@AllArgsConstructor
public enum SettingKey {

    LOGIN_MODE("登录模式"),
    INIT_PASSWORD("初始化密码"),
    ;

    private final String message;

    public boolean equals(String name) {
        return this.name().equals(name);
    }

}
