package top.ticho.rainbow.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.intranet.service.ClientService;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网络应用程序
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Component
@Slf4j
public class IntranetApplicationReadyEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private ServerHandler serverHandler;

    @Resource
    private ClientService clientService;

    @Async
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        log.info("内网应用程序启动");
        List<ClientInfo> clientInfos = clientService.listEffectClientInfo();
        serverHandler.saveClientBatch(clientInfos);
    }

}
