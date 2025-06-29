package top.ticho.rainbow.infrastructure.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 通用静态变量
 *
 * @author zhajianjun
 * @date 2020-08-26 21:59
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommConst {

    public static final long PARENT_ID = 0;
    public static final String PERM_KEY = "perm";
    public static final String FILE_PERM_KEY = "file_perm";
    public static final String GUEST_ROLE_CODE = "guest";
    public static final String USERNAME_KEY = "username";
    public static final String API_KEY = "api";
    // 最大操作数量
    public static final int MAX_OPERATION_COUNT = 100;

}
