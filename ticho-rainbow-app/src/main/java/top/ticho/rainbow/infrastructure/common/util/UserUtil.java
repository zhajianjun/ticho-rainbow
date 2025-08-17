package top.ticho.rainbow.infrastructure.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.ticho.rainbow.domain.entity.UserHelper;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.common.dto.SecurityUser;
import top.ticho.starter.security.util.TiUserUtil;
import top.ticho.tool.core.TiCollUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtil {


    public static SecurityUser getCurrentUser() {
        return TiUserUtil.getCurrentUser();
    }

    public static String getCurrentUsername() {
        return TiUserUtil.getCurrentUsername();
    }


    /**
     * 当前用户是否是管理员
     */
    public static boolean isAdmin() {
        SecurityUser currentUser = TiUserUtil.getCurrentUser();
        return isAdmin(currentUser);
    }

    /**
     * 查询用户是否是管理员
     *
     * @param currentUser 当前用户
     */
    public static boolean isAdmin(SecurityUser currentUser) {
        if (currentUser == null) {
            return false;
        }
        List<String> roleCodes = currentUser.getRoles();
        if (TiCollUtil.isEmpty(roleCodes)) {
            return false;
        }
        return roleCodes.contains(SecurityConst.ADMIN);
    }

    /**
     * 是否是本人
     *
     * @param userHelper 用户帮助
     */
    public static boolean isSelf(UserHelper userHelper) {
        SecurityUser loginUser = TiUserUtil.getCurrentUser();
        return isSelf(userHelper, loginUser);
    }

    /**
     * 查询两个用户是否一致
     *
     * @param userHelper 用户帮助
     * @param loginUser  登录用户
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
