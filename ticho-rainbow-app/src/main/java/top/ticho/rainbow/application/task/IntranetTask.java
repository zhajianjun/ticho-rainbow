package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.service.ClientService;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.handler.ServerHandler;
import top.ticho.trace.common.prop.TraceProperty;

import java.util.List;

/**
 * 内网映射数据刷新定时任务
 *
 * @author zhajianjun
 * @date 2024-04-03 15:16
 */
@Component
public class IntranetTask extends AbstracTask<String> {

    private final ClientService clientService;
    private final ServerHandler serverHandler;

    public IntranetTask(Environment environment, TraceProperty traceProperty, TaskLogRepository taskLogRepository, ClientService clientService, ServerHandler serverHandler) {
        super(environment, traceProperty, taskLogRepository);
        this.clientService = clientService;
        this.serverHandler = serverHandler;
    }


    @Override
    public void run(JobExecutionContext context) {
        List<ClientInfo> clients = clientService.listEffectClientInfo();
        serverHandler.flushClientBatch(clients);
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
