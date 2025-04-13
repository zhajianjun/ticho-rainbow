package top.ticho.rainbow.infrastructure.common.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件状态
 * <p>文件状态;1-正常,2-停用,3-分片上传,99-作废</p>
 *
 * @author zhajianjun
 * @date 2024-04-29 18:19
 */
public enum FileInfoStatus {

    /** 正常 */
    NORMAL(1, "正常"),

    /** 停用 */
    DISABLED(2, "停用"),

    /** 分片上传 */
    CHUNK(3, "分片上传"),

    /** 作废 */
    CANCE(99, "作废"),
    ;

    private final int code;
    private final String message;

    FileInfoStatus(int code, String message) {
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
        map = Arrays.stream(values()).collect(Collectors.toMap(FileInfoStatus::code, FileInfoStatus::message));
    }

    public static Map<Integer, String> get() {
        return map;
    }

    public static String getByCode(Integer code) {
        return map.get(code);
    }

}
