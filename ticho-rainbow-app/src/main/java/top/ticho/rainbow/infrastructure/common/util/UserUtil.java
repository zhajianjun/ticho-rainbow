package top.ticho.rainbow.infrastructure.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.UserHelper;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.starter.security.util.TiUserUtil;
import top.ticho.trace.core.util.BeetlUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     *
     * @return boolean
     */
    public static boolean isAdmin() {
        SecurityUser currentUser = TiUserUtil.getCurrentUser();
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
        SecurityUser loginUser = TiUserUtil.getCurrentUser();
        return isSelf(userHelper, loginUser);
    }

    /**
     * 查询两个用户是否一致
     *
     * @param userHelper 用户帮助
     * @param loginUser  登录用户
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

    public static void userTrace() {
        userTrace(UserUtil.getCurrentUsername());
    }

    public static void userTrace(String username) {
        if (Objects.isNull(username)) {
            return;
        }
        MDC.put(CommConst.USERNAME_KEY, username);
        String traceKey = Optional.ofNullable(MDC.get("trace")).orElse(StrUtil.EMPTY) + ".${username!}";
        String trace = BeetlUtil.render(traceKey, MDC.getCopyOfContextMap());
        MDC.put("trace", trace);
    }

}
