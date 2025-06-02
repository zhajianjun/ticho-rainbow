package top.ticho.rainbow.domain.entity;

import top.ticho.starter.view.exception.TiBizException;

import java.util.Objects;

/**
 * @author zhajianjun
 * @date 2025-05-31 16:33
 */
public interface Entity {

    Long getId();

    Long getVersion();

    default void checkVersion(Long version) {
        checkVersion(version, "版本不一致，数据已被修改");
    }

    default void checkVersion(Long version, String message) {
        if (!isCurrentVersion(version)) {
            throw new TiBizException(message);
        }
    }

    default boolean isCurrentVersion(Long version) {
        return Objects.equals(getVersion(), version);
    }

}
