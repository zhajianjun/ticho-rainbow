package top.ticho.rainbow.application.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.ticho.intranet.server.core.IntranetServerHandler;
import top.ticho.intranet.server.entity.IntranetClient;
import top.ticho.intranet.server.entity.IntranetPort;
import top.ticho.rainbow.application.assembler.ClientAssembler;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private final IntranetServerHandler intranetServerHandler;
    private final ClientRepository clientRepository;
    private final ClientAssembler clientAssembler;
    private final PortRepository portRepository;
    private final PortAssembler portAssembler;

    public void enable() {
        intranetServerHandler.enable();
    }

    public void disable() {
        intranetServerHandler.disable();
    }

    public <T> Map<String, List<T>> getPortMap(List<String> accessKeys, Function<Port, T> function) {
        return portRepository.listAndGroupByAccessKey(accessKeys, function, filter());
    }

    public Map<String, List<Port>> getPortMap(List<String> accessKeys) {
        return portRepository.listAndGroupByAccessKey(accessKeys, Function.identity(), filter());
    }

    public Optional<IntranetClient> findByAccessKey(String accessKey) {
        return intranetServerHandler.findByAccessKey(accessKey);
    }

    public List<IntranetClient> findAll() {
        return intranetServerHandler.findAll();
    }

    public void create(String accessKey, String name) {
        intranetServerHandler.create(accessKey, name);
    }

    public void remove(String accessKey) {
        intranetServerHandler.remove(accessKey);
    }

    public boolean bind(String accessKey, Integer port, String endpoint) {
        return intranetServerHandler.bind(accessKey, port, endpoint);
    }

    public void unbind(String accessKey, Integer port) {
        intranetServerHandler.unbind(accessKey, port);
    }

    public boolean exists(Integer portNum) {
        return intranetServerHandler.exists(portNum);
    }

    public void flush() {
        List<Client> clients = clientRepository.listEffect();
        List<String> accessKeys = clients.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Map<String, List<IntranetPort>> protMap = getPortMap(accessKeys, portAssembler::toIntranetPort);
        List<IntranetClient> intranetClients = clients
            .stream()
            .map(clientAssembler::toIntranetClient)
            .peek(intranetClient -> {
                List<IntranetPort> intranetPorts = protMap.getOrDefault(intranetClient.getAccessKey(), Collections.emptyList());
                Map<Integer, IntranetPort> portMap = intranetClient.getPortMap();
                intranetPorts.forEach(intranetPort -> portMap.put(intranetPort.port(), intranetPort));
            })
            .collect(Collectors.toList());
        intranetServerHandler.flush(intranetClients);
    }

    /**
     * 初始化客户端并绑定端口
     */
    public void init() {
        List<Client> dtos = clientRepository.listEffect();
        List<String> accessKeys = dtos.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Map<String, List<Port>> protMap = getPortMap(accessKeys);
        dtos.forEach(client -> {
            intranetServerHandler.create(client.getAccessKey(), client.getName());
            List<Port> ports = protMap.getOrDefault(client.getAccessKey(), Collections.emptyList());
            ports.forEach(port -> intranetServerHandler.bind(port.getAccessKey(), port.getPort(), port.getEndpoint()));
        });
    }

    public Predicate<Port> filter() {
        return port -> Objects.equals(port.getStatus(), CommonStatus.ENABLE.code()) && LocalDateTime.now().isBefore(port.getExpireAt());
    }

}
