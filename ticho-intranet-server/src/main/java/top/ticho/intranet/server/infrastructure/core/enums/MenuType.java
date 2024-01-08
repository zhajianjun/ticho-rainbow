package top.ticho.intranet.server.infrastructure.core.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单类型
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public enum MenuType {

    /** 目录 */
    DIR(1, "目录"),

    /** 菜单 */
    MENU(2, "菜单"),

    /** 按钮 */
    BUTTON(3, "按钮"),
    ;

    private final int code;
    private final String message;

    MenuType(int code, String message) {
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
        map = Arrays.stream(values()).collect(Collectors.toMap(MenuType::code, MenuType::message));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

    public static List<Integer> dirOrMenus() {
        return Stream.of(DIR, MENU).map(MenuType::code).collect(Collectors.toList());
    }

}
