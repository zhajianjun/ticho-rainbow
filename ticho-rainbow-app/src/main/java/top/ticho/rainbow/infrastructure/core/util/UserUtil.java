package top.ticho.rainbow.infrastructure.core.util;

import cn.hutool.core.collection.CollUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.ticho.boot.security.util.BaseUserUtil;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.rainbow.interfaces.dto.UserHelper;

import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtil {


    public static SecurityUser getCurrentUser() {
        return BaseUserUtil.getCurrentUser();
    }

    public static String getCurrentUsername() {
        return BaseUserUtil.getCurrentUsername();
    }


    /**
     * 当前用户是否是管理员
     *
     * @return boolean
     */
    public static boolean isAdmin() {
        SecurityUser currentUser = BaseUserUtil.getCurrentUser();
        return isAdmin(currentUser);
    }

    /**
     * 查询用户是否是管理员
     *
     * @param currentUser 当前用户
     * @return boolean
     */
    public static boolean isAdmin(SecurityUser currentUser) {
        if (currentUser == null) {
            return false;
        }
        List<String> roleCodes = currentUser.getRoles();
        if (CollUtil.isEmpty(roleCodes)) {
            return false;
        }
        return roleCodes.contains(SecurityConst.ADMIN);
    }

    /**
     * 是否是本人
     *
     * @param userHelper 用户帮助
     * @return boolean
     */
    public static boolean isSelf(UserHelper userHelper) {
        SecurityUser loginUser = BaseUserUtil.getCurrentUser();
        return isSelf(userHelper, loginUser);
    }

    /**
     * 查询两个用户是否一致
     *
     * @param userHelper 用户帮助
     * @param loginUser 登录用户
     * @return boolean
     */
    public static boolean isSelf(UserHelper userHelper, UserHelper loginUser) {
        if (userHelper == null || loginUser == null) {
            return false;
        }
        String username = userHelper.getUsername();
        String loginUsername = loginUser.getUsername();
        return Objects.equals(username, loginUsername);
    }

}
