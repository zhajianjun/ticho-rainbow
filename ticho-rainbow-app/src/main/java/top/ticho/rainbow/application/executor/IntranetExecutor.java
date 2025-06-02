package top.ticho.rainbow.application.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.ticho.intranet.server.core.ServerHandler;
import top.ticho.intranet.server.entity.PortInfo;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2025-05-25 22:55
 */
@RequiredArgsConstructor
@Component
public class IntranetExecutor {
    private final ServerHandler serverHandler;
    private final ClientRepository clientRepository;
    private final PortRepository portRepository;
    private final PortAssembler portAssembler;

    public void enable() {
        serverHandler.enable();
    }

    public void disable() {
        serverHandler.disable();
    }

    public <T> Map<String, List<T>> getPortMap(List<String> accessKeys, Function<Port, T> function) {
        return portRepository.listAndGroupByAccessKey(accessKeys, function, filter());
    }

    public Map<String, List<Port>> getPortMap(List<String> accessKeys) {
        return portRepository.listAndGroupByAccessKey(accessKeys, Function.identity(), filter());
    }

    public void flush() {
        List<Client> dtos = clientRepository.listEffect();
        List<String> accessKeys = dtos.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Map<String, List<PortInfo>> protMap = getPortMap(accessKeys, portAssembler::toInfo);
        dtos.forEach(client -> {
            List<PortInfo> ports = protMap.getOrDefault(client.getAccessKey(), Collections.emptyList());
            Map<Integer, PortInfo> collect = ports.stream().collect(Collectors.toMap(PortInfo::getPort, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));
            serverHandler.flush(client.getAccessKey(), collect);
        });
    }

    /**
     * 初始化客户端并绑定端口
     */
    public void init() {
        List<Client> dtos = clientRepository.listEffect();
        List<String> accessKeys = dtos.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Map<String, List<Port>> protMap = getPortMap(accessKeys);
        dtos.forEach(client -> {
            serverHandler.create(client.getAccessKey(), client.getName());
            List<Port> ports = protMap.getOrDefault(client.getAccessKey(), Collections.emptyList());
            ports.forEach(port -> serverHandler.bind(port.getAccessKey(), port.getPort(), port.getEndpoint()));
        });
    }

    public Predicate<Port> filter() {
        return port -> Objects.equals(port.getStatus(), CommonStatus.ENABLE.code()) && LocalDateTime.now().isBefore(port.getExpireAt());
    }

}
