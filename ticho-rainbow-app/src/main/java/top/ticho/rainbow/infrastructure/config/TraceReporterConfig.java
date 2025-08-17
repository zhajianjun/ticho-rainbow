package top.ticho.rainbow.infrastructure.config;

import org.springframework.stereotype.Component;
import top.ticho.trace.common.TiConsoleTraceReporter;
import top.ticho.trace.common.TiSpan;
import top.ticho.trace.common.TiTraceReporter;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-07-20 16:07
 */
@Component
public class TraceReporterConfig implements TiTraceReporter {
    private final TiTraceReporter tiReporter = new TiConsoleTraceReporter();

    @Override
    public void report(TiSpan tiSpan) {

    }

    @Override
    public void report(List<TiSpan> tiSpans) {
        tiReporter.report(tiSpans);
    }

}
