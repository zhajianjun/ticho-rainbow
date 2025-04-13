package top.ticho.rainbow.application.listen;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.ticho.rainbow.application.assembler.OpLogAssembler;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.common.prop.OpLogProperty;
import top.ticho.starter.log.event.TiWebLogEvent;
import top.ticho.starter.view.log.TiHttpLog;

import java.util.List;
import java.util.Objects;

/**
 * @author zhajianjun
 * @date 2024-03-24 17:15
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class OpLogListen {
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private final OpLogRepository opLogRepository;
    private final OpLogAssembler opLogAssembler;
    private final OpLogProperty opLogProperty;

    @Async("asyncTaskExecutor")
    @EventListener(value = TiWebLogEvent.class)
    public void logEventHandle(TiWebLogEvent webLogEvent) {
        TiHttpLog httpLog = webLogEvent.getTiHttpLog();
        if (Objects.isNull(httpLog)) {
            return;
        }
        List<String> ignorePatterns = opLogProperty.getIgnorePatterns();
        boolean anyMatch = CollUtil.isNotEmpty(ignorePatterns)
            && ignorePatterns.stream().anyMatch(x -> ANT_PATH_MATCHER.match(x, httpLog.getUrl()));
        if (anyMatch) {
            return;
        }
        // 不统计非登录用户的操作日志
        if (Objects.isNull(httpLog.getUsername())) {
            return;
        }
        OpLog entity = opLogAssembler.toEntity(httpLog);
        opLogRepository.save(entity);
    }

}
