package top.ticho.rainbow.infrastructure.common.interceptor;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.ticho.rainbow.infrastructure.common.util.TraceUtil;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.starter.view.log.TiLogProperty;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义链路拦截器
 *
 * @author zhajianjun
 * @date 2024-04-19 18:05
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomTraceInterceptor implements HandlerInterceptor, Ordered {

    /** 链路配置 */
    private final TiLogProperty tiLogProperty;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String api = StrUtil.format("{} {}", request.getMethod(), request.getRequestURI());
        TraceUtil.trace(api, UserUtil.getCurrentUsername());
        return true;
    }

    @Override
    public int getOrder() {
        return tiLogProperty.getOrder() + 1;
    }

}
