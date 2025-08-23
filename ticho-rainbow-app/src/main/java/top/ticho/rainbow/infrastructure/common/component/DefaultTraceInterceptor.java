package top.ticho.rainbow.infrastructure.common.component;

import lombok.NonNull;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.tool.core.TiStrUtil;
import top.ticho.trace.common.TiTraceContext;
import top.ticho.trace.common.TiTraceProperty;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 *
 * @author zhajianjun
 * @date 2025-08-23 19:35
 */
public record DefaultTraceInterceptor(TiTraceProperty tiTraceProperty) implements HandlerInterceptor, Ordered {

    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String trace = TiTraceContext.getTrace();
        String currentUsername = UserUtil.getCurrentUsername();
        if (TiStrUtil.isBlank(currentUsername)) {
            return true;
        }
        String usernameKey = "username";
        trace = trace + ".[${username!}]";
        TiTraceContext.renderTrace(trace, usernameKey, currentUsername);
        return true;
    }

    @Override
    public int getOrder() {
        return tiTraceProperty.getOrder() + 1;
    }

}
