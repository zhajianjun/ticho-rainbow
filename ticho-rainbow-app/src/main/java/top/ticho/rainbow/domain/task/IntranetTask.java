package top.ticho.rainbow.domain.task;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.intranet.service.ClientService;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内网映射数据刷新定时任务
 *
 * @author zhajianjun
 * @date 2024-04-03 15:16
 */
@Component
public class IntranetTask extends AbstracTask<String> {

    @Resource
    private ClientService clientService;

    @Resource
    private ServerHandler serverHandler;

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
