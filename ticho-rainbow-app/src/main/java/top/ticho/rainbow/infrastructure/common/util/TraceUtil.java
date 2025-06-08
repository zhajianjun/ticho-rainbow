package top.ticho.rainbow.infrastructure.common.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.MDC;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.trace.core.util.TiBeetlUtil;

import java.util.Optional;

/**
 * @author zhajianjun
 * @date 2025-06-08 13:01
 */
public class TraceUtil {

    public static void trace(String api, String username) {
        if (StrUtil.isBlank(api)) {
            return;
        }
        String traceKey = Optional.ofNullable(MDC.get("trace")).orElse(StrUtil.EMPTY);
        String template = "{}.[${{}!}]";
        traceKey = StrUtil.format(template, traceKey, CommConst.API_KEY);
        MDC.put(CommConst.API_KEY, api);

        if (StrUtil.isNotBlank(username)) {
            traceKey = StrUtil.format(template, traceKey, CommConst.USERNAME_KEY);
            MDC.put(CommConst.USERNAME_KEY, username);
        }
        String trace = TiBeetlUtil.render(traceKey, MDC.getCopyOfContextMap());
        MDC.put("trace", trace);
    }

}
