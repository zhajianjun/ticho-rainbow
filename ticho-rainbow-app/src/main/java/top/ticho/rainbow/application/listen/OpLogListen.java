package top.ticho.rainbow.application.listen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.ticho.rainbow.application.assembler.OpLogAssembler;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.starter.log.event.TiWebLogEvent;
import top.ticho.starter.view.log.TiHttpLog;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhajianjun
 * @date 2024-03-24 17:15
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class OpLogListen {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();    private final List<String> ignorePaths = Stream.of(
        "/opLog/page",
        "/file/uploadChunk",
        "/oauth/imgCode"
    ).collect(Collectors.toList());

    private final OpLogRepository opLogRepository;    private final OpLogAssembler opLogAssembler;
    @Async("asyncTaskExecutor")
    @EventListener(value = TiWebLogEvent.class)
    public void logEventHandle(TiWebLogEvent webLogEvent) {
        TiHttpLog httpLog = webLogEvent.getTiHttpLog();
        if (Objects.isNull(httpLog)) {
            return;
        }
        boolean anyMatch = ignorePaths.stream().anyMatch(x -> antPathMatcher.match(x, httpLog.getUrl()));
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
