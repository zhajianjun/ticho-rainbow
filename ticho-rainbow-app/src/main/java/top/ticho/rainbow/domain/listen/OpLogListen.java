package top.ticho.rainbow.domain.listen;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.assembler.OpLogAssembler;
import top.ticho.starter.log.event.TiWebLogEvent;
import top.ticho.starter.view.log.TiHttpLog;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.tool.json.util.TiJsonUtil;
import top.ticho.trace.common.constant.LogConst;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhajianjun
 * @date 2024-03-24 17:15
 */
@Component
@Slf4j
public class OpLogListen {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final List<String> ignorePaths = Stream.of(
            "/opLog/page",
            "/file/uploadChunk",
            "/oauth/imgCode"
    ).collect(Collectors.toList());

    @Resource
    private OpLogRepository opLogRepository;

    @Async("asyncTaskExecutor")
    @EventListener(value = TiWebLogEvent.class)
    public void logEventHandle(TiWebLogEvent webLogEvent) {
        TiHttpLog httpLog = webLogEvent.getTiHttpLog();
        OpLog entity = OpLogAssembler.INSTANCE.toEntity(httpLog);
        if (Objects.isNull(entity)) {
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
        Long start = httpLog.getStart();
        Long end = httpLog.getEnd();
        if (Objects.nonNull(start)) {
            entity.setStartTime(LocalDateTimeUtil.of(start));
        }
        if (Objects.nonNull(end)) {
            entity.setEndTime(LocalDateTimeUtil.of(end));
        }
        Integer status = httpLog.getStatus();
        int isError = Objects.equals(status, HttpStatus.OK.value()) ? 0 : 1;
        entity.setIsErr(isError);
        entity.setResStatus(status);
        entity.setOperateBy(httpLog.getUsername());
        if (StrUtil.isBlank(entity.getResBody())) {
            entity.setResBody(null);
        }
        Map<String, String> mdcMap = httpLog.getMdcMap();
        entity.setTraceId(mdcMap.get(LogConst.TRACE_ID_KEY));
        entity.setMdc(TiJsonUtil.toJsonString(mdcMap));
        entity.setId(TiIdUtil.getId());
        Map<String, String> map = TiJsonUtil.toMap(httpLog.getReqHeaders(), String.class, String.class);
        String ip = entity.getIp();
        String realIp = Optional.ofNullable(map.get("Real-Ip")).orElse(ip);
        entity.setIp(realIp);
        opLogRepository.save(entity);
    }

}
