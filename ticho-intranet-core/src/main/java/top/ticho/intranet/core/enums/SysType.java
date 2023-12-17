package top.ticho.intranet.core.enums;

/**
 * 系统类型
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
public enum SysType {

    /**  */
    WIN("win"),
    LINUX("linux");

    private final String code;

    SysType(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

}
