package top.ticho.rainbow.domain.listen;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import top.ticho.boot.log.event.WebLogEvent;
import top.ticho.boot.view.log.HttpLog;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.assembler.OpLogAssembler;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author zhajianjun
 * @date 2024-03-24 17:15
 */
@Component
public class OpLogListen {

    @Autowired
    @Qualifier("asyncTaskExecutor")
    private Executor executor;

    @Autowired
    private OpLogRepository opLogRepository;

    @EventListener(value = WebLogEvent.class)
    public void log(WebLogEvent webLogEvent) {
        CompletableFuture.runAsync(() -> handleLog(webLogEvent.getHttpLog()), executor);
    }

    private void handleLog(HttpLog httpLog) {
        OpLog entity = OpLogAssembler.INSTANCE.toEntity(httpLog);
        if (Objects.isNull(entity)) {
            return;
        }
        Long start = httpLog.getStart();
        Long end = httpLog.getEnd();
        if (Objects.nonNull(start)) {
            entity.setStartTime(LocalDateTimeUtil.of(start));
        }
        if (Objects.nonNull(end)) {
            entity.setEndTime(LocalDateTimeUtil.of(end));
        }
        Integer status = httpLog.getStatus();
        int isError = Objects.equals(status, HttpStatus.OK.value()) ? 1 : 0;
        entity.setIsErr(isError);
        entity.setResStatus(status);
        opLogRepository.save(entity);
    }

}
