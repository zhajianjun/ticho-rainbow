package top.ticho.rainbow.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import top.ticho.rainbow.application.service.FlowMonitorService;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.interfaces.assembler.PortAssembler;
import top.ticho.rainbow.interfaces.dto.FlowMonitorDTO;
import top.ticho.rainbow.interfaces.dto.FlowMonitorStatsDTO;
import top.ticho.tool.intranet.server.entity.AppDataCollector;
import top.ticho.tool.intranet.server.entity.AppDataSummary;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.handler.AppHandler;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流量监控 服务实现
 *
 * @author zhajianjun
 * @date 2024-05-16 09:42
 */
@Slf4j
@Service
public class FlowMonitorServiceImpl implements FlowMonitorService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private ServerHandler serverHandler;

    @Override
    public FlowMonitorStatsDTO info() {
        AppHandler appHandler = serverHandler.getAppHandler();
        Map<String, ClientInfo> clientMap = serverHandler.getClientMap();
        List<Client> clients = clientRepository.list();
        int activeClients = Long.valueOf(clients.stream().filter(x -> clientMap.containsKey(x.getAccessKey())).count()).intValue();
        // 激活的端口号总数
        Set<Integer> portNums = appHandler.getBindPortChannelMap().keySet();
        // 激活的端口流量信息
        List<AppDataSummary> dataSummaries = AppDataCollector.getAllData();
        Map<Integer, AppDataSummary> appDataMap = dataSummaries.stream().collect(Collectors.toMap(AppDataSummary::getPort, Function.identity()));
        // 所有端口
        List<Port> ports = portRepository.list();
        List<FlowMonitorDTO> flowDetails = ports
            .stream()
            .filter(x-> portNums.contains(x.getPort()))
            .sorted(Comparator.comparing(Port::getSort).thenComparing(Port::getPort))
            .map(x-> convertToFlowMonitor(x, appDataMap.get(x.getPort())))
            .collect(Collectors.toList());
        FlowMonitorStatsDTO flowMonitorStatsDTO = new FlowMonitorStatsDTO();
        flowMonitorStatsDTO.setClients(clients.size());
        flowMonitorStatsDTO.setActiveClients(activeClients);
        flowMonitorStatsDTO.setPorts(ports.size());
        flowMonitorStatsDTO.setActivePorts(portNums.size());
        flowMonitorStatsDTO.setDateTime(LocalDateTime.now());
        flowMonitorStatsDTO.setFlowDetails(flowDetails);
        return flowMonitorStatsDTO;
    }

    public FlowMonitorDTO convertToFlowMonitor(Port port, AppDataSummary summary) {
        if (Objects.isNull(port)) {
            return null;
        }
        FlowMonitorDTO flowMonitorDTO = new FlowMonitorDTO();
        flowMonitorDTO.setPort(port.getPort());
        flowMonitorDTO.setPortInfo(PortAssembler.INSTANCE.entityToDto(port));
        if (Objects.isNull(summary)) {
            flowMonitorDTO.setReadBytes(0);
            flowMonitorDTO.setWriteBytes(0);
            flowMonitorDTO.setReadMsgs(0);
            flowMonitorDTO.setWriteMsgs(0);
            flowMonitorDTO.setChannels(0);
            return flowMonitorDTO;
        }
        flowMonitorDTO.setReadBytes(toKb(summary.getReadBytes()));
        flowMonitorDTO.setWriteBytes(toKb(summary.getWriteBytes()));
        flowMonitorDTO.setReadMsgs(toKb(summary.getReadMsgs()));
        flowMonitorDTO.setWriteMsgs(toKb(summary.getWriteMsgs()));
        flowMonitorDTO.setChannels(summary.getChannels());
        return flowMonitorDTO;
    }

    public Integer toKb(Long size) {
        if (size == null) {
            return 0;
        }
        return Long.valueOf(DataSize.ofBytes(size).toKilobytes()).intValue();
    }

}
