package top.ticho.rainbow.application.listen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import top.ticho.rainbow.application.assembler.OpLogAssembler;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.starter.log.event.TiWebLogEvent;
import top.ticho.starter.view.log.TiHttpLog;

import java.util.Objects;

/**
 * @author zhajianjun
 * @date 2024-03-24 17:15
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class OpLogListen {
    private final OpLogRepository opLogRepository;
    private final OpLogAssembler opLogAssembler;

    @Async("asyncTaskExecutor")
    @EventListener(value = TiWebLogEvent.class)
    public void logEventHandle(TiWebLogEvent webLogEvent) {
        TiHttpLog httpLog = webLogEvent.getTiHttpLog();
        Object handler = webLogEvent.getHandler();
        // 不统计非登录用户的操作日志
        if (!(handler instanceof HandlerMethod handlerMethod) || Objects.isNull(httpLog) || Objects.isNull(httpLog.getUsername())) {
            return;
        }
        ApiLog apiLog = handlerMethod.getMethodAnnotation(ApiLog.class);
        if (apiLog == null) {
            return;
        }
        httpLog.setName(apiLog.value());
        OpLog entity = opLogAssembler.toEntity(httpLog);
        opLogRepository.save(entity);
    }

}
